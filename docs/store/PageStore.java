PageStore��ʵ����org.h2.engine.Database.getPageStore()������

Ȼ����������̵���:
org.h2.store.PageStore.open()
	=> org.h2.store.PageStore.openNew() //���ݿ��ļ�������
	
��	=> org.h2.store.PageStore.openExisting() //���ݿ��ļ��Ѵ���
		=> org.h2.engine.Database.openFile(String, String, boolean)
			=> org.h2.store.FileStore.open(DataHandler, String, String, String, byte[])
				=> org.h2.store.FileStore.FileStore(DataHandler, String, String)

			=> org.h2.store.FileStore.init()


PAGE_INDEX��
CREATE CACHED TABLE "".PAGE_INDEX(
    ID INTEGER,
    TYPE INTEGER,
    PARENT INTEGER,
    HEAD INTEGER,
    OPTIONS VARCHAR,
    COLUMNS VARCHAR
)
    private void openMetaIndex() {
        CreateTableData data = new CreateTableData();
        ArrayList<Column> cols = data.columns;
        cols.add(new Column("ID", Value.INT)); //index id
        cols.add(new Column("TYPE", Value.INT)); //��ӦMETA_TYPE_DATA_INDEX��META_TYPE_BTREE_INDEX
        cols.add(new Column("PARENT", Value.INT)); //table id
        cols.add(new Column("HEAD", Value.INT)); //RootPageId
        cols.add(new Column("OPTIONS", Value.STRING)); //CompareMode name��Strength����ʱ��d(��ʾ��PageDelegateIndex)
        cols.add(new Column("COLUMNS", Value.STRING)); //��id/sortType
        metaSchema = new Schema(database, 0, "", null, true);
        data.schema = metaSchema;
        data.tableName = "PAGE_INDEX";
        data.id = META_TABLE_ID; //id��-1
        data.temporary = false;
        data.persistData = true;
        data.persistIndexes = true;
        data.create = false;
        data.session = systemSession;
        metaTable = new RegularTable(data);
        metaIndex = (PageDataIndex) metaTable.getScanIndex(
                systemSession);
        metaObjects.clear();
        metaObjects.put(-1, metaIndex);
    }

//�ڴ����ݿⲻ������PageStoreʵ��

//ÿ��Page��sizeĬ����2k
//һ��PageStore�� ʵ���ʹ���һ��".h2.db"�ļ�


���ȿ�ʼ����org.h2.store.PageStore.allocatePage()������client�˷���ķ��ڴ����ݿ����ʱ
��org.h2.engine.Database.open��meta = mainSchema.createTable(data)����
-------------------------------------------------------------
java.lang.Error
	at org.h2.index.PageDataIndex.<init>(PageDataIndex.java:78)
	at org.h2.table.RegularTable.<init>(RegularTable.java:86)
	at org.h2.schema.Schema.createTable(Schema.java:556)
	at org.h2.engine.Database.open(Database.java:622)
	at org.h2.engine.Database.openDatabase(Database.java:221)
	at org.h2.engine.Database.<init>(Database.java:216)
	at org.h2.engine.Engine.openSession(Engine.java:59)
	at org.h2.engine.Engine.openSession(Engine.java:167)
	at org.h2.engine.Engine.createSessionAndValidate(Engine.java:145)
	at org.h2.engine.Engine.createSession(Engine.java:127)
	at org.h2.server.TcpServerThread.run(TcpServerThread.java:136)
	at java.lang.Thread.run(Thread.java:662)


�������org.h2.store.PageStore.getFreeList(int)������:
-------------------------------------------------------------
java.lang.Error
	at org.h2.store.PageStore.getFreeList(PageStore.java:1084)
	at org.h2.store.PageStore.getFreeListForPage(PageStore.java:1079)
	at org.h2.store.PageStore.allocatePage(PageStore.java:1124)
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



org.h2.store.PageStore.addMeta(PageIndex, Session)
( /* key:17 */ 16, 1, 15, 70, 'OFF,0,,', '1')

��org.h2.engine.Database.getPageStore()��ʼ����PageStore�ĳ�ʼ��

	//fileName = E:/H2/test.h2.db
	//accessMode = rw
	//cacheSizeDefault = 16384 (Ĭ��16K)����ͨ��CACHE_SIZE��������
    public PageStore(Database database, String fileName, String accessMode, int cacheSizeDefault) {
        this.fileName = fileName;
        this.accessMode = accessMode;
        this.database = database;
        trace = database.getTrace(Trace.PAGE_STORE);
        // if (fileName.endsWith("X.h2.db"))
        // trace.setLevel(TraceSystem.DEBUG);
        String cacheType = database.getCacheType(); //Ĭ��LRU
        this.cache = CacheLRU.getCache(this, cacheType, cacheSizeDefault);
        systemSession = new Session(database, null, 0);
    }

	//org.h2.store.PageFreeList.getPagesAddressed(int)
    public static int getPagesAddressed(int pageSize) { //2048 (2K)
        return (pageSize - DATA_START) * 8; //cacheSizeDefault��16K�����Դ���ܴ�8��page
    }

org.h2.store.PageStore.open()
	=> org.h2.store.PageStore.openExisting()
		=> org.h2.engine.Database.openFile(String, String, boolean)
			=> org.h2.store.FileStore.open(DataHandler, String, String, String, byte[])
				=> org.h2.store.FileStore.FileStore(DataHandler, String, String)

			=> org.h2.store.FileStore.init()


    public void init() {
        int len = Constants.FILE_BLOCK_SIZE;
        byte[] salt;
        byte[] magic = HEADER.getBytes(); //HEADER_LENGTH = 48
        if (length() < HEADER_LENGTH) { //��һ�ν���*.h2.db�ļ�ʱlengthΪ0
            // write unencrypted
            checkedWriting = false;
			//д������"-- H2 0.5/B -- \n"��ÿ��16�ֽ�
            writeDirect(magic, 0, len);
            salt = generateSalt();
            writeDirect(salt, 0, len);
            initKey(salt);
            // write (maybe) encrypted
            write(magic, 0, len);
            checkedWriting = true;
        } else {
            // read unencrypted
            seek(0);
            byte[] buff = new byte[len];
            readFullyDirect(buff, 0, len);
            if (Utils.compareNotNull(buff, magic) != 0) {
                throw DbException.get(ErrorCode.FILE_VERSION_ERROR_1, name);
            }
            salt = new byte[len];
            readFullyDirect(salt, 0, len);
            initKey(salt);
            // read (maybe) encrypted
            readFully(buff, 0, Constants.FILE_BLOCK_SIZE);
            if (textMode) {
                buff[10] = 'B';
            }
            if (Utils.compareNotNull(buff, magic) != 0) {
                throw DbException.get(ErrorCode.FILE_ENCRYPTION_ERROR_1, name);
            }
        }
    }

	//increment 6
	private void increaseFileSize(int increment) {
		//pageCount 0
        for (int i = pageCount; i < pageCount + increment; i++) {
            freed.set(i);
			//freed(0,..., 5)
        }
        pageCount += increment;
        long newLength = (long) pageCount << pageSizeShift;
        file.setLength(newLength);
        writeCount++;
        fileLength = newLength;
    }

	public void setLength(long newLength) {
        if (SysProperties.CHECK && newLength % Constants.FILE_BLOCK_SIZE != 0) {
            DbException.throwInternalError("unaligned setLength " + name + " pos " + newLength);
        }
        checkPowerOff();
        checkWritingAllowed();
        try {
            if (newLength > fileLength) {
                long pos = filePos;
                file.position(newLength - 1);
                FileUtils.writeFully(file, ByteBuffer.wrap(new byte[1])); //������Ǹ�λ��д0
                file.position(pos);
            } else {
                file.truncate(newLength);
            }
            fileLength = newLength;
        } catch (IOException e) {
            closeFileSilently();
            throw DbException.convertIOException(e, name);
        }
    }


org.h2.store.PageStore.openMetaIndex()

TODO ��������
**************�ǳ���Ҫ****************************
        ����ʲôʱ��ͬ����Ӳ��
**************************************************
org.h2.engine.Session.commit(boolean)��org.h2.engine.Session.rollback()
	=>org.h2.engine.Database.commit(Session)
		=>org.h2.store.PageStore.commit(Session)
			=>org.h2.store.PageStore.checkpoint()
				=>org.h2.store.PageLog.removeUntil(int)
					=>org.h2.store.PageStore.setLogFirstPage(int, int, int)
						=>org.h2.store.PageStore.writeVariableHeader()
							=>org.h2.store.FileStore.sync()
								=>java.nio.channels.FileChannel.force(true)

**************************************************

    private void openForWriting() {
    	//readModeֻ��org.h2.store.PageStore.openExisting()������һ��
    	//����ͨ��openForWriting()���ﴥ��log.openForWriting���ٴ���setLogFirstPage
    	//��������writeVariableHeaderͬ�����ݵ�Ӳ���лᷢ��һ�Σ�
    	//writeVariableHeader�Ĵ����������ͨ��commit���
        if (!readMode || database.isReadOnly()) {
            return;
        }
        readMode = false;
        recoveryRunning = true;
        log.free();
        logFirstTrunkPage = allocatePage();
        log.openForWriting(logFirstTrunkPage, false);
        recoveryRunning = false;
        freed.set(0, pageCount, true);
        checkpoint();
    }

���߻������:
java.lang.Error
	at org.h2.store.PageStore.writeVariableHeader(PageStore.java:995)
	at org.h2.store.PageStore.setLogFirstPage(PageStore.java:975)
	at org.h2.store.PageLog.removeUntil(PageLog.java:708)
	at org.h2.store.PageStore.checkpoint(PageStore.java:447)
	at org.h2.engine.Database.closeOpenFilesAndUnlock(Database.java:1207)
	at org.h2.engine.Database.close(Database.java:1160)
	at org.h2.engine.Database.removeSession(Database.java:1039)
	at org.h2.engine.Session.close(Session.java:562)
	at org.h2.server.TcpServerThread.closeSession(TcpServerThread.java:175)
	at org.h2.server.TcpServerThread.process(TcpServerThread.java:270)
	at org.h2.server.TcpServerThread.run(TcpServerThread.java:149)
	at java.lang.Thread.run(Thread.java:662)
�����:
java.lang.Error
	at org.h2.store.PageStore.writeVariableHeader(PageStore.java:995)
	at org.h2.store.PageStore.setLogFirstPage(PageStore.java:975)
	at org.h2.store.PageLog.openForWriting(PageLog.java:190)
	at org.h2.store.PageStore.compact(PageStore.java:509)
	at org.h2.engine.Database.closeOpenFilesAndUnlock(Database.java:1210)
	at org.h2.engine.Database.close(Database.java:1160)
	at org.h2.engine.Database.removeSession(Database.java:1039)
	at org.h2.engine.Session.close(Session.java:562)
	at org.h2.server.TcpServerThread.closeSession(TcpServerThread.java:175)
	at org.h2.server.TcpServerThread.process(TcpServerThread.java:270)
	at org.h2.server.TcpServerThread.run(TcpServerThread.java:149)
	at java.lang.Thread.run(Thread.java:662)
