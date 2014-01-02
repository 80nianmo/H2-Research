package my.test.command.dml;

import java.sql.Connection;
import java.sql.SQLException;

import my.test.TestBase;

//�Ҷϵ�����
//table.getName().equalsIgnoreCase("InsertTest");
public class InsertTest extends TestBase {
	public static void main(String[] args) throws Exception {
		new InsertTest().start();
	}

	public static class MyInsertTrigger implements org.h2.api.Trigger {

		@Override
		public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type)
				throws SQLException {
			System.out.println("schemaName=" + schemaName + " tableName=" + tableName);

		}

		@Override
		public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
			System.out.println("oldRow=" + oldRow + " newRow=" + newRow);
		}

		@Override
		public void close() throws SQLException {
			System.out.println("my.test.sql.InsertTest.MyInsertTrigger.close()");
		}

		@Override
		public void remove() throws SQLException {
			System.out.println("my.test.sql.InsertTest.MyInsertTrigger.remove()");
		}

	}

	//����org.h2.command.Parser.parseInsert()��org.h2.command.dml.Insert
	@Override
	public void startInternal() throws Exception {
		conn.setAutoCommit(false);

		stmt.executeUpdate("DROP TABLE IF EXISTS InsertTest");
		//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS InsertTest(id int not null, name varchar(500) not null)");
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS InsertTest(id int, name varchar(500) as '123')");

		stmt.executeUpdate("CREATE TRIGGER IF NOT EXISTS TriggerInsertTest BEFORE INSERT ON InsertTest "
		//+ "FOR EACH ROW CALL \"my.test.sql.InsertTest$MyInsertTrigger\"");
				+ "CALL \"my.test.sql.InsertTest$MyInsertTrigger\"");

		stmt.executeUpdate("DROP TABLE IF EXISTS tmpSelectTest");
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tmpSelectTest(id int, name varchar(500))");
		stmt.executeUpdate("INSERT INTO tmpSelectTest VALUES(DEFAULT, DEFAULT),(10, 'a'),(20, 'b')");

		//����һ������ݣ�Ȼ�����˱�
		sql = "INSERT INTO InsertTest(SELECT * FROM tmpSelectTest)";
		//sql = "INSERT INTO InsertTest(FROM tmpSelectTest SELECT *)"; //FROM��ͷ��Ҳ��֧�ֵ�

		//DEFAULT VALUES�����﷨���ʺ�����not null�ֶ�
		//sql = "INSERT INTO InsertTest DIRECT SORTED DEFAULT VALUES";
		//DEFAULT VALUES�����﷨�����ڱ���֮����ָ���ֶ��б�
		//sql = "INSERT INTO InsertTest(name) DIRECT SORTED DEFAULT VALUES");

		//�����﷨�ɲ��������¼
		//null null
		//10 a
		//20 b
		//sql = "INSERT INTO InsertTest VALUES(DEFAULT, DEFAULT),(10, 'a'),(20, 'b')";

		//SET�﷨�����ڱ���֮����ָ���ֶ��б�
		//sql = "INSERT INTO InsertTest(name) SET name='xyz')";
		//��Ȼ���﷨�Ͽ����ظ���ͬ���ֶΣ�����������������¼������ʵ����ֻ��һ�����������һ��id��name
		//sql = "INSERT INTO InsertTest SET id=DEFAULT, name=DEFAULT, id=10, name='a', id=20, name='b'");

		//�б���һ���࣬����:
		//Exception in thread "main" org.h2.jdbc.JdbcSQLException: Column count does not match; SQL statement:
		//INSERT INTO InsertTest(name) (SELECT * FROM tmpSelectTest) [21002-169]
		//sql = "INSERT INTO InsertTest(name) (SELECT * FROM tmpSelectTest)";
		//sql = "INSERT INTO InsertTest(name) (FROM tmpSelectTest SELECT *)"; //FROM��ͷ��Ҳ��֧�ֵ�
		//
		//		sql = "INSERT INTO InsertTest(name) (SELECT name FROM tmpSelectTest)";
		//		sql = "INSERT INTO InsertTest(name) (FROM tmpSelectTest SELECT name)"; //FROM��ͷ��Ҳ��֧�ֵ�

		//SELECT��䲻������Ҳ�������
		//		sql = "INSERT INTO InsertTest(name) SELECT name FROM tmpSelectTest";
		//		sql = "INSERT INTO InsertTest(name) FROM tmpSelectTest SELECT name"; //FROM��ͷ��Ҳ��֧�ֵ�
		//
		//		sql = "INSERT INTO InsertTest(name) DIRECT FROM tmpSelectTest SELECT name"; //FROM��ͷ��Ҳ��֧�ֵ�

		sql = "INSERT INTO InsertTest(id, name) SORTED VALUES(100,'abc')"; //FROM��ͷ��Ҳ��֧�ֵ�
		sql = "INSERT INTO InsertTest(id, name) SORTED VALUES(100,DEFAULT)"; //FROM��ͷ��Ҳ��֧�ֵ�
		
		stmt.executeUpdate("CREATE SCHEMA IF NOT EXISTS myschema AUTHORIZATION sa");
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS myschema.InsertTest2(id int, name varchar(500) as '123')");
		stmt.executeUpdate("SET SCHEMA_SEARCH_PATH INFORMATION_SCHEMA, PUBLIC, myschema");
		
		sql = "INSERT INTO InsertTest2(id, name) SORTED VALUES(100,DEFAULT)"; //FROM��ͷ��Ҳ��֧�ֵ�
		stmt.executeUpdate(sql);

		//		ps = conn.prepareStatement("INSERT INTO InsertTest(id, name) VALUES(?, ?)");
		//		ps.setInt(1, 30);
		//		ps.setString(2, "c");
		//		ps.executeUpdate();

		stmt.executeQuery("EXPLAIN INSERT INTO InsertTest(name) DIRECT FROM tmpSelectTest SELECT name");

		sql = "select id,name from InsertTest";
		sql = "select * from SYS";
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
			System.out.println(rs.getString(1) + " " + rs.getString(2));
		}

		conn.commit();
		//conn.rollback();
	}
}