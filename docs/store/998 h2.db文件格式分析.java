
��H2�У�ÿ�����ݿ�ı��¼�ͱ�Ԫ���ݷ���һ����"h2.db"��β���ļ��У�
����"test.h2.db"���Ǵ��"test"������ݿ���ļ�


"h2.db"�ļ��ĸ�ʽ:

���"h2.db"�ļ�����Ҫ���ܣ����������ֶζ���ͬ������"-- H2 0.5/B -- \n"(16���ֽ�)
����Ϊ�˼�����������Ǽ��ܵ����

�ֽ���  ����
========================
16      magic
16      salt
16      magic
(�������д��Ͷ�ȡ��org.h2.store.FileStore.init())

��������ÿ��Page

StaticHeader
=================
4       pageSize Ĭ����2048(2K)
1       WRITE_VERSION ����3
1       READ_VERSION ����3
1994    ����
(��4���д����org.h2.store.PageStore.writeStaticHeader())
(����7��պ���2k)

VariableHeader
=================
4       CRC
8       writeCount
4       logKey
4       logFirstTrunkPage
4       logFirstDataPage
2024    ����(����2k)

4       CRC
8       writeCount
4       logKey
4       logFirstTrunkPage
4       logFirstDataPage
2024    ����(����2k)
(����������ͬ������ֻ����ǰ����Чʱ(����CRC����)������)
(��12���д����org.h2.store.PageStore.writeVariableHeader())

��һ��PageFreeList��pageId��3

��4��pageҲ����pageId=4��ҳ��metaIndex

java.lang.Error
	at org.h2.store.PageStore.allocatePage(PageStore.java:1113)
	at org.h2.store.PageStore.update(PageStore.java:1062)
	at org.h2.index.PageDataIndex.getPage(PageDataIndex.java:231)
	at org.h2.index.PageDataIndex.<init>(PageDataIndex.java:82)
	at org.h2.table.RegularTable.<init>(RegularTable.java:86)
	at org.h2.store.PageStore.openMetaIndex(PageStore.java:1570)
	at org.h2.store.PageStore.openNew(PageStore.java:306)
	at org.h2.store.PageStore.open(PageStore.java:288)
	at org.h2.engine.Database.getPageStore(Database.java:2123)
	at org.h2.engine.Database.open(Database.java:582)
	at org.h2.engine.Database.openDatabase(Database.java:222)
	at org.h2.engine.Database.<init>(Database.java:217)
	at org.h2.engine.Engine.openSession(Engine.java:56)
	at org.h2.engine.Engine.openSession(Engine.java:159)
	at org.h2.engine.Engine.createSessionAndValidate(Engine.java:138)
	at org.h2.engine.Engine.createSession(Engine.java:121)
	at org.h2.server.TcpServerThread.run(TcpServerThread.java:136)
	at java.lang.Thread.run(Thread.java:662)


��5��pageҲ����pageId=5��ҳ��PageStreamTrunk

Ȼ����Ԥ����PageStreamTrunk.getPagesAddressed(pageSize)��ҳ��PageStreamData

��дPageStreamTrunk
java.lang.Error
	at org.h2.store.PageStore.writePage(PageStore.java:1337)
	at org.h2.store.PageStreamTrunk.write(PageStreamTrunk.java:140)
	at org.h2.store.PageOutputStream.initNextData(PageOutputStream.java:100)
	at org.h2.store.PageOutputStream.reserve(PageOutputStream.java:78)
	at org.h2.store.PageLog.openForWriting(PageLog.java:186)
	at org.h2.store.PageStore.openNew(PageStore.java:308)
	at org.h2.store.PageStore.open(PageStore.java:288)
	at org.h2.engine.Database.getPageStore(Database.java:2123)
	at org.h2.engine.Database.open(Database.java:582)
	at org.h2.engine.Database.openDatabase(Database.java:222)
	at org.h2.engine.Database.<init>(Database.java:217)
	at org.h2.engine.Engine.openSession(Engine.java:56)
	at org.h2.engine.Engine.openSession(Engine.java:159)
	at org.h2.engine.Engine.createSessionAndValidate(Engine.java:138)
	at org.h2.engine.Engine.createSession(Engine.java:121)
	at org.h2.server.TcpServerThread.run(TcpServerThread.java:136)
	at java.lang.Thread.run(Thread.java:662)
