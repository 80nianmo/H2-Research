�ӿ�: org.h2.server.Service
	=> org.h2.server.TcpServer
	=> org.h2.server.web.WebServer
	=> org.h2.server.pg.PgServer

������org.h2.tools.Server����ͬʱ����������������

��org.h2.tools.Server�ఴ�����˳�����org.h2.server.Service���ķ�:
init(String...) => start() => listen()

org.h2.tools.Server��̳��Գ�����org.h2.util.Tool


main Server -> tcp Server(web/pg Server)

tcp Server(web/pg Server)��ShutdownHandlerָ��tcp Server


Server��������:

���ʲô���������ӣ���ô��ͬʱ����tcp��web��pg��browser
org.h2.tools.Server.main(String...)
	=> org.h2.tools.Server.runTool(String...)