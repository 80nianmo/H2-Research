��Ҫ��ص�����org.h2.command����

SQL�����org.h2.command.Parser�����

SQL��������:
DML (data manipulation language)
	=> ��Ӧorg.h2.command.dml��

DDL (data definition language) 
	=> ��Ӧorg.h2.command.ddl��


����CREATE TABLE����Ӧorg.h2.command.ddl.CreateTable��
�ٱ���INSERT����Ӧorg.h2.command.dml.Insert��

SHOW�����Parser���е���SELECT��䣬��Ӧorg.h2.command.dml.Select��

org.h2.command.Prepared������SQL����Ӧ����ĳ���

Prepared�����api��һ�����ȵ���prepare���ٵ���update��query��update��Ӧ���²�����query��Ӧ��ѯ������


org.h2.command.CommandInterface�ӿ�������ʾִ�е�SQL����
	<= org.h2.command.Command (���ڷ����)
		<= org.h2.command.CommandContainer
		<= org.h2.command.CommandList
	<= org.h2.command.CommandRemote (���ڿͻ���)

	Command��CommandRemoteͨ���Ƕ�Ӧ�ģ�CommandRemote������JDBC��ʵ����(org.h2.jdbc���е���)��
	��CommandRemote�������������������Command��֮��Ӧ��



1. DDL����

DDL������:
alter
drop
create

2. Schema����

��7��Schema����
---------------------------------
    private final HashMap<String, Table> tablesAndViews;
    private final HashMap<String, Index> indexes;
    private final HashMap<String, Sequence> sequences;
    private final HashMap<String, TriggerObject> triggers;
    private final HashMap<String, Constraint> constraints;
    private final HashMap<String, Constant> constants;
    private final HashMap<String, FunctionAlias> functions;


