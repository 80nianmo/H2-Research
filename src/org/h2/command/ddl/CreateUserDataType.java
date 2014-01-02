/*
 * Copyright 2004-2013 H2 Group. Multiple-Licensed under the H2 License,
 * Version 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.command.ddl;

import org.h2.command.CommandInterface;
import org.h2.constant.ErrorCode;
import org.h2.engine.Database;
import org.h2.engine.Session;
import org.h2.engine.UserDataType;
import org.h2.message.DbException;
import org.h2.table.Column;
import org.h2.table.Table;
import org.h2.value.DataType;

/**
 * This class represents the statement
 * CREATE DOMAIN
 */
//CREATE DOMAIN��CREATE TYPE��CREATE DATATYPE����һ����
public class CreateUserDataType extends DefineCommand {

    private String typeName;
    private Column column;
    private boolean ifNotExists;

    public CreateUserDataType(Session session) {
        super(session);
    }

    public void setTypeName(String name) {
        this.typeName = name;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public void setIfNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }

    public int update() {
        session.getUser().checkAdmin();
        session.commit(true);
        Database db = session.getDatabase();
        session.getUser().checkAdmin();
        if (db.findUserDataType(typeName) != null) {
            if (ifNotExists) {
                return 0;
            }
            throw DbException.get(ErrorCode.USER_DATA_TYPE_ALREADY_EXISTS_1, typeName);
        }
        DataType builtIn = DataType.getTypeByName(typeName);
        if (builtIn != null) {
            if (!builtIn.hidden) {
            	//�ӵڶ������ƿ�ʼ�Ķ����������͵ģ��������int
                //new String[]{"INTEGER", "INT", "MEDIUMINT", "INT4", "SIGNED"}
                //�����������û������ݿ���û�н���ʱ���Ը���
                //��CREATE DATATYPE IF NOT EXISTS int AS VARCHAR(255)
                //���Ƿ��������;Ͳ��ܸ���
                //��CREATE DATATYPE IF NOT EXISTS integer AS VARCHAR(255)
                throw DbException.get(ErrorCode.USER_DATA_TYPE_ALREADY_EXISTS_1, typeName);
            }
            
            //����û������ݿ���û�н�����ô�Զ�����ֶ����Ϳ����������ֶ����͵�����һ��
            //��CREATE DATATYPE IF NOT EXISTS int AS VARCHAR(255)
            Table table = session.getDatabase().getFirstUserTable();
            if (table != null) {
                throw DbException.get(ErrorCode.USER_DATA_TYPE_ALREADY_EXISTS_1, typeName + " (" + table.getSQL() + ")");
            }
        }
        int id = getObjectId();
        UserDataType type = new UserDataType(db, id, typeName);
        type.setColumn(column);
        db.addDatabaseObject(session, type);
        return 0;
    }

    public int getType() {
        return CommandInterface.CREATE_DOMAIN;
    }

}
