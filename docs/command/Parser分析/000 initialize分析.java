	//�˷����漰����ʵ���ֶ�:
	//originalSQL
	//sqlCommand
	//sqlCommandChars
	//characterTypes
	//parseIndex(��0��ʼ)

	//types��ʼ��ʱÿ��Ԫ�ض���0
	//�˷�����ע�͡�$$�ÿո��滻, ��"`"��"["����˫���ţ�
	//ͬʱ��SQL�е�ÿ���ַ����������ͣ��Ա���һ����read������ʶ��sql�еĸ��ֽṹ��
    private void initialize(String sql) {
        if (sql == null) {
            sql = "";
        }
        originalSQL = sql; //����䣬��ԭʼ��SQL
        sqlCommand = sql; //���
        int len = sql.length() + 1;

		//command��types�ĳ���Ҫ��sql�ĳ��ȶ�1��command�����һ���ַ�command[len]�ǿո�
		//types�����һ��Ԫ��types[len]��CHAR_END(��1)

		//���յ�command��types��ֱ�浽sqlCommandChars��characterTypes�ֶ�
		//command����б䶯����sqlCommand�ֶε�ֵ���´�command����
        char[] command = new char[len];
        int[] types = new int[len];
        len--;
        sql.getChars(0, len, command, 0);
        boolean changed = false;
        command[len] = ' ';
        int startLoop = 0;
        int lastType = 0;
        for (int i = 0; i < len; i++) {
            char c = command[i];
            int type = 0;
            switch (c) {
			//"����"/"��ʾCHAR_SPECIAL_1�ַ���"/*"�ǿ�ע�͵Ŀ�ʼ��־��"//"�ǵ���ע�͵Ŀ�ʼ��־
            case '/':
                if (command[i + 1] == '*') {
                    // block comment
                    changed = true;
                    command[i] = ' ';
                    command[i + 1] = ' ';
					
					//startLoop��"/"�ſ�ʼ��λ�ã��������﷨����ʱ�����ã���������λ��֮ǰ����[*]
					//����: Syntax error in SQL statement "DROP [*]/*TABLE TEST";
                    startLoop = i;
                    i += 2;
                    checkRunOver(i, len, startLoop);
                    while (command[i] != '*' || command[i + 1] != '/') {
                        command[i++] = ' ';
                        checkRunOver(i, len, startLoop);
                    }
                    command[i] = ' ';
                    command[i + 1] = ' ';
					//�����i���Ӻ���ָ��'/'��λ�ã�
					//��Ϊtype��ʱ��0�����Ժ����types[i] = type;��lastType = type;����0
                    i++;
                } else if (command[i + 1] == '/') {
                    // single line comment
                    changed = true;
                    startLoop = i;
                    while (true) {
                        c = command[i];
                        if (c == '\n' || c == '\r' || i >= len - 1) { //i >= len - 1�Ƕ�Ӧ���һ��û�лس����з�

							//��c == '\n' || c == '\r' || i >= len - 1ʱ������˳��ˣ�command[i]��ֵû��
							//����"DROP TABLE //single line comment, drop table t"��û�лس����з�, ��ʱi >= len - 1
							//sql�����"DROP TABLE                                   t "
							//���Ƕ�Ӧ"t"��type��0���������ɱ���������: 
							//Syntax error in SQL statement "DROP TABLE                                   t "; 
							//expected "identifier"; SQL statement:DROP TABLE //single line comment, drop table t [42001-168]
                            break;
                        }
                        command[i++] = ' ';
                        checkRunOver(i, len, startLoop);
                    }
                } else {
                    type = CHAR_SPECIAL_1;
                }
                break;
            case '-': //��"//"��ͬ�����Ǳ�ʾ����ע��
                if (command[i + 1] == '-') {
                    // single line comment
                    changed = true;
                    startLoop = i;
                    while (true) {
                        c = command[i];
                        if (c == '\n' || c == '\r' || i >= len - 1) {
                            break;
                        }
                        command[i++] = ' ';
                        checkRunOver(i, len, startLoop);
                    }
                } else {
                    type = CHAR_SPECIAL_1;
                }
                break;
            case '$':
				//$$...$$������ʾjavaԴ���룬��h2�ĵ�: Features => User-Defined Functions and Stored Procedures
				//(i == 0 || command[i - 1] <= ' ')��ʾ���sql��$$��ʼ������$$ǰ���һ���ַ��ǿո������ַ���
				//˵������������ʾjavaԴ����
				//ASCII�����0��31�ǿ����ַ�, 32�ǿո�
                if (command[i + 1] == '$' && (i == 0 || command[i - 1] <= ' ')) {
                    // dollar quoted string
                    changed = true;
                    command[i] = ' ';
                    command[i + 1] = ' ';
                    startLoop = i;
                    i += 2;
                    checkRunOver(i, len, startLoop);
                    while (command[i] != '$' || command[i + 1] != '$') {
                        types[i++] = CHAR_DOLLAR_QUOTED_STRING;
                        checkRunOver(i, len, startLoop);
                    }
                    command[i] = ' ';
                    command[i + 1] = ' ';
                    i++;
                } else {
					//$��Ϊ��ʶ����һ����
                    if (lastType == CHAR_NAME || lastType == CHAR_VALUE) {
                        // $ inside an identifier is supported
                        type = CHAR_NAME;
                    } else {
                        // but not at the start, to support PostgreSQL $1
                        type = CHAR_SPECIAL_1;
                    }
                }
                break;
            case '(':
            case ')':
            case '{':
            case '}':
            case '*':
            case ',':
            case ';':
            case '+':
            case '%':
            case '?':
            case '@':
            case ']':
                type = CHAR_SPECIAL_1;
                break;
            case '!':
            case '<':
            case '>':
            case '|':
            case '=':
            case ':':
            case '~':
                type = CHAR_SPECIAL_2;
                break;
            case '.':
                type = CHAR_DOT;
                break;
            case '\'':
                type = types[i] = CHAR_STRING;
                startLoop = i;
                while (command[++i] != '\'') {
                    checkRunOver(i, len, startLoop);
                }
                break;
            case '[': //SQL Server alias�﷨
                if (database.getMode().squareBracketQuotedNames) {
                    // SQL Server alias for "
                    command[i] = '"';
                    changed = true;
                    type = types[i] = CHAR_QUOTED;
                    startLoop = i;
                    while (command[++i] != ']') {
                        checkRunOver(i, len, startLoop);
                    }
                    command[i] = '"';
                } else {
                    type = CHAR_SPECIAL_1;
                }
                break;
            case '`': //MySQL alias�﷨�����������ִ�Сд��Ĭ�϶��Ǵ�д
                // MySQL alias for ", but not case sensitive
                command[i] = '"';
                changed = true;
                type = types[i] = CHAR_QUOTED;
                startLoop = i;
                while (command[++i] != '`') {
                    checkRunOver(i, len, startLoop);
                    c = command[i];
                    command[i] = Character.toUpperCase(c);
                }
                command[i] = '"';
                break;
            case '\"':
                type = types[i] = CHAR_QUOTED;
                startLoop = i;
                while (command[++i] != '\"') {
                    checkRunOver(i, len, startLoop);
                }
                break;
            case '_':
                type = CHAR_NAME;
                break;
            default:
                if (c >= 'a' && c <= 'z') {
                    if (identifiersToUpper) {
                        command[i] = (char) (c - ('a' - 'A'));
                        changed = true;
                    }
                    type = CHAR_NAME;
                } else if (c >= 'A' && c <= 'Z') {
                    type = CHAR_NAME;
                } else if (c >= '0' && c <= '9') {
                    type = CHAR_VALUE;
                } else {
                    if (c <= ' ' || Character.isSpaceChar(c)) {
                        // whitespace
                    } else if (Character.isJavaIdentifierPart(c)) {
                        type = CHAR_NAME;
                        if (identifiersToUpper) {
                            char u = Character.toUpperCase(c);
                            if (u != c) {
                                command[i] = u;
                                changed = true;
                            }
                        }
                    } else {
                        type = CHAR_SPECIAL_1;
                    }
                }
            }
            types[i] = type;
            lastType = type;
        }
        sqlCommandChars = command;
        types[len] = CHAR_END;
        characterTypes = types;
        if (changed) {
            sqlCommand = new String(command);
        }
        parseIndex = 0;
    }
