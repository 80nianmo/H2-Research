�������ע���: PageBtreeNode���ӽ��Ҫôȫ��PageBtreeNode��Ҫ��ȫ��PageBtreeLeaf�����������ߡ�

��ͼ:
��org.h2.index.PageBtreeLeaf����

��ͼ:

org.h2.util.CacheObject (�����е�pos�ֶξ���PageBtreeLeaf��pageId)
	org.h2.store.Page
		org.h2.index.PageBtree
			org.h2.index.PageBtreeNode


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


org.h2.index.PageBtree���ֶ���:
	 protected final PageBtreeIndex index
	 protected int parentPageId
	 protected final Data data
	 protected int[] offsets
	 protected int entryCount
	 protected SearchRow[] rows
	 protected int start
	 protected boolean onlyPosition
	 protected boolean written
	 protected int memoryEstimated


org.h2.index.PageBtreeNode���ֶ���:
    private final boolean pageStoreInternalCount; //Ĭ����false��ͨ������PAGE_STORE_INTERNAL_COUNT����
    /**
     * The page ids of the children.
     */
    private int[] childPageIds;

    private int rowCountStored = UNKNOWN_ROWCOUNT; // -1;

    private int rowCount = UNKNOWN_ROWCOUNT; // -1;


�����Ǹ�ʽ����
------------------------------------------------------------------

�ֽ���   ����ʲô

ͷ
=======================
1        type ����ֵ(Page.TYPE_BTREE_NODE | Page.FLAG_LAST(���һҳ)) �� Page.TYPE_BTREE_NODE
2        checksum Ԥ��д0����д��page�����(��org.h2.store.PageStore.writePage(int, Data))
4        parentPageId
VarInt   index����id(ע�⣬���Ǳ����id��PageDataNode���Ǳ����id)
4        rowCountStored �Ѵ洢������
2        entryCount �ָ���ĸ���(rows�������Ч����)��Ҳ�����ӽڵ����-1

��:
=======================
4        rightmost child page id (���ұߵ���ҳid)

entryCount��
{
	4         child page id
	2         offset
}

entryCount��
{	(��offset��ʼд)

	columnCount��
	{
		VarLong key
		Value   ������ֵ (���onlyPositionΪtrue����ô��������һ���֣�ֻ��ǰ���VarLong key)
	}
	
}

(****һ��PageBtreeNode�����4��PageBtreeLeaf�� ����4��ʱ�з�****)
������仰�����ˣ����PageBtreeLeaf����С��4ʱ���ٷ���-1��ʾ����Ҫ��PageBtreeNode�з֣�
PageBtreeNodeʲôʱ���з���Ҫ��pageSize�������ֶεĳ��ȣ�
����PageBtreeNode������4��PageBtreeLeaf



page size = 128

rowLength = 12

[( /* 8 */ '1000000008' ), ( /* 7 */ '1000000007' ), ( /* 6 */ '1000000006' ), ( /* 5 */ '1000000005' ), ( /* 4 */ '1000000004' ), ( /* 3 */ '1000000003' ), ( /* 2 */ '1000000002' ), ( /* 1 */ '1000000001' ), null, null]
[116, 104, 92, 80, 68, 56, 44, 32, 0, 0]

splitPoint= 2
���±�Ϊ2��λ�ÿ�ʼsplit(�з�)����splitPoint(����)��������Ԫ�ط���page2
�ӿ�ʼ��splitPoint��Ԫ����page1��
splitPointǰ���Ԫ�ص����������Ϊ�����ķָ�key��
Ҳ����˵�м��Ľ���еķָ�key��>=�ָ�key������ߵ��ӽ�㣬<�ָ�key�����ұߵ��ӽ��
pivot = ( /* 7 */ '1000000007' )


page1 = 2
[( /* 8 */ '1000000008' ), ( /* 7 */ '1000000007' ), ( /* 1 */ '1000000001' ), ( /* 1 */ '1000000001' ), ( /* 1 */ '1000000001' )]

page2 = 6
[( /* 6 */ '1000000006' ), ( /* 5 */ '1000000005' ), ( /* 4 */ '1000000004' ), ( /* 3 */ '1000000003' ), ( /* 2 */ '1000000002' ), ( /* 1 */ '1000000001' ), null, null, null, null]


[116, 104, 92, 80, 68]
[116, 104, 92, 80, 0]


PageBtreeNode�ָ�ǰ
---------------------------
childPageIds = [74, 86, 84, 81, 79, 76, 73, 0, 0, 0, 0]
rows(�ָ�key) = [( /* 37 */ '1000000037' ), ( /* 31 */ '1000000031' ), ( /* 25 */ '1000000025' ), ( /* 19 */ '1000000019' ), ( /* 13 */ '1000000013' ), ( /* 7 */ '1000000007' ), null, null, null, null]
entryCount = 6
splitPoint =3
pivot = ( /* 25 */ '1000000025' )
---------------------------


PageBtreeNode p2
---------------------------
childPageIds = [81, 79, 76, 73, 0, 0]
rows(�ָ�key) = [( /* 19 */ '1000000019' ), ( /* 13 */ '1000000013' ), ( /* 7 */ '1000000007' ), null, null]
entryCount = 3
---------------------------
pageId=89

PageBtreeNode p1
---------------------------
childPageIds = [74, 86, 84, 73, 73, 73]
rows(�ָ�key) = [( /* 37 */ '1000000037' ), ( /* 31 */ '1000000031' ), ( /* 25 */ '1000000025' ), ( /* 7 */ '1000000007' ), ( /* 7 */ '1000000007' )]
entryCount = 2
---------------------------
pageId=90 (�·���һ��pageId)

�õ�һ���µ�PageBtreeNode��
��PageBtreeNode��childPageIds��[90, 89] rows(�ָ�key) = [( /* 25 */ '1000000025' )]


0 1 2 3 4 5 6 7 ... 116 117 118 119 120 121 122 123 124 125 126 127

( /* key:1 */ 1, 'abcdef1234')

116 117 118 119 120 121 122 123 124 125 126 127
1   10  a   b   c   d   e   f   1   2   3   4


104 ... 115
( /* key:2 */ 2, 'abcdef1234')

92 ... 103
( /* key:3 */ 3, 'abcdef1234')

80 ... 91
( /* key:4 */ 4, 'abcdef1234')

68 ... 79
( /* key:5 */ 5, 'abcdef1234')

56 ... 67
( /* key:6 */ 6, 'abcdef1234')

44 ... 55
( /* key:7 */ 7, 'abcdef1234')


keys = [1, 2, 3, 4, 5, 6, 7, 0, 0, 0]
rows = [( /* key:1 */ 1, 'abcdef1234'), ( /* key:2 */ 2, 'abcdef1234'), ( /* key:3 */ 3, 'abcdef1234'), ( /* key:4 */ 4, 'abcdef1234'), ( /* key:5 */ 5, 'abcdef1234'), ( /* key:6 */ 6, 'abcdef1234'), ( /* key:7 */ 7, 'abcdef1234'), null, null, null]
offsets = [116, 104, 92, 80, 68, 56, 44, 0, 0, 0]


entryCount = 7
last = 44
rowLength = 12
start = 32
keyOffsetPairLen = 3

last - rowLength = 44 - 12 = 32
start + keyOffsetPairLen = 32 + 3 = 35

��ǰ��: ( /* key:8 */ 8, 'abcdef1234')

Ҳ����˵�ȿ�����ǰpage(128�ֽ�)���Ҫ�浱ǰ�еĻ��Ƿ����㹻�ռ��keyOffsetPair��
��Ϊ��ǰ�еĳ�����12��page��ֻʣ��44���ֽ���(0��43)���ٴ�12����ֻʣ32�����ã�
��start��ǰ�Ѿ���32�ˣ��ٴ浱ǰ�е�keyOffsetPair�Ļ�Ҫ���3����startλ���Ƶ�35������ʣ����õ�32���ֽ��ˡ�
���Դ�ʱҪ�Ե�ǰ��PageDataLeaf�����и

		if (entryCount > 0 && last - rowLength < start + keyOffsetPairLen) {
            int x = findInsertionPoint(row.getKey()); //x = 7
            if (entryCount > 1) {
                if (entryCount < 5) {
                    // required, otherwise the index doesn't work correctly
                    return entryCount / 2;
                }
                if (index.isSortedInsertMode()) {
                    return x < 2 ? 1 : x > entryCount - 1 ? entryCount - 1 : x;
                }
                // split near the insertion point to better fill pages
                // split in half would be:
                // return entryCount / 2;
                int third = entryCount / 3;
                return x < third ? third : x >= 2 * third ? 2 * third : x; //����4 (��rows[4]=( /* key:5 */ 5, 'abcdef1234')��ʼ�и�)
            }
            return x;
        }

�µ�PageDataLeaf��
rows = [( /* key:5 */ 5, 'abcdef1234'), ( /* key:6 */ 6, 'abcdef1234'), ( /* key:7 */ 7, 'abcdef1234'), null, null, null]



splitPoint = 3
childPageIds = [46, 45, 48, 50, 52, 54, 56, 0, 0, 0, 0]
firstChild = 50

entryCount = 2
[46, 45, 48, 56, 56, 56]

[50, 52, 54, 56, 0, 0]




PageBtreeNode {
	pageId = 8
	parentPageId = 0
	childPageIds = 84, 83, 89
	childPageIds.length = 3
	entryCount = 2
	rows = {
		( /* key:8 */ 8, '1000000008', null)
		( /* key:16 */ 16, '1000000016', null)
	}

	PageBtreeNode {
		pageId = 84
		parentPageId = 8
		childPageIds = 13, 12
		childPageIds.length = 2
		entryCount = 1
		rows = {
			( /* key:4 */ 4, '1000000004', null)
		}

		PageBtreeLeaf {
			indexId = 15
			parentPageId = 84
			pageId = 13
			start = 18
			offsets = 115, 102, 89, 76
			entryCount = 4
			rows = {
				( /* key:1 */ 1, '1000000001', null)
				( /* key:2 */ 2, '1000000002', null)
				( /* key:3 */ 3, '1000000003', null)
				( /* key:4 */ 4, '1000000004', null)
			}
		}
		PageBtreeLeaf {
			indexId = 15
			parentPageId = 84
			pageId = 12
			start = 18
			offsets = 115, 102, 89, 76
			entryCount = 4
			rows = {
				( /* key:5 */ 5, '1000000005', null)
				( /* key:6 */ 6, '1000000006', null)
				( /* key:7 */ 7, '1000000007', null)
				( /* key:8 */ 8, '1000000008', null)
			}
		}
	}
	PageBtreeNode {
		pageId = 83
		parentPageId = 8
		childPageIds = 15, 16
		childPageIds.length = 2
		entryCount = 1
		rows = {
			( /* key:12 */ 12, '1000000012', null)
		}

		PageBtreeLeaf {
			indexId = 15
			parentPageId = 83
			pageId = 15
			start = 18
			offsets = 115, 102, 89, 76
			entryCount = 4
			rows = {
				( /* key:9 */ 9, '1000000009', null)
				( /* key:10 */ 10, '1000000010', null)
				( /* key:11 */ 11, '1000000011', null)
				( /* key:12 */ 12, '1000000012', null)
			}
		}
		PageBtreeLeaf {
			indexId = 15
			parentPageId = 83
			pageId = 16
			start = 18
			offsets = 115, 102, 89, 75
			entryCount = 4
			rows = {
				( /* key:13 */ 13, '1000000013', null)
				( /* key:14 */ 14, '1000000014', null)
				( /* key:15 */ 15, '1000000015', null)
				( /* key:16 */ 16, '1000000016', null)
			}
		}
	}
	PageBtreeNode {
		pageId = 89
		parentPageId = 8
		childPageIds = 18, 81, 85, 87, 90, 92
		childPageIds.length = 6
		entryCount = 5
		rows = {
			( /* key:20 */ 20, '1000000020', null)
			( /* key:24 */ 24, '1000000024', null)
			( /* key:28 */ 28, '1000000028', null)
			( /* key:32 */ 32, '1000000032', null)
			( /* key:36 */ 36, '1000000036', null)
		}

		PageBtreeLeaf {
			indexId = 15
			parentPageId = 89
			pageId = 18
			start = 18
			offsets = 114, 100, 86, 72
			entryCount = 4
			rows = {
				( /* key:17 */ 17, '1000000017', null)
				( /* key:18 */ 18, '1000000018', null)
				( /* key:19 */ 19, '1000000019', null)
				( /* key:20 */ 20, '1000000020', null)
			}
		}
		PageBtreeLeaf {
			indexId = 15
			parentPageId = 89
			pageId = 81
			start = 18
			offsets = 114, 100, 86, 72
			entryCount = 4
			rows = {
				( /* key:21 */ 21, '1000000021', null)
				( /* key:22 */ 22, '1000000022', null)
				( /* key:23 */ 23, '1000000023', null)
				( /* key:24 */ 24, '1000000024', null)
			}
		}
		PageBtreeLeaf {
			indexId = 15
			parentPageId = 89
			pageId = 85
			start = 18
			offsets = 114, 100, 86, 72
			entryCount = 4
			rows = {
				( /* key:25 */ 25, '1000000025', null)
				( /* key:26 */ 26, '1000000026', null)
				( /* key:27 */ 27, '1000000027', null)
				( /* key:28 */ 28, '1000000028', null)
			}
		}
		PageBtreeLeaf {
			indexId = 15
			parentPageId = 89
			pageId = 87
			start = 18
			offsets = 114, 100, 86, 72
			entryCount = 4
			rows = {
				( /* key:29 */ 29, '1000000029', null)
				( /* key:30 */ 30, '1000000030', null)
				( /* key:31 */ 31, '1000000031', null)
				( /* key:32 */ 32, '1000000032', null)
			}
		}
		PageBtreeLeaf {
			indexId = 15
			parentPageId = 89
			pageId = 90
			start = 18
			offsets = 114, 100, 86, 72
			entryCount = 4
			rows = {
				( /* key:33 */ 33, '1000000033', null)
				( /* key:34 */ 34, '1000000034', null)
				( /* key:35 */ 35, '1000000035', null)
				( /* key:36 */ 36, '1000000036', null)
			}
		}
		PageBtreeLeaf {
			indexId = 15
			parentPageId = 89
			pageId = 92
			start = 18
			offsets = 114, 100, 86, 72
			entryCount = 4
			rows = {
				( /* key:37 */ 37, '1000000037', null)
				( /* key:38 */ 38, '1000000038', null)
				( /* key:39 */ 39, '1000000039', null)
				( /* key:40 */ 40, '1000000040', null)
			}
		}
	}
}
