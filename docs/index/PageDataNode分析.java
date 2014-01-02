    private void writeHead() {
        data.reset();
        data.writeByte((byte) Page.TYPE_DATA_NODE);
        data.writeShortInt(0);
        if (SysProperties.CHECK2) {
            if (data.length() != START_PARENT) {
                DbException.throwInternalError();
            }
        }
        data.writeInt(parentPageId);
        data.writeVarInt(index.getId()); //��������id
        data.writeInt(rowCountStored); //����
        data.writeShortInt(entryCount); //leaf��ҳ��
    }

[8, 7, 9, 10, 11, 12, 13, 14, 0, 0, 0, 0]

�������ע���: PageDataNode���ӽ��Ҫôȫ��PageDataNode��Ҫ��ȫ��PageDataLeaf�����������ߡ�

��ͼ:
��org.h2.index.PageDataLeaf����

��ͼ:

org.h2.util.CacheObject (�����е�pos�ֶξ���PageDataNode��pageId)
	org.h2.store.Page
		org.h2.index.PageData
			org.h2.index.PageDataNode


CacheObject��ֱ������ֻ��org.h2.util.CacheHead��org.h2.store.Page



org.h2.store.Page�ඨ����8��ҳ�����ͣ��ֱ��Ӧ8������:

ҳ������id��ҳ������            ��Ӧ����
------------------------------------------------------------------
0           TYPE_EMPTY          ��û�࣬��Ҫ�����쳣���������

1           TYPE_DATA_LEAF      org.h2.index.PageDataLeaf
2           TYPE_DATA_NODE      org.h2.index.PageDataNode
3           TYPE_DATA_OVERFLOW  org.h2.index.PageDataOverflow

4           TYPE_BTREE_LEAF     org.h2.index.PageBtreeLeaf
5           TYPE_BTREE_NODE     org.h2.index.PageBtreeNode

6           TYPE_FREE_LIST      org.h2.store.PageFreeList
7           TYPE_STREAM_TRUNK   org.h2.store.PageStreamTrunk
8           TYPE_STREAM_DATA    org.h2.store.PageStreamData
------------------------------------------------------------------

PageDataLeaf��PageBtreeLeaf��PageBtreeNode�����ߵĿ�����ҵ�������ֶ��м���FLAG_LAST
FLAG_LAST��ʾ��ҳ�����һҳ��



org.h2.util.CacheObject���ֶ���:
	public CacheObject cachePrevious
	public CacheObject cacheNext
	public CacheObject cacheChained
	private int pos
	private boolean changed

org.h2.store.Page���ֶ���:
	protected int changeCount;
	
	�������󷽷�:

	moveTo(Session, int) //�ƶ���ǰpage���µ�λ��
	write() //д��ǰҳ������ݵ�Ӳ��

	�ṩ������static������add��insert��remove�����ڴ������и�ԭ��Ԫ�ؼ���һ��(�����Ǹ�ֵ)������һ����Ԫ�ء�ɾ��һ��Ԫ��
	���෽����������rows�����offsets��keys�����У�rows���ڷż�¼��offsets���ڷż�¼��page�е����λ�ã�keys���ڷż�¼��key��


org.h2.index.PageData���ֶ���:
	 protected final PageDataIndex index
	 protected int parentPageId
	 protected final Data data
	 protected int entryCount
	 protected long[] keys;
	 protected boolean written
	 protected int memoryEstimated


org.h2.index.PageDataNode���ֶ���:
    private int[] childPageIds;
    private int rowCountStored = UNKNOWN_ROWCOUNT; // -1;
    private int rowCount = UNKNOWN_ROWCOUNT; // -1;
	private int length;


�����Ǹ�ʽ����
------------------------------------------------------------------

�ֽ���   ����ʲô

ͷ
=======================
1        type Page.TYPE_DATA_NODE
2        checksum Ԥ��д0����д��page�����(��org.h2.store.PageStore.writePage(int, Data))
4        parentPageId
VarInt   index����id(ʵ���Ǳ����id)
4        rowCountStored
2        entryCount �ָ���ĸ���(keys�������Ч����)��Ҳ�����ӽڵ����-1

��:
=======================
4        rightmost child page id (���ұߵ���ҳid)

entryCount��
{
	4         child page id
	VarLong   key
}
