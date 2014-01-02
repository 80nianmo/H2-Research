�����ֲ���TableView�ķ�ʽ:
1. CREATE VIEW���

��: CREATE OR REPLACE FORCE VIEW IF NOT EXISTS my_view COMMENT IS 'my view'(f1,f2) 
		AS SELECT id,name FROM CreateViewTest


2. Ƕ����FROM�е���ʱ��ͼ

��: select id,name from (select id,name from CreateViewTest)


3. WITH RECURSIVE���

��: WITH RECURSIVE my_tmp_table(f1,f2) 
		AS (select id,name from CreateViewTest UNION ALL select 1, 2) 
			select f1, f2 from my_tmp_table
RECURSIVE����ؼ����ǿ�ѡ��


ֻ��2���Դ�Parameters������ͨ�������������: org.h2.table.TableView.createTempView(Session, User, String, Query, Query)
ֻ��3��recursive��true 


TableView���캯��
	=> init
		=> ViewIndex
		=> initColumnsAndTables
			=> removeViewFromTables
			=> compileViewQuery //���¶�select�����н�����prepare
			=> setColumns
			=> addViewToTables

removeViewFromTables��addViewToTables���Ӧ����view����ӵ���ص�Table��
һ��view��������table��أ�ȡ����select������漰�ı����:
������������:
CREATE OR REPLACE FORCE VIEW my_view (f1,f2) AS SELECT t1.f1, t2.f2 FROM t1, t2?
��ômy_view���漰������t1��t2
removeViewFromTables���Ǵ�t1��t2�а�my_viewɾ����
��addViewToTables���ǰ�my_view�ӵ�t1��t2�С�

Ϊ��ôҪ��removeViewFromTables��addViewToTables�أ�
��Ϊinit������OR REPLACEʱҲ��ͨ��TableView���replace�������ã�������t1��t2������my_view�ˣ�
����CREATE VIEW����䣬���Դ�ʱ��my_view�����п����Ǿɵģ�����Ҫɾ��

initColumnsAndTables���Ǹ���select��䶨��view������Щ�ֶΣ���Щ�ֶε����͸�select�е�select���ʽ�б��еĶ�Ӧ���ʽһ��

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






��Ȼִ��������initColumnsAndTables������init�л�������ViewIndex
���������Ǳ������init��
replace
	=> init
		=> ViewIndex
		=> initColumnsAndTables
			=> removeViewFromTables
			=> compileViewQuery //���¶�select�����н�����prepare
			=> setColumns
			=> addViewToTables
	=> recompile
		=> compileViewQuery
		=> getViews
		=> initColumnsAndTables
		=> getViews�еõ��������ڵ�ǰview�Ķ���Ҫrecompile


