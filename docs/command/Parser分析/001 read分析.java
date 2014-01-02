    private void read(String expected) {
		//��`��[]������Χ�������ַ����������Token��ʹ��currentTokenQuoted=true
		//��"CREATE or `REPLACE` TABLE IF NOT EXISTS
		//expected = currentToken = REPLACE
		//currentTokenQuoted = true
        if (currentTokenQuoted || !equalsToken(expected, currentToken)) {
            addExpected(expected);
            throw getSyntaxError();
        }
        read();
    }

	//read��readIf�Ĳ��
	//read: expected��currentToken����һ������һ�����﷨��������û���﷨����Ԥ����һ��token
	//readIf: ֻ��token��currentTokenһ��ʱ��Ԥ����һ��token������ͬ���ᱨ�﷨����
    private boolean readIf(String token) {
        if (!currentTokenQuoted && equalsToken(token, currentToken)) {
            read();
            return true;
        }
        addExpected(token);
        return false;
    }

	//��readIf(String token)�󲿷���ͬ��Ψһ�����isToken����Ԥ����һ��token
	private boolean isToken(String token) {
        boolean result = equalsToken(token, currentToken) && !currentTokenQuoted;
        if (result) {
            return true;
        }
        addExpected(token);
        return false;
    }

    private boolean equalsToken(String a, String b) {
        if (a == null) {
            return b == null;
        } else if (a.equals(b)) {
            return true;
        } else if (!identifiersToUpper && a.equalsIgnoreCase(b)) {
            return true;
        }
        return false;
    }

    private void read() {
        currentTokenQuoted = false;
        if (expectedList != null) {
            expectedList.clear();
        }
        int[] types = characterTypes;
        lastParseIndex = parseIndex;
        int i = parseIndex;
        int type = types[i];
        while (type == 0) { //������ǰ��typeΪ0��Ԫ�أ���Ϊ0��Ӧ���ַ��ǿհ���ģ�û����
            type = types[++i];
        }
        int start = i;
        char[] chars = sqlCommandChars;
        char c = chars[i++];
        currentToken = "";
        switch (type) {
        case CHAR_NAME:
            while (true) {
                type = types[i];
                if (type != CHAR_NAME && type != CHAR_VALUE) {
                    break;
                }
                i++;
            }
            currentToken = StringUtils.fromCacheOrNew(sqlCommand.substring(start, i));
            currentTokenType = getTokenType(currentToken);
            parseIndex = i;
            return;
        case CHAR_QUOTED: {
            String result = null;
			//�ڲ���forѭ�������ҳ���һ��˫�����а������ַ�
			//���˫�����а������ַ�����˫���ţ�whileѭ������Ѱ�Һ�����ַ�
			//�������"aaa""bbb"��i�ȴӵ�һ��a��ʼ������ڶ���"��ʱ��if (chars[i] == '\"')Ϊtrue��
			//��Ϊ��ʱresultΪnull������result = sqlCommand.substring(begin, i) = aaa
			//�����˳�forѭ������Ϊchars[++i]="������whileѭ������,��ʱbegin�ӵ�һ��b��ʼ��
			//���뵽if (chars[i] == '\"')ʱ����Ϊǰ��result = aaa��
			//����result += sqlCommand.substring(begin - 1, i) = aaa"bbb
			//Ҳ����˵˫�����а������ַ����������������""��ô�ͱ�ʾ"������
            while (true) {
                for (int begin = i;; i++) {
                    if (chars[i] == '\"') {
                        if (result == null) {
                            result = sqlCommand.substring(begin, i);
                        } else {
                            result += sqlCommand.substring(begin - 1, i); //begin - 1��ʾ��ǰ���"��Ҳ�ӽ���
                        }
                        break;
                    }
                }
                if (chars[++i] != '\"') { //����"aaa""bbb"�ĳ��������ջ�ת����aaa"bbb
                    break;
                }
                i++;
            }
            currentToken = StringUtils.fromCacheOrNew(result);
            parseIndex = i;
            currentTokenQuoted = true;
            currentTokenType = IDENTIFIER;
            return;
        }
        case CHAR_SPECIAL_2:
			//����CHAR_SPECIAL_2���͵��ַ�Ҫ�ϲ�������!=
            if (types[i] == CHAR_SPECIAL_2) {
                i++;
            }
            currentToken = sqlCommand.substring(start, i);
            currentTokenType = getSpecialType(currentToken);
            parseIndex = i;
            return;
        case CHAR_SPECIAL_1:
            currentToken = sqlCommand.substring(start, i);
            currentTokenType = getSpecialType(currentToken);
            parseIndex = i;
            return;
        case CHAR_VALUE:
            if (c == '0' && chars[i] == 'X') { //��initialize���Ѱ�xת���ɴ�дX
                // hex number
                long number = 0;
                start += 2;
                i++;
                while (true) {
                    c = chars[i];
                    if ((c < '0' || c > '9') && (c < 'A' || c > 'F')) {
                        checkLiterals(false);
                        currentValue = ValueInt.get((int) number);
                        currentTokenType = VALUE;
                        currentToken = "0";
                        parseIndex = i;
                        return;
                    }
                    number = (number << 4) + c - (c >= 'A' ? ('A' - 0xa) : ('0'));
                    if (number > Integer.MAX_VALUE) {
                        readHexDecimal(start, i);
                        return;
                    }
                    i++;
                }
            }
            long number = c - '0';
            while (true) {
                c = chars[i];
                if (c < '0' || c > '9') {
                    if (c == '.') {
                        readDecimal(start, i);
                        break;
                    }
                    if (c == 'E') {
                        readDecimal(start, i);
                        break;
                    }
                    checkLiterals(false);
                    currentValue = ValueInt.get((int) number);
                    currentTokenType = VALUE;
                    currentToken = "0";
                    parseIndex = i;
                    break;
                }
                number = number * 10 + (c - '0');
                if (number > Integer.MAX_VALUE) {
                    readDecimal(start, i);
                    break;
                }
                i++;
            }
            return;
        case CHAR_DOT:
            if (types[i] != CHAR_VALUE) {
                currentTokenType = KEYWORD;
                currentToken = ".";
                parseIndex = i;
                return;
            }
            readDecimal(i - 1, i);
            return;
        case CHAR_STRING: {
            String result = null;
			//��CHAR_QUOTED����
            while (true) {
                for (int begin = i;; i++) {
                    if (chars[i] == '\'') {
                        if (result == null) {
                            result = sqlCommand.substring(begin, i);
                        } else {
                            result += sqlCommand.substring(begin - 1, i);
                        }
                        break;
                    }
                }
                if (chars[++i] != '\'') {
                    break;
                }
                i++;
            }
            currentToken = "'";
            checkLiterals(true);
            currentValue = ValueString.get(StringUtils.fromCacheOrNew(result));
            parseIndex = i;
            currentTokenType = VALUE;
            return;
        }
        case CHAR_DOLLAR_QUOTED_STRING: {
            String result = null;
            int begin = i - 1;
            while (types[i] == CHAR_DOLLAR_QUOTED_STRING) {
                i++;
            }
            result = sqlCommand.substring(begin, i);
            currentToken = "'";
            checkLiterals(true);
            currentValue = ValueString.get(StringUtils.fromCacheOrNew(result));
            parseIndex = i;
            currentTokenType = VALUE;
            return;
        }
        case CHAR_END:
            currentToken = "";
            currentTokenType = END;
            parseIndex = i;
            return;
        default:
            throw getSyntaxError();
        }
    }
