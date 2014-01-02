package my.test.command.dml;

import my.test.TestBase;

public class ScriptCommandTest extends TestBase {
	public static void main(String[] args) throws Exception {
		new ScriptCommandTest().start();
	}

	@Override
	public void startInternal() throws Exception {
		sql = "BACKUP TO E:/H2/baseDir/myBackup"; //�ļ���Ҫ�ӵ�����
		sql = "BACKUP TO 'E:/H2/baseDir/myBackup'";
		sql = "SCRIPT NODATA"; //���ɸ���Create SQL��������ؽ����������Ҫ��executeQuery
		executeQuery();
	}
}