package my.test.command.dml;

import my.test.TestBase;

public class SetTest extends TestBase {
	public static void main(String[] args) throws Exception {
		new SetTest().start();
	}
	@Override
	public void init() throws Exception {
		//��org.h2.message.TraceSystem
		//0: OFF�� 1: ERROR��2: INFO��3: DEBUG��4: ADAPTER
		//ֵԽ����ô�ܸ��ٵ���Ϣ��Խ��ϸ
		//prop.setProperty("TRACE_LEVEL_SYSTEM_OUT", "2");
	}
	@Override
	public void startInternal() throws Exception {
		stmt.executeUpdate("SET @v1 1");
		stmt.executeUpdate("SET @v2 TO 2");
		stmt.executeUpdate("SET @v3 = 3");

		sql = "select @v1, @v2, @v3";
		executeQuery();
	}
}