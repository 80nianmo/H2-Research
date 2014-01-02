package my.test.command.ddl;

import my.test.TestBase;

public class CreateViewTest extends TestBase {
	public static void main(String[] args) throws Exception {

		new CreateViewTest().start();
		System.out.println(Integer.bitCount(3));
		System.out.println(Integer.bitCount(1));
		System.out.println(Integer.bitCount(2));
		System.out.println(Integer.bitCount(7));
	}

	//����org.h2.command.Parser.parseCreateView(boolean, boolean)
	//org.h2.command.ddl.CreateView
	@Override
	public void startInternal() throws Exception {
		//stmt.executeUpdate("drop table IF EXISTS CreateViewTest CASCADE");
		stmt.executeUpdate("create table IF NOT EXISTS CreateViewTest(id int, name varchar(500), b boolean)");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS CreateViewTestIndex ON CreateViewTest(name)");

		stmt.executeUpdate("insert into CreateViewTest(id, name, b) values(1, 'a1', true)");
		stmt.executeUpdate("insert into CreateViewTest(id, name, b) values(1, 'b1', true)");
		stmt.executeUpdate("insert into CreateViewTest(id, name, b) values(2, 'a2', false)");
		stmt.executeUpdate("insert into CreateViewTest(id, name, b) values(2, 'b2', true)");
		stmt.executeUpdate("insert into CreateViewTest(id, name, b) values(3, 'a3', false)");
		stmt.executeUpdate("insert into CreateViewTest(id, name, b) values(3, 'b3', true)");

		//stmt.executeUpdate("DROP VIEW IF EXISTS my_view");
		sql = "CREATE OR REPLACE FORCE VIEW IF NOT EXISTS my_view COMMENT IS 'my view'(f1,f2) " //
				+ "AS SELECT id,name FROM CreateViewTest";

		sql = "CREATE OR REPLACE FORCE VIEW my_view COMMENT IS 'my view'(f1,f2) " //
				+ "AS SELECT id,name FROM CreateViewTest";

		//select�ֶθ�����view�ֶζ�������������İ�select�ֶ�ԭ������
		//����ʵ����f1��name
		sql = "CREATE OR REPLACE FORCE VIEW my_view COMMENT IS 'my view'(f1) " //
				+ "AS SELECT id,name FROM CreateViewTest";

		//select�ֶθ�����view�ֶ��ٵ������view���ٵ��ֶα�����
		//����ʵ����f1����f2�������ˣ�Ҳ����ʾ����
		sql = "CREATE OR REPLACE FORCE VIEW my_view COMMENT IS 'my view'(f1, f2) " //
				+ "AS SELECT id FROM CreateViewTest";

		//���ܼӲ���FORCE��������Ҳһ��
		sql = "CREATE OR REPLACE VIEW my_view COMMENT IS 'my view'(f1, f2) " //
				+ "AS SELECT id FROM CreateViewTest";

		sql = "CREATE OR REPLACE FORCE VIEW my_view COMMENT IS 'my view'(f1,f2) " //
				+ "AS SELECT id,name FROM CreateViewTest";

		stmt.executeUpdate("CREATE OR REPLACE FORCE VIEW view1 AS SELECT f1 FROM my_view");
		stmt.executeUpdate("CREATE OR REPLACE FORCE VIEW view2 AS SELECT f2 FROM my_view");

		//		sql = "CREATE OR REPLACE FORCE VIEW my_view COMMENT IS 'my view'(f1,f2) " //
		//			+ "AS SELECT top 2 id,name FROM CreateViewTest order by id";
		//		
		//��������������������Ҫ����ͼʱҲҪ��CreateViewTest���ֶ�������
		//sql = "CREATE OR REPLACE FORCE VIEW IF NOT EXISTS my_view " //
		//		+ "AS SELECT id,name FROM CreateViewTest";

		//Ŀǰ��֧�ֲ���:
		//org.h2.jdbc.JdbcSQLException: Feature not supported: "parameters in views"; SQL statement:
		//sql = "CREATE OR REPLACE FORCE VIEW IF NOT EXISTS my_view (f1,f2) AS SELECT id,name FROM CreateViewTest where id=?";
		//		ps = conn.prepareStatement(sql);
		//		ps.setInt(1, 2);
		//		ps.executeUpdate();
		stmt.executeUpdate(sql);

		sql = "select * from my_view where f1 > 2";
		sql = "select * from my_view where f2 > 'b1'";
		sql = "select * from my_view where f2 between 'b1' and 'b2'";

		sql = "select * from my_view where f1=2 and f2 between 'b1' and 'b2'";

		//		sql = "select name from (select id,name from CreateViewTest where id=? and name=?) where name='b2'";
		//		ps = conn.prepareStatement(sql);
		//		ps.setInt(1, 2);
		//		ps.setString(2, "b2");
		//		ps.executeQuery();

		//sql = "select * from CreateViewTest";

		//����org.h2.command.Parser.parserWith()
		//stmt.executeUpdate("CREATE LOCAL TEMPORARY TABLE IF NOT EXISTS my_tmp_table(f1 int)");
		//stmt.executeUpdate("DROP VIEW IF EXISTS my_tmp_table");
		//stmt.executeUpdate("CREATE OR REPLACE FORCE VIEW my_tmp_table AS SELECT f2 FROM my_view");
		//sql = "WITH RECURSIVE my_tmp_table(f1,f2) AS(select id,name from CreateViewTest) select f1, f2 from my_tmp_table";
		//sql = "WITH my_tmp_table(f1,f2) AS(select id,name from CreateViewTest) select f1, f2 from my_tmp_table";

		//AS���������UNION ALL
		sql = "WITH RECURSIVE my_tmp_table(f1,f2) AS(select id,name from CreateViewTest UNION ALL select 1, 2)"
				+ "select f1, f2 from my_tmp_table";

		sql = "WITH RECURSIVE my_tmp_table(f1,f2) AS(select id,name from CreateViewTest UNION ALL select id,name from CreateViewTest)"
				+ "select f1, f2 from my_tmp_table";

		//������from��������ţ���ʱfrom����ı���Ϊ��һ����ʱ��ͼ
		sql = "select f1, f2 from (select id,name from CreateViewTest)"; //f1,f2�Ҳ���
		sql = "select id,name from (select id,name from CreateViewTest)";
		
		//��������ʹ��parameters.size>0
		sql = "CREATE OR REPLACE FORCE VIEW IF NOT EXISTS my_view2(f1,f2) " //
				+ "AS select id,name from (select id,name from CreateViewTest) where id=? and name=?";
		
		//��������
//		sql = "CREATE OR REPLACE FORCE VIEW IF NOT EXISTS my_view2(f1,f2) " //
//			+ "AS select id,name from (select id,name from CreateViewTest where id=? and name=?)";
//
//		ps = conn.prepareStatement(sql);
//		ps.setInt(1, 2);
//		ps.setString(2, "b2");
//		ps.executeUpdate();
		
		sql = "select id,name from (select id,name from CreateViewTest where id=? and name=?)";
		ps = conn.prepareStatement(sql);
		ps.setInt(1, 2);
		ps.setString(2, "b2");
		rs = ps.executeQuery();
		printResultSet(rs);

		sql = "select * from my_view2";
		sql = "select * from my_view2 where f1=2 and f2 between 'b1' and 'b2'";
		
		
		//stmt.setFetchSize(2);
		//executeQuery();
	}
}