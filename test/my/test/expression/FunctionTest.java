package my.test.expression;

import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.test.TestBase;

import org.h2.expression.Function;

public class FunctionTest extends TestBase {
	public static void main(String[] args) throws Exception {
		new FunctionTest().start();
	}

	@Override
	public void init() throws Exception {
		prop.setProperty("MODE", "PostgreSQL"); //ֻ��PostgreSQL�Ű�LOG��LN����һ�����������ݿ�Ѱ�LOG��LOG10
	}

	void soundex_index() {
		// SOUNDEX_INDEX
		// 34���ַ�(26����д��ĸ��1��8��8������)
		// 7: AEIOUY �����ǵ�Сд(��ͬ)
		// 8: HW
		// 1: BFPV
		// 2: CGJKQSXZ
		// 3: DT
		// 4: L
		// 5: MN
		// 6: R
		char[] SOUNDEX_INDEX = new char[128];
		// SOUNDEX_INDEX
		String index = "7AEIOUY8HW1BFPV2CGJKQSXZ3DT4L5MN6R";
		char number = 0;
		for (int i = 0, length = index.length(); i < length; i++) {
			char c = index.charAt(i);
			if (c < '9') {
				number = c;
			} else {
				SOUNDEX_INDEX[c] = number;
				SOUNDEX_INDEX[Character.toLowerCase(c)] = number;
			}
		}
	}

	void printFunctionInfos() {
		HashMap<Integer, List<Object>> FUNCTIONS = new java.util.LinkedHashMap<Integer, List<Object>>();
		for (Object fi : Function.getFunctionInfos()) {
			List<Object> list = FUNCTIONS.get(fi.hashCode());
			if (list == null)
				list = new ArrayList<Object>();
			list.add(fi);
			FUNCTIONS.put(fi.hashCode(), list);
			//System.out.println(fi);
		}

		for (List<Object> fi : FUNCTIONS.values()) {
			Object last = null;
			for (Object o : fi) {
				System.out.print(o);
				if (last != null) {
					if (!last.equals(o))
						System.out.print(" ��ǰ�治ͬ  ");
					else
						System.out.print(" ��ǰ����ͬ  ");

					//System.out.println(last);
					//System.out.println(o);
					//System.out.println();
				}
				last = o;

			}
			System.out.println();
			//System.out.println(fi);
		}
	}

	//����org.h2.expression.Function
	@Override
	public void startInternal() throws Exception {
		stmt.executeUpdate("CREATE SCHEMA IF NOT EXISTS myschema AUTHORIZATION sa");
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS FunctionTest(id int primary key, name varchar(500))");
		stmt.executeUpdate("CREATE ALIAS IF NOT EXISTS myschema.MY_SQRT FOR \"java.lang.Math.sqrt\"");

		//callableStatement();

		sql = "SELECT * FROM TABLE(ID INT=(1, 2), NAME VARCHAR=('Hello', 'World'))";
		sql = "SELECT * FROM SYSTEM_RANGE(1, 9)";
		sql = "SELECT ROWNUM(), * FROM SYSTEM_RANGE(1, 9)";
		sql = "SELECT ABS(-1) FROM FunctionTest";
		sql = "SELECT ABS(-1)";

		numericFunctions();
		stringFunctions();
		timeAndDateFunctions();
		//systemFunctions();

		executeQuery();
	}

	void numericFunctions() throws Exception {
		sql = "SELECT ROUND(12.234, 2)"; //12.23,  4��5�룬С��������λ
		sql = "SELECT ROUND(12.235, 2)"; //12.24
		sql = "SELECT ROUND(12.236, 2)"; //12.24

		sql = "SELECT TRUNCATE(12.236, 2)"; //12.23
		sql = "SELECT TRUNCATE(0.236, 2)"; //12.23
		//sql = "SELECT TRUNCATE(-0.236, 2)"; //-0.23 

		sql = "SELECT ABS(null)";
		sql = "SELECT CEILING(1.22)"; //2.0
		sql = "SELECT CEILING(0.22)"; //1.0 //�컨��

		sql = "SELECT FLOOR(1.22)"; //1.0
		sql = "SELECT FLOOR(0.22)"; //0.0 //�ذ�

		sql = "SELECT LN(100)"; //4.605170185988092,  ��e��4.605170185988092������100
		//��org.h2.engine.Mode.logIsLogBase10��trueʱ��LOG��LNһ������false��LOG10һ����Ĭ����false
		sql = "SELECT LOG(100)"; //4.605170185988092,  ��e��4.605170185988092������100
		sql = "SELECT LOG10(100)"; //2.0,  ��10��2������100

		sql = "SELECT RAND()";
		sql = "SELECT RAND(10), RAND(10), RAND(10)";

		sql = "SELECT ROUNDMAGIC(0.199933)";
		sql = "SELECT ROUNDMAGIC(0.1999333455444444444444444444444)";

		sql = "SELECT EXPAND(x'1234')"; //��ѹ��ֻ�ܽ�COMPRESS����
		sql = "SELECT UTF8TOSTRING(EXPAND(COMPRESS(STRINGTOUTF8('Test'))))";

		sql = "SELECT RANDOM_UUID()";
	}

	void stringFunctions() throws Exception {
		sql = "SELECT ASCII('Hi')";

		sql = "SELECT CONCAT_WS(', ', 1,2,3), CONCAT(1,2,3)";
		sql = "SELECT HEXTORAW('abc')";

		sql = "SELECT HEXTORAW('abcd1234')";
		sql = "SELECT HEXTORAW(RAWTOHEX('abc'))='abc'";

		// 34���ַ�(26����д��ĸ��1��8��8������)
		// 7: AEIOUY �����ǵ�Сд(��ͬ)
		// 8: HW
		// 1: BFPV
		// 2: CGJKQSXZ
		// 3: DT
		// 4: L
		// 5: MN
		// 6: R
		//�㷨��: �������еķ��ַ���Ȼ������һ���ַ������Զ�Ӧ7��8���ַ���������ת�ɶ�Ӧ�����֣��ظ��Ĳ��㣬����4λ�Ĳ�0
		sql = "SELECT SOUNDEX('1aaa')"; //1��ȥ����������һ��a���ڶ��͵�����a��Ӧ7�����ԣ����������a000

		//B������Hȥ����Cת��2��Wȥ����Dת��3��Hȥ����Aȥ���������B23��Ϊ��3λ�����Բ���4λ�������B230
		sql = "SELECT SOUNDEX('BHCWDHA')";

		sql = "SELECT 'ab'||SPACE(10)||'cd'";

		sql = "SELECT XMLCOMMENT('aaa--bbb--ccc')";
	}

	void timeAndDateFunctions() throws Exception {

		//������21��ʱ�������ں���, 
		//�����������org.h2.expression.Function.getSimpleValue(Session, Value, Expression[], Value[])
		//----------------------------------------------------------------------------------------
		sql = "SELECT DAYNAME(DATE '2000-01-01')";
		sql = "SELECT DAY_OF_MONTH(CURRENT_DATE),DAY_OF_WEEK(CURRENT_DATE),DAY_OF_YEAR(CURRENT_DATE)";

		sql = "SELECT HOUR(CURRENT_TIMESTAMP),MINUTE(CURRENT_TIMESTAMP)";

		sql = "SELECT MONTH(CURRENT_DATE)"; //������Ҳ����SELECT MONTH(CURRENT_DATE())

		sql = "SELECT MONTHNAME(CURRENT_DATE)"; //����MONTH_NAME��û���»���

		sql = "SELECT QUARTER(CURRENT_DATE)"; //�ڼ������ȣ���1��2��3��4��ʾ

		sql = "SELECT SECOND(CURRENT_TIMESTAMP)";

		sql = "SELECT WEEK(CURRENT_DATE),YEAR(CURRENT_DATE)";

		//ISO_DAY_OF_WEEK����������ˣ���1������1��ʾ����DAY_OF_WEEK��һ��
		sql = "SELECT ISO_YEAR(CURRENT_DATE),ISO_WEEK(CURRENT_DATE),ISO_DAY_OF_WEEK(CURRENT_DATE)";

		//NOW(1)��ʾ������ֻ����һλ����NOW()="2012-12-03 22:03:44.647" ��NOW(1)="2012-12-03 22:03:44.6"
		//������һ����3λ�����NOW(100)��100>3�ˣ�����NOW(100)��NOW()һ��
		sql = "SELECT CURDATE(),CURRENT_DATE(),CURTIME(),CURRENT_TIME(),NOW(),CURRENT_TIMESTAMP(),NOW(1),NOW(100)";

		//CURDATE=CURRENT_DATE, CURTIME=CURRENT_TIME, NOW=CURRENT_TIMESTAMP
		//����������ſ���
		sql = "SELECT CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP";
		//����Ͳ���
		//sql = "SELECT CURDATE, CURTIME, NOW";
		//
		//
		//
		//������5��ʱ�������ں���, 
		//�����������org.h2.expression.Function.getValueWithArgs(Session, Expression[])
		//----------------------------------------------------------------------------------------

		//�·ݼ�1�������2001-02-28 00:00:00.0 
		sql = "SELECT DATEADD('MONTH', 1, DATE '2001-01-31')";

		//�ú����YEAR��ȥǰ���YEAR��1999-2001=-2
		sql = "SELECT DATEDIFF('YEAR', DATE '2001-01-31', DATE '1999-01-31')";

		//��ȡ���ں���� CURRENT_TIMESTAMP=2012-12-03 22:20:08.597  DAY=3  YEAR=2012 
		sql = "SELECT CURRENT_TIMESTAMP, EXTRACT(DAY FROM CURRENT_TIMESTAMP), EXTRACT(YEAR FROM CURRENT_TIMESTAMP)";

		//format datetime ��ʽ������ʱ��
		//timestamp = TIMESTAMP '2001-02-03 04:05:06' , 
		//formatString = 'EEE, d MMM yyyy HH:mm:ss z'
		//localeString = 'en'
		//timeZoneString = 'GMT'
		//��� Fri, 2 Feb 2001 20:05:06 GMT 
		sql = "SELECT FORMATDATETIME(TIMESTAMP '2001-02-03 04:05:06', 'EEE, d MMM yyyy HH:mm:ss z', 'en', 'GMT')";
		//ֻҪtimestamp��formatString����֮һΪnull�����Ϊnull
		sql = "SELECT FORMATDATETIME(null, 'EEE, d MMM yyyy HH:mm:ss z')"; //null
		sql = "SELECT FORMATDATETIME(TIMESTAMP '2001-02-03 04:05:06', null)"; //null
		
		//parse datetime��������ʱ��
		//��������������ָ���ĸ�ʽ������һ���������õ�һ��java.util.Date
		//���: 2001-02-03 11:05:06.0 
		sql = "SELECT PARSEDATETIME('Sat, 3 Feb 2001 03:05:06 GMT', 'EEE, d MMM yyyy HH:mm:ss z', 'en', 'GMT')";
	}

	void systemFunctions() throws Exception {
		sql = "SELECT DECODE(RAND()>0.5, 0, 'Red', 1, 'Black')";

		sql = "SELECT DECODE(RAND()>0.5, 0, 'Red1', 0, 'Red2', 1, 'Black1', 1, 'Black2')";

		sql = "SELECT DECODE(RAND()>0.5, 2, 'Red1', 2, 'Red2', 2, 'Black1', 2)";
		sql = "SELECT DECODE(RAND()>0.5, 2, 'Red1', 2, 'Red2', 2, 'Black1', 2, 'Black2')";

		sql = "SELECT DECODE(0, 0, 'v1', 0,/'v2', 1, 'v3', 1, 'v4')";

		//ROW_NUMBER������Ȼ�����ˣ���ROW_NUMBER()������Ч����֧���������﷨
		sql = "SELECT ROW_NUMBER()";
		//ROWNUM������Ȼû�ж��壬��ROWNUM()����Ч��Parser�ڽ���ʱ��������ROWNUMα�ֶδ���
		//������org.h2.expression.Rownum����org.h2.command.Parser.readTerm()
		sql = "SELECT ROWNUM()";
		//������û������,�����������org.h2.command.Parser.readFunction(Schema, String)
		//��ROW_NUMBERת��org.h2.expression.Rownum��
		sql = "SELECT ROW_NUMBER()OVER()";

		//��ȷ���null������ȷ���v0
		sql = "SELECT NULLIF(1,2)"; //1
		sql = "SELECT NULLIF(1,1)"; //null

		sql = "SELECT DATABASE()";
		sql = "SELECT USER(), CURRENT_USER()";
		sql = "SELECT IDENTITY(), SCOPE_IDENTITY()";
		sql = "SELECT LOCK_TIMEOUT()";
		sql = "SELECT MEMORY_FREE(), MEMORY_USED()";

		sql = "SELECT GREATEST(1,2,3), LEAST(1,2,3)";

		sql = "SELECT ARRAY_GET(('Hello', 'World'), 2), ARRAY_LENGTH(('Hello', 'World')), "
				+ "ARRAY_CONTAINS(('Hello', 'World'), 'Hello')";

		//sql = "SELECT CASE(1>0, 1, b<0, 2)"; //����������

		sql = "SELECT SET(@v, 1), CASE @v WHEN 0 THEN 'No' WHEN 1 THEN 'One' ELSE 'Some' END";
		sql = "SELECT SET(@v, 11), CASE WHEN @v<10 THEN 'Low' ELSE 'High' END";
		stmt.executeUpdate("CREATE SEQUENCE IF NOT EXISTS SEQ_ID");

		sql = "SELECT CURRVAL('SEQ_ID'), NEXTVAL('SEQ_ID')";

		sql = "SELECT LENGTH(FILE_READ('E:/H2/my-h2/my-h2-src/my/test/expression/FunctionTest.java'))";
	}

	void callableStatement() throws Exception {
		CallableStatement cs = conn.prepareCall("?= CALL myschema.MY_SQRT(2.2)");
		cs.registerOutParameter(1, 0);
		cs.execute();
		//System.out.println(cs.getDouble(1));
	}
}
