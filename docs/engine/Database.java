һ��ʼ����org.h2.server.TcpServer.initManagementDb()����һ���ڴ����ݿ�ĳ�ʼ��
url��: "jdbc:h2:mem:management_db_" + port (����: jdbc:h2:mem:management_db_9092)


Ĭ�Ͻ���6�����ݿ����
User: DBA (��AdminȨ��)
Schema: PUBLIC
Schema: INFORMATION_SCHEMA
Role: PUBLIC

RegularTable: SYS
Index: SYS_ID  ��org.h2.index.TreeIndex



DBA���User��PUBLIC��INFORMATION_SCHEMA������Schema��owner

����ȷָ��Schemaʱ��Ĭ�Ͼ���PUBLIC Schema
PUBLIC���RoleĬ��û������Ȩ��

SYS�������е�ddl��䣬�˱���4���ֶΣ�ID��HEAD��TYPE��SQL��HEAD�ֶα���û�ã�����0
SYS_ID��SYS����ID�ֶ��ϵ�����

SYS����ͨ��JDBC���ʣ���Ϊ��û���ӵ�PUBLIC Schema��


Schema�ƹ�7���ܴ�Schemaǰ׺��ģʽ���ݿ����
    private final HashMap<String, Table> tablesAndViews;
    private final HashMap<String, Index> indexes;
    private final HashMap<String, Constraint> constraints;
    private final HashMap<String, Sequence> sequences;
    private final HashMap<String, TriggerObject> triggers;
    private final HashMap<String, Constant> constants;
    private final HashMap<String, FunctionAlias> functions;

��Database�������ƹ�������8�����ݿ������Щ������Ϊ����ģʽ���ݿ�������Բ��ܴ�Schemaǰ׺

    private final HashMap<String, User> users = New.hashMap();
    private final HashMap<String, Role> roles = New.hashMap();
    private final HashMap<String, Right> rights = New.hashMap();
    private final HashMap<String, Schema> schemas = New.hashMap();
    private final HashMap<String, UserDataType> userDataTypes = New.hashMap(); //�Զ�����ֶ�����
    private final HashMap<String, UserAggregate> aggregates = New.hashMap(); //�Զ���ۺϺ���
	private final HashMap<String, Comment> comments = New.hashMap(); //ע��
    private final HashMap<String, Setting> settings = New.hashMap(); //���ݡ�Session����Ķ�̬����


1. Database����

	User
	=======

	��create��drop��alter���


	Role
	=======

	��create��drop���


	Right
	=======
	��create��drop��alter���
	һ��Rightʵ����Ӧһ��GRANT ROLE��GRANT RIGHT���


	Schema
	=======

	��create��drop��alter���


	UserDataType
	=======

	��create��drop���
	CREATE DOMAIN��CREATE TYPE��CREATE DATATYPE����һ����


	UserAggregate
	=======
	��create��drop���


	Comment
	=======
	��create��drop��alter���
	ֻ��COMMENT ON��
	���ڱ���ͼ���������С��û���Լ����ע��ֱ�Ӹ��µ����ǵĶ�������
	�������½�Commentʵ��
	��: COMMENT ON ROLE myrole IS 'role comment'


	Setting
	=======
	��create��drop��alter���
	ͨ��SET�������ɣ�SET�����DDL��Ҳ���Ǵ�DML


2. Schema����


	Table
	=======

	��create��drop��alter���

	
	Index
	=======

	��create��drop��alter���


	
	Constraint
	=======

	��create��drop��alter���



	Sequence
	=======

	��create��drop���

	

	TriggerObject
	=======

	��create��drop���

	
	Constant
	=======

	��create��drop���


	FunctionAlias
	=======

	��create��drop���





