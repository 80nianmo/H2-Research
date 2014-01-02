package my.test.expression;

import my.test.TestBase;

public class ConditionInSelectTest extends TestBase {
	public static void main(String[] args) throws Exception {
		new ConditionInSelectTest().start();
	}

	//����org.h2.expression.ConditionInSelect
	@Override
	public void startInternal() throws Exception {
		stmt.executeUpdate("drop table IF EXISTS ConditionInSelectTest");
		stmt.executeUpdate("create table IF NOT EXISTS ConditionInSelectTest(id int, name varchar(500))");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS ConditionInSelectTestIndex ON ConditionInSelectTest(name)");

		stmt.executeUpdate("insert into ConditionInSelectTest(id, name) values(1, 'a1')");
		stmt.executeUpdate("insert into ConditionInSelectTest(id, name) values(1, 'b1')");
		stmt.executeUpdate("insert into ConditionInSelectTest(id, name) values(2, 'a2')");
		stmt.executeUpdate("insert into ConditionInSelectTest(id, name) values(2, 'b2')");
		stmt.executeUpdate("insert into ConditionInSelectTest(id, name) values(3, 'a3')");
		stmt.executeUpdate("insert into ConditionInSelectTest(id, name) values(3, 'b3')");

		sql = "delete top 3 from ConditionInSelectTest where id in(select id from ConditionInSelectTest where id=3)";
		//�Ӳ�ѯ���ܶ���1����
		//sql = "delete from ConditionInSelectTest where id in(select id,name from ConditionInSelectTest where id=3)";
		sql = "delete from ConditionInSelectTest where id in(select id from ConditionInSelectTest where id>2)";

		//sql = "delete from ConditionInSelectTest where id > ALL(select id from ConditionInSelectTest where id>10)";
		//ANY��SOMEһ��
		//sql = "delete from ConditionInSelectTest where id > ANY(select id from ConditionInSelectTest where id>1)";
		//sql = "delete from ConditionInSelectTest where id > SOME(select id from ConditionInSelectTest where id>10)";
		
		//�ϸ���˵����sql����Subquery�������in��ALL��ANY��SOME��ֻ����ͨ��select
		//Subquery�������������ܴ���1����in��ALL��ANY��SOMEû���ƣ�
		//��һ��Ҳ��⣬����id> (select id from ConditionInSelectTest where id>1)������Subquery����1�У���ôid�Ͳ�֪����˭�Ƚ�
		//sql = "delete from ConditionInSelectTest where id > (select id from ConditionInSelectTest where id>1)";
	    //����Subquery�����ж���
		//sql = "delete from ConditionInSelectTest where id > (select id, name from ConditionInSelectTest where id=1 and name='a1')";
		stmt.executeUpdate(sql);

		//		sql = "delete top 3 from ConditionInSelectTest where name > ?";
		//		ps = conn.prepareStatement(sql);
		//		ps.setString(1, "b1");
		//		ps.executeUpdate();

		sql = "select * from ConditionInSelectTest";
		executeQuery();
	}
}
