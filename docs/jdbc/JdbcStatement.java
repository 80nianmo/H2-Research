
1.executeUpdate

���һ��executeUpdate������client�����������ݰ�
(org.h2.jdbc.JdbcPreparedStatement.executeUpdate()ֻҪ������
��Ϊһ��JdbcPreparedStatementʵ��ֻ��Ӧһ��CommandInterfaceʵ��������ֻ���ڹر�JdbcPreparedStatementʱ�ŷ�COMMAND_CLOSE���ݰ�)

executeUpdate����:

org.h2.jdbc.JdbcStatement.executeUpdate(String)
	=> org.h2.jdbc.JdbcStatement.executeUpdateInternal(String)
		=> org.h2.jdbc.JdbcConnection.translateSQL(String, boolean) (ת��JDBC���е�SQL�﷨)
		=> org.h2.jdbc.JdbcConnection.prepareCommand(String, int) (�õ�һ��org.h2.command.CommandRemote����Ӧ����SQL)
			=> org.h2.engine.SessionRemote.prepareCommand(String, int)
				=> org.h2.command.CommandRemote
					=> org.h2.command.CommandRemote.prepare(SessionRemote, boolean) (����һ��SESSION_PREPARE_READ_PARAMS���ݰ�)
		=> org.h2.command.CommandRemote.executeUpdate() (����һ��COMMAND_EXECUTE_UPDATE���ݰ�)
		=> org.h2.command.CommandRemote.close() (����һ��COMMAND_CLOSE���ݰ�)



2.executeQuery

���һ��executeQuery������client�����������ݰ�
(JdbcPreparedStatementͬ�ϣ�Ҳ���÷�COMMAND_CLOSE���ݰ����ر�JdbcPreparedStatementʱ�ŷ�)

executeQuery����:

org.h2.jdbc.JdbcStatement.executeQuery(String)
	=> org.h2.jdbc.JdbcConnection.translateSQL(String, boolean) (ת��JDBC���е�SQL�﷨)
	=> org.h2.jdbc.JdbcConnection.prepareCommand(String, int) (�õ�һ��org.h2.command.CommandRemote����Ӧ����SQL)
		=> org.h2.engine.SessionRemote.prepareCommand(String, int)
			=> org.h2.command.CommandRemote
				=> org.h2.command.CommandRemote.prepare(SessionRemote, boolean) (����һ��SESSION_PREPARE_READ_PARAMS���ݰ�)
	=> org.h2.command.CommandRemote.executeQuery(int, boolean) (����һ��COMMAND_EXECUTE_QUERY���ݰ�)
	=> org.h2.command.CommandRemote.close() (����һ��COMMAND_CLOSE���ݰ�)
	=> org.h2.jdbc.JdbcResultSet




JdbcStatement��JdbcPreparedStatement������ͬ����org.h2.command.CommandRemote.executeQuery��
����JdbcStatement��Parameters����0����JdbcPreparedStatement��>=0����























































































































































































































































