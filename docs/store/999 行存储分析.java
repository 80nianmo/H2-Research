(H2��HBase)������or�����еĴ洢ģ��?

                Ŀ¼��(H2��HBase)������or�����еĴ洢ģ��? http://t.cn/zOLdrmE ����״̬���ѣ���ð��û���񣬲������Ǿ�������ˡ����ϻ�������·��һ�����H2��HBase�Ĵ洢���涼��ȫ������̫�ѣ�ʱ�����ޣ�H2�о�͸�˾�������ʱ��д��
	0. ʾ��

	1. H2��ô�洢pet��ļ�¼?
		1. 1 DATA_LEAFҳ��ʽ
		1. 2 DATA_NODEҳ��ʽ

	2. HBase��ô�洢pet��ļ�¼?
		2. 1 Data Blockҳ��ʽ
		2. 2 Data Block��δ�������Щ��¼?
		2. 3 leaf������ĸ�ʽ:
		2. 4 root������ĸ�ʽ:
		2. 5 IntermediateLevel������

0. ʾ��

����������һ��pet��(�ı���MySQL�ο��ֲ� http://dev.mysql.com/doc/refman/5.1/zh/tutorial.html#database-use)

CREATE TABLE pet (
    id INT PRIMARY KEY,
	name VARCHAR(20),
	owner VARCHAR(20),
	species VARCHAR(20),
	sex CHAR(1)
);

�����¼�¼:
id    name    owner   species sex
===============================
1001  Fluffy  Harold  cat	 f
1002  Claws   Gwen    cat	 m
1003  Buffy   Harold  dog	 f


1. H2��ô�洢pet��ļ�¼?

H2��һ��Java SQL Database Engine��ʹ��������(row-oriented)�Ĵ洢ģ��(��������ֿڣ��ͽл�����(row-based)�Ĵ洢ģ�Ͱ�)��
��Ԫ�������ļ�¼�ֿ�������ͨ����INFORMATION_SCHEMA��ص�ϵͳ��������Ԫ���ݣ�
������ͨ��JDBC��java.sql.DatabaseMetaData�ṩ�����API�����ҡ�


H2�ڲ��Ĵ洢����ʹ��ҳ(Page)����֯���ݣ�ҳ�Ĵ�СĬ����2K����ͨ������PAGE_SIZE������
��8�ֲ�ͬ��ҳ��
DATA_LEAF
DATA_NODE
DATA_OVERFLOW
BTREE_LEAF
BTREE_NODE
FREE_LIST
STREAM_TRUNK
STREAM_DATA

����ֻ��˵��H2��ô���еķ�ʽ����֯���ݣ�����ֻ�ص㽲DATA_LEAF��DATA_NODE������ҳ��

1. 1 DATA_LEAFҳ��ʽ

��             ռ���ֽ���
======================================================
type           1
checksum       2 (���������һ��������0ֵռλ����д��һҳ��Դ�ҳ�����ݼ���У����ٻ���)
parentPageId   4 (���Ǹ�DATA_NODE�ڵ��ID)
tableId        �ɱ�int (��ҳ�����ı��ID)
columnCount    �ɱ�int (��ҳ�����ı��ж�����)
entryCount     2 (��ҳ�ж�������¼)

entryCount��
{
	key        �ɱ�long (��������������������BYTE��SHORT��INT��LONG���ͣ����key����������ֵ������Ϊÿ������һ��Ψһ����ֵ)
	offset     2 (�����ڴ�ҳ�е����λ��)
}

entryCount��
{
	columnValues ÿһ�м�¼�Ӷ�Ӧ��offsetλ�ÿ�ʼ��ţ���ͬ���͵��ֶλ�ʹ��1��Byte����ʶ�Լ��ĸ�ʽ
}
======================================================

DATA_LEAFҳ������ű�������������������ЩԪ����
����pet���е����м�¼�ŵ�һ��DATA_LEAFҳ�л�������(Ϊ�˷����Ķ�δϸ��ÿһ���ֽڣ�ʵ�ʸ�����һЩ):

id    name    owner   species sex
===============================
1001  Fluffy  Harold  cat	 f
1002  Claws   Gwen    cat	 m
1003  Buffy   Harold  dog	 f


��             ֵ
======================================================
type           17 (Page.TYPE_DATA_LEAF | Page.FLAG_LAST(��ǰֻ��һ��ҳ))
checksum       0x75CE (У��ͼ���Ƚϸ��ӣ�Ҳ�ܵ�ǰpageId��pageTyep��pageSizeӰ��)
parentPageId   0 (���Ǹ�DATA_NODE�ڵ��ID��0˵����root�ڵ�)
tableId        14 (��ҳ�����ı��ID)
columnCount    5 (��ҳ�����ı��ж�����)
entryCount     3 (��ҳ�ж�������¼)

{
	1001, 2024, 1002, 2003, 1003, 1980 (����1001��1002��1003��key��2024��2003��1980��offset)
}
{
	( /* key:1001 */ 1001, 'Fluffy', 'Harold', 'cat', 'f')
	( /* key:1002 */ 1002, 'Claws', 'Gwen', 'cat', 'm')
	( /* key:1003 */ 1003, 'Buffy', 'Harold', 'dog', 'f')
}
======================================================


1. 2 DATA_NODEҳ��ʽ

��ĳ��DATA_LEAFҳ(page0)�Ĵ�С����pageSizeʱ��������е�һ���֣��õ�һ���µ�DATA_LEAFҳ(page1)��
page0���и�㿪ʼ���ҵ�keys�ͼ�¼��ת��page1�У��и����ߵļ�������page0��
ͬʱ����һ���µ�DATA_NODEҳ��Ϊpage0��page1�ĸ��ڵ㡣

DATA_NODEҳ��ʽ

��             ռ���ֽ���
======================================================
type           1
checksum       2 (���������һ��������0ֵռλ����д��һҳ���Щҳ�����ݼ���У����ٻ���)
parentPageId   4 (���Ǹ�DATA_NODE�ڵ��ID)
tableId        �ɱ�int (��ҳ�����ı��ID)
rowCountStored 4
entryCount     2

entryCount��
{
	childPageId  4
	key          �ɱ�long
}

======================================================



2. HBase��ô�洢pet��ļ�¼?

HBaseʹ��������(column-oriented)�Ĵ洢ģ�ͣ�����Ҫ�����Ľṹ(schema-free),
������ʱ��̬����µ��У������϶����еĸ���û�����ƣ�
����кܶ࣬���԰���ص�һ���й�����һ��������(Column Family)����ַ������ݵľۺ����ԡ�

ͨ��RowKey�ܰ�ͬһ�����������е��й���������
HBase��RowKey�����������е������ڴ�ͳ��ϵ���ݿ�����������ù�ϵ������������
����ֻ���������룬HBase��������ͬ������ƽ�ȵģ�û�����ӹ�ϵ��ֻ��ͨ��RowKey�����߹���������

HBase��Data Block��ӦH2��DATA_LEAF��
HBase��Leaf Index Block��ӦH2��DATA_NODE��


2. 1 Data Blockҳ��ʽ

Data Block�ĸ�ʽ�е㸴�ӣ���������㿴������Բ��ù��ĵ�

ÿ��block��һ��33�ֽڵ�ͷ
===========================
ǰ8���ֽ��Ǳ�ʾblock���͵�MAGIC����Ӧorg.apache.hadoop.hbase.io.hfile.BlockType����Щö�ٳ�������
����4���ֽڱ�ʾonDiskBytesWithHeader.length - HEADER_SIZE
����4���ֽڱ�ʾuncompressedSizeWithoutHeader
����8���ֽڱ�ʾprevOffset (ǰһ�����offset�����磬���ڵ�һ���飬��ô��������prevOffset��-1�����ڵڶ����飬��0)

����1���ֽڱ�ʾchecksumType code(Ĭ����1: org.apache.hadoop.hbase.util.ChecksumType.CRC32)
����4���ֽڱ�ʾbytesPerChecksum(Ĭ��16k������С��blockͷ����(ͷ������33���ֽ�))
���4���ֽڱ�ʾonDiskDataSizeWithHeader



����ʹ��ѹ��ʱonDiskBytesWithHeader������checksums,
��ʱchecksums����onDiskChecksum�У�
��ʹ��ѹ��ʱchecksums����onDiskBytesWithHeader

checksums���ǰ�onDiskBytesWithHeader�е������ֽ���bytesPerChecksum���ֽ�Ϊ��λ��У��ͣ����У�����int(4�ֽ�)��ʾ��
ע�⣬����У���ʱ��onDiskBytesWithHeader�л�û��checksums


Ĭ��ÿ��Data Block�Ĵ�С��64K(ͷ(33�ֽ�)����������)(����ͨ��HColumnDescriptor.setBlocksize����)��
64Kֻ��һ����ֵ��ʵ�ʵĿ��СҪ������(ȡ�����������KeyValue�Ĵ�С)��
�����ϴδ����KeyValue���¿��С���63K�ˣ����ǻ�û��64K����ô���Ŵ�����һ��KeyValue�������KeyValue��5K��
��ô�����Ĵ�С�ͱ����63+5=68K�ˡ�

ÿ��append��writer�е���һ��KeyValue��
����4�ֽڵ�key���Ⱥ�4�ֽڵ�value���ȣ�
������д��key���ֽ�����value���ֽ��롣

ͬʱ��ÿ��block�ĵ�һ��key���浽firstKeyInBlock�ֶ���
=================================================================================


���⿪ʼ���ص�:

Data Block�ĺ�����һ��KeyValue��
KeyValue�ĸ�ʽ����:

���ơ�����          �ֽ���                  ˵��
--------------------------------------------------------------------
keyLength����         4                     ��ʾKey��ռ�����ֽ���
valueLength           4                     ��ʾValue��ռ�����ֽ���

rowKeyLength          2                     ��ʾrowKey��ռ���ֽ���
rowKey                rowKeyLength          rowKey
columnFamilyLength    1                     ��ʾ����������ռ���ֽ���
columnFamily          columnFamilyLength    ��������
columnName            columnNameLength      ����
timestamp             8                     ʱ���
type                  1                     Key���ͣ�����������(Put)������ɾ��(Delete)

value                 valueLength           ��ֵ
--------------------------------------------------------------------
                      ��2.1

����KeyValue����������ϸ�������뿴����: http://zhh2009.iteye.com/blog/1412315


2. 2 Data Block��δ�������Щ��¼?

id    name    owner   species sex
===============================
1001  Fluffy  Harold  cat	 f
1002  Claws   Gwen    cat	 m
1003  Buffy   Harold  dog	 f

��id��Ϊrowkey�������в���������HBase������Ҫһ�����壬������������"mycf"��
�����м�¼�����4*3=12��KeyValue�� 
�浽Data Block�����������Ȱ�rowkey����rowkey��ͬ�İ����������ţ�
���Ĳ�����������(Ϊ�˼�ȥ����һЩϸ��):

<rowKey �������� ���� ʱ���, ��ֵ>
====================================================
<1001  mycf  name     timestamp, Fluffy>
<1001  mycf  owner    timestamp, Harold>
<1001  mycf  sex      timestamp, f>
<1001  mycf  species  timestamp, cat>

<1002  mycf  name     timestamp, Claws>
<1002  mycf  owner    timestamp, Gwen>
<1002  mycf  sex      timestamp, m>
<1002  mycf  species  timestamp, cat>

<1003  mycf  name     timestamp, Buffy>
<1003  mycf  owner    timestamp, Harold>
<1003  mycf  sex      timestamp, f>
<1003  mycf  species  timestamp, dog>
====================================================

��H2��DATA_LEAF��ȣ�
====================================================
( /* key:1001 */ 1001, 'Fluffy', 'Harold', 'cat', 'f')
( /* key:1002 */ 1002, 'Claws', 'Gwen', 'cat', 'm')
( /* key:1003 */ 1003, 'Buffy', 'Harold', 'dog', 'f')
======================================================

HBase�ĸ�ʽ���ڴ���������(����rowKey���������ơ�������ʱ���)
֮����Ҫ��������Ϊ��ˮƽ��չ���ļ��ϲ��зָ����ף�
��ΪHBase������������������ЩԪ���ݺ���ֵ����һ���ˣ������ڷ���ʱֻ��򵥰�rowkey�з֣�
���ܰ��������ݶ�ת�Ƶ���һ̨�����ϣ�����Ҫ��H2����Ҫ����INFORMATION_SCHEMA�е�Ԫ��������¼�Ƿ�ͬ�������⡣

HBase����LSM-Tree�����KeyValue��H2������B+Tree�Ľṹ��
LSM-Treeֻ����һ������ӣ�����Ҫ���ǽ���ɾ���޸ģ�
���ݻ���д���ڴ�(MemStore)��Ȼ���ڴ����˾�flush��Ӳ�̱��һ��СLSM-Tree��
���LSM-Tree�ᶨ�ںϲ���һ�ø����LSM-Tree����һ���̶����з��Զ���ɢ������������

B+Tree�����С��е���ӡ�ɾ����Ҫ�Խ����е��������ݸ��»����overflow��


���⣬�۲������������ݣ�H2�ķ���ֻ�ǰ�Ԫ���ݳ�����ŵ����ˣ�Ȼ��ͨ����id��DATA_LEAF��Ԫ���ݹ�����
HBase 0.94����ʹ��ǰ׺ѹ���İ취�����ظ��Ķ�����ȡ������
�����������������ݷֱ𴮳�һ�У���ʵ��𲻴�ֻ��HBase���˺ܶ�������Ϣ���ѣ�
��������Ϣ���һ�����ҿ�����row-oriented��column-oriented��ʲô������������HBase��H2���е����Ƶġ�

��������������:
1) LSM-Tree��B+Tree
2) �Ƿ���schema-free��


(�������ݲ���Ҫ)

2. 3 leaf������ĸ�ʽ:

   ���ݿ��ܸ���N(int����,4�ֽ�)
   N��"���ݿ��ڴ��������е����λ��"(��0��ʼ������ÿ��Entry�Ĵ�С�ۼӣ�ÿ�����λ����int����,4�ֽ�)
   N��Entry�����ֽ���(int����,4�ֽ�)
   N��Entry {
     ���ݿ����ļ��е����λ��(long����,8�ֽ�)
     ���ݿ���ܳ���(����ͷ)  (int����,4�ֽ�)
	 ���ݿ��һ��KeyValue�е�Key�ֽ�����
   }


2. 4 root������ĸ�ʽ:

   N��leaf������Entry {
     leaf���������ļ��е����λ��(long����,8�ֽ�)
     leaf��������ܳ���(����ͷ)  (int����,4�ֽ�)
	 leaf�������һ��Entry��Key�ֽ�����
   }


2. 5 IntermediateLevel������

   ��leaf���������ƣ�ֻ��������Entry�ڵ�һ��IntermediateLevel��leaf������Entry���ڶ����Ժ���IntermediateLevel���entry��

����key��˳��
root������ ==> IntermediateLevel������ ==> leaf������ ==> ���ݿ�


