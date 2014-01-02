package my.test.command;

import my.test.TestBase;

public class ParserTest extends TestBase {
	public static void main(String[] args) throws Exception {
		new ParserTest().start();
	}

	@Override
	public void init() throws Exception {
		prop.setProperty("DATABASE_TO_UPPER", "false");
	}

	@Override
	public void startInternal() throws Exception {
		stmt.executeUpdate("DROP TABLE IF EXISTS ParserTest");
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ParserTest(id int, name varchar(500) as '123')");

		//stmt.executeUpdate("set @[set] 10");
		sql = "INSERT INTO ParserTest VALUES(DEFAULT, DEFAULT),(10, 'a'),(20, 'b')";
		stmt.executeUpdate(sql);

		sql = "select id,name from ParserTest";

		initialize();
		read();
		//readTerm();

		//sql = "select id,name from ParserTest;select id,name from ParserTest;select id,name from ParserTest";
		executeQuery();

	}

	void initialize() throws Exception {
		//("DROP /*TA");
		//stmt.executeUpdate("DROP /*TA*/ t");
		//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS A(id int, name varchar(500) as '123')");
		//stmt.executeUpdate("DROP //TA");
		//stmt.executeUpdate("DROP TABLE //TA");
		//		stmt.executeUpdate("DROP TABLE //T\n\rA");
		//		
		//		PreparedStatement ps = conn.prepareStatement("delete top ?2 from ParserTest where id>10 and name=?1");
		//		ps.setString(1, "abc");
		//		ps.setInt(2, 3);
		//		ps.executeUpdate();

		//stmt.executeUpdate("delete from ParserTest where name='abc' and id>0");

		//stmt.executeUpdate("DROP TABLE //single line comment, drop table t");

		//stmt.executeUpdate("CREATE TABLE IF NOT != EXISTS t (name VARCHAR(20));");

		//stmt.executeUpdate("`CREATE TABLE IF NOT``aaaa` != EXISTS t (name VARCHAR(20));");

		//stmt.executeUpdate("90770000000005555559999999999999 CREATE TABLE IF NOT aaaa  != EXISTS t (name VARCHAR(20));");

		//stmt.executeUpdate("0x13ABCDEFabcdef CREATE TABLE IF NOT aaaa  != EXISTS t (name VARCHAR(20));");
	}

	void read() throws Exception {
		//���DATABASE_TO_UPPER��false����ô0x�Ǵ��
    	//��sql = "select id,name from ParserTest where id > 0x2";
    	//ֻ���ô�д0X������Ҳֻ����A-F��
    	//������where id > 0X2ab��ʵ����where id > 0X2������abû�ж�����
    	//���ж�org.h2.command.Parser.prepareCommand(String)ʱ��(currentTokenType != END)Ϊfalse�ͳ���
		sql = "select id,name from ParserTest where id > 0x2";
		
		sql = "select id,name from ParserTest where id > 0X2ab";
		
		sql = "select id,name from ParserTest where id > 0X7FFFFFFF123";
		
		sql = "select id,name from ParserTest where id > 123.01";
		
		sql = "select id,name from ParserTest where id > 123.0e-11";
		
		
		sql = "select id,name from ParserTest where id > .123";
		//ALLOW_LITERALS_ALL=2 ������
    	//ALLOW_LITERALS_NONE=0 ˵���������������ֵ
    	//ALLOW_LITERALS_NUMBERS=1 ֻ������������ֵ
		stmt.executeUpdate("SET ALLOW_LITERALS 1"); //ֻ������������ֵ
		sql = "select id,name from ParserTest where name = 'abc'"; //��ʱ�Ͳ���������ַ�������ֵ��
	}

	public static void testPG_GET_OID(String str) {
		System.out.println("testPG_GET_OID: " + str);
	}

	void readTerm() throws Exception {
		//stmt.executeUpdate("SET @topVariableName=3");
		//stmt.executeUpdate("SET @topVariableName to 3");
		sql = "SELECT @topVariableName";

		sql = "SELECT @topVariableName:=4";
		sql = "SELECT ('Hello', 'World')[2]"; //null, �±�Ҫ��0��ʼ���ڲ����1
		sql = "SELECT ('Hello', 'World')[1]"; //World

		stmt.executeUpdate("CREATE ALIAS IF NOT EXISTS PG_GET_OID FOR \"my.test.ParserTest.testPG_GET_OID\"");

		sql = "SELECT 'ddd'::REGCLASS";

		//��sql = "SELECT 12::varchar";����ʾCAST(12 AS varchar)����12ת��varchar����
		//�����r����12��Ȼ����CAST����
		sql = "SELECT 'ddd'::varchar f"; //f�������ˣ���('ddd'::varchar)��һ��ı���
		sql = "SELECT 'ddd'::varchar";
		sql = "SELECT 12::varchar";

		sql = "SELECT CASE WHEN END CASE"; //��
		sql = "SELECT SET(@v, 11), CASE WHEN @v<10 THEN 'Low' END CASE";
		sql = "SELECT SET(@v, 11), CASE WHEN @v<10 THEN 'Low' ELSE 'High' END";
		sql = "SELECT SET(@v, 1), CASE @v WHEN 0 THEN 'No' WHEN 1 THEN 'One' ELSE 'Some' END";

		sql = "SELECT -1.4e-10";

		//stmt.executeUpdate("delete top @topVariableName from ParserTest where id>10");
		//stmt.executeUpdate("delete top 3 from ParserTest where id>10");
		//stmt.executeUpdate("delete top ?1 from ParserTest where id>10");

		//stmt.executeUpdate("delete top true from ParserTest where id>10");
		//stmt.executeUpdate("update ParserTest set name='1234567890' where id>10");
		//stmt.executeUpdate("delete top rownum from ParserTest where id>10");
		//stmt.executeUpdate("delete top CURRENT_TIME from ParserTest where id>10");
		//stmt.executeUpdate("delete top MAX(id) from ParserTest where id>10");
	}

}
