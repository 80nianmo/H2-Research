package my.test.command.ddl;

import my.test.TestBase;

public class CreateUserDataTypeTest extends TestBase {
	public static void main(String[] args) throws Exception {
		new CreateUserDataTypeTest().start();
	}

	//����org.h2.command.Parser.parseCreateUserDataType()
	//��org.h2.command.ddl.CreateUserDataType��org.h2.engine.UserDataType
	@Override
	public void startInternal() throws Exception {
		stmt.executeUpdate("DROP DOMAIN IF EXISTS EMAIL");
		//VALUE��CREATE DOMAIN����Ĭ����ʱ����
		stmt.executeUpdate("CREATE DOMAIN IF NOT EXISTS EMAIL AS VARCHAR(255) CHECK (POSITION('@', VALUE) > 1)");
		stmt.executeUpdate("CREATE TYPE IF NOT EXISTS EMAIL AS VARCHAR(255) CHECK (POSITION('@', VALUE) > 1)");
		stmt.executeUpdate("CREATE DATATYPE IF NOT EXISTS EMAIL AS VARCHAR(255) CHECK (POSITION('@', VALUE) > 1)");
		
		//stmt.executeUpdate("CREATE DATATYPE IF NOT EXISTS int AS VARCHAR(255) CHECK (POSITION('@', VALUE) > 1)");
		
		//�ӵڶ������ƿ�ʼ�Ķ����������͵ģ��������int
        //new String[]{"INTEGER", "INT", "MEDIUMINT", "INT4", "SIGNED"}
        //�����������û������ݿ���û�н���ʱ���Ը���
        //��CREATE DATATYPE IF NOT EXISTS int AS VARCHAR(255)
        //���Ƿ��������;Ͳ��ܸ���
        //��CREATE DATATYPE IF NOT EXISTS integer AS VARCHAR(255)
		//stmt.executeUpdate("CREATE DATATYPE IF NOT EXISTS integer AS VARCHAR(255) CHECK (POSITION('@', VALUE) > 1)");
	}
}
