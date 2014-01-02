package my.test.command;

import my.test.TestBase;

public class JoinTest extends TestBase {
	public static void main(String[] args) throws Exception {
		new JoinTest().start();
	}

	@Override
	public void init() throws Exception {
		//prop.setProperty("DATABASE_TO_UPPER", "false");
		//prop.setProperty("NESTED_JOINS", "false"); //��ʹ��Ƕ��join

		//��org.h2.table.TableFilter.fullCondition��ע��
		//��org.h2.table.Plan.removeUnusableIndexConditions()��ע��
		prop.setProperty("EARLY_FILTER", "true");

		prop.setProperty("OPTIMIZE_IS_NULL", "false");
	}

	@Override
	public void startInternal() throws Exception {
        init2();
        insert2();
        test2();
        if(prop!=null)
        	return;
		
		stmt.executeUpdate("DROP TABLE IF EXISTS JoinTest1 CASCADE");
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS JoinTest1(id int, name varchar(500), b boolean)");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS JoinTest1Index ON JoinTest1(name)");

		stmt.executeUpdate("DROP TABLE IF EXISTS JoinTest2 CASCADE");
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS JoinTest2(id2 int, name2 varchar(500))");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS JoinTest2Index ON JoinTest2(name2)");

		stmt.executeUpdate("DROP TABLE IF EXISTS JoinTest3 CASCADE");
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS JoinTest3(id3 int, name3 varchar(500))");

		stmt.executeUpdate("DROP TABLE IF EXISTS JoinTest4 CASCADE");
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS JoinTest4(id int, name varchar(500))");

		stmt.executeUpdate("insert into JoinTest1(id, name, b) values(10, 'a1', true)");
		stmt.executeUpdate("insert into JoinTest1(id, name, b) values(20, 'b1', true)");
		stmt.executeUpdate("insert into JoinTest1(id, name, b) values(30, 'a2', false)");
		stmt.executeUpdate("insert into JoinTest1(id, name, b) values(40, 'b2', true)");

		stmt.executeUpdate("insert into JoinTest2(id2, name2) values(60, 'a11')");
		stmt.executeUpdate("insert into JoinTest2(id2, name2) values(70, 'a11')");
		stmt.executeUpdate("insert into JoinTest2(id2, name2) values(80, 'a11')");
		stmt.executeUpdate("insert into JoinTest2(id2, name2) values(90, 'a11')");

		stmt.executeUpdate("insert into JoinTest3(id3, name3) values(100, 'a11')");
		stmt.executeUpdate("insert into JoinTest3(id3, name3) values(200, 'a11')");

		stmt.executeUpdate("insert into JoinTest4(id, name) values(10, 'a1')");
		stmt.executeUpdate("insert into JoinTest4(id, name) values(10, 'a1')");
		stmt.executeUpdate("insert into JoinTest4(id, name) values(20, 'a1')");
		stmt.executeUpdate("insert into JoinTest4(id, name) values(30, 'a1')");

		sql = "select rownum, * from JoinTest1 LEFT OUTER JOIN JoinTest2";
		sql = "select rownum, * from JoinTest1 RIGHT OUTER JOIN JoinTest2";
		sql = "select rownum, * from JoinTest1 INNER JOIN JoinTest2";
		sql = "select rownum, * from JoinTest1 JOIN JoinTest2";
		sql = "select rownum, * from JoinTest1 CROSS JOIN JoinTest2";
		sql = "select rownum, * from JoinTest1 NATURAL JOIN JoinTest2";

		sql = "select rownum, * from JoinTest1 LEFT OUTER JOIN JoinTest3 NATURAL JOIN JoinTest2";
		sql = "FROM USER() SELECT * ";

		sql = "SELECT * FROM (JoinTest1)";
		sql = "SELECT * FROM (JoinTest1 LEFT OUTER JOIN (JoinTest2))";

		sql = "SELECT * FROM JoinTest1 LEFT OUTER JOIN JoinTest2 LEFT OUTER JOIN JoinTest3";

		sql = "SELECT t1.id, t1.b FROM JoinTest1 t1 NATURAL JOIN JoinTest4 t2";

		//org.h2.table.TableFilter.next()
		//��ϵ�:table.getName().equalsIgnoreCase("JoinTest1") || table.getName().equalsIgnoreCase("JoinTest2")
		sql = "SELECT rownum, * FROM JoinTest1 LEFT OUTER JOIN JoinTest2 ON id>30";
		sql = "SELECT rownum, * FROM JoinTest1 RIGHT OUTER JOIN JoinTest2 ON id2>70";
		sql = "SELECT rownum, * FROM JoinTest1 JOIN JoinTest2 ON id>30";

		sql = "SELECT rownum, * FROM JoinTest1 LEFT OUTER JOIN JoinTest2 JOIN JoinTest3";

		sql = "SELECT rownum, * FROM (JoinTest1) LEFT OUTER JOIN JoinTest2 ON id>30";

		sql = "SELECT rownum, * FROM JoinTest1 LEFT OUTER JOIN (JoinTest2) ON id>30";
		sql = "SELECT rownum, * FROM JoinTest1 JOIN JoinTest2 ON id>30";

		sql = "SELECT rownum, * FROM JoinTest1 LEFT OUTER JOIN JoinTest2 ON id>30 WHERE 1>2";

		sql = "SELECT rownum, * FROM JoinTest1 LEFT OUTER JOIN JoinTest2 ON name2=null";

		sql = "SELECT rownum, * FROM JoinTest1 LEFT OUTER JOIN JoinTest2 ON id2=90";

		sql = "SELECT rownum, * FROM JoinTest1 LEFT OUTER JOIN JoinTest2 WHERE id2=90";
		sql = "SELECT rownum, * FROM JoinTest1 JOIN JoinTest2 WHERE id2=90";

		sql = "SELECT rownum, * FROM JoinTest1 JOIN JoinTest2 WHERE id2=90 and name='a1' and b=true";
		sql = "SELECT rownum, * FROM JoinTest1 JOIN JoinTest2 WHERE id2=90 and name='a1' and id=10";

		sql = "SELECT rownum, * FROM JoinTest1 WHERE name='a1' and id=10";
		executeQuery();

	}
	
	
    void init2() throws Exception {
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS JoinTest1(" //
                + "_rowkey_ int, id int, name varchar(500), b boolean)");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS JoinTest2(" //
                + "_rowkey_ int, id2 int, name2 varchar(500))");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS JoinTest3(" //
                + "_rowkey_ int, id3 int, name3 varchar(500))");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS JoinTest4(" //
                + "_rowkey_ int, id int, name varchar(500))");

        //insert();
    }

    void insert2() throws Exception {

        stmt.executeUpdate("insert into JoinTest1(_rowkey_, id, name, b) values(1, 10, 'a1', true)");
        stmt.executeUpdate("insert into JoinTest1(_rowkey_, id, name, b) values(2, 20, 'b1', true)");
        stmt.executeUpdate("insert into JoinTest1(_rowkey_, id, name, b) values(3, 30, 'a2', false)");
        stmt.executeUpdate("insert into JoinTest1(_rowkey_, id, name, b) values(4, 40, 'b2', true)");

        stmt.executeUpdate("insert into JoinTest2(_rowkey_, id2, name2) values(1, 60, 'a11')");
        stmt.executeUpdate("insert into JoinTest2(_rowkey_, id2, name2) values(2, 70, 'a11')");
        stmt.executeUpdate("insert into JoinTest2(_rowkey_, id2, name2) values(3, 80, 'a11')");
        stmt.executeUpdate("insert into JoinTest2(_rowkey_, id2, name2) values(4, 90, 'a11')");

        stmt.executeUpdate("insert into JoinTest3(_rowkey_, id3, name3) values(1, 100, 'a11')");
        stmt.executeUpdate("insert into JoinTest3(_rowkey_, id3, name3) values(2, 200, 'a11')");

        stmt.executeUpdate("insert into JoinTest4(_rowkey_, id, name) values(1, 10, 'a1')");
        stmt.executeUpdate("insert into JoinTest4(_rowkey_, id, name) values(2, 10, 'a1')");
        stmt.executeUpdate("insert into JoinTest4(_rowkey_, id, name) values(3, 20, 'a1')");
        stmt.executeUpdate("insert into JoinTest4(_rowkey_, id, name) values(4, 30, 'a1')");
    }

    void test2() throws Exception {

        sql = "select rownum, * from JoinTest1 LEFT OUTER JOIN JoinTest2";
        sql = "select rownum, * from JoinTest1 RIGHT OUTER JOIN JoinTest2";
        sql = "select rownum, * from JoinTest1 INNER JOIN JoinTest2";
        sql = "select rownum, * from JoinTest1 JOIN JoinTest2";
        sql = "select rownum, * from JoinTest1 CROSS JOIN JoinTest2";
        sql = "select rownum, * from JoinTest1 NATURAL JOIN JoinTest2";

        sql = "select rownum, * from JoinTest1 LEFT OUTER JOIN JoinTest3 NATURAL JOIN JoinTest2";
        sql = "FROM USER() SELECT * ";

        sql = "SELECT * FROM (JoinTest1)";
        sql = "SELECT * FROM (JoinTest1 LEFT OUTER JOIN (JoinTest2))";

        sql = "SELECT * FROM JoinTest1 LEFT OUTER JOIN JoinTest2 LEFT OUTER JOIN JoinTest3";

        sql = "SELECT t1.id, t1.b FROM JoinTest1 t1 NATURAL JOIN JoinTest4 t2";

        //org.h2.table.TableFilter.next()
        //��ϵ�:table.getName().equalsIgnoreCase("JoinTest1") || table.getName().equalsIgnoreCase("JoinTest2")
        sql = "SELECT rownum, * FROM JoinTest1 LEFT OUTER JOIN JoinTest2 ON id>30";
        sql = "SELECT rownum, * FROM JoinTest1 RIGHT OUTER JOIN JoinTest2 ON id2>70";
        sql = "SELECT rownum, * FROM JoinTest1 JOIN JoinTest2 ON id>30";

        sql = "SELECT rownum, * FROM JoinTest1 LEFT OUTER JOIN JoinTest2 JOIN JoinTest3";

        sql = "SELECT rownum, * FROM (JoinTest1) LEFT OUTER JOIN JoinTest2 ON id>30";

        sql = "SELECT rownum, * FROM JoinTest1 LEFT OUTER JOIN (JoinTest2) ON id>30";
        sql = "SELECT rownum, * FROM JoinTest1 JOIN JoinTest2 ON id>30";

        sql = "SELECT rownum, * FROM JoinTest1 LEFT OUTER JOIN JoinTest2 ON id>30 WHERE 1>2";

        sql = "SELECT rownum, * FROM JoinTest1 LEFT OUTER JOIN JoinTest2 ON name2=null";

        sql = "SELECT rownum, * FROM JoinTest1 JOIN JoinTest2 WHERE JoinTest1._rowkey_ = 1 and JoinTest2._rowkey_ = 2";
        sql = "SELECT rownum, * FROM JoinTest1 JOIN JoinTest2 WHERE JoinTest1._ROWID_ = 1 and JoinTest2._ROWID_ = 2";
        //sql = "SELECT rownum, * FROM JoinTest1";
        executeQuery();

    }
}
