package my.test.expression;

import my.test.TestBase;

public class ConditionInTest extends TestBase {
	public static void main(String[] args) throws Exception {
		new ConditionInTest().start();
	}

	//����org.h2.expression.ConditionIn
	@Override
	public void startInternal() throws Exception {
		stmt.executeUpdate("drop table IF EXISTS ConditionInTest");
		stmt.executeUpdate("create table IF NOT EXISTS ConditionInTest(id int, name varchar(500))");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS ConditionInTestIndex ON ConditionInTest(name)");

		stmt.executeUpdate("insert into ConditionInTest(id, name) values(1, 'a1')");
		stmt.executeUpdate("insert into ConditionInTest(id, name) values(1, 'b1')");
		stmt.executeUpdate("insert into ConditionInTest(id, name) values(2, 'a2')");
		stmt.executeUpdate("insert into ConditionInTest(id, name) values(2, 'b2')");
		stmt.executeUpdate("insert into ConditionInTest(id, name) values(3, 'a3')");
		stmt.executeUpdate("insert into ConditionInTest(id, name) values(3, 'b3')");

		sql = "select count(*) from ConditionInTest where id in()";
		sql = "select count(*) from ConditionInTest where not id in()";

		//��id in(+(select id from SubqueryTest where id=1 and name='a1'))
		//Subquery�ļ�¼���ܶ���1����������ǰ���+�žͿ����ƹ�isSelect()����ʱ���صľ���һ��Subquery
		//���Ƿż����ǲ��еģ���õ�һ��Operation
		//+�Ż�ת��ConditionInSelect��������ʹ��ConditionIn
		sql = "select count(*) from ConditionInTest where id in(+(select id from SubqueryTest where id=1 and name='a1'))";
		sql = "select count(*) from ConditionInTest where id in(-(select id from SubqueryTest where id=1 and name='a1'))";
		sql = "select count(*) from ConditionInTest where id in(3, (select id from SubqueryTest where id=1 and name='a1'))";

		sql = "select count(*) from ConditionInTest where null in(1,2)";
		//		sql = "select count(*) from ConditionInTest where 2 in(1,2)";
		//		sql = "select count(*) from ConditionInTest where id in(2)";
		//		sql = "select count(*) from ConditionInTest where id in(30,40,null)";
		sql = "select count(*) from ConditionInTest where id in(1,2,2)"; //ֵ�б������ظ�

		//��ConditionInSelect��һ��������û��ALL��ANY
		//sql = "select count(*) from ConditionInTest where id > ALL(1,2)";
		//ANY��SOMEһ��
		//sql = "select count(*) from ConditionInTest where id > ANY(1,2)";
		//sql = "select count(*) from ConditionInTest where id > SOME(1,2)";

		//sql = "select count(*) from ConditionInTest";
		executeQuery();
	}
}
