PageLog��������������־��:

��9����־����
1. UNDO
2. COMMIT
3. PREPARE_COMMIT (����XA�����׶��ύ)
4. ROLLBACK
5. ADD
6. REMOVE
7. TRUNCATE
8. CHECKPOINT
9. FREE_LOG

����һ��NOOP(ֵΪ0)��ʾʲô������

������ÿ����־���͵ľ����ʽ:

1. UNDO

�ֽ���   ����ʲô
=======================
1        type �̶���UNDO (ֵ��1)
VarInt   pageId

��������4�����
���page������org.h2.store.Page.TYPE_EMPTY
---------------------
VarInt   �̶���1

���page�����ǳ�Page.TYPE_EMPTY���������
���ʹ��ѹ��:
�ȶ�page����ѹ�������ѹ����Ĵ�С<pageSize
---------------------
VarInt   size ѹ����Ĵ�С
size     ѹ������ֽ�

���ѹ����Ĵ�С>pageSize
---------------------
VarInt   �̶���0
pageSize pageδѹ�����ֽ�


δʹ��ѹ��������
---------------------
VarInt   �̶���0
pageSize pageδѹ�����ֽ�





2. COMMIT

�ֽ���   ����ʲô
=======================
1        type �̶���COMMIT (ֵ��2)
VarInt   sessionId




3. PREPARE_COMMIT

�ֽ���   ����ʲô
=======================
1        type �̶���PREPARE_COMMIT (ֵ��3)
VarInt   sessionId
String   transaction name

PREPARE_COMMITҪ��ռһ��PageStreamData




4. ROLLBACK

�ֽ���   ����ʲô
=======================
1        type �̶���ROLLBACK (ֵ��4)
VarInt   sessionId




5. ADD

�ֽ���   ����ʲô
=======================
1        type �̶���ADD (ֵ��5)
VarInt   sessionId
VarInt   tableId
VarLong  row key
VarInt   row length
length   row data




6. REMOVE

�ֽ���   ����ʲô
=======================
1        type �̶���REMOVE (ֵ��6)
VarInt   sessionId
VarInt   tableId
VarLong  row key

��ADD���˺�������






7. TRUNCATE

�ֽ���   ����ʲô
=======================
1        type �̶���TRUNCATE (ֵ��7)
VarInt   sessionId
VarInt   tableId








8. CHECKPOINT

�ֽ���   ����ʲô
=======================
1        type �̶���CHECKPOINT (ֵ��8)

���л��µ�PageStreamData





9. FREE_LOG

�ֽ���   ����ʲô
=======================
1        type �̶���FREE_LOG (ֵ��9)
VarInt   page����

page��{
    VarInt pageId
}








