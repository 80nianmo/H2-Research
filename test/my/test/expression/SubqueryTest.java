package my.test.expression;

import my.test.TestBase;

public class SubqueryTest extends TestBase {
	public static void main(String[] args) throws Exception {
		new SubqueryTest().start();
	}

	@Override
	public void startInternal() throws Exception {
		stmt.executeUpdate("drop table IF EXISTS SubqueryTest");
		stmt.executeUpdate("create table IF NOT EXISTS SubqueryTest(id int, name varchar(500))");

		stmt.executeUpdate("insert into SubqueryTest(id, name) values(1, 'a1')");
		stmt.executeUpdate("insert into SubqueryTest(id, name) values(1, 'b1')");
		stmt.executeUpdate("insert into SubqueryTest(id, name) values(2, 'a2')");
		stmt.executeUpdate("insert into SubqueryTest(id, name) values(2, 'b2')");
		stmt.executeUpdate("insert into SubqueryTest(id, name) values(3, 'a3')");
		stmt.executeUpdate("insert into SubqueryTest(id, name) values(3, 'b3')");

		//�ϸ���˵����sql����Subquery�������in��ALL��ANY��SOME��ֻ����ͨ��select
		//Subquery�������������ܴ���1����in��ALL��ANY��SOMEû���ƣ�
		//��һ��Ҳ��⣬����id> (select id from SubqueryTest where id>1)������Subquery����1�У���ôid�Ͳ�֪����˭�Ƚ�
		//sql = "select * from SubqueryTest where id > (select id from SubqueryTest where id>1)";
		//����Subquery�����ж���
		sql = "select * from SubqueryTest where id > (select id, name from SubqueryTest where id=1 and name='a1')";

		executeQuery();
	}

}
