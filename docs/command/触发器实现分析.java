��ش���:

1. org.h2.command.Parser.parseCreateTrigger(boolean)
���������������SQL�﷨


2. org.h2.command.ddl.CreateTrigger
������������SQL�﷨ʱ�õ������ݷ���CreateTrigger�����У�
����org.h2.command.ddl.CreateTrigger.update()�������ڲ���org.h2.schema.TriggerObjectʵ����
���Ѵ�TriggerObjectʵ����һЩ��Ϣ�ŵ�Database��Meta��Schema����Table����

3. org.h2.schema.TriggerObject
�ں�org.h2.api.Trigger�ӿڵ�ʵ�����ʵ�����Լ�CreateTrigger���󴫽�����һЩ������
������CRUD����ʱ�ᴥ��:
	org.h2.schema.TriggerObject.fire(Session, int, boolean)
		�˷����Բ���FOR EACH ROW�Ĵ�������Ч
	org.h2.schema.TriggerObject.fireRow(Session, Row, Row, boolean, boolean)
		�˷���ֻ�Լ�FOR EACH ROW�Ĵ�������Ч


4. org.h2.table.Table���е�һЩfire����

org.h2.table.Table.fire(Session, int, boolean)
	�˷����Բ���FOR EACH ROW�Ĵ�������Ч
	��Ϊһ��insert sql�������Ӷ��м�¼���˷����൱��Ӧ��������insert sql�ģ�
	��������������Ӧ����insert sql�е�ÿ�м�¼��
	�����org.h2.command.dml.Insert.insertRows()
	��org.h2.command.dml.Insert.insertRows()��ǰ�����fire��
	����org.h2.command.dml.Insert.insertRows()�ڲ���forѭ���е����������������е�ǰ����
	org.h2.table.Table.fireRow()ֻ����delete��update
	
org.h2.table.Table.fireBeforeRow(Session, Row, Row)
org.h2.table.Table.fireAfterRow(Session, Row, Row, boolean)
org.h2.table.Table.fireRow()
	����������ֻ�Լ�FOR EACH ROW�Ĵ�������Ч

