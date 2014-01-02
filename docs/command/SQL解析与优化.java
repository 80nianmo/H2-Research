1. SQL����

�ʷ��������﷨����������������


ʹ������ɨ��

1.1 ��һ��ɨ��: ��ʼ���׶�

a. ȷ��ÿ���ַ�������
   SQL��Ӧһ���ַ��������飬�������ÿ��Ԫ�ر�ʾ�ַ��Ĵʷ�����
    // used during the tokenizer phase
    private static final int CHAR_END = 1, CHAR_VALUE = 2, CHAR_QUOTED = 3;
    private static final int CHAR_NAME = 4, CHAR_SPECIAL_1 = 5, CHAR_SPECIAL_2 = 6;
    private static final int CHAR_STRING = 7, CHAR_DOT = 8, CHAR_DOLLAR_QUOTED_STRING = 9;

	����һ��0ֵ��������9�����������(������ʾע�͡��հ׵�)

b. sqlԤ����

������������:

1)
����"//"��"--"��ͷ�ĵ���ע��ȫ���ɿո�
��"/* ... */"��ע��ȫ���ɿո�

ע�����ַ����������ж�Ӧ�����Ͷ���0,����"//"��"--"��"/*"��"*/"

2)
$$...$$(�����Զ��庯����洢����)
��:
CREATE ALIAS IP_ADDRESS AS $$
import java.net.*;
@CODE
String ipAddress(String host) throws Exception {
    return InetAddress.getByName(host).getHostAddress();
}
$$;
��ô��Ԥ�Ȱ�$$�滻�ɿո�������0,����$$�а������ַ����䣬������CHAR_DOLLAR_QUOTED_STRING��
$$����������ո񡢻س���������ַ�Ҳ�������⴦��

3) �ַ���
��sql���ַ������õ�����������������java����˫���ţ�
�ַ������ֲ��䣬���Ƕ����ַ���������ֻ�Ե�һ����������CHAR_STRING��ʾ����������0��
�ڵ���read��ȡtokenʱ��ֻҪ�жϵ�һ���ַ�������CHAR_STRING����ô��һֱ�ҵ�������һ��������Ϊֹ


4) ����
a. SQL Server֧��[..]��ʽ����Ҫ����MODE = MSSQLServer
��[��]�滻��˫���ţ���Ӧ[��������CHAR_QUOTED��������������0��
�ڵ���read��ȡtokenʱ��ֻҪ�жϵ�һ���ַ�������CHAR_QUOTED����ô��һֱ�ҵ�������һ��˫����Ϊֹ

b. MySQL֧��"`"�Ÿ�ʽ
��"`"���滻��˫���ţ���Ӧ��һ��"`"�ŵ�������CHAR_QUOTED��������������0��
"`"�����������ַ�ȫת�ɴ�д,
�ڵ���read��ȡtokenʱ��ֻҪ�жϵ�һ���ַ�������CHAR_QUOTED����ô��һֱ�ҵ�������һ��˫����Ϊֹ

c. ˫���Ÿ�ʽ
��Ӧ��һ��˫���ŵ�������CHAR_QUOTED��������������0��
�ڵ���read��ȡtokenʱ��ֻҪ�жϵ�һ���ַ�������CHAR_QUOTED����ô��һֱ�ҵ�������һ��˫����Ϊֹ




5)
�»��ߡ�a-z��A-Z����CHAR_NAME�����DATABASE_TO_UPPER����Ϊtrue����ôa-zҪת�ɴ�д
0-9������CHAR_VALUE

����������ַ�����java.lang.Character.isJavaIdentifierPart(char)��
���Ϊtrue��ô����CHAR_NAME,���DATABASE_TO_UPPER����Ϊtrue��ôת���ɴ�д


1.2 read ����������һ��token

Token����:
    // this are token types
    private static final int KEYWORD = 1, IDENTIFIER = 2, PARAMETER = 3, END = 4, VALUE = 5;
    private static final int EQUAL = 6, BIGGER_EQUAL = 7, BIGGER = 8;
    private static final int SMALLER = 9, SMALLER_EQUAL = 10, NOT_EQUAL = 11, AT = 12;
    private static final int MINUS = 13, PLUS = 14, STRING_CONCAT = 15;
    private static final int OPEN = 16, CLOSE = 17, NULL = 18, TRUE = 19, FALSE = 20;
    private static final int CURRENT_TIMESTAMP = 21, CURRENT_DATE = 22, CURRENT_TIME = 23, ROWNUM = 24;



�ܹ�38������token�����ܳ䵱��ʶ����

7�����������ʱ������:
---------------------------------------------------
CURRENT_TIMESTAMP��CURRENT_TIME��CURRENT_DATE�����������token�����Ǽ����ǹؼ��֣�Ҳ���Ǳ�ʶ����
���ǲ��������ǳ䵱��ʶ��ȥ�����粻����CURRENT_DATE������:
CREATE TABLE current_date �������﷨�Ǵ���ġ�

SYSTIMESTAMP��SYSTIME��SYSDATE��CURRENT_TIMESTAMP��CURRENT_TIME��CURRENT_DATE�ȼ�
TODAYҲ��CURRENT_DATE�ȼ�

3������token����:
---------------------------------------------------
FALSE
TRUE
NULL

28��KEYWORD(�ؼ���)
---------------------------------------------------
CROSS
DISTINCT
EXCEPT
EXISTS

FROM
FOR
FULL
FETCH ������ݿ�֧��supportOffsetFetch(DB2��Derby��PostgreSQL��֧��)

GROUP
HAVING
INNER
INTERSECT
IS
JOIN

LIMIT
LIKE
MINUS
NOT
NATURAL

ON
OFFSET  ������ݿ�֧��supportOffsetFetch(DB2��Derby��PostgreSQL��֧��)
ORDER
ORDER
PRIMARY

ROWNUM
SELECT

UNIQUE
UNION
WHERE





2. SQL�Ż�

2.1 ���ʽ�Ż�

2.1 ����ѡ��(ִ�мƻ�)