�ֽ���   ����ʲô

ͷ
=======================
1        type ����ֵ(Page.TYPE_DATA_LEAF | Page.FLAG_LAST(���һҳ)) �� Page.TYPE_DATA_LEAF
2        checksum Ԥ��д0����д��page�����(��org.h2.store.PageStore.writePage(int, Data))
4        parentPageId
VarInt   index����id(ʵ���Ǳ����id)
VarInt   columnCount
2        entryCount (����)

��:
=======================
4        with overflow: the first overflow page id(���firstOverflowPageId !=0)

entryCount��
{
	VarLong   key
	2         offset
}

entryCount��
{	(��offset��ʼд)

	columnCount��
	{
		column value
	}
	
}



page size = 128

rowLength = 12

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

