/*
 * Copyright 2004-2013 H2 Group. Multiple-Licensed under the H2 License,
 * Version 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.index;


/**
 * A page store index.
 */
public abstract class PageIndex extends BaseIndex {

    /**
     * The root page of this index.
     */
	//ÿ�����Ӽ�¼ʱ���ᰴrootPageId�ҳ���һ��ҳ�棬Ȼ��Ӵ�ҳ��Ƚ�row key�����ʵ�λ�ò�����У�
	//����ʵ�λ���кܶ��������ͨ���ʼʱrootPageIdָ�����һ��PageBtreeLeaf�����Լ�¼�����PageBtreeLeaf�У�
	//���ǵ�Խ��Խ�࣬һ��PageBtreeLeaf�Ų���ʱ����Ѵ�PageBtreeLeaf�иһ��Ϊ�������½�һ��PageBtreeNode��Ϊ�������ĸ���㣬
	//��ʱrootPageId�ʹ���˸�����ҳ��id��ͬʱ��PageBtreeNode�����һ���ָ�key������ʶ����������ֽڵ���Щ��¼���ĸ��ӽڵ㣬
	//���´��������¼�¼ʱ���ᰴ�˼�¼��row key��ָ�key�Ƚϣ�ȷ�����¼�¼�ŵ��ĸ��ӽڵ��У�����ӽڵ�����һ��PageBtreeNode��
	//��ôִ��ͬ���Ĳ�����ֱ���ҵ�һ��PageBtreeLeafΪֹ
    protected int rootPageId;
    
    //ֻ��org.h2.index.PageDataLeaf���õ���
    //��insert into IndexTestTable(id, name, address) SORTED values(...)
    private boolean sortedInsertMode; 

    /**
     * Get the root page of this index.
     *
     * @return the root page id
     */
    public int getRootPageId() {
        return rootPageId;
    }

    /**
     * Write back the row count if it has changed.
     */
    public abstract void writeRowCount(); //ֻ��PageDataNode��PageBtreeNode�Ŵ�rowCount

    public void setSortedInsertMode(boolean sortedInsertMode) {
        this.sortedInsertMode = sortedInsertMode;
    }

    boolean isSortedInsertMode() {
        return sortedInsertMode;
    }

}
