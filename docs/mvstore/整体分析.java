�漰����:

org.h2.engine.Right

org.h2.engine.RightOwner
	<= org.h2.engine.Role
	<= org.h2.engine.User

org.h2.command.ddl.CreateRole
org.h2.command.ddl.CreateUser
org.h2.command.ddl.GrantRevoke

org.h2.command.ddl.DropRole
org.h2.command.ddl.DropUser

����Ȩ�����跽ʽ:
1. ��Ȩ������RightOwner
2. ����ɫ����RightOwner

RightOwner������ϵͳԤ�����public��ɫ��Ҳ�������Զ���Ľ�ɫ�����������û�

ֻ֧��SELECT��DELETE��INSERT��UPDATE�ܹ�4��Ȩ��(ALL��ʾ����Ȩ�޶�֧��)



ͨ��org.h2.engine.User.checkRight(Table, int)����ΪȨ�޼������



DDL��䶼��ҪadminȨ��




�����������������:
GRANT myrole1 TO myrole3
GRANT myrole3 TO PUBLIC
GRANT myrole1 TO PUBLIC



��GRANT myrole1 TO myrole3ʱ,grantedRole = myrole1, grantee = myrole3
����myrole3.grantedRoles�б���myrole1

��GRANT myrole3 TO PUBLICʱ,grantedRole = myrole3, grantee = PUBLIC
����PUBLIC.grantedRoles�б���myrole3

��GRANT myrole1 TO PUBLICʱ,grantedRole = myrole1, grantee = PUBLIC
��ΪPUBLIC.grantedRoles������myrole3����myrole3.grantedRoles������myrole1��
���Դ�ʱ��if (grantedRole != grantee && grantee.isRoleGranted(grantedRole))��Ϊtrue��ֱ�ӷ���
    
org.h2.command.ddl.GrantRevoke.grantRole(Role)
==================================================
	private void grantRole(Role grantedRole) {
        if (grantedRole != grantee && grantee.isRoleGranted(grantedRole)) {
            return;
        }
        if (grantee instanceof Role) {
            Role granteeRole = (Role) grantee;
            if (grantedRole.isRoleGranted(granteeRole)) {
                // cyclic role grants are not allowed
                throw DbException.get(ErrorCode.ROLE_ALREADY_GRANTED_1, grantedRole.getSQL());
            }
        }
        Database db = session.getDatabase();
        int id = getObjectId();
        Right right = new Right(db, id, grantee, grantedRole);
        db.addDatabaseObject(session, right);
        grantee.grantRole(grantedRole, right);
    }

org.h2.engine.RightOwner.isRoleGranted(Role)
==================================================
    public boolean isRoleGranted(Role grantedRole) {
        if (grantedRole == this) {
            return true;
        }
        if (grantedRoles != null) {
            for (Role role : grantedRoles.keySet()) {
                if (role == grantedRole) {
                    return true;
                }

				//�����Ӧ���������GRANT��䣬�ݹ�鿴grantedRoles�е�ÿ��Roles�Ƿ�������grantedRole
                if (role.isRoleGranted(grantedRole)) {
                    return true;
                }
            }
        }
        return false;
    }
