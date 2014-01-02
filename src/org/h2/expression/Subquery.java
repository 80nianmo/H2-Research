/*
 * Copyright 2004-2013 H2 Group. Multiple-Licensed under the H2 License,
 * Version 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.expression;

import java.util.ArrayList;
import org.h2.command.dml.Query;
import org.h2.constant.ErrorCode;
import org.h2.engine.Session;
import org.h2.message.DbException;
import org.h2.result.ResultInterface;
import org.h2.table.ColumnResolver;
import org.h2.table.TableFilter;
import org.h2.value.Value;
import org.h2.value.ValueArray;
import org.h2.value.ValueNull;

/**
 * A query returning a single value.
 * Subqueries are used inside other statements.
 */
public class Subquery extends Expression {

    private final Query query;
    private Expression expression; //query��select�ֶ��б�����ж��У���ô��һ��ExpressionList

    public Subquery(Query query) {
        this.query = query;
    }
    
    //�Ӳ�ѯ���ܶ���1����
	//sql = "delete from ConditionInSelectTest where id in(select id,name from ConditionInSelectTest where id=3)";
	//sql = "delete from ConditionInSelectTest where id in(select id from ConditionInSelectTest where id>2)";

	//sql = "delete from ConditionInSelectTest where id > ALL(select id from ConditionInSelectTest where id>10)";
	//ANY��SOMEһ��
	//sql = "delete from ConditionInSelectTest where id > ANY(select id from ConditionInSelectTest where id>1)";
	//sql = "delete from ConditionInSelectTest where id > SOME(select id from ConditionInSelectTest where id>10)";
	
	//�ϸ���˵����sql����Subquery�������in��ALL��ANY��SOME��ֻ����ͨ��select
	//Subquery�������������ܴ���1����in��ALL��ANY��SOMEû���ƣ�
	//��һ��Ҳ��⣬����id> (select id from ConditionInSelectTest where id>1)������Subquery����1�У���ôid�Ͳ�֪����˭�Ƚ�
	//sql = "delete from ConditionInSelectTest where id > (select id from ConditionInSelectTest where id>1)";
    //����Subquery�����ж���
	//sql = "delete from ConditionInSelectTest where id > (select id, name from ConditionInSelectTest where id=1 and name='a1')";
    public Value getValue(Session session) {
        query.setSession(session);
        //getValue��Ȼ������ѯ�ж�����¼������¶��ᱻ���ã�����query�ڲ����л���ģ�ֻ��һ��ǳ���������Զ�����Ӱ�첻��
        ResultInterface result = query.query(2);
        try {
            int rowcount = result.getRowCount();
            if (rowcount > 1) {
                throw DbException.get(ErrorCode.SCALAR_SUBQUERY_CONTAINS_MORE_THAN_ONE_ROW);
            }
            Value v;
            if (rowcount <= 0) {
                v = ValueNull.INSTANCE;
            } else {
                result.next();
                Value[] values = result.currentRow();
                if (result.getVisibleColumnCount() == 1) {
                    v = values[0];
                } else {
                	//����id > (select id, name from ConditionInSelectTest where id=1 and name='a1')
                	//��ʱ��id, name�õ�һ��ValueArray
                	//�����бȽ�ʱ����ߵ�idҲ��ת����һ��String���飬�����ValueArray�Ƚ�
                	//��������Ӳ�ѯֻ��Ҫһ����ʱ���Ͳ�Ӧ�ö�дһ���У��������ܸ��ߡ�
                    v = ValueArray.get(values);
                }
            }
            return v;
        } finally {
            //����org.h2.result.LocalResultֻ��external��Ϊnullʱ�Ű�closed��Ϊtrue
        	//����org.h2.command.dml.Query.query(int)�ж�org.h2.result.LocalResult.isClosed()ʱ��ΪclosedΪfalse
        	//�������close������ûЧ����
            result.close();
        }
    }

    public int getType() {
        return getExpression().getType();
    }

    public void mapColumns(ColumnResolver resolver, int level) {
        query.mapColumns(resolver, level + 1);
    }

    public Expression optimize(Session session) {
        query.prepare();
        return this;
    }

    public void setEvaluatable(TableFilter tableFilter, boolean b) {
        query.setEvaluatable(tableFilter, b);
    }

    public int getScale() {
        return getExpression().getScale();
    }

    public long getPrecision() {
        return getExpression().getPrecision();
    }

    public int getDisplaySize() {
        return getExpression().getDisplaySize();
    }

    public String getSQL() {
        return "(" + query.getPlanSQL() + ")";
    }

    public void updateAggregate(Session session) {
        query.updateAggregate(session);
    }

    private Expression getExpression() {
        if (expression == null) {
            ArrayList<Expression> expressions = query.getExpressions();
            int columnCount = query.getColumnCount();
            if (columnCount == 1) {
                expression = expressions.get(0);
            } else {
                Expression[] list = new Expression[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    list[i] = expressions.get(i);
                }
                expression = new ExpressionList(list);
            }
        }
        return expression;
    }

    public boolean isEverything(ExpressionVisitor visitor) {
        return query.isEverything(visitor);
    }

    public Query getQuery() {
        return query;
    }

    public int getCost() {
        return query.getCostAsExpression();
    }

    public Expression[] getExpressionColumns(Session session) {
        return getExpression().getExpressionColumns(session);
    }
}
