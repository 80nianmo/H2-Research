
1. ��Ҫ���Ȱ�װ��������Щ����

JDK 1.5+
TortoiseSVN 1.7+
Eclipse 3.5+


2. ����H2���ݿ��Դ����

H2���ݿ��Դ�������Google code: http://h2database.googlecode.com/svn
Ŀǰ���µİ汾��1.3.168�����еİ汾������svn��tagsĿ¼���ҵ���
��ΪH2Ŀǰ�Ƚ��ȶ��ˣ������ύƵ�ʲ��Ǻܸߣ�����Ҳ����һЩ���£�
Ϊ���ܱ�����H2����ͬ����������������ѡ��trunk��Ĵ���: 
http://h2database.googlecode.com/svn/trunk/h2
����ֱ��������������URL��������TortoiseSVN��Repository Browser������
����ͼ��ʾ:


��TortoiseSVN checkout���svn�Ĵ���: http://h2database.googlecode.com/svn/trunk/h2
�ŵ�E:\H2\trunk (����Ի����Ŀ¼������Ҫ�޸���������֮��صĵط�)


3. һЩ��ϰ��

��ÿ�ο�һ���µĿ�Դ��Ŀʱ������дһ�µĴ������ӻ��и��ַ����ĵ���Ȼ��Ҫ��ʱ�ύ������svn�����ⶪʧ��
��������E:\H2����һ��Ŀ¼my-h2����Ŀ¼�л���������Ŀ¼: my-h2-src��my-h2-docs
my-h2-src���������Լ���������ʱд�ĸ������ӣ�
my-h2-docs��һЩ�����ĵ���


4. ����Eclipse

H2����һ����maven��������Ŀ��Ҳû��ant������Java�Լ�д��һ����������
�����Ҹо���Щ���ӣ�����Ҳû�����ģ�����ֱ���޸�Eclipse��Ŀ�ű��Ƚϼ򵥡�

H2��һЩ����������������lucene��servlet��jdk tools�ȣ�
���Կ�����E:\H2�н���libĿ¼�������е�����jar���ŵ����档(ע: ��Щjar�ڴ������ĸ�������)

��ΪH2Ҳ��������Client��Server�ˣ�����Ϊ�˷�����Է������룬���Խ�������Eclipse workspace��
������Eclipse workspace��".classpath"��".project"�ļ�������ȫһ����(ע: �������ļ��ڴ������ĸ�������)

".project"�ļ�:

<?xml version="1.0" encoding="UTF-8"?>
<projectDescription>
	<name>h2</name>
	<buildSpec>
		<buildCommand>
			<name>org.eclipse.jdt.core.javabuilder</name>
		</buildCommand>
	</buildSpec>
	<natures>
		<nature>org.eclipse.jdt.core.javanature</nature>
	</natures>
	<linkedResources>
		<link>
			<name>my-h2-src</name>
			<type>2</type>
			<location>E:/H2/my-h2/my-h2-src</location>
		</link>

		<link>
			<name>h2-main-src</name>
			<type>2</type>
			<location>E:/H2/trunk/src/main</location>
		</link>

		<link>
			<name>h2-tools-src</name>
			<type>2</type>
			<location>E:/H2/trunk/src/tools</location>
		</link>

		<link>
			<name>h2-test-src</name>
			<type>2</type>
			<location>E:/H2/trunk/src/test</location>
		</link>
	</linkedResources>
</projectDescription>

ʹ����linkedResources���������Eclipse workspace���Թ���ͬ����JavaԴ���룬�������ĸ�workspace�и�������һ�߶��ܲ������


".classpath"�ļ�:
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="src" path="my-h2-src"/>
	<classpathentry kind="src" path="h2-main-src"/>
	<classpathentry kind="src" path="h2-tools-src"/>
	<classpathentry kind="src" path="h2-test-src"/>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER"/>
	<classpathentry kind="lib" path="E:/H2/lib/junit-4.8.2.jar"/>
	<classpathentry kind="lib" path="E:/H2/lib/lucene-core-3.4.0.jar"/>
	<classpathentry kind="lib" path="E:/H2/lib/org.eclipse.osgi_3.6.2.R36x_v20110210.jar"/>
	<classpathentry kind="lib" path="E:/H2/lib/servlet-api-2.4.jar"/>
	<classpathentry kind="lib" path="E:/H2/lib/slf4j-api-1.6.1.jar"/>
	<classpathentry kind="lib" path="E:/H2/lib/tools.jar"/>
	<classpathentry kind="output" path="target/classes"/>
</classpath>

ǰ4��src��Ŀ���Ƕ�ǰ��linkedResources�е����á�

������E:\H2�н�������Ŀ¼: eclipse-workspace-server��eclipse-workspace-client��
Ȼ������������ļ���copyһ�ݵ�������Ŀ¼�С�

�����ţ���Eclipse������һ��"Workspace Launcher"�򣬱༭һ�£��ĳ�h2-server�������ͻ�õ�һ���µ�Workspace��

����Eclipse�󣬵�"File->Import->General->Existing Projects into Workspace"��
����E:\H2\eclipse-workspace-server���ܿ���H2�����Ŀ�ˡ�

�ٴ�Eclipse������һ��"Workspace Launcher"�򣬱༭һ�£��ĳ�h2-client�������ͻ�õ�һ���µ�h2-client Workspace��

����Eclipse�󣬵�"File->Import->General->Existing Projects into Workspace"��
����E:\H2\eclipse-workspace-clientҲ�ܿ���H2�����Ŀ��



5. ��Eclipse����H2������

��h2-server�Ǹ�workspace�У���my-h2-src���½�һ���࣬����:
package my.test;

import java.sql.SQLException;
import java.util.ArrayList;

public class MyServer {
	public static void main(String[] args) throws SQLException {
		ArrayList<String> list = new ArrayList<String>();
		list.add("-tcp");
		org.h2.tools.Server.main(list.toArray(new String[list.size()]));
	}
}

�ڱ༭���һ�����"Run As->Java Aplication"��
�����Console�г���"TCP server running at..."��������ʾ�ͱ�ʾH2 Server�������ˡ�


Ȼ��ת��h2-client�Ǹ�eclipse workspace����my-h2-src���½�һ���࣬����:

package my.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCTest {
	public static void main(String[] args) throws Exception {
		Class.forName("org.h2.Driver");

		Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/mydb", "sa", "");
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("DROP TABLE IF EXISTS my_table");
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS my_table(name varchar(20))");
		stmt.executeUpdate("INSERT INTO my_table(name) VALUES('zhh')");

		ResultSet rs = stmt.executeQuery("SELECT name FROM my_table");
		rs.next();
		System.out.println(rs.getString(1));

		stmt.close();
		conn.close();
	}
}

�ڱ༭���һ�����"Run As->Java Aplication"��
�����Console�г���"zhh"��������ʾ�ͱ�ʾH2 ClientҲ�����ˡ�

���⣬ת��E:\H2\eclipse-workspace-serverĿ¼���ᷢ���ڴ�Ŀ¼�ж���һ��mydb.h2.db�ļ���my_table������Ҳ���ڴ��ļ��С�


6. H2������Լ�����

ֱ�ӵ�Eclipse���Ǹ���ɫ��Terminate��ťͣ��h2-server��
Ȼ����my.test.MyServer��ı༭���һ�����"Debug As->Java Aplication"��
���������org.h2.server.TcpServer.listen()�����д���ϵ㣬����ͼ:

�������������JDBCTest���ӣ���h2-server��org.h2.server.TcpServer�оͽ���ϵ��ˣ�
��h2-server��org.h2.command.Parser.parse(String)��������¶ϵ㣬Ȼ��F8���������档

