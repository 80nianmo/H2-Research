��ص���:
org.h2.command.dml.SetTypes   //��̬����������ʱ���޸�
org.h2.engine.ConnectionInfo  //connection��صĲ�������̬������������ʱָ��
org.h2.constant.DbSettings    //���ݿ⼶��Ĳ�������̬����
org.h2.constant.SysProperties //ϵͳ��������̬����

1.SetTypes

SetTypes�еĲ���������Ϊ�Ƕ�̬�ģ�����ͨ��SET�����޸ģ�
SetTypes����������Щ����(���Ǵ�д�ģ�����Ϊ�˷����Ķ��ĳ�Сд)
�ܹ�37������һ�����㣬��null���������ʵ��
	null
	ignorecase
	max_log_size
	mode
	readonly
	lock_timeout
	default_lock_timeout
	default_table_type
	cache_size
	trace_level_system_out
	trace_level_file
	trace_max_file_size
	collation
	cluster
	write_delay
	database_event_listener
	max_memory_rows
	lock_mode
	db_close_delay
	log
	throttle
	max_memory_undo
	max_length_inplace_lob
	compress_lob
	allow_literals
	multi_threaded
	schema
	optimize_reuse_results
	schema_search_path
	undo_log
	referential_integrity
	mvcc
	max_operation_memory
	exclusive
	create_build
	@
	query_timeout
	redo_log_binary

//������
	@
	allow_literals
	cache_size
	cluster
	collation
	compress_lob
	create_build
	database_event_listener
	db_close_delay
	default_lock_timeout
	default_table_type
	exclusive
	ignorecase
	lock_mode
	lock_timeout
	log
	max_length_inplace_lob
	max_log_size
	max_memory_rows
	max_memory_undo
	max_operation_memory
	mode
	multi_threaded
	mvcc
	optimize_reuse_results
	query_timeout
	readonly
	redo_log_binary
	referential_integrity
	schema
	schema_search_path
	throttle
	trace_level_file
	trace_level_system_out
	trace_max_file_size
	undo_log
	write_delay


2.ConnectionInfo

ConnectionInfo��׷����connection��صĲ�����(���Ǵ�д�ģ�����Ϊ�˷����Ķ��ĳ�Сд)
�ܹ�21��
	access_mode_data
	autocommit
	cipher
	create
	cache_type
	file_lock
	ignore_unknown_settings
	ifexists
	init
	password
	recover
	recover_test
	user
	auto_server
	auto_server_port
	no_upgrade
	auto_reconnect
	open_new
	page_size
	password_hash
	jmx
//������
	access_mode_data
	auto_reconnect
	auto_server
	auto_server_port
	autocommit
	cache_type
	cipher
	create
	file_lock
	ifexists
	ignore_unknown_settings
	init
	jmx
	no_upgrade
	open_new
	page_size
	password
	password_hash
	recover
	recover_test
	user

3.DbSettings

DbSettings�ܹ�37��:(���Ǵ�д�ģ�����Ϊ�˷����Ķ��ĳ�Сд)
��ͨ��-D����java.lang.System.setProperty(String, String)�޸ģ�����һ�����˾Ͳ��ܱ���
��-Dmax_compact_time=200��System.setProperty("max_compact_time", "200")

	optimize_insert_from_select			true
	max_compact_time			200
	early_filter			false
	optimize_distinct			true
	reconnect_check_delay			200
	nested_joins			true
	large_result_buffer_size			4096
	share_linked_connections			true
	optimize_is_null			true
	optimize_or			true
	optimize_in_select			true
	analyze_sample			10000
	analyze_auto			2000
	large_transactions			true
	optimize_in_list			true
	optimize_two_equals			true
	database_to_upper			true
	alias_column_name			false
	defrag_always			false
	estimated_function_table_rows			1000
	max_memory_rows_distinct			10000
	optimize_evaluatable_subqueries			true
	recompile_always			false
	drop_restrict			true
	max_compact_count			2147483647
	select_for_update_mvcc			true
	default_connection			false
	rowid			true
	query_cache_size			8
	optimize_update			true
	db_close_on_exit			true
	page_store_trim			true
	functions_in_schema			true
	max_query_timeout			0
	default_escape			\
	page_store_internal_count			false
	page_store_max_growth			131072

//������
	alias_column_name			false
	analyze_auto			2000
	analyze_sample			10000
	database_to_upper			true
	db_close_on_exit			true
	default_connection			false
	default_escape			\
	defrag_always			false
	drop_restrict			true
	early_filter			false
	estimated_function_table_rows			1000
	functions_in_schema			true
	large_result_buffer_size			4096
	large_transactions			true
	max_compact_count			2147483647
	max_compact_time			200
	max_memory_rows_distinct			10000
	max_query_timeout			0
	nested_joins			true
	optimize_distinct			true
	optimize_evaluatable_subqueries			true
	optimize_in_list			true
	optimize_in_select			true
	optimize_insert_from_select			true
	optimize_is_null			true
	optimize_or			true
	optimize_two_equals			true
	optimize_update			true
	page_store_internal_count			false
	page_store_max_growth			131072
	page_store_trim			true
	query_cache_size			8
	recompile_always			false
	reconnect_check_delay			200
	rowid			true
	select_for_update_mvcc			true
	share_linked_connections			true