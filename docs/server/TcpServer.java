������org.h2.tools.Server����

�����еķ�������˳����:

init => start => listen => isRunning

listen��isRunning������ͬʱִ�У�isRunning���ڲ����Ƿ��ڱ���������TcpServer


//ÿ����һ���µ�Session����ʱ���������浽�ڴ����ݿ�management_db_9092��SESSIONS��
stat.execute("CREATE TABLE IF NOT EXISTS SESSIONS(ID INT PRIMARY KEY, URL VARCHAR, USER VARCHAR, CONNECTED TIMESTAMP)");
managementDbAdd = conn.prepareStatement("INSERT INTO SESSIONS VALUES(?, ?, ?, NOW())");
managementDbRemove = conn.prepareStatement("DELETE FROM SESSIONS WHERE ID=?");


server����һ��listen�̣߳�
org.h2.server.TcpServer.listen()

һֱ�ڼ����ͻ������ӣ�
һ���յ��ͻ�����������Ϳ�һ���µ��̴߳�����(��δʹ���̳߳ء�ÿ����ÿ�߳�)

Э������:
client�ȷ�����:(��: org.h2.engine.SessionRemote.initTransfer(ConnectionInfo, String, String))
int     minClientVersion
int     maxClientVersion
String  db
String  URL
String  userName
byte[]  userPasswordHash
byte[]  filePasswordHash
int     keys length

keys length��
{
	String key
	String value
}

���������������:
��org.h2.server.TcpServerThread.run()������������Ӧif (db == null && originalURL == null)������

SessionRemote.SESSION_CANCEL_STATEMENT
====================================
int     minClientVersion
int     maxClientVersion
String  null
String  null
String  sessionId
int     SessionRemote.SESSION_CANCEL_STATEMENT(13)
int     statement id
====================================
��: org.h2.engine.SessionRemote.cancelStatement(int)

SessionRemote.SESSION_CHECK_KEY
====================================
int     minClientVersion
int     maxClientVersion
String  null
String  null
String  sessionId
int     SessionRemote.SESSION_CHECK_KEY(14)
====================================
��: org.h2.store.FileLock.checkServer()




server��Ӧ:

STATUS_OK:
====================================
int     STATUS_OK(1)
int     clientVersion
====================================


STATUS_ERROR: 
====================================
int     STATUS_ERROR(0)
String  SQLState value
String  message
String  sql
int     errorCode 
String  trace
====================================
��org.h2.server.TcpServerThread.sendError(Throwable)



��һ�����ְ�������֮�󣬾Ϳ���һֱʹ�ó���������������:

����Э���:

int     operation
ÿ��������Լ��ĸ�ʽ

�����: org.h2.server.TcpServerThread.process()

client�˷��ĵ�һ������һ����SessionRemote.SESSION_PREPARE_READ_PARAMS/SESSION_PREPARE
�����: org.h2.command.CommandRemote.prepare(SessionRemote, boolean)


����:

org.h2.server.TcpServer.listen()
	=> org.h2.server.TcpServerThread.run()
		=> org.h2.engine.Engine.createSession(ConnectionInfo)
		=> org.h2.server.TcpServerThread.process()