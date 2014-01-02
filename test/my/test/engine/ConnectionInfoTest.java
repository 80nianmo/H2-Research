package my.test.engine;

import java.util.Properties;

import org.h2.engine.ConnectionInfo;

public class ConnectionInfoTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Properties prop = new Properties();
		prop.setProperty("user", "sa");
		//prop.setProperty("password", "");
		prop.put("password", new char[]{});

		prop.setProperty("not_exists", "haha"); //û���쳣

		//System.setProperty("h2.urlMap", "E:/H2/my-h2/my-h2-src/my/test/h2.urlMap.properties");

		prop.setProperty("max_compact_time", "300");
		
		prop.setProperty("PASSWORD_HASH", "true");
		prop.setProperty("CIPHER", "AES"); //AES��XTEA��FOG
		
		//���������CIPHER�������ܰ��������ݣ��ÿո�ֿ�����һ������filePassword���ڶ���������ʵ����
		//����ʹ��16�����ַ����ַ�������ż�������PASSWORD_HASH������true��ô���ٽ���SHA256
		
		//abc�ǻ���
		//org.h2.message.DbException: Hexadecimal string with odd number of characters: "abc" [90003-169]
		//prop.setProperty("password", "abc 123");
		
		//�ַ�"g"����16�����ַ�
		//org.h2.message.DbException: Hexadecimal string contains non-hex character: "abcg" [90004-169]
		//prop.setProperty("password", "abcg 123");
		
		//��ȷ��
		prop.setProperty("password", "abcd 1234");

		String url = "my.url";
		url = "jdbc:h2:tcp://localhost:9092/test9;optimize_distinct=true;early_filter=true;nested_joins=false";
		url = "jdbc:h2:test";
		
		//���쳣: org.h2.message.DbException: Unsupported connection setting "NOT_EXISTS" [90113-169]
		//url = "jdbc:h2:tcp://localhost:9092/test9;optimize_distinct=true;early_filter=true;nested_joins=false;not_exists=haha";
		
		//���쳣: org.h2.message.DbException: Duplicate property "USER" [90066-169]
		//���ֵ��ͬ�Ͳ��ᱨ��
		//url = "jdbc:h2:tcp://localhost:9092/test9;optimize_distinct=true;early_filter=true;nested_joins=false;user=sa2";
		ConnectionInfo info = new ConnectionInfo(url, prop);
		
		System.out.println(info.getName());

	}

}
