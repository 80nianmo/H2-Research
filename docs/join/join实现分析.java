    private void parseSelectSimpleFromPart(Select command) {
        do {
            TableFilter filter = readTableFilter(false);
            parseJoinTableFilter(filter, command);
        } while (readIf(","));
    }

    private void parseJoinTableFilter(TableFilter top, final Select command) {
        top = readJoin(top, command, false, top.isJoinOuter());
        command.addTableFilter(top, true);
        boolean isOuter = false;
        while (true) {
            TableFilter n = top.getNestedJoin();
            if (n != null) {
                n.visit(new TableFilterVisitor() {
                    public void accept(TableFilter f) {
                        command.addTableFilter(f, false);
                    }
                });
            }
            TableFilter join = top.getJoin();
            if (join == null) {
                break;
            }
            isOuter = isOuter | join.isJoinOuter();
            if (isOuter) {
                command.addTableFilter(join, false);
            } else {
                // make flat so the optimizer can work better
                Expression on = join.getJoinCondition();
                if (on != null) {
                    command.addCondition(on);
                }
                join.removeJoinCondition();
                top.removeJoin();
                command.addTableFilter(join, true);
            }
            top = join;
        }
    }
    
    private TableFilter readTableFilter(boolean fromOuter) { //�ʼ�Ǵ�parseSelectSimpleFromPart��������
        Table table;
        String alias = null;
        if (readIf("(")) { //��from��ֱ�Ӹ������ţ���"from (select 1)"
        	//isSelect�л���,�������������ţ�Ȼ����һ��token�Ƿ���select��from
        	//Ȼ���ٴӵ���isSelectǰ��lastParseIndex��ʼ��������ǰtoken�ǵ���isSelectǰ��token.
            if (isSelect()) { //��"FROM ((select 1) union (select 1))";
                Query query = parseSelectUnion();
                read(")");
                query.setParameterList(New.arrayList(parameters));
                query.init();
                Session s;
                if (createView != null) {
                    s = database.getSystemSession();
                } else {
                    s = session;
                }
                alias = session.getNextSystemIdentifier(sqlCommand);
                table = TableView.createTempView(s, session.getUser(), alias, query, currentSelect);
            } else {
            	//��"FROM (mytable) SELECT * "
            	//"FROM (mytable1 RIGHT OUTER JOIN mytable2 ON mytable1.id1=mytable2.id2) AS t SELECT * "
                TableFilter top;
                if (database.getSettings().nestedJoins) {
                    top = readTableFilter(false);
                    top = readJoin(top, currentSelect, false, false);
                    top = getNested(top);
                } else {
                    top = readTableFilter(fromOuter);
                    top = readJoin(top, currentSelect, false, fromOuter);
                }
                read(")");
                alias = readFromAlias(null);
                if (alias != null) {
                    top.setAlias(alias);
                }
                return top;
            }
        } else if (readIf("VALUES")) {
            table = parseValuesTable().getTable(); //��"SELECT * FROM VALUES(1, 'Hello'), (2, 'World')"
        } else {
            String tableName = readIdentifierWithSchema(null);
            Schema schema = getSchema();
            if (readIf("(")) {
                Schema mainSchema = database.getSchema(Constants.SCHEMA_MAIN);
                //��"FROM SYSTEM_RANGE(1,100) SELECT * ";
                if (equalsToken(tableName, RangeTable.NAME)) {
                    Expression min = readExpression();
                    read(",");
                    Expression max = readExpression();
                    read(")");
                    table = new RangeTable(mainSchema, min, max, false);
                } else {
                	//��"FROM TABLE(ID INT=(1, 2), NAME VARCHAR=('Hello', 'World')) SELECT * "
                	//������Ϸ�����FunctionTable�л��״�sql = "FROM USER() SELECT * "; //��������ֵ���ͱ�����RESULT_SET
                	//ֻ��CSVREAD��LINK_SCHEMA��TABLE��TABLE_DISTINCT��4�������ķ���ֵ������RESULT_SET
                    Expression expr = readFunction(schema, tableName);
                    if (!(expr instanceof FunctionCall)) {
                        throw getSyntaxError();
                    }
                    FunctionCall call = (FunctionCall) expr;
                    if (!call.isDeterministic()) {
                        recompileAlways = true;
                    }
                    table = new FunctionTable(mainSchema, session, expr, call);
                }
            } else if (equalsToken("DUAL", tableName)) {
                table = getDualTable(false); //��"FROM DUAL SELECT * "
            } else if (database.getMode().sysDummy1 && equalsToken("SYSDUMMY1", tableName)) {
                table = getDualTable(false); //��"FROM SYSDUMMY1 SELECT * "��Ҫ�ʵ�����MODE����
            } else { //������ from tableName�﷨
                table = readTableOrView(tableName);
            }
        }
        alias = readFromAlias(alias);
        return new TableFilter(session, table, alias, rightsChecked, currentSelect);
    }

    private String readFromAlias(String alias) {
        if (readIf("AS")) {
            alias = readAliasIdentifier();
        } else if (currentTokenType == IDENTIFIER) {
            // left and right are not keywords (because they are functions as
            // well)
            if (!isToken("LEFT") && !isToken("RIGHT") && !isToken("FULL")) {
                alias = readAliasIdentifier();
            }
        }
        return alias;
    }

    
    //���RIGHT��LEFT��INNER��JOIN��5��JOIN����û��ֱ�ӽ�ON�ͻ���ֵݹ����readJoin�������
    //��: SELECT * FROM t1 RIGHT OUTER JOIN t2 LEFT OUTER JOIN t3 INNER JOIN t4 JOIN t5 CROSS JOIN t6 NATURAL JOIN t7
    //��ON�Ļ���ȻҲ��ݹ����readJoin������Ϊ�����ŵ�token��ON������ʵ����readJoinʲô������
    private TableFilter readJoin(TableFilter top, Select command, boolean nested, boolean fromOuter) { //Ƕ�ס��ⲿ
        boolean joined = false;
        TableFilter last = top;
        boolean nestedJoins = database.getSettings().nestedJoins;
        while (true) {
            if (readIf("RIGHT")) {
                readIf("OUTER");
                read("JOIN");
                joined = true;
                // the right hand side is the 'inner' table usually
                TableFilter newTop = readTableFilter(fromOuter);
                newTop = readJoin(newTop, command, nested, true);
                Expression on = null;
                if (readIf("ON")) {
                    on = readExpression();
                }
                //��t1 RIGHT OUTER JOIN t2ʱ��t2����ǰ�棬t1Ƕ����һ��DualTable�У�Ȼ��t2ȥjoin���DualTable
                if (nestedJoins) {
                    top = getNested(top);
                    //RIGHT OUTER��LEFT OUTERʱ��addJoin�ĵڶ�����������true
                    newTop.addJoin(top, true, false, on); //�ⲿ��Ƕ��(addJoin��readJoin��˳��պ��෴)
                } else {
                    newTop.addJoin(top, true, false, on);
                }
                top = newTop;
                last = newTop;
            } else if (readIf("LEFT")) {
                readIf("OUTER");
                read("JOIN");
                joined = true;
                TableFilter join = readTableFilter(true);
                if (nestedJoins) {
                    join = readJoin(join, command, true, true);
                } else {
                    top = readJoin(top, command, false, true);
                }
                Expression on = null;
                if (readIf("ON")) {
                    on = readExpression();
                }
                top.addJoin(join, true, false, on);
                last = join;
            } else if (readIf("FULL")) {
                throw getSyntaxError();
            } else if (readIf("INNER")) {
                read("JOIN");
                joined = true;
                TableFilter join = readTableFilter(fromOuter);
                top = readJoin(top, command, false, false);
                Expression on = null;
                if (readIf("ON")) {
                    on = readExpression();
                }
                if (nestedJoins) {
                    top.addJoin(join, false, false, on);
                } else {
                    top.addJoin(join, fromOuter, false, on);
                }
                last = join;
            } else if (readIf("JOIN")) { //��INNER JOINһ��
                joined = true;
                TableFilter join = readTableFilter(fromOuter);
                top = readJoin(top, command, false, false);
                Expression on = null;
                if (readIf("ON")) {
                    on = readExpression();
                }
                if (nestedJoins) {
                    top.addJoin(join, false, false, on);
                } else {
                    top.addJoin(join, fromOuter, false, on);
                }
                last = join;
            } else if (readIf("CROSS")) {
                read("JOIN");
                joined = true;
                TableFilter join = readTableFilter(fromOuter);
                if (nestedJoins) {
                    top.addJoin(join, false, false, null);
                } else {
                    top.addJoin(join, fromOuter, false, null);
                }
                last = join;
            } else if (readIf("NATURAL")) {
                read("JOIN");
                joined = true;
                TableFilter join = readTableFilter(fromOuter);
                Column[] tableCols = last.getTable().getColumns();
                Column[] joinCols = join.getTable().getColumns();
                String tableSchema = last.getTable().getSchema().getName();
                String joinSchema = join.getTable().getSchema().getName();
                Expression on = null;
                for (Column tc : tableCols) {
                    String tableColumnName = tc.getName();
                    for (Column c : joinCols) {
                        String joinColumnName = c.getName();
                        if (equalsToken(tableColumnName, joinColumnName)) {
                            join.addNaturalJoinColumn(c);
                            Expression tableExpr = new ExpressionColumn(database, tableSchema, last
                                    .getTableAlias(), tableColumnName);
                            Expression joinExpr = new ExpressionColumn(database, joinSchema, join
                                    .getTableAlias(), joinColumnName);
                            Expression equal = new Comparison(session, Comparison.EQUAL, tableExpr, joinExpr);
                            if (on == null) {
                                on = equal;
                            } else {
                                on = new ConditionAndOr(ConditionAndOr.AND, on, equal);
                            }
                        }
                    }
                }
                if (nestedJoins) {
                    top.addJoin(join, false, nested, on);
                } else {
                    top.addJoin(join, fromOuter, false, on);
                }
                last = join;
            } else {
                break;
            }
        }
        if (nested && joined) {
            top = getNested(top);
        }
        return top;
    }

    private TableFilter getNested(TableFilter n) {
        String joinTable = Constants.PREFIX_JOIN + parseIndex; //�磺SYSTEM_JOIN_25
        TableFilter top = new TableFilter(session, getDualTable(true), joinTable, rightsChecked, currentSelect);
        top.addJoin(n, false, true, null);
        return top;
    }
