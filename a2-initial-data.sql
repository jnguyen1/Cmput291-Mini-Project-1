-- Data prepared by: Davood Rafiei (drafiei@cs.ualberta.ca)
-- Published on Jan 28, 2010
-- This is a small initial dataset and is not sufficient for testing.

insert into users values ('st@nhl.com','Steve Tambellini','Edmonton','M','ssss');
insert into users values ('pat@nhl.com','Pat Quinn','Edmonton','M','pppp');
insert into users values ('av@nhl.com','Alain Vigneault','Vancouver','M','aaaa');
insert into users values ('ds@nhl.com','Darryl Sutter','Calgary','M','dddd');
insert into users values ('dr@cs.ualberta.ca','Davood Rafiei','Edmonton','M','dddd');
insert into users values ('joe@gmail.com','Joe Smith','Edmonton','M','jjjj');
insert into users values ('joan@gmail.com','Joan Smith','Vancouver','F','jjj2');
insert into users values ('laden@blacklist.com','Osama BinLaden','Afghanistan','M','llll');
insert into users values ('zarqawi@blacklist.com','Abu Zarqawi','Iraq','M','zzzz');
insert into users values ('pman@gmail.com','Poor Man','Edmonton','M','pppp');
insert into users values ('ssam@gmail.com','Sam Suspect','Calgary','M','mmmm');

insert into pages values ('oilers','10-NOV-2009','Edmonton Oilers Fans','Edmonton Oilers Fans page is here','st@nhl.com');
insert into pages values ('flames','10-NOV-2009','Calgary Flames Fans','Calgary Flames Fans page is here','ds@nhl.com');
insert into pages values ('alqaed','11-SEP-2001','Alqaeda Group','Alqaeda group page is here','laden@blacklist.com');

insert into fans values ('dr@cs.ualberta.ca','oilers','20-NOV-2006');
insert into fans values ('joe@gmail.com','flames','20-NOV-2007');
insert into fans values ('zarqawi@blacklist.com','alqaed','11-DEC-2008');

insert into friends values ('joe@gmail.com','joan@gmail.com');
insert into friends values ('joan@gmail.com','joe@gmail.com');
insert into friends values ('st@nhl.com','pat@nhl.com');
insert into friends values ('pat@nhl.com','st@nhl.com');
insert into friends values ('st@nhl.com','av@nhl.com');
insert into friends values ('av@nhl.com','st@nhl.com');

insert into messages values (100,'10-JAN-2010','Bluh Bluh Bluh','zarqawi@blacklist.com');

insert into receives values (100,'pman@gmail.com');
insert into receives values (100,'ssam@gmail.com');
