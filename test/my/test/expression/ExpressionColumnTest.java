package my.test.expression;

import my.test.TestBase;

public class ExpressionColumnTest extends TestBase {
	public static void main(String[] args) throws Exception {
		new ExpressionColumnTest().start();
	}

	//����org.h2.expression.ExpressionColumn
	@Override
	public void startInternal() throws Exception {
		stmt.executeUpdate("drop table IF EXISTS ExpressionColumnTest");
		stmt.executeUpdate("create table IF NOT EXISTS ExpressionColumnTest(id int, name varchar(500))");

		stmt.executeUpdate("insert into ExpressionColumnTest(id, name) values(1, 'a1')");
		stmt.executeUpdate("insert into ExpressionColumnTest(id, name) values(1, 'b1')");
		stmt.executeUpdate("insert into ExpressionColumnTest(id, name) values(2, 'a2')");
		stmt.executeUpdate("insert into ExpressionColumnTest(id, name) values(2, 'b2')");
		stmt.executeUpdate("insert into ExpressionColumnTest(id, name) values(3, 'a3')");
		stmt.executeUpdate("insert into ExpressionColumnTest(id, name) values(3, 'b3')");

		//public.ExpressionColumnTest.id�������ǲ��Ե�
		//��org.h2.expression.ExpressionColumn.optimize(Session)�м��
		sql = "SELECT public.ExpressionColumnTest.id FROM ExpressionColumnTest as t";
		//public.t.id�������У���Ϊ�ұ���as t
		sql = "SELECT public.t.id FROM ExpressionColumnTest as t";
		sql = "SELECT _rowid_, id, name FROM ExpressionColumnTest WHERE _rowid_>2";

		executeQuery();
	}
}
