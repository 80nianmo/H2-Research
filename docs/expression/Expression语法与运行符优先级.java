���ȼ�: ��͵����
or
and
|| (�ַ�������)

+ -
* / %
Term


���������optimize�������Ժ���

org.h2.expression.Parameter
org.h2.expression.Rownum
org.h2.expression.SequenceValue
org.h2.expression.ValueExpression
org.h2.expression.Variable
org.h2.expression.Wildcard

Expression�����Ҫ��������˳��

mapColumns => optimize => updateAggregate => getValue

ֻ��updateAggregate�ǿ�ѡ��
���ھۺϡ�������
��org.h2.command.dml.Select.queryGroup(int, LocalResult)
��org.h2.command.dml.Select.queryGroupSorted(int, ResultTarget)���õ�
�ڱ�����¼�Ĺ�����ִ�У���getValue֮ǰ����



mapColumns������Ҫ�����org.h2.expression.ExpressionColumn�ģ�
��ҪĿ���ǰѱ���й���������Column column�ֶΣ�����ס���ExpressionColumn��ColumnResolver��
��ColumnResolver�ɻ����ֵ����org.h2.table.TableFilter.getValue(Column)
TableFilter��ʵ����ColumnResolver�ӿ�

optimize������Ҫ����һЩ�Ż������翴���Ƿ��ǳ����ȵȺܶ�С��ϸ���Ż��㣬�����ÿ��Expression����Ĳ�ͬʵ��

getValue�õ����ʽ��ֵ










