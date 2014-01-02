/*
 * Copyright 2004-2013 H2 Group. Multiple-Licensed under the H2 License,
 * Version 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.engine;

import org.h2.message.DbException;
import org.h2.message.Trace;
import org.h2.table.Table;

/**
 * Represents a role. Roles can be granted to users, and to other roles.
 */
public class Role extends RightOwner {

    private final boolean system;

    //org.h2.engine.Database.open(int, int)��Ԥ������һ��publicRole = new Role(this, 0, Constants.PUBLIC_ROLE_NAME, true);
    //Constants.PUBLIC_ROLE_NAME="PUBLIC"
    public Role(Database database, int id, String roleName, boolean system) {
        super(database, id, roleName, Trace.USER);
        this.system = system;
    }

    public String getCreateSQLForCopy(Table table, String quotedName) {
        throw DbException.throwInternalError();
    }

    public String getDropSQL() {
        return null;
    }

    /**
     * Get the CREATE SQL statement for this object.
     *
     * @param ifNotExists true if IF NOT EXISTS should be used
     * @return the SQL statement
     */
    public String getCreateSQL(boolean ifNotExists) {
        if (system) { //system��ɫ����Ҫ����create��䣬��org.h2.engine.Database.open(int, int)����Զ�����
            return null;
        }
        StringBuilder buff = new StringBuilder("CREATE ROLE ");
        if (ifNotExists) {
            buff.append("IF NOT EXISTS ");
        }
        buff.append(getSQL());
        return buff.toString();
    }

    public String getCreateSQL() {
        return getCreateSQL(false);
    }

    public int getType() {
        return DbObject.ROLE;
    }
    
    //dorp roleʱ�����
    //ɾ��Ȩ��(�����ڵ���revoke����)
    public void removeChildrenAndResources(Session session) {
    	//��һ����role��ص�Ȩ��������
    	//ǰ�����Ǵ�role�������������RightOwner��(�����û���������ɫ)
    	//��һ�����������role�Լ���
    	
    	//��role���������Щuser�ˣ�������ЩuserҪ�Ѵ�Ȩ��ɾ��
        for (User user : database.getAllUsers()) {
            Right right = user.getRightForRole(this);
            if (right != null) {
            	//�˷����ڲ������Right��removeChildrenAndResources��Ȼ��ᴥ��User��revokeRole
                database.removeDatabaseObject(session, right);
            }
        }
        
        //��role���������ЩRole�ˣ�������ЩRoleҪ�Ѵ�Ȩ��ɾ��
        for (Role r2 : database.getAllRoles()) {
            Right right = r2.getRightForRole(this);
            if (right != null) {
            	//�˷����ڲ������Right��removeChildrenAndResources��Ȼ��ᴥ��Role��revokeRole
                database.removeDatabaseObject(session, right);
            }
        }
        
        //�������role�Լ���Ȩ��Ҫɾ��
        for (Right right : database.getAllRights()) {
            if (right.getGrantee() == this) {
            	//�˷����ڲ������Right��removeChildrenAndResources��Ȼ��ᴥ��Role��revokeRole
                database.removeDatabaseObject(session, right);
            }
        }
        database.removeMeta(session, getId());
        invalidate();
    }

    public void checkRename() {
        // ok
    }

}
