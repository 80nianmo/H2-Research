��14���ۺϷ������м��������в�ͬ����:

COUNT         COUNT_ALL(count(*)) �� COUNT(�ֶ�)��COUNT_ALL��COUNT�ϲ���������COUNT_ALL���������֣�����COUNT
SUM
MIN
MAX
AVG

GROUP_CONCAT

STDDEV_SAMP   Ҳ��: STDDEV
STDDEV_POP    Ҳ��: STDDEVP
VAR_SAMP      Ҳ��: VAR��VARIANCE
VAR_POP       Ҳ��: VARP

BOOL_OR       Ҳ��: SOME
BOOL_AND      Ҳ��: EVERY

SELECTIVITY
HISTOGRAM     ����ۺϺ�����H2���ĵ���û�н���


�ۺϺ���ֻ�ܳ�����select����"select (1)... from (2) ...."�е�(1)

static {
        addAggregate("COUNT", COUNT);
        addAggregate("SUM", SUM);
        addAggregate("MIN", MIN);
        addAggregate("MAX", MAX);
        addAggregate("AVG", AVG);
        addAggregate("GROUP_CONCAT", GROUP_CONCAT);
        addAggregate("STDDEV_SAMP", STDDEV_SAMP);
        addAggregate("STDDEV", STDDEV_SAMP);
        addAggregate("STDDEV_POP", STDDEV_POP);
        addAggregate("STDDEVP", STDDEV_POP);
        addAggregate("VAR_POP", VAR_POP);
        addAggregate("VARP", VAR_POP);
        addAggregate("VAR_SAMP", VAR_SAMP);
        addAggregate("VAR", VAR_SAMP);
        addAggregate("VARIANCE", VAR_SAMP);
        addAggregate("BOOL_OR", BOOL_OR);
        // HSQLDB compatibility, but conflicts with x > EVERY(...)
        addAggregate("SOME", BOOL_OR);
        addAggregate("BOOL_AND", BOOL_AND);
        // HSQLDB compatibility, but conflicts with x > SOME(...)
        addAggregate("EVERY", BOOL_AND);
        addAggregate("SELECTIVITY", SELECTIVITY);
        addAggregate("HISTOGRAM", HISTOGRAM);
    }

��������˳��:

org.h2.expression.Aggregate.mapColumns(ColumnResolver, int)
org.h2.expression.Aggregate.optimize(Session)
org.h2.expression.Aggregate.updateAggregate(Session) //���ÿ�е��þۺϷ���
	=>org.h2.expression.AggregateData.add(Database, boolean, Value)
org.h2.expression.Aggregate.getValue(Session) //����ܽ��
	=>org.h2.expression.AggregateData.getValue(Database, boolean)


����ۺϺ���˵��:

1. SELECTIVITY

�ǻ���ĳ�����ʽ(�����ǵ����ֶ�)�㲻�ظ��ļ�¼����ռ�ܼ�¼���İٷֱ�
org.h2.engine.Constants.SELECTIVITY_DISTINCT_COUNTĬ����1�����ֵ���ܸģ�
��ͳ��ֵӰ��ܴ�ͨ�����ֵԽ��ͳ��Խ��ȷ�����ǻ�ʹ�ø����ڴ档
SELECTIVITYԽ��˵���ظ��ļ�¼Խ�٣���ѡ������ʱ��������

�����㷨ʵ��:
��select SELECTIVITY(�ֶ�) �ĳ�select count(�ֶ�), count(DISTINCT �ֶ�)
Ȼ������: {��count(�ֶ�)/��count(DISTINCT �ֶ�)} * 100





��׼���ڶ�ڵ��ϵĲ����㷨��: ��ÿ�ڵ�������: ��¼�����ֶκ͡��ֶ�ƽ���ͣ�Ȼ��鲢���нڵ��Ϸ��ص�����������������׼�� = {�ֶ�ƽ���ܺ�/�ܼ�¼��- (�ֶ��ܺ�/�ܼ�¼��)��ƽ��} ��ƽ������HBaseҲ����ô���ġ�

���Խ�����: ���ڱ�׼��ķֲ�ʽ���м����㷨���Ż��ǽ�"select std(�ֶ�)"�ȸ�д��"select count(�ֶ�)��sum(�ֶ�)��sum(�ֶ�*�ֶ�)"���鲢�󰴴˹�ʽ��: ��׼�� = {�ֶ�ƽ���ܺ�/�ܼ�¼��- (�ֶ��ܺ�/�ܼ�¼��)��ƽ��} ��ƽ����

�ۺϺ����ӵ������չ����������������˺ö࣬������ƽ������˵����client���յ�"select avg(�ֶ�) from ��"������sql����Ҫ��дsql����avg����count��sum���ټ�����ͳ�������where���������"select count(�ֶ�), sum(�ֶ�) from �� where �ֶ� between x and y"��ֱ��ÿ�������avg�ǲ��Ե�

1 2 3 4 5

ƽ��ֵ: 3

(1-3)��ƽ�� + (2-3)��ƽ�� + (3-3)��ƽ�� + (4-3)��ƽ�� + (5-3)��ƽ��
= 4 + 1 + 0 + 1 + 4
=10

��׼�� = (10/5)��ƽ���� =1.4
================================
1 2 3

ƽ��ֵ: 2

(1-2)��ƽ�� + (2-2)��ƽ�� + (3-2)��ƽ��
= 1 + 0 + 1
= 2

��׼�� = (2/3)��ƽ���� = 0.8

================================
4 5 

ƽ��ֵ: 4.5

(4-4.5)��ƽ�� + (5-4.5)��ƽ��
= 0.25 + 0.25
= 0.5

��׼�� = (0.5/2)��ƽ���� = 0.5

1
mean = 1;
m2 = 0;

2

mean = 1.5;
m2 = 0;


1 3 5

�ܺ�   1 + 3 + 5


ƽ���� 1*1 + 3*3 + 5*5

���� 3

7 9 11

�ܺ�   7 + 9 + 11


ƽ���� 7*7 + 9*9 + 11*11

���� 3
============================

���߻���:
�ܺ�   (1 + 3 + 5)  +  (7 + 9 + 11)
ƽ���� (1*1 + 3*3 + 5*5)  +  (7*7 + 9*9 + 11*11)
����   3 + 3  = 6

{(1 + 3 + 5)  +  (7 + 9 + 11)} / 6
{(1*1 + 3*3 + 5*5)  +  (7*7 + 9*9 + 11*11)} / 6

��׼��:
{ {(1*1 + 3*3 + 5*5)  +  (7*7 + 9*9 + 11*11)} / 6 } - {{(1 + 3 + 5) + (7 + 9 + 11)} / 6} * {{(1 + 3 + 5) + (7 + 9 + 11)} / 6}

= (1+9+25+49+81+121)/6 - (36/6) * (36/6)
= 286/6 - (36/6) * (36/6)
= 11.666666666666666666666666666667
= 3.4156502553198661277403462268404

(1-6) (3-6) (5-6) (7-6) (9-6) (11-6)
25 + 9 + 1 + 1 + 9 + 25
= 70
= 3.4156502553198661277403462268404


(x-2)*(x-2) = x*x-2x-2x+4

(x- {{(1 + 3 + 5) + (7 + 9 + 11)} / 6})

x1 * x1 - 2x * {{(1 + 3 + 5) + (7 + 9 + 11)} / 6} + {{(1 + 3 + 5) + (7 + 9 + 11)} / 6}

(1 - (1 + 3 + 5)/3)��ƽ�� + (3 - (1 + 3 + 5)/3)��ƽ��  + (5 - (1 + 3 + 5)/3)��ƽ��


1*1 - 2*1*(1 + 3 + 5)/3 + (1 + 3 + 5)/3*(1 + 3 + 5)/3
+ 3*3 - 2*3*(1 + 3 + 5)/3 + (1 + 3 + 5)/3*(1 + 3 + 5)/3
+ 5*5 - 2*5*(1 + 3 + 5)/3 + (1 + 3 + 5)/3*(1 + 3 + 5)/3


1*1 - (2*1*(1 + 3 + 5)/3 - (1 + 3 + 5)/3*(1 + 3 + 5)/3)
+ 3*3 - (2*3*(1 + 3 + 5)/3 - (1 + 3 + 5)/3*(1 + 3 + 5)/3)
+ 5*5 - (2*5*(1 + 3 + 5)/3 - (1 + 3 + 5)/3*(1 + 3 + 5)/3)

