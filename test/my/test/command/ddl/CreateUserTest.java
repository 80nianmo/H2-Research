package my.test.command.ddl;

import my.test.TestBase;

public class CreateUserTest extends TestBase {
	public static void main(String[] args) throws Exception {
		new CreateUserTest().start();
	}

	public void init() throws Exception {
		//prop.setProperty("user", "SA2");
		//prop.setProperty("password", "78");
	}

	//����org.h2.command.Parser.parseCreateUser()��org.h2.command.ddl.CreateUser
	//��org.h2.engine.User
	@Override
	public void startInternal() throws Exception {
		stmt.executeUpdate("DROP ROLE IF EXISTS sa1");
		// stmt.executeUpdate("CREATE ROLE IF NOT EXISTS sa1");

		stmt.executeUpdate("DROP USER IF EXISTS sa1 CASCADE");
		//stmt.executeUpdate("DROP USER IF EXISTS SA2 CASCADE");
		stmt.executeUpdate("DROP USER IF EXISTS SA3 CASCADE");

		stmt.executeUpdate("CREATE USER IF NOT EXISTS sa1 PASSWORD 'abc' ADMIN");
		//X����Ҳ�ǿ��Ե�
		stmt.executeUpdate("CREATE USER IF NOT EXISTS SA2 SALT X'123456' HASH X'78' ADMIN"); // X'...'������ż����
		stmt.executeUpdate("CREATE USER IF NOT EXISTS SA3 IDENTIFIED BY abc"); // ���벻������

		stmt.executeUpdate("CREATE SCHEMA IF NOT EXISTS TEST_SCHEMA2 AUTHORIZATION SA2");

		stmt.executeUpdate("DROP USER IF EXISTS guest");
		stmt.executeUpdate("CREATE USER IF NOT EXISTS guest COMMENT 'create a guest user' PASSWORD 'abc'");
		
		stmt.executeUpdate("ALTER USER SA2 SET PASSWORD '123'");
		stmt.executeUpdate("ALTER USER SA2 SET SALT X'123456' HASH X'78'");
		
		stmt.executeUpdate("ALTER USER SA2 RENAME TO SA222");
		stmt.executeUpdate("ALTER USER SA222 ADMIN false");
		//rightTest();
	}

	void rightTest() throws Exception {
		stmt.executeUpdate("DROP TABLE IF EXISTS CreateUserTest");
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS CreateUserTest (f1 int)");
		//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS CreateUserTest (f1 IDENTITY)");
		//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS CreateUserTest (f1 IDENTITY(1,10))");
		//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS CreateUserTest (f1 SERIAL(1,10)))");

		//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS CreateUserTest (f1 IDENTITY(1,10),PRIMARY KEY(f1))");
		//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS CreateUserTest (f1 int,PRIMARY KEY(f1))");

		//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS CreateUserTest (f1 int,CONSTRAINT IF NOT EXISTS my_constraint COMMENT IS 'haha' INDEX int)");

		//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS CreateUserTest (f1 int,f2 int,CONSTRAINT IF NOT EXISTS my_constraint COMMENT IS 'haha' INDEX my_int(f1,f2))");
		//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS TEST9.public.CreateUserTest (f1 int,f2 int,"
		//		+ "CONSTRAINT IF NOT EXISTS my_constraint COMMENT IS 'haha' CHECK f1>0)");
		//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS CreateUserTest (f1 int,f2 int,"
		//		+ "CONSTRAINT IF NOT EXISTS my_constraint COMMENT IS 'haha' UNIQUE KEY INDEX my_constraint2(f1,f2) INDEX myi)");
		//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS CreateUserTest (f1 int,f2 int,"
		//		+ "CONSTRAINT IF NOT EXISTS my_constraint COMMENT IS 'haha' FOREIGN KEY(f1,f2))  INDEX my-i REFERENCES(f1)");

		stmt.executeUpdate("CREATE ROLE IF NOT EXISTS myrole1");
		stmt.executeUpdate("CREATE ROLE IF NOT EXISTS myrole2");
		stmt.executeUpdate("CREATE ROLE IF NOT EXISTS myrole3");

		//GRANT
		stmt.executeUpdate("GRANT SELECT,DELETE,INSERT ON CreateUserTest TO PUBLIC");
		stmt.executeUpdate("GRANT UPDATE ON CreateUserTest TO PUBLIC");
		stmt.executeUpdate("GRANT SELECT,DELETE,INSERT,UPDATE ON CreateUserTest TO SA2");
		stmt.executeUpdate("GRANT SELECT,DELETE,INSERT,UPDATE ON CreateUserTest TO myrole1");

		stmt.executeUpdate("GRANT myrole1 TO myrole2");
		//stmt.executeUpdate("GRANT myrole2 TO myrole2");
		stmt.executeUpdate("GRANT myrole2 TO myrole1");
		stmt.executeUpdate("GRANT myrole1 TO myrole3");
		stmt.executeUpdate("GRANT myrole3 TO PUBLIC");

		stmt.executeUpdate("GRANT myrole1 TO PUBLIC");
		stmt.executeUpdate("GRANT myrole1 TO SA3");
		stmt.executeUpdate("GRANT myrole1 TO myrole2");
		//stmt.executeUpdate("GRANT myrole2 TO myrole2");//cyclic role grants are not allowed

		//REVOKE
		stmt.executeUpdate("REVOKE SELECT,DELETE,INSERT,UPDATE ON CreateUserTest FROM PUBLIC");
		stmt.executeUpdate("REVOKE SELECT,DELETE,INSERT,UPDATE ON CreateUserTest FROM SA2");
		stmt.executeUpdate("REVOKE SELECT,DELETE,INSERT,UPDATE ON CreateUserTest FROM myrole1");

		stmt.executeUpdate("REVOKE myrole1 FROM PUBLIC");
		stmt.executeUpdate("REVOKE myrole1 FROM SA3");
		stmt.executeUpdate("REVOKE myrole1 FROM myrole2");
	}
}
