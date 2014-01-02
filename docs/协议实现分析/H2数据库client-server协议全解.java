H2���ݿ�İ汾: 1.3.169

server����һ��listen�̣߳�
org.h2.server.TcpServer.listen()

���߳�һֱ�ڼ����ͻ������ӣ�
һ���յ��ͻ�����������Ϳ�һ���µ��̴߳�����(��δʹ���̳߳ء�ÿ����ÿ�߳�)


Э�鹲��18������(��0��17)


1. ��ʼ���׶�(�����ֽ׶�)

client�ȷ�����:
int     minClientVersion
int     maxClientVersion
String  db               //���ݿ���
String  URL
String  userName
byte[]  userPasswordHash //32�ֽڣ�ʹ��SHA256�㷨�� userName + "@" + userPassword ����hash����Ϊ�û����浽���ݿ��е�����
byte[]  filePasswordHash //32�ֽڣ�ʹ��SHA256�㷨�� "file@" + filePassword ����hash�������������ݿ��ļ�
int     keys length //url�е�key-value��������

keys length��
{
	String key
	String value
}

��ǰ֧�ֵ�Э��汾��6��7��8��9��10��11��12

minClientVersion��maxClientVersion��������server�˵�ǰclient��֧�ֵ���С�����Э��汾�Ƕ��٣�
����������������server�˻�ѡ��һ�����ʵ�Э��汾��clientͨ�ţ�
ѡ����ʵ�Э��汾�ķ�������:
6<=minClientVersion<=12��
���maxClientVersion>=12����ô����12������ʹ��minClientVersion


server��Ӧ: STATUS_OK �� STATUS_ERROR

STATUS_OK:
------------------------------------
int     STATUS_OK(1)
int     clientVersion
------------------------------------


STATUS_ERROR: 
------------------------------------
int     STATUS_ERROR(0)
String  SQLState value
String  message
String  sql
int     errorCode 
String  trace
------------------------------------
��org.h2.server.TcpServerThread.sendError(Throwable)


2. sessionId����

client�Լ�����һ������Ϊ64���ַ���sessionId��Ȼ�󴫸�server��
client��server���͵���һ��SESSION_SET_ID���ݰ�

sessionId��server�˻����ڴ��б��棬��client����org.h2.jdbc.JdbcStatement.cancel()ʱ���õ�

------------------------------------
int     SESSION_SET_ID(12)
String  sessionId
------------------------------------

server��Ӧ: ֻ��STATUS_OK
------------------------------------
int     STATUS_OK(1)
------------------------------------


�����1��2��client��server֮��ÿ�ν���һ���µ�connection(���Ϊsession)ʱ����̶����ͣ�
(��: org.h2.engine.SessionRemote.initTransfer(ConnectionInfo, String, String))




3. prepare�׶�


��������ִ��SQL�������ֳ�����: QUERY��UPDATE

ִ��SQL֮ǰ����Ҫ�Ƚ���prepare��
client��server���͵���һ��SESSION_PREPARE_READ_PARAMS��SESSION_PREPARE���ݰ������߸�ʽ������һ����
SESSION_PREPARE_READ_PARAMS��Ҫserver��Ӧ�����ݰ��а���SQL����еĲ���Ԫ���ݣ�SESSION_PREPARE����Ҫ��
SESSION_PREPARE���ڵڶ��ζ�ͬһ��SQL����prepareʱ��

SESSION_PREPARE_READ_PARAMS
------------------------------------
int     SESSION_PREPARE_READ_PARAMS(11)
int     id(ִ�е�ǰsqlʱΪ�˲������ɵ�һ��������)
String  sql
------------------------------------

server��Ӧ:
------------------------------------
int     STATUS_OK �� STATUS_OK_STATE_CHANGED
boolean isQuery
boolean readonly
int     sql parameter size
Parameter MetaData[]
------------------------------------

SESSION_PREPARE
------------------------------------
int     SESSION_PREPARE(0)
int     id(ִ�е�ǰsqlʱΪ�˲������ɵ�һ��������)
String  sql
------------------------------------

server�յ������������󣬻ᰴid�Ѷ�Ӧ��SQL����������
server��Ӧ:
------------------------------------
int     STATUS_OK �� STATUS_OK_STATE_CHANGED
boolean isQuery
boolean readonly
int     sql parameter size
------------------------------------

���Դ�����: org.h2.command.CommandRemote.prepare(SessionRemote, boolean)



4. update

������java.sql.Statement.executeUpdate֮��Ĳ���ʱͨ���ᴥ��update

COMMAND_EXECUTE_UPDATE
------------------------------------
int     COMMAND_EXECUTE_UPDATE(3)
int     id(��Ӧprepare�׶����ɵ�id)
int     sql parameter size
Parameter[]
------------------------------------

server��Ӧ:
------------------------------------
int     STATUS_OK �� STATUS_OK_STATE_CHANGED
int     updateCount
boolean autoCommit
------------------------------------

���Դ�����: org.h2.command.CommandRemote.executeUpdate()



5. query

(TODO ��Ҫ��������)

������java.sql.Statement.executeQuery֮��Ĳ���ʱͨ���ᴥ��query

COMMAND_EXECUTE_QUERY
------------------------------------
int     COMMAND_EXECUTE_QUERY(2)
int     id(��Ӧprepare�׶����ɵ�id)
int     objectId(��id���ƣ�ʵ���Ͼ���һ������������)
int     maxRows
int     fetchSize
int     sql parameter size
Parameter[]
------------------------------------

server��Ӧ:
------------------------------------
int     STATUS_OK �� STATUS_OK_STATE_CHANGED
int     columnCount
int     rowCount
ResultColumn[] ��������е�Ԫ����
	ResultColumn {
		String  Alias
		String  SchemaName
		String  TableName
		String  ColumnName
		int     ColumnType
		long    ColumnPrecision
		int     ColumnScale
		int     DisplaySize
		boolean isAutoIncrement
		int     Nullable
	}
row result {
	boolean hasNext
	���hasNext==true
	�ֶ�ֵ��...
}
------------------------------------

���Դ�����: org.h2.command.CommandRemote.executeQuery(int, boolean)





6. ��������

6.1 ��ȡ�����Ԫ����


COMMAND_GET_META_DATA
------------------------------------
int     COMMAND_GET_META_DATA(10)
int     id(��Ӧprepare�׶����ɵ�id)
int     objectId(��id���ƣ�ʵ���Ͼ���һ������������)
------------------------------------

server��Ӧ:
------------------------------------
int     STATUS_OK
int     columnCount
int     rowCount (�̶���0)
ResultColumn[] ��������е�Ԫ����
	ResultColumn {
		String  Alias
		String  SchemaName
		String  TableName
		String  ColumnName
		int     ColumnType
		long    ColumnPrecision
		int     ColumnScale
		int     DisplaySize
		boolean isAutoIncrement
		int     Nullable
	}
}
------------------------------------

���Դ�����: org.h2.command.CommandRemote.getMetaData()




6.2 COMMAND_CLOSE

COMMAND_CLOSE
------------------------------------
int     COMMAND_CLOSE(4)
int     id(��Ӧprepare�׶����ɵ�id)
------------------------------------

server�����Ӧid�Ļ��棬����Ӧ��

���Դ�����: org.h2.command.CommandRemote.close()



6.3 RESULT_FETCH_ROWS

RESULT_FETCH_ROWS
------------------------------------
int     RESULT_FETCH_ROWS(5)
int     id(��ӦQuery�׶����ɵ�id)
int     fetchSize
------------------------------------


server��Ӧ:
------------------------------------
int     STATUS_OK
int     columnCount
row result {
	boolean hasNext
	���hasNext==true
	�ֶ�ֵ��...
}
------------------------------------

���Դ�����: org.h2.result.ResultRemote.fetchRows(boolean)


6.4 RESULT_CLOSE

RESULT_CLOSE
------------------------------------
int     RESULT_CLOSE(7)
int     id(��ӦQuery�׶����ɵ�id)
------------------------------------


server�����Ӧid�Ļ��棬����Ӧ��

���Դ�����: org.h2.result.ResultRemote.sendClose()



6.5 RESULT_RESET

�ڽ���java.sql.ResultSet��first��beforeFirst��absolute�ᴥ���˲���
RESULT_RESET
------------------------------------
int     RESULT_RESET(6)
int     id(��ӦQuery�׶����ɵ�id)
------------------------------------


server�˵�Result����reset������Ӧ��

���Դ�����: org.h2.result.ResultRemote.reset()



6.6 COMMAND_COMMIT

�ڼ�Ⱥ�����»��Զ��ύģʽ����false����ִ�в������client�˻��Զ��������������server��֪ͨ�����ύ.
COMMAND_COMMIT
------------------------------------
int     COMMAND_COMMIT(8)
------------------------------------


server��Ӧ:
------------------------------------
int     STATUS_OK �� STATUS_OK_STATE_CHANGED
------------------------------------

���Դ�����: org.h2.engine.SessionRemote.autoCommitIfCluster()




6.7 CHANGE_ID

ͨ����server��ÿ��session��cache��С���������Ƶģ�Ĭ����64�������������id��С(Ҳ����ζ�źܾ���)
��ô��Ҫ�ӵ�ǰ��SessionRemote�õ�һ���µ�id��Ȼ��֪ͨserver�������id��ԭ��cache�еĽ��������ӳ��һ��.

CHANGE_ID
------------------------------------
int     CHANGE_ID(9)
int     ��ID
int     ��ID
------------------------------------


server��Ӧ:
------------------------------------
server������Ӧ��
------------------------------------

���Դ�����: org.h2.result.ResultRemote.remapIfOld()




6.8 SESSION_SET_AUTOCOMMIT

�ı�autoCommitģʽ

SESSION_SET_AUTOCOMMIT
------------------------------------
int     SESSION_SET_AUTOCOMMIT(15)
boolean true or false
------------------------------------


server��Ӧ:
------------------------------------
int     STATUS_OK
------------------------------------

���Դ�����: org.h2.engine.SessionRemote.setAutoCommitSend(boolean)



6.9 SESSION_UNDO_LOG_POS

�ر�����ʱ������server���Ƿ��г�����־������У���ִ��rollback����

SESSION_UNDO_LOG_POS
------------------------------------
int     SESSION_UNDO_LOG_POS(16)
------------------------------------


server��Ӧ:
------------------------------------
int     STATUS_OK
int     Undo Log Pos
------------------------------------

���Դ�����: org.h2.engine.SessionRemote.getUndoLogPos()



6.10 LOB_READ

TODO



7 misc

���������������:
��org.h2.server.TcpServerThread.run()������������Ӧif (db == null && originalURL == null)������

SessionRemote.SESSION_CANCEL_STATEMENT
------------------------------------
int     minClientVersion
int     maxClientVersion
String  null
String  null
String  sessionId
int     SessionRemote.SESSION_CANCEL_STATEMENT(13)
int     statement id
------------------------------------
��: org.h2.engine.SessionRemote.cancelStatement(int)


server��Ӧ:
------------------------------------
����Ҫ��Ӧ
------------------------------------



SessionRemote.SESSION_CHECK_KEY
------------------------------------
int     minClientVersion
int     maxClientVersion
String  null
String  null
String  sessionId
int     SessionRemote.SESSION_CHECK_KEY(14)
------------------------------------
��: org.h2.store.FileLock.checkServer()




server��Ӧ:

STATUS_OK:
------------------------------------
int     STATUS_OK(1)
int     clientVersion
------------------------------------


STATUS_ERROR: 
------------------------------------
int     STATUS_ERROR(0)
String  SQLState value
String  message
String  sql
int     errorCode 
String  trace
------------------------------------
��org.h2.server.TcpServerThread.sendError(Throwable)



��һ�����ְ�������֮�󣬾Ϳ���һֱʹ�ó���������������:

����Э���:

int     operation
ÿ��������Լ��ĸ�ʽ

�����: org.h2.server.TcpServerThread.process()

client�˷��ĵ�һ������һ����SessionRemote.SESSION_PREPARE_READ_PARAMS/SESSION_PREPARE
�����: org.h2.command.CommandRemote.prepare(SessionRemote, boolean)




�������͵�close�����������С����ֱ���:
RESULT_CLOSE   ֪ͨserver�رս����������session��cache��ɾ�����������
COMMAND_CLOSE  ֪ͨserver�ر�SQL�������session��cache��ɾ�������
SESSION_CLOSE  ֪ͨserver�ر�session��ͣ���̣߳�ɾ����session��ص�������Դ(����������ͷŴ�����Դ)




����:

org.h2.server.TcpServer.listen()
	=> org.h2.server.TcpServerThread.run()
		=> org.h2.engine.Engine.createSession(ConnectionInfo)
		=> org.h2.server.TcpServerThread.process()