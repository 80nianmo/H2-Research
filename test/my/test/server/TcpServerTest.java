package my.test.server;

import org.h2.server.TcpServer;

import my.test.TestBase;

public class TcpServerTest extends TestBase {
	public static void main(String[] args) throws Exception {
		new TcpServerTest().start();
	}

	@Override
	public void startInternal() throws Exception {
		//������һ���Զ���ĺ���STOP_SERVER��
        //ͨ����������CALL STOP_SERVER(9092, '', 0)���ܹر�H2���ݿ�
        //���õ���org.h2.server.TcpServer.stopServer(int, String, int)����
        //������Ϊ����STOP_SERVER�����ڴ����ݿ�ģ�����ͨ��TCPԶ�̵����ǲ��еģ�
        //Ҫ��Client���ֹ��ٽ���ͬ���ĺ�������: org.h2.server.TcpServer.initManagementDb()
		stmt.executeUpdate("CREATE ALIAS IF NOT EXISTS STOP_SERVER FOR \"" //
				+ TcpServer.class.getName() + ".stopServer\"");

		//SHUTDOWN_NORMAL = 0;
		//SHUTDOWN_FORCE = 1;

		//�����ر�H2���ݿ�
		stmt.executeUpdate("CALL STOP_SERVER(9092, '', 0)");

		//ǿ�ƹر�H2���ݿ�
		//stmt.executeUpdate("CALL STOP_SERVER(9092, '', 1)");
	}

}
