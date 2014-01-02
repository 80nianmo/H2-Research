/*
 * Copyright 2004-2013 H2 Group. Multiple-Licensed under the H2 License,
 * Version 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.expression;

import org.h2.command.dml.Query;
import org.h2.constant.ErrorCode;
import org.h2.engine.Database;
import org.h2.engine.Session;
import org.h2.index.IndexCondition;
import org.h2.message.DbException;
import org.h2.result.LocalResult;
import org.h2.table.ColumnResolver;
import org.h2.table.TableFilter;
import org.h2.util.StringUtils;
import org.h2.value.Value;
import org.h2.value.ValueBoolean;
import org.h2.value.ValueNull;

/**
 * An 'in' condition with a subquery, as in WHERE ID IN(SELECT ...)
 */
public class ConditionInSelect extends Condition {

    private final Database database;
    private Expression left;
    private final Query query;
    private final boolean all;
    private final int compareType;
    private int queryLevel; //û�����ô�

    public ConditionInSelect(Database database, Expression left, Query query, boolean all, int compareType) {
        this.database = database;
        this.left = left;
        this.query = query;
        this.all = all;
        this.compareType = compareType;
    }

    public Value getValue(Session session) {
        query.setSession(session);
        LocalResult rows = query.query(0);
        session.addTemporaryResult(rows);
        Value l = left.getValue(session);
        
        //�Ӳ�ѯû�м�¼ʱ�������ALL���͵��Ӳ�ѯ����ô��Ϊ����Ϊtrue��
        //����Ϊfalse��
        //��������ֶ�id��ֵ��1��6���������
        //delete from ConditionInSelectTest where id > ALL(select id from ConditionInSelectTest where id>10)
        //������Ӳ�ѯû��ֵ������where id<ALL()ʱ��Ϊtrue��ʵ������ɾ�����м�¼
        //����ĳ�ANY����ôʲô��¼����ɾ
        if (rows.getRowCount() == 0) {
            return ValueBoolean.get(all);
        } else if (l == ValueNull.INSTANCE) {
        	//���left��null����ô����null
            return l;
        }
        if (!session.getDatabase().getSettings().optimizeInSelect) {
            return getValueSlow(rows, l);
        }
        if (all || (compareType != Comparison.EQUAL && compareType != Comparison.EQUAL_NULL_SAFE)) {
            return getValueSlow(rows, l);
        }
        //��������Ǵ����all������EQUAL��EQUAL_NULL_SAFE�����
        
        //��ý�����е�һ�е�����
        int dataType = rows.getColumnType(0);
        //����е�������null����ô����false
        if (dataType == Value.NULL) {
            return ValueBoolean.get(false);
        }
        //��left��ֵת�ɽ�����е�һ�е����ͣ�Ȼ���жϽ�������Ƿ������true
        l = l.convertTo(dataType);
        if (rows.containsDistinct(new Value[] { l })) {
            return ValueBoolean.get(true);
        }
        //������в�����left����null����ô����null
        if (rows.containsDistinct(new Value[] { ValueNull.INSTANCE })) {
            return ValueNull.INSTANCE;
        }
        return ValueBoolean.get(false);
    }

    private Value getValueSlow(LocalResult rows, Value l) {
        // this only returns the correct result if the result has at least one
        // row, and if l is not null
        boolean hasNull = false;
        boolean result = all;
        while (rows.next()) {
            boolean value;
            Value r = rows.currentRow()[0];
            if (r == ValueNull.INSTANCE) {
                value = false;
                hasNull = true;
            } else {
                value = Comparison.compareNotNull(database, l, r, compareType);
            }
            if (!value && all) {
                result = false;
                break;
            } else if (value && !all) {
                result = true;
                break;
            }
        }
        if (!result && hasNull) {
            return ValueNull.INSTANCE;
        }
        return ValueBoolean.get(result);
    }

    public void mapColumns(ColumnResolver resolver, int level) {
        left.mapColumns(resolver, level);
        query.mapColumns(resolver, level + 1);
        this.queryLevel = Math.max(level, this.queryLevel); //û�����ô�
    }

    public Expression optimize(Session session) {
        left = left.optimize(session);
        query.setRandomAccessResult(true);
        query.prepare();
        //��where id in(select id,name from ConditionInSelectTest where id=3)
        //org.h2.jdbc.JdbcSQLException: Subquery is not a single column query
        //�Ӳ�ѯ���ܶ���1����
        if (query.getColumnCount() != 1) {
            throw DbException.get(ErrorCode.SUBQUERY_IS_NOT_SINGLE_COLUMN);
        }
        // Can not optimize: the data may change
        return this;
    }

    public void setEvaluatable(TableFilter tableFilter, boolean b) {
        left.setEvaluatable(tableFilter, b);
        query.setEvaluatable(tableFilter, b);
    }

    public String getSQL() {
        StringBuilder buff = new StringBuilder();
        buff.append('(').append(left.getSQL()).append(' ');
        if (all) {
            buff.append(Comparison.getCompareOperator(compareType)).
                append(" ALL");
        } else {
            buff.append("IN");
        }
        buff.append("(\n").append(StringUtils.indent(query.getPlanSQL(), 4, false)).
            append("))");
        return buff.toString();
    }

    public void updateAggregate(Session session) {
        left.updateAggregate(session);
        query.updateAggregate(session);
    }

    public boolean isEverything(ExpressionVisitor visitor) {
        return left.isEverything(visitor) && query.isEverything(visitor);
    }

    public int getCost() {
        return left.getCost() + query.getCostAsExpression();
    }

    public void createIndexConditions(Session session, TableFilter filter) {
        if (!session.getDatabase().getSettings().optimizeInList) {
            return;
        }
        if (!(left instanceof ExpressionColumn)) {
            return;
        }
        ExpressionColumn l = (ExpressionColumn) left;
        if (filter != l.getTableFilter()) {
            return;
        }
        //query�е�columnResolver��filter����ͬһ��ʵ��ʱ�Ͱ�query������������
        //Ҳ����˵���query�е�columnResolver��filter��ͬһ��ʵ����
        //���൱��filter�Լ����Լ��ˣ��������ѭ��
        ExpressionVisitor visitor = ExpressionVisitor.getNotFromResolverVisitor(filter);
        if (!query.isEverything(visitor)) {
            return;
        }
        filter.addIndexCondition(IndexCondition.getInQuery(l, query));
    }

}
