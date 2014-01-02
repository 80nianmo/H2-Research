package my.test.command.dml;

import java.sql.SQLException;

import my.test.TestBase;

public class SelectTest extends TestBase {
	public static void main(String[] args) throws Exception {
		new SelectTest().start();
	}

	public void init() throws Exception {
		prop.setProperty("MODE", "DB2"); //֧��SYSDUMMY1��supportOffsetFetch
		prop.setProperty("PAGE_SIZE", "128");
		//		prop.setProperty("TRACE_LEVEL_FILE", "10");
		//		prop.setProperty("TRACE_LEVEL_SYSTEM_OUT", "20");
		//		prop.setProperty("PAGE_SIZE", "1024");
		//		prop.setProperty("FILE_LOCK", "FS");
	}

	@Override
	public void startInternal() throws Exception {
		stmt.executeUpdate("drop table IF EXISTS mytable,natural_join_test_table1,natural_join_test_table2,mytable1,mytable2");
		//stmt.executeUpdate("create table IF NOT EXISTS mytable(id int primary key, name varchar(500))");
		stmt.executeUpdate("create table IF NOT EXISTS mytable(id int, name varchar(500))");
		stmt.executeUpdate("ALTER TABLE mytable ALTER COLUMN id SELECTIVITY 10");
		stmt.executeUpdate("ALTER TABLE mytable ALTER COLUMN name SELECTIVITY 10");
		stmt.executeUpdate("ALTER TABLE mytable ADD CONSTRAINT NAME_UNIQUE UNIQUE(name,id)");
		stmt.executeUpdate("create index IF NOT EXISTS mytable_index on mytable(id)");

		stmt.executeUpdate("create table IF NOT EXISTS mytable1(id1 int primary key, name1 varchar(500))");
		stmt.executeUpdate("create table IF NOT EXISTS mytable2(id2 int primary key, name2 varchar(500))");
		stmt.executeUpdate("create table IF NOT EXISTS mytable3(id3 int primary key, name3 varchar(500))");
		stmt.executeUpdate("create table IF NOT EXISTS mytable4(id4 int primary key, name4 varchar(500))");
		stmt.executeUpdate("create table IF NOT EXISTS mytable5(id5 int primary key, name5 varchar(500))");
		stmt.executeUpdate("create table IF NOT EXISTS mytable6(id6 int primary key, name6 varchar(500))");
		stmt.executeUpdate("create table IF NOT EXISTS mytable7(id7 int primary key, name7 varchar(500))");

		stmt.executeUpdate("create table IF NOT EXISTS natural_join_test_table1(id int primary key, name varchar(500), age1 int)");
		stmt.executeUpdate("create table IF NOT EXISTS natural_join_test_table2(id int primary key, name varchar(500), age2 int)");

		for (int i = 1; i <= 0; i++) {
			if (i % 2 == 0)
				stmt.executeUpdate("insert into mytable(id, name) values(" + i + ", '" + i + "abcdef1234')");
			else
				stmt.executeUpdate("insert into mytable(id, name) values(" + i + ", null)");
			stmt.executeUpdate("insert into natural_join_test_table1(id, name) values(" + i + ", 'abcdef1234')");
			stmt.executeUpdate("insert into natural_join_test_table2(id, name) values(" + i + ", 'abcdef1234')");
			stmt.executeUpdate("insert into mytable1(id1, name1) values(" + i + ", 'abcdef1234')");
			stmt.executeUpdate("insert into mytable2(id2, name2) values(" + i * 10 + ", 'abcdef1234')");
		}
		stmt.executeUpdate("insert into mytable(id, name) values(" + 1 + ", '" + 1 + "abcdef1234')");
		stmt.executeUpdate("insert into mytable(id, name) values(" + 1 + ", '" + 2 + "abcdef1234')");
		stmt.executeUpdate("insert into mytable(id, name) values(" + 2 + ", '" + 3 + "abcdef1234')");
		stmt.executeUpdate("insert into mytable(id, name) values(" + 2 + ", '" + 4 + "abcdef1234')");
		stmt.executeUpdate("insert into mytable(id, name) values(" + 3 + ", '" + 5 + "abcdef1234')");
		stmt.executeUpdate("insert into mytable(id, name) values(" + 3 + ", '" + 6 + "abcdef1234')");

		parseSelectSimpleSelectPart();
		readTableFilter();
		parseEndOfQuery();
		parseSelectSimpleFromPart();

		//����org.h2.command.dml.Select.queryGroup(int, LocalResult)
		sql = "SELECT DISTINCT count(*),max(id),min(id),sum(id) FROM mytable ";

		//����org.h2.command.dml.Select.queryQuick(int, ResultTarget)
		//sql = "SELECT DISTINCT count(*) FROM mytable ";

		//sql = "SELECT DISTINCT LENGTH(NAME) FROM mytable ";

		//����org.h2.command.dml.Select.queryDistinct(ResultTarget, long)
		sql = "select distinct name from mytable";

		select_init();
		Query_initOrder();

		sql = "select name from mytable where id=3";

		sql = "select max(name) from mytable";
		queryGroup();
		queryGroupSorted();

		queryQuick();
		rs = stmt.executeQuery(sql);
		int n = rs.getMetaData().getColumnCount();
		while (rs.next()) {
			for (int i = 1; i <= n; i++) {
				System.out.print(rs.getString(i) + " ");
			}
			System.out.println();
			//System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4));
		}

	}

	//������org.h2.command.Parser.parseSelectSimpleSelectPart(Select)
	void parseSelectSimpleSelectPart() {

		sql = "select 2";
		sql = "select TOP 10 DISTINCT * from mytable";
		sql = "select LIMIT 2 5 *, id, name n, name as n2 from mytable";
		sql = "select LIMIT 2 5 t.* from mytable t";

		sql = "select * from mytable";

		sql = "select 1 union select 1";

		sql = "select (select 1)";

		sql = "select 1";

		sql = "select id, name from mytable where id>0";

		sql = "select public.t.* from mytable as t where id>199";

		sql = "select public.t.id, *, name from mytable as t where id>199 group by id having id>99 order by t.id desc";
		sql = "select id,name from mytable as t where id>199 group by id having id>99 order by t.id desc";
		sql = "select name from mytable as t where id>199 group by id having id>99 order by t.id desc";

		//		sql = "select id, name from mytable as t where id>199  order by t.id desc";
		//		sql = "select name from mytable as t where id>199  order by id desc";
		//		sql = "select distinct name from mytable as t where id>199  order by id desc";
		//		sql = "select distinct name from mytable order by id desc";
		//		sql = "select name from mytable order by id desc";
		//		sql = "SELECT 1 AS A FROM DUAL ORDER BY -A"; //�д� Column "A" not found; SQL statement:
		//		sql = "SELECT 1 AS A FROM DUAL ORDER BY A";

		sql = "select * from mytable1 t1 LEFT OUTER JOIN mytable2 t2 on t1.id1=t2.id2 where t1.id1>100 group by t1.id1 having t1.id1>150 order by t1.id1 desc";
		//sql = "select * from mytable1 t1 RIGHT OUTER JOIN mytable2 t2 on t1.id1=t2.id2 where t1.id1>100 group by t1.id1 having t1.id1>150 order by t1.id1 desc";

		//sql = "select id, name from mytable where id>198";

		sql = "select id1,name2 from mytable1 t1 LEFT OUTER JOIN mytable2 t2 on t1.id1=t2.id2 where t1.id1>100 group by id1,name1 having t1.id1>150 order by t1.id1 desc";
		sql = "select id1,name2 from mytable1 t1 LEFT OUTER JOIN mytable2 t2 on t1.id1=t2.id2 where t1.id1>100 group by id1,name1 having id2>150 order by t1.id1 desc, name2";
		sql = "select count(id) from mytable";
		sql = "select distinct name from mytable";
	}

	//����org.h2.command.Parser.readTableFilter(boolean)
	void readTableFilter() {
		sql = "FROM (select 1, 2) SELECT * ";
		sql = "FROM ((select 1, 2)) SELECT * ";
		sql = "FROM (((select 1, 2))) SELECT * ";

		sql = "FROM (mytable) SELECT * ";
		sql = "FROM (mytable1 RIGHT OUTER JOIN mytable2 ON mytable1.id1=mytable2.id2) AS t SELECT * ";

		sql = "FROM VALUES(1, 'Hello'), (2, 'World') AS t SELECT * ";

		sql = "FROM SYSTEM_RANGE(1,100) SELECT * ";
		sql = "FROM TABLE(ID INT=(1, 2), NAME VARCHAR=('Hello', 'World')) SELECT * ";
		//sql = "FROM USER() SELECT * "; //��������ֵ���ͱ�����RESULT_SET
		sql = "FROM DUAL SELECT * ";
		sql = "FROM SYSDUMMY1 SELECT * "; //Ҫ��prop.setProperty("MODE", "DB2")

		sql = "FROM mytable SELECT * ";

	}

	//���²��ֲ���org.h2.command.Parser.parseEndOfQuery(Query)
	void parseEndOfQuery() {

		sql = "select name from mytable order by id";

		//		sql = "(select name from mytable order by id)";
		//		sql = "(select name from (select name from mytable where id=? and name=?) where id=?)";
		//		sql = "(select name from mytable where id=? and id in(select name from mytable where id=? and name=?))";
		//		ps = conn.prepareStatement(sql);
		//		ps.setInt(1, 1);
		//		ps.setInt(2, 1);
		//		ps.setString(3, "abc");
		//		rs = ps.executeQuery();

		//unionʱLIMIT��ordey by��FOR UPDATE���ܷ����Ӿ��У�Ҫ�������
		//���������Ǵ����:
		//sql = "select name1 from mytable1 order by name1 union select name2 from mytable2";
		//Ҫ�ĳ�����:
		sql = "select name1 from mytable1 union select name2 from mytable2 order by name1";

		sql = "select id,name from mytable order by id DESC";
		sql = "select id,name from mytable order by -1";

		//		sql = "select id,name from mytable order by ?";
		//		ps = conn.prepareStatement(sql);
		//		ps.setInt(1, -1);
		//		rs = ps.executeQuery();
		sql = "select id,name from mytable order by =-1 DESC";
		sql = "select id,name from mytable order by -1 DESC"; //������ʾ�����ټ�DESC�ͱ�ʾ����Ľ���ʵ�ʾ�������
		sql = "select id,name from mytable order by -2 NULLS FIRST";
		sql = "select id,name from mytable order by -2 NULLS LAST";

		//OFFSET Ҫ��ROW��ROWS��������
		sql = "select id,name from mytable order by -2 NULLS LAST OFFSET 2 ROW";
		sql = "select id,name from mytable order by -2 NULLS LAST OFFSET 2 ROWS";

		//�������еȼ� FETCH FIRST ROW ONLY��FETCH NEXT ROW ONLYһ��
		sql = "select id,name from mytable order by -2 NULLS LAST OFFSET 2 ROWS FETCH FIRST ROW ONLY";
		sql = "select id,name from mytable order by -2 NULLS LAST OFFSET 2 ROWS FETCH NEXT ROW ONLY";

		//�������еȼ�
		sql = "select id,name from mytable order by -2 NULLS LAST OFFSET 2 ROWS FETCH NEXT 1 ROW ONLY";
		sql = "select id,name from mytable order by -2 NULLS LAST OFFSET 2 ROWS FETCH NEXT 4 ROWS ONLY";

		//�������еȼ�
		sql = "select id,name from mytable order by -2 NULLS LAST OFFSET 2 ROWS FETCH FIRST 1 ROW ONLY";
		sql = "select id,name from mytable order by -2 NULLS LAST OFFSET 2 ROWS FETCH FIRST 4 ROWS ONLY";

		sql = "select id,name from mytable order by id LIMIT 2 OFFSET 3"; //OFFSET��0�㣬OFFSET 3���Ǵӵ�4����¼��ʼ

		sql = "select id,name from mytable order by id LIMIT 3, 2"; //������Ч��һ��"3, 2"��ʾOFFSET 3 LIMIT 2
		sql = "select id,name from mytable order by id LIMIT 3, 5 SAMPLE_SIZE 2"; //SAMPLE_SIZE 2ʱ�������¼
		sql = "select id,name from mytable order by id LIMIT 3, 5 SAMPLE_SIZE 6"; //SAMPLE_SIZE 6ʱ�����¼��
		sql = "select id,name from mytable FOR UPDATE";
		sql = "select id,name from mytable FOR READ ONLY WITH RS"; //"FOR READ ONLY WITH RS"���п���
	}

	//����org.h2.command.Parser.parseSelectSimpleFromPart(Select)
	//org.h2.command.Parser.parseJoinTableFilter(TableFilter, Select)
	//org.h2.command.Parser.readJoin(TableFilter, Select, boolean, boolean)
	void parseSelectSimpleFromPart() {
		sql = "SELECT * FROM mytable1 RIGHT OUTER JOIN mytable2 ON mytable1.id1 = mytable2.id2";
		sql = "SELECT * FROM mytable1 LEFT OUTER JOIN mytable2 ON mytable1.id1 = mytable2.id2";
		//��֧��FULL JOIN
		//sql = "SELECT * FROM mytable1 FULL JOIN mytable2 ON mytable1.id1 = mytable2.id2";

		//INNER JOIN��JOINһ��
		sql = "SELECT * FROM mytable1 INNER JOIN mytable2 ON mytable1.id1 = mytable2.id2";
		sql = "SELECT * FROM mytable1 JOIN mytable2 ON mytable1.id1 = mytable2.id2";

		sql = "SELECT * FROM mytable1 JOIN mytable2";

		//CROSS JOIN���ܸ�ON
		//sql = "SELECT * FROM mytable1 CROSS JOIN mytable2 ON mytable1.id1 = mytable2.id2";
		//sql = "SELECT * FROM mytable1 CROSS JOIN mytable2";

		//NATURAL JOIN���ܸ�ON
		//sql = "SELECT * FROM mytable1 NATURAL JOIN mytable2 ON mytable1.id1 = mytable2.id2";
		//sql = "SELECT * FROM natural_join_test_table1 NATURAL JOIN natural_join_test_table2";

		//sql = "SELECT * FROM mytable1 INNER JOIN mytable2 ON mytable1.id1 = mytable2.id2 LEFT OUTER JOIN mytable3";

		//sql = "FROM ((select 1) union (select 1)) RIGHT OUTER JOIN ((select 2) union (select 2)) SELECT * ";
		//
		//		sql = "SELECT * FROM mytable1 RIGHT OUTER JOIN mytable2 LEFT OUTER JOIN mytable3 INNER JOIN mytable4 "
		//				+ "JOIN mytable5 CROSS JOIN mytable6 NATURAL JOIN mytable7 "
		//				+ " ON mytable7.id7 = mytable6.id6 ON mytable6.id6 = mytable5.id5 ON mytable5.id5 = mytable4.id4"
		//				+ " ON mytable4.id4 = mytable3.id3 ON mytable3.id3 = mytable2.id2 ON mytable2.id2 = mytable1.id1";
		//
		//		//ON���������ǲ��Ե�
		//		sql = "SELECT * FROM mytable1 RIGHT OUTER JOIN mytable2 LEFT OUTER JOIN mytable3 INNER JOIN mytable4 "
		//				+ "JOIN mytable5 CROSS JOIN mytable6" //
		//				+ " ON mytable5.id5 = mytable4.id4" //
		//				+ " ON mytable4.id4 = mytable3.id3 ON mytable3.id3 = mytable2.id2 ON mytable2.id2 = mytable1.id1";
		//
		//		sql = "SELECT * FROM mytable1 RIGHT OUTER JOIN mytable2 ON mytable2.id2 = mytable1.id1";
		//
		//		sql = "SELECT * FROM  mytable3 INNER JOIN mytable4 ON mytable4.id4 = mytable3.id3";
		//
		//		sql = "SELECT * FROM mytable1 RIGHT OUTER JOIN mytable2 LEFT OUTER JOIN mytable3 INNER JOIN mytable4 "
		//				+ "JOIN mytable5 CROSS JOIN mytable6 NATURAL JOIN mytable7 " + " ON mytable5.id5 = mytable4.id4"
		//				+ " ON mytable4.id4 = mytable3.id3 ON mytable3.id3 = mytable2.id2 ON mytable2.id2 = mytable1.id1";
		//
		//		//ON���������û����
		//		sql = "SELECT * FROM mytable1"//
		//				+ " RIGHT OUTER JOIN mytable2 ON mytable2.id2 = mytable1.id1"//
		//				+ " LEFT OUTER JOIN mytable3 ON mytable3.id3 = mytable2.id2"//
		//				+ " INNER JOIN mytable4 ON mytable4.id4 = mytable3.id3"//
		//				+ " JOIN mytable5 ON mytable5.id5 = mytable4.id4"//
		//				+ " CROSS JOIN mytable6"//
		//				+ " NATURAL JOIN mytable7";

	}

	void select_init() {
		sql = "select public.mytable.*, name from mytable";
		sql = "select public.mytable.*, name from mytable as t";
		//����myschema.t.*�������﷨����ʹmyschema�����ڣ���parser��Ҳû�б����
		//������org.h2.command.dml.Select.expandColumnList()�б���
		//��Ϊmytableû��ָ��schema��Ϊǰ׺������Ĭ����public�����myschema��ͬ��������ΪTable "T" not found;
		sql = "select DISTINCT myschema.t.*, name from mytable as t";

		sql = "select * from mytable1, mytable2";

		sql = "select id, name from natural_join_test_table1, natural_join_test_table2";

		sql = "select * from natural_join_test_table1  natural join natural_join_test_table2";
	}

	void Query_initOrder() throws SQLException {
		//���������ڵļ���Ƿ���org.h2.expression.ExpressionColumn.optimize(Session)����
		//Column "ID3" not found;
		sql = "select name,id3 from mytable order by 1*1";

		stmt.executeUpdate("CREATE CONSTANT IF NOT EXISTS ONE VALUE 1");

		sql = "select name,ONE from mytable order by name";

		sql = "select name,ONE from mytable where name = 'abc' || '123'";
	}

	void queryGroup() {
		sql = "select id from mytable group by id";

		sql = "select id from mytable group by id having id>2";

		sql = "select id, count(id) from mytable group by id having id>2";
		sql = "select id, count(id) from mytable group by id";
		sql = "select count(id) from mytable group by id";
		//sql = "select id,name,count(id) from mytable where id>0";
		sql = "select id,count(id) from mytable where id>0";
		//sql = "select id,count(id) from mytable where id>0  group by id";

		//sql = "select max(id), count(id) from mytable where id>1";

		sql = "select id,name,count(id) from mytable where id>0  group by id,name";

		sql = "select id,count(id) from mytable where id>2  group by id having id=3";
	}

	void queryGroupSorted() {
		sql = "select id from mytable group by id";

		sql = "select id from mytable group by id having id>2";

		sql = "select id, count(id) from mytable group by id having id>2";
		sql = "select id, count(id) from mytable group by id";
		sql = "select count(id) from mytable group by id";
		//sql = "select id,name,count(id) from mytable where id>0";
		sql = "select id,count(id) from mytable where id>0";
		//sql = "select id,count(id) from mytable where id>0  group by id";

		//sql = "select max(id), count(id) from mytable where id>1";

		sql = "select id,name,count(id) from mytable where id>0  group by id,name";

		sql = "select id,count(id) from mytable where id>2  group by id having id=3";

		//sql = "select count(id) from mytable where id>2 order by id"; //���ᴥ��queryGroupSorted
		sql = "select id,count(id) from mytable where id>2 group by id having id=3 order by id";

		//���漸�������ܴ���
		//		sql = "select id,count(id) from mytable where id>2 group by name,id having id=3 order by id";
		//		sql = "select id,count(id) from mytable where id>2 group by name,id having id=3 order by id,name";
		//		sql = "select id,count(id) from mytable where id>2 group by name,id having id=3 order by name,id";
		//		sql = "select id,count(id) from mytable where id>2 group by id,name having id=3 order by id";
		//		sql = "select id,count(id) from mytable where id>2 group by id,name having id=3 order by id,name";
	}

	void queryQuick() throws SQLException {
		//�������У���Ϊid�ֶο���Ϊnull
		sql = "select count(id),min(id),max(id) from mytable";

		stmt.executeUpdate("ALTER TABLE mytable ALTER COLUMN id SET NOT NULL");
		sql = "select count(id),min(id),max(id) from mytable";

	}
}
