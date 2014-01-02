package my.test.engine;

import java.sql.ResultSetMetaData;

import my.test.TestBase;

public class SessionRemoteTest extends TestBase {
	public static void main(String[] args) throws Exception {
		new SessionRemoteTest().start();
	}

	@Override
	public void init() throws Exception {
		prop.setProperty("user", "sa3");
		//prop.setProperty("password", "22");
		
		//url = "jdbc:h2:mem:memdb";
		//ע��: localhost:9092,localhost:9093��˵���Ǽ�Ⱥ���������ǲ�����XA��
		//Ҳ����˵������һ̨server���³ɹ��ˣ�����������һ̨���²��ɹ�
		//url = "jdbc:h2:tcp://localhost:9092,localhost:9093/mydb_SessionRemoteTest";
		//url = "jdbc:h2:tcp://localhost:9092/mydb_SessionRemoteTest";
		url = "jdbc:h2:tcp://localhost:9092/mydb_SessionRemoteTest5";
		//prop.setProperty("AUTO_SERVER", "true"); //AUTO_SERVERΪtrueʱurl�в���ָ�����server
		prop.setProperty("AUTO_RECONNECT", "true");
		
		

		//TRACE_LEVEL_FILE������System����Ч������url�зǷ���ֻ�ܷ���prop��
		//System.setProperty("TRACE_LEVEL_FILE", "E:\\H2\\baseDir\\MY_TRACE_LEVEL_FILE");
		//url +="; TRACE_LEVEL_FILE=E:\\H2\\baseDir\\MY_TRACE_LEVEL_FILE";

		//prop.setProperty("TRACE_LEVEL_FILE", "E:\\H2\\baseDir\\MY_TRACE_LEVEL_FILE.txt"); //ֻ��������

		//prop.setProperty("TRACE_LEVEL_FILE", "10");
		//prop.setProperty("TRACE_LEVEL_SYSTEM_OUT", "20");

		prop.setProperty("DATABASE_EVENT_LISTENER", "my.test.MyDatabaseEventListener");

		//ͬһ�����ݿ��һ�δ�ʱ���ûʹ��CIPHER����ô��������Ҳ������CIPHER�ˣ�
		//�����һ������CIPHER����ô��һֱҪ��CIPHER����
		//prop.setProperty("CIPHER", "my_cipher"); //ֻ֧��XTEA��AES��FOG
		prop.setProperty("CIPHER", "AES");
		//���������CIPHER����������������ݣ��ÿո�ֿ�����һ������filePassword���ڶ��������û�����
		//prop.setProperty("password", "myFilePassword myUserPassword");
		prop.setProperty("password", "123456 654321");
		//prop.setProperty("PASSWORD_HASH", "true"); //���Ϊtrue��ô����Ҫ�ٶ�filePassword��userPassword����hash��
	}

	//����org.h2.engine.SessionRemote.connectEmbeddedOrServer(boolean)
	@Override
	public void startInternal() throws Exception {
		conn.setAutoCommit(true); //����Ǽ�Ⱥ�������������������ʵû��
						stmt.executeUpdate("drop table IF EXISTS SessionRemoteTest CASCADE");
						stmt.executeUpdate("create table IF NOT EXISTS SessionRemoteTest(id int, name varchar(500), b boolean)");
						stmt.executeUpdate("CREATE INDEX IF NOT EXISTS SessionRemoteTestIndex ON SessionRemoteTest(name)");
				
						stmt.executeUpdate("insert into SessionRemoteTest(id, name, b) values(10, 'a1', true)");
						stmt.executeUpdate("insert into SessionRemoteTest(id, name, b) values(20, 'b1', true)");
						stmt.executeUpdate("insert into SessionRemoteTest(id, name, b) values(30, 'a2', false)");
						stmt.executeUpdate("insert into SessionRemoteTest(id, name, b) values(40, 'b2', true)");
						stmt.executeUpdate("insert into SessionRemoteTest(id, name, b) values(50, 'a3', false)");
						stmt.executeUpdate("insert into SessionRemoteTest(id, name, b) values(60, 'b3', true)");
						stmt.executeUpdate("insert into SessionRemoteTest(id, name, b) values(70, 'b3', true)");

		new StatementCancelThread().start();
		//stmt.executeUpdate("CREATE USER IF NOT EXISTS SA3 IDENTIFIED BY abc"); // ���벻������

		stmt.executeUpdate("drop table IF EXISTS tempSessionRemoteTest");
		//��������SQL���Բ���org.h2.engine.SessionWithState.readSessionState()
		stmt.executeUpdate("create LOCAL TEMPORARY table IF NOT EXISTS tempSessionRemoteTest(i int)");
		sql = "select * from SessionRemoteTest where id>? and b=?";
		sql = "select * from SessionRemoteTest where id>? and b=? for update";
		//sql = "select * from SessionRemoteTest where id>? and b=? and id<RAND()";
		//sql = "update SessionRemoteTest set b=true where id>? and b=?";
		ps = conn.prepareStatement(sql);
		ps.setFetchSize(2);
		ps.setInt(1, 50);
		ps.setBoolean(2, true);

		rs = ps.executeQuery();
		//�ȼ���ResultSet.getMetaData()��ֻ����PreparedStatement.getMetaData()����Ҫ����ִ�в�ѯ
		ResultSetMetaData rsmd = ps.getMetaData(); //������ǲ�ѯsql���˷����᷵��null
		int n = rsmd.getColumnCount();
		while (rs.next()) {
			for (int i = 1; i <= n; i++) {
				System.out.print(rs.getString(i) + " ");
			}
			System.out.println();
		}
	}

	private class StatementCancelThread extends Thread {
		public void run() {
			try {
				//��eclipse debugģʽ�µ��Ե�������ͣ��Ȼ��ִ��stmt.executeUpdate����stmt.executeUpdate������ͣ����ת���������
				//stmt.cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
