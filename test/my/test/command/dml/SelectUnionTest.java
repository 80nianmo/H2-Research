package my.test.command.dml;

import my.test.TestBase;

public class SelectUnionTest extends TestBase {
	public static void main(String[] args) throws Exception {
		new SelectUnionTest().start();
	}

	@Override
	public void startInternal() throws Exception {
		stmt.executeUpdate("DROP TABLE IF EXISTS SelectUnionTest1");
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS SelectUnionTest1(id int, name varchar(500), b boolean)");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS SelectUnionTestIndex1 ON SelectUnionTest1(name)");

		stmt.executeUpdate("DROP TABLE IF EXISTS SelectUnionTest2");
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS SelectUnionTest2(id int, name varchar(500), b boolean)");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS SelectUnionTestIndex2 ON SelectUnionTest2(name)");

		stmt.executeUpdate("insert into SelectUnionTest1(id, name, b) values(1, 'a1', true)");
		stmt.executeUpdate("insert into SelectUnionTest1(id, name, b) values(1, 'b1', true)");
		stmt.executeUpdate("insert into SelectUnionTest1(id, name, b) values(2, 'a2', false)");
		stmt.executeUpdate("insert into SelectUnionTest1(id, name, b) values(2, 'b2', true)");
		stmt.executeUpdate("insert into SelectUnionTest1(id, name, b) values(3, 'a3', false)");
		stmt.executeUpdate("insert into SelectUnionTest1(id, name, b) values(3, 'b3', true)");

		stmt.executeUpdate("insert into SelectUnionTest2(id, name, b) values(4, 'a1', true)");
		stmt.executeUpdate("insert into SelectUnionTest2(id, name, b) values(4, 'b1', true)");
		stmt.executeUpdate("insert into SelectUnionTest2(id, name, b) values(5, 'a2', false)");
		stmt.executeUpdate("insert into SelectUnionTest2(id, name, b) values(5, 'b2', true)");
		stmt.executeUpdate("insert into SelectUnionTest2(id, name, b) values(6, 'a3', false)");
		stmt.executeUpdate("insert into SelectUnionTest2(id, name, b) values(6, 'b3', true)");

		stmt.executeUpdate("insert into SelectUnionTest2(id, name, b) values(3, 'a3', false)");
		stmt.executeUpdate("insert into SelectUnionTest2(id, name, b) values(3, 'b3', true)");

		//��ѯ�и�������һ��
		sql = "select id from SelectUnionTest1 UNION select name, b from SelectUnionTest2 order by id";

		//��ѯ��������һ��
		sql = "select id from SelectUnionTest1 UNION select name from SelectUnionTest2 order by id";

		sql = "select id from SelectUnionTest1 UNION select id from SelectUnionTest2 order by id";

		//�������ǵȼ۵ģ�û���ظ�
		sql = "select * from SelectUnionTest1 UNION select * from SelectUnionTest2 order by id";
		sql = "select * from SelectUnionTest1 UNION DISTINCT select * from SelectUnionTest2 order by id";

		//���ظ�
		sql = "select * from SelectUnionTest1 UNION ALL select * from SelectUnionTest2 order by id";

		//�������ǵȼ۵ģ���������ظ�
		sql = "select * from SelectUnionTest1 EXCEPT select * from SelectUnionTest2 order by id";
		sql = "select * from SelectUnionTest1 MINUS select * from SelectUnionTest2 order by id";
		
		sql = "select * from SelectUnionTest1 INTERSECT select * from SelectUnionTest2 order by id";
		executeQuery();
	}
}
