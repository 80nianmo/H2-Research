    /**
     * Initialize the lob storage.
     */
    public void init() {
        if (init) {
            return;
        }
        synchronized (handler) {
			//org.h2.engine.Database��org.h2.engine.SessionRemote�ȶ�ʵ����org.h2.store.DataHandler�ӿ�
			//ֻ��org.h2.engine.Database������null, ��Ӧserver��
			//org.h2.engine.SessionRemote����null����Ӧclient��
            conn = handler.getLobConnection();
            init = true;
            if (conn == null) {
                return;
            }
            try {
                Statement stat = conn.createStatement();
                // stat.execute("SET UNDO_LOG 0");
                // stat.execute("SET REDO_LOG_BINARY 0");
                boolean create = true;
                PreparedStatement prep = conn.prepareStatement(
                        "SELECT ZERO() FROM INFORMATION_SCHEMA.COLUMNS WHERE " +
                        "TABLE_SCHEMA=? AND TABLE_NAME=? AND COLUMN_NAME=?");
                prep.setString(1, "INFORMATION_SCHEMA");
                prep.setString(2, "LOB_MAP");
                prep.setString(3, "POS");
                ResultSet rs;
                rs = prep.executeQuery();
                if (rs.next()) {
                    prep = conn.prepareStatement(
                            "SELECT ZERO() FROM INFORMATION_SCHEMA.TABLES WHERE " +
                            "TABLE_SCHEMA=? AND TABLE_NAME=?");
                    prep.setString(1, "INFORMATION_SCHEMA");
                    prep.setString(2, "LOB_DATA");
                    rs = prep.executeQuery();
                    if (rs.next()) {
                        create = false;
                    }
                }
                if (create) {
                    stat.execute("CREATE CACHED TABLE IF NOT EXISTS " + LOBS +
                            "(ID BIGINT PRIMARY KEY, BYTE_COUNT BIGINT, TABLE INT) HIDDEN");
                    stat.execute("CREATE INDEX IF NOT EXISTS " +
                            "INFORMATION_SCHEMA.INDEX_LOB_TABLE ON " + LOBS + "(TABLE)");
                    stat.execute("CREATE CACHED TABLE IF NOT EXISTS " + LOB_MAP +
                            "(LOB BIGINT, SEQ INT, POS BIGINT, HASH INT, BLOCK BIGINT, PRIMARY KEY(LOB, SEQ)) HIDDEN");
                    // TODO the column name OFFSET was used in version 1.3.156,
                    // so this can be remove in a later version
                    stat.execute("ALTER TABLE " + LOB_MAP + " RENAME TO " + LOB_MAP + " HIDDEN");
                    stat.execute("ALTER TABLE " + LOB_MAP + " ADD IF NOT EXISTS POS BIGINT BEFORE HASH");
                    stat.execute("ALTER TABLE " + LOB_MAP + " DROP COLUMN IF EXISTS \"OFFSET\"");
                    stat.execute("CREATE INDEX IF NOT EXISTS " +
                            "INFORMATION_SCHEMA.INDEX_LOB_MAP_DATA_LOB ON " + LOB_MAP + "(BLOCK, LOB)");
                    stat.execute("CREATE CACHED TABLE IF NOT EXISTS " + LOB_DATA +
                            "(BLOCK BIGINT PRIMARY KEY, COMPRESSED INT, DATA BINARY) HIDDEN");
                }
                rs = stat.executeQuery("SELECT MAX(BLOCK) FROM " + LOB_DATA);
                rs.next();
                nextBlock = rs.getLong(1) + 1;
                stat.close();
            } catch (SQLException e) {
                throw DbException.convert(e);
            }
        }
    }
