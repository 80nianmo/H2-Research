�����public����ֻ��������Щ:

static��:

org.h2.command.Parser.isKeyword(String, boolean)
org.h2.command.Parser.quoteIdentifier(String)

��static��:
org.h2.command.Parser.Parser(Session)
org.h2.command.Parser.getSession()
org.h2.command.Parser.parseExpression(String)
org.h2.command.Parser.prepare(String)
org.h2.command.Parser.prepareCommand(String)
org.h2.command.Parser.setRightsChecked(boolean)

������ķ���ֻ��һ��:
org.h2.command.Parser.parse(String) //�������⣬ֻ������org.h2.command.CommandContainer.recompileIfRequired()��ʹ��


org.h2.command.Parser.parse(String) �����

���������ߴ���

org.h2.command.Parser.parse(String)

	<= org.h2.command.Parser.prepare(String) (�õ�һ��Prepared)
		<= org.h2.engine.Session.prepare(String, boolean) ����ʱ���ã����ڷ�Զ�̿ͻ��˷���ĵ��ã�org.h2.engine.MetaRecord.execute(Database, Session, DatabaseEventListener)

	(�õ�һ��CommandContainer��CommandContainer��Prepared��PreparedҲ�ж�CommandContainer������)
	<= org.h2.command.Parser.prepareCommand(String)
		<= org.h2.engine.Session.prepareLocal(String) Զ�̿ͻ��˷���ĵ��ã���org.h2.server.TcpServerThread.process()

�������߶������Prepared.prepare()


���̷���:
org.h2.command.Parser.parse(String)
	=> org.h2.command.Parser.parse(String, boolean) ֻ��parse(String)�е���
		=> org.h2.command.Parser.initialize(String)
		=> org.h2.command.Parser.read()
		=> org.h2.command.Parser.parsePrepared() ����������

    /**
     * Parse the statement, but don't prepare it for execution.
     *
     * @param sql the SQL statement to parse
     * @return the prepared object
     */
    Prepared parse(String sql) {
        Prepared p;
        try {
            // first, try the fast variant
        	//����������SQL������ȷ�ģ�������������Щ�Ż�: Ĭ�ϲ�ʹ��expectedList�������ִ���ʱ����DbException��
			//������﷨������ô�ٽ���һ�Σ�����expectedList��¼SQL���﷨����ȱ����Щ������
			//����Ƿ��﷨�������SQL�������쳣��ֱ���׳��쳣��
        	//�������������Ż�����ô��ΪƵ������readIf->addExpected�ᵼ��expectedList��úܴ�
            p = parse(sql, false);
        } catch (DbException e) {
            if (e.getErrorCode() == ErrorCode.SYNTAX_ERROR_1) {
                // now, get the detailed exception
                p = parse(sql, true);
            } else {
                throw e.addSQL(sql);
            }
        }
        p.setPrepareAlways(recompileAlways);
        p.setParameterList(parameters);
        return p;
    }

    private Prepared parse(String sql, boolean withExpectedList) {
        initialize(sql);
        if (withExpectedList) {
            expectedList = New.arrayList();
        } else {
            expectedList = null;
        }
        parameters = New.arrayList();
        currentSelect = null;
        currentPrepared = null;
        createView = null;
        recompileAlways = false;
        indexedParameterList = null;
        read();
        return parsePrepared();
    }


org.h2.command.Parser.initialize(String)
��ǰ����sql����е�ÿ���ַ��������ע���滻�ɿո�



