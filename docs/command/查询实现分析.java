1. ����

select���﷨�����������:
org.h2.command.Parser.parseSelect()

����ͨ���ܶ�ط���������
������Ǵ�org.h2.command.Parser.parsePrepared()������

��������parseSelect()��sql�﷨���������:
�ֱ�����"("��"select"��"from"��ͷ��sql�﷨(���ִ�Сд��)


2. �������̷���

���sql��select name1 from mytable1 union select name2 from mytable2

parseSelect()
	=>parseSelectUnion()
		=>parseSelectSub()  
			=>parseSelectSimple()
		=>parseSelectUnionExtension(Query, int, boolean)

���ڼ򵥵�sql����select name1 from mytable1 order by name1
parseSelectSub()�������select name1 from mytable1
parseSelectUnionExtension������� order by name1

���union sql��select name1 from mytable1 union select name2 from mytable2 order by name1
parseSelectSub()�������select name1 from mytable1
parseSelectUnionExtension������� union select name2 from mytable2 order by name1


3. join����

��6��join����
RIGHT OUTER JOIN
LEFT OUTER JOIN
FULL //��ʱ��֧��
INNER JOIN
JOIN
CROSS JOIN
NATURAL JOIN

4.��ͼ
org.h2.command.Prepared
	=>org.h2.command.dml.Query
		=>org.h2.command.dml.Select
		=>org.h2.command.dml.SelectUnion

����˳��
	=>org.h2.command.dml.Query.init()(org.h2.command.dml.Select.init()��org.h2.command.dml.SelectUnion.init())
	=>org.h2.command.dml.Select.prepare()
	=>org.h2.command.dml.Query.query(int)

init�ڵ�����org.h2.command.Parser.parseSelect()�����(�ڵõ�һ��Query��)
�����ڽ�����һ���Ӳ�ѯʱorg.h2.command.Parser.readTableFilter(boolean)
��ʱҲ�õ�һ��Query��
����һ�����������org.h2.command.Parser.parseValues()
������VALUES(1, 'Hello'), (2, 'World')�������﷨

org.h2.command.dml.Select.init()�������


DROP TABLE IF EXISTS JoinTest1 CASCADE;
CREATE TABLE IF NOT EXISTS JoinTest1(id int, name varchar(500), b boolean);
		
DROP TABLE IF EXISTS JoinTest2 CASCADE;
CREATE TABLE IF NOT EXISTS JoinTest2(id2 int, name2 varchar(500));

insert into JoinTest1(id, name, b) values(10, 'a1', true);
insert into JoinTest1(id, name, b) values(20, 'b1', true);
insert into JoinTest1(id, name, b) values(30, 'a2', false);
insert into JoinTest1(id, name, b) values(40, 'b2', true);
		
insert into JoinTest2(id2, name2) values(90, 'a11');
insert into JoinTest2(id2, name2) values(90, 'a11');
insert into JoinTest2(id2, name2) values(90, 'a11');
insert into JoinTest2(id2, name2) values(90, 'a11');

select rownum, * from JoinTest1 LEFT OUTER JOIN JoinTest2;
select rownum, * from JoinTest1 RIGHT OUTER JOIN JoinTest2;
select rownum, * from JoinTest1 INNER JOIN JoinTest2;
select rownum, * from JoinTest1 JOIN JoinTest2;
select * from JoinTest1 CROSS JOIN JoinTest2;
select from JoinTest1 NATURAL JOIN JoinTest2;

select * from JoinTest1 JOIN JoinTest2;


