pageId: 0
========================
�ļ�ͷ
---------------------
16      magic
16      salt  (�����ʹ�ü��ܣ���16�ֽھ���magic�����ʹ���˼��ܣ���ô��������ɵ�)
16      magic (���ʹ���˼��ܣ���ô��16�ֽ��Ƕ�magic���ܺ��ֵ)

StaticHeader
---------------------
4       pageSize Ĭ����2048(2K)
1       WRITE_VERSION ����3
1       READ_VERSION ����3
1994    ����
(��4���д����org.h2.store.PageStore.writeStaticHeader())
(����7��պ���2k)


pageId: 1
========================
VariableHeader
---------------------
4       CRC
8       writeCount
4       logKey
4       logFirstTrunkPage
4       logFirstDataPage
2024    ����(����2k)

pageId: 2 (��pageId 1�����࣬��1��������ʱ��ʹ��2)
========================
VariableHeader
---------------------
4       CRC
8       writeCount
4       logKey
4       logFirstTrunkPage
4       logFirstDataPage
2024    ����(����2k)

page 1��2�ǿɱ�ģ��������ݵĲ���д�룬ǰ5���ֶλ᲻�ϵĸ���


pageId: 3
========================
PageFreeList

pageId: 4
========================
metaIndex

pageId: 5
========================
PageStreamTrunk





ÿ��PageFreeList���Լ���pageId��

PageFreeListҲ��һ��page��

PageFreeList��pageId�ɴ˹�ʽ����: 
pageId = PAGE_ID_FREE_LIST_ROOT + i * freeListPagesPerList
       = 3 + i * freeListPagesPerList
       = 3 + i * ((pageSize - DATA_START) * 8)
       = 3 + i * ((pageSize - DATA_START) * 8)
	   = 3 + i * ((pageSize - 3) * 8)
i>=0 



����һ��page��size��128�ֽڣ���ô��ȥͷ�����ֽں����ʣ�µ��ֽ������ٳ���8�Ա�ʾbitλ��
һ��bit���ܴ���һ��pageId��
��ôһ��PageFreeList�ܱ�ʾ��page������:
(128 - 3) * 8 = 1000


����PageFreeList�Լ���pageId��3����ô�������������pageId��Χ��
3+1 �� 3+1000-1

��һ��PageFreeList��pageId��1003��ʼ
PageFreeList: pageId = 3
PageFreeList: pageId = 1003
PageFreeList: pageId = 2003
PageFreeList: pageId = 3003
PageFreeList: pageId = 4003
PageFreeList: pageId = 5003
PageFreeList: pageId = 6003
PageFreeList: pageId = 7003
PageFreeList: pageId = 8003
PageFreeList: pageId = 9003
PageFreeList: pageId = 10003
PageFreeList: pageId = 11003
PageFreeList: pageId = 12003
PageFreeList: pageId = 13003
PageFreeList: pageId = 14003
PageFreeList: pageId = 15003
PageFreeList: pageId = 16003



��һ��PageFreeList��pageId��4�������metaIndex
java.lang.Error
	at org.h2.store.PageFreeList.allocate(PageFreeList.java:138)
	at org.h2.store.PageStore.allocatePage(PageStore.java:1125)
	at org.h2.store.PageStore.update(PageStore.java:1064)
	at org.h2.index.PageDataIndex.getPage(PageDataIndex.java:233)
	at org.h2.index.PageDataIndex.<init>(PageDataIndex.java:84)
	at org.h2.table.RegularTable.<init>(RegularTable.java:86)
	at org.h2.store.PageStore.openMetaIndex(PageStore.java:1585)
	at org.h2.store.PageStore.openNew(PageStore.java:308)
	at org.h2.store.PageStore.open(PageStore.java:290)
	at org.h2.engine.Database.getPageStore(Database.java:2129)
	at org.h2.engine.Database.open(Database.java:582)
	at org.h2.engine.Database.openDatabase(Database.java:221)
	at org.h2.engine.Database.<init>(Database.java:216)
	at org.h2.engine.Engine.openSession(Engine.java:59)
	at org.h2.engine.Engine.openSession(Engine.java:167)
	at org.h2.engine.Engine.createSessionAndValidate(Engine.java:145)
	at org.h2.engine.Engine.createSession(Engine.java:127)
	at org.h2.server.TcpServerThread.run(TcpServerThread.java:136)
	at java.lang.Thread.run(Thread.java:662)
