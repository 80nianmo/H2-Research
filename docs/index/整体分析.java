
1.����

���е�������ʵ����org.h2.index.Index�ӿ�

Index�ӿ���add��remove��find����������û��update����

ʵ��Index�ӿڵ�����ֱ��������:
org.h2.index.MultiVersionIndex
org.h2.index.BaseIndex

BaseIndex��һ�������࣬���Ҫʵ���µ��������ͣ����ԴӴ�����չ��

H2Ŀǰʵ����13����������(�뿴"Index��ͼ"�ļ�)


2. ������������˵��

org.h2.index.MetaIndex

MetaIndex��ֻ���ģ���֧��add��remove������
���ڲ������ݿ�Ԫ���ݣ�����ͨ��java.sql.DatabaseMetaData�ṩ�ķ�������


org.h2.index.FunctionIndex

FunctionIndex��ֻ���ģ���֧��add��remove������
����"SELECT * FROM VALUES(1, 'Hello'), (2, 'World')" ������VALUES


3. ��ѯ��¼ʱ�����ͨ�������е�key��PageDataIndex�е����ݹ�������

��org.h2.command.dml.Select.queryFlat(int, ResultTarget, long)�е���
				for (int i = 0; i < columnCount; i++) {
                    Expression expr = expressions.get(i);
                    //����:
                    //org.h2.expression.ExpressionColumn.getValue(Session)
                    //org.h2.table.TableFilter.getValue(Column)
                    row[i] = expr.getValue(session);
                }
��ȡ�õ�ǰ�е������ֶΣ�expr.getValue(session)�ᴥ��org.h2.table.TableFilter.getValue(Column)
    public Value getValue(Column column) {
        if (currentSearchRow == null) {
            return null;
        }
        int columnId = column.getColumnId();
        if (columnId == -1) {
            return ValueLong.get(currentSearchRow.getKey());
        }
        if (current == null) {
            Value v = currentSearchRow.getValue(columnId);
            if (v != null) {
                return v;
            }
            current = cursor.get();
            if (current == null) {
                return ValueNull.INSTANCE;
            }
        }
        return current.getValue(columnId);
    }
currentSearchRow�������ֶ���ɵ��У����Ҫȡ���а�����currentSearchRow�У���ôֱ�ӷ���currentSearchRow�е��У�
�������org.h2.index.IndexCursor.get() => org.h2.index.PageBtreeCursor.get()
	public Row get() {
        if (currentRow == null && currentSearchRow != null) {
			//currentSearchRow.getKey()�õ��ľ���PageDataIndex�е�key
            currentRow = index.getRow(session, currentSearchRow.getKey());
        }
        return currentRow;
    }

=>org.h2.index.PageBtreeIndex.getRow(Session, long)
    public Row getRow(Session session, long key) {
        return tableData.getRow(session, key);
    }

=>org.h2.table.RegularTable.getRow(Session, long)
    public Row getRow(Session session, long key) {
        return scanIndex.getRow(session, key);
    }

=>org.h2.index.PageDataIndex.getRow(Session, long)
    public Row getRow(Session session, long key) {
        return getRowWithKey(key);
    }