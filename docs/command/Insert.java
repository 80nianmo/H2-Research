new Error().printStackTrace();

org.h2.command.dml.Insert.update()
	=> org.h2.command.dml.Insert.insertRows()


	org.h2.command.dml.Insert.addRow(Expression[])
	org.h2.command.dml.Insert.prepare()
	org.h2.command.dml.Insert.update()
	org.h2.command.dml.Insert.insertRows()

	=> org.h2.table.RegularTable.addRow(Session, Row)

����:
CREATE TABLE TEST(ID INT PRIMARY KEY, NAME VARCHAR(255))

����insert sql
INSERT INTO TEST(ID, NAME) VALUES(3000, 'aaa')

��1��
//����insert sql���õ��������磺[ID, NAME]
org.h2.command.dml.Insert.setColumns(Column[])
java.lang.Error
	at org.h2.command.dml.Insert.setColumns(Insert.java:62)
	at org.h2.command.Parser.parseInsert(Parser.java:968)
	at org.h2.command.Parser.parsePrepared(Parser.java:375)
	at org.h2.command.Parser.parse(Parser.java:279)
	at org.h2.command.Parser.parse(Parser.java:251)
	at org.h2.command.Parser.prepareCommand(Parser.java:217)
	at org.h2.engine.Session.prepareLocal(Session.java:415)
	at org.h2.server.TcpServerThread.process(TcpServerThread.java:253)
	at org.h2.server.TcpServerThread.run(TcpServerThread.java:149)
	at java.lang.Thread.run(Unknown Source)

��2��
//����insert sql���õ���ֵ���磺[3000, 'aaa']
org.h2.command.dml.Insert.addRow(Expression[])
java.lang.Error
	at org.h2.command.dml.Insert.addRow(Insert.java:75)
	at org.h2.command.Parser.parseInsert(Parser.java:993)
	at org.h2.command.Parser.parsePrepared(Parser.java:375)
	at org.h2.command.Parser.parse(Parser.java:279)
	at org.h2.command.Parser.parse(Parser.java:251)
	at org.h2.command.Parser.prepareCommand(Parser.java:217)
	at org.h2.engine.Session.prepareLocal(Session.java:415)
	at org.h2.server.TcpServerThread.process(TcpServerThread.java:253)
	at org.h2.server.TcpServerThread.run(TcpServerThread.java:149)
	at java.lang.Thread.run(Unknown Source)

��3��
org.h2.command.dml.Insert.prepare()
java.lang.Error
	at org.h2.command.dml.Insert.prepare(Insert.java:215)
	at org.h2.command.Parser.prepareCommand(Parser.java:218)
	at org.h2.engine.Session.prepareLocal(Session.java:415)
	at org.h2.server.TcpServerThread.process(TcpServerThread.java:253)
	at org.h2.server.TcpServerThread.run(TcpServerThread.java:149)
	at java.lang.Thread.run(Unknown Source)

��4��
org.h2.command.dml.Insert.update()
java.lang.Error
	at org.h2.command.dml.Insert.update(Insert.java:79)
	at org.h2.command.CommandContainer.update(CommandContainer.java:75)
	at org.h2.command.Command.executeUpdate(Command.java:230)
	at org.h2.server.TcpServerThread.process(TcpServerThread.java:328)
	at org.h2.server.TcpServerThread.run(TcpServerThread.java:149)
	at java.lang.Thread.run(Unknown Source)


org.h2.table.RegularTable.addRow(Session, Row)
org.h2.index.PageDataIndex.add(Session, Row)
org.h2.index.PageDataIndex.addTry(Session, Row)

org.h2.index.PageDataLeaf.addRowTry(Row)

��PageDataLeaf�е�org.h2.index.PageData.keys ���key
��org.h2.index.PageDataLeaf.rows�д�Ŷ�������

ÿ��PageDataLeaf������org.h2.store.PageStore.cache��
��org.h2.store.PageStore.writeBack()�а�ÿ��PageDataLeaf������д��Ӳ�̡�

java.lang.Error
	at org.h2.util.CacheLRU.addToFront(CacheLRU.java:225)
	at org.h2.util.CacheLRU.update(CacheLRU.java:127)
	at org.h2.store.PageStore.update(PageStore.java:1059)
	at org.h2.index.PageDataLeaf.addRowTry(PageDataLeaf.java:206)
	at org.h2.index.PageDataIndex.addTry(PageDataIndex.java:167)
	at org.h2.index.PageDataIndex.add(PageDataIndex.java:130)
	at org.h2.table.RegularTable.addRow(RegularTable.java:121)
	at org.h2.command.dml.Insert.insertRows(Insert.java:124)
	at org.h2.command.dml.Insert.update(Insert.java:84)
	at org.h2.command.CommandContainer.update(CommandContainer.java:75)
	at org.h2.command.Command.executeUpdate(Command.java:230)
	at org.h2.server.TcpServerThread.process(TcpServerThread.java:328)
	at org.h2.server.TcpServerThread.run(TcpServerThread.java:149)
	at java.lang.Thread.run(Unknown Source)

java.lang.Exception
	at org.h2.index.PageDataNode.create(PageDataNode.java:66)
	at org.h2.index.PageDataIndex.addTry(PageDataIndex.java:181)
	at org.h2.index.PageDataIndex.add(PageDataIndex.java:130)
	at org.h2.table.RegularTable.addRow(RegularTable.java:121)
	at org.h2.command.dml.Insert.insertRows(Insert.java:124)
	at org.h2.command.dml.Insert.update(Insert.java:84)
	at org.h2.command.CommandContainer.update(CommandContainer.java:75)
	at org.h2.command.Command.executeUpdate(Command.java:230)
	at org.h2.server.TcpServerThread.process(TcpServerThread.java:328)
	at org.h2.server.TcpServerThread.run(TcpServerThread.java:149)
	at java.lang.Thread.run(Unknown Source)


java.lang.Error
	at org.h2.index.PageDataLeaf.write(PageDataLeaf.java:456)
	at org.h2.store.PageStore.writeBack(PageStore.java:1009)
	at org.h2.store.PageStore.writeBack(PageStore.java:412)
	at org.h2.store.PageStore.checkpoint(PageStore.java:430)
	at org.h2.engine.Database.closeOpenFilesAndUnlock(Database.java:1196)
	at org.h2.engine.Database.close(Database.java:1149)
	at org.h2.engine.Database.removeSession(Database.java:1028)
	at org.h2.engine.Session.close(Session.java:563)
	at org.h2.server.TcpServerThread.closeSession(TcpServerThread.java:175)
	at org.h2.server.TcpServerThread.process(TcpServerThread.java:270)
	at org.h2.server.TcpServerThread.run(TcpServerThread.java:149)
	at java.lang.Thread.run(Unknown Source)



��һ�ν��������ѡ����һ��root PageDataLeaf
��org.h2.index.PageDataIndex.PageDataIndex(RegularTable, int, IndexColumn[], IndexType, boolean, Session)
		if (create) {
            rootPageId = store.allocatePage();
            store.addMeta(this, session);
            PageDataLeaf root = PageDataLeaf.create(this, rootPageId, PageData.ROOT);
            store.update(root);

����PageDataLeafʱ����дͷ����ʱchecksum��û�У�����0
PageDataLeaf����org.h2.store.PageStore.cache


