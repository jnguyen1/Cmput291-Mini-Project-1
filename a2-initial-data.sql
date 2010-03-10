-- Data prepared by: Steven Maschmeyer (maschmey@ualberta.ca)
-- Published on 7-FEB-2010
-- Revised on 11-FEB-2010


--users(email,name,city,gender,pwd)
insert into users values ('joe@gmail.com', 'Joe Dirt', 'Edmonton', 'M', 'some');
insert into users values ('agent1@matrix.com', 'Agent 1', 'Mega City', 'M', '4931');
insert into users values ('agent2@matrix.com', 'Agent 2', 'Mega City', 'M', '4932');
insert into users values ('agent3@matrix.com', 'Agent 3', 'Mega City', 'M', '4933');
insert into users values ('agent4@matrix.com', 'Agent 4', 'Mega City', 'M', '4934');
insert into users values ('agent5@matrix.com', 'Agent 5', 'Mega City', 'M', '4935');
insert into users values ('agent6@matrix.com', 'Agent 6', 'Mega City', 'M', '4936');
insert into users values ('agent7@matrix.com', 'Agent 7', 'Mega City', 'M', '4937');
insert into users values ('agent8@matrix.com', 'Agent 8', 'Mega City', 'M', '4938');
insert into users values ('agent9@matrix.com', 'Agent 9', 'Mega City', 'M', '4939');
insert into users values ('agent10@matrix.com', 'Agent 10', 'Mega City', 'M', '4910');
insert into users values ('jeremy@pwn.ca', 'Jeremy', 'Toronto', 'M', '1.1i');
insert into users values ('kyle@pwn.ca', 'Kyle', 'Toronto', 'M', 'film');
insert into users values ('doug@pwn.ca', 'Doug', 'Toronto', 'M', 'head');
insert into users values ('dave@pwn.ca', 'Dawei', 'Toronto', 'M', 'pro@');
insert into users values ('barker@cbs.com', 'Bob Barker', 'B. Hills', 'M', '$42');
insert into users values ('yukon@bear.fm', 'Yukon Jack', 'Edmonton', 'M', 'bigy');
insert into users values ('vegeta@capsule.co', 'Vegeta', 'West City', 'M', 'NAD?');
insert into users values ('goku@capsule.co', 'Goku', 'Mount Paozu', 'M', 'pepp');
insert into users values ('tien@capsule.co', 'Tien', 'Earth', 'M', 'inos');
insert into users values ('piccolo@capsule.co', 'Piccolo', 'Earth', 'M', 'sbc');
insert into users values ('admin@nhl.com', 'Masterer', 'Edmonton', 'M', '1R2L');
insert into users values ('heatley@sharks.com', 'Daniel Heatley', 'San Jose', 'M', 'mrbr');
insert into users values ('rafiei@prof.ca', 'Davood Rafiei', 'Edmonton', 'M', '2012');
insert into users values ('lucy@valleyzoo.ca', 'Lucy', 'Edmonton', 'F', 'ivor');
insert into users values ('kavna@vanaqua.org', 'Kavna', 'Vancouver', 'F', 'whal');
insert into users values ('mandel@edmonton.ca', 'Stephen Mandel', 'Edmonton', 'M', 'LRT!');
insert into users values ('katz@oil.ca', 'Daryl Katz', 'Edmonton', 'M', 'odru');
insert into users values ('sakic@nhl.com', 'Joseph Sakic', 'Vancouver', 'M', 'sjoe');
insert into users values ('turner@gc.ca', 'John Turner', 'Vancouver', 'M', 'btp');
insert into users values ('dubya@whitehouse.gov', 'George Bush', 'Dallas', 'M', 'bush');
insert into users values ('cheney@whitehouse.gov', 'Dick Cheney', 'McLean', 'M', 'wmds');
insert into users values ('mterringo@osteria.de', 'M Terringo', 'Calgary', 'M', 'afic');
insert into users values ('moreau@oil.ca', 'Ethan Moreau', 'Edmonton', 'M', 'Chop');
insert into users values ('obl@cia.nwo', 'Osama Bin Laden', 'D.C', 'M', 'thie');


--friends(email,femail)
insert into friends values ('dubya@whitehouse.gov', 'cheney@whitehouse.gov');
insert into friends values ('cheney@whitehouse.gov', 'dubya@whitehouse.gov');
insert into friends values ('mandel@edmonton.ca', 'turner@gc.ca');
insert into friends values ('turner@gc.ca', 'mandel@edmonton.ca');
insert into friends values ('mandel@edmonton.ca', 'katz@oil.ca');
insert into friends values ('katz@oil.ca', 'mandel@edmonton.ca');
insert into friends values ('lucy@valleyzoo.ca', 'kavna@vanaqua.org');
insert into friends values ('kavna@vanaqua.org', 'lucy@valleyzoo.ca');
insert into friends values ('goku@capsule.co', 'tien@capsule.co');
insert into friends values ('tien@capsule.co', 'goku@capsule.co');
insert into friends values ('goku@capsule.co', 'piccolo@capsule.co');
insert into friends values ('piccolo@capsule.co', 'goku@capsule.co');
insert into friends values ('goku@capsule.co', 'vegeta@capsule.co');
insert into friends values ('vegeta@capsule.co', 'goku@capsule.co');
insert into friends values ('jeremy@pwn.ca', 'doug@pwn.ca');
insert into friends values ('doug@pwn.ca', 'jeremy@pwn.ca');
insert into friends values ('jeremy@pwn.ca', 'dave@pwn.ca');
insert into friends values ('dave@pwn.ca', 'jeremy@pwn.ca');
insert into friends values ('admin@nhl.com', 'heatley@sharks.com');
insert into friends values ('heatley@sharks.com', 'admin@nhl.com');
insert into friends values ('admin@nhl.com', 'katz@oil.ca');
insert into friends values ('katz@oil.ca', 'admin@nhl.com');
insert into friends values ('admin@nhl.com', 'sakic@nhl.com');
insert into friends values ('sakic@nhl.com', 'admin@nhl.com');
insert into friends values ('admin@nhl.com', 'moreau@oil.ca');
insert into friends values ('moreau@oil.ca', 'admin@nhl.com');
insert into friends values ('admin@nhl.com', 'rafiei@prof.ca');
insert into friends values ('rafiei@prof.ca', 'admin@nhl.com');
insert into friends values ('admin@nhl.com', 'jeremy@pwn.ca');
insert into friends values ('jeremy@pwn.ca', 'admin@nhl.com');
insert into friends values ('admin@nhl.com', 'agent1@matrix.com');
insert into friends values ('agent1@matrix.com', 'admin@nhl.com');
insert into friends values ('joe@gmail.com', 'kyle@pwn.ca');
insert into friends values ('kyle@pwn.ca', 'joe@gmail.com');
insert into friends values ('joe@gmail.com', 'dubya@whitehouse.gov');
insert into friends values ('dubya@whitehouse.gov', 'joe@gmail.com');
insert into friends values ('agent1@matrix.com', 'joe@gmail.com');
insert into friends values ('joe@gmail.com', 'agent1@matrix.com');
insert into friends values ('agent2@matrix.com', 'joe@gmail.com');
insert into friends values ('joe@gmail.com', 'agent2@matrix.com');
insert into friends values ('agent3@matrix.com', 'joe@gmail.com');
insert into friends values ('joe@gmail.com', 'agent3@matrix.com');
insert into friends values ('agent4@matrix.com', 'joe@gmail.com');
insert into friends values ('joe@gmail.com', 'agent4@matrix.com');
insert into friends values ('agent5@matrix.com', 'joe@gmail.com');
insert into friends values ('joe@gmail.com', 'agent5@matrix.com');
insert into friends values ('agent6@matrix.com', 'joe@gmail.com');
insert into friends values ('joe@gmail.com', 'agent6@matrix.com');
insert into friends values ('agent7@matrix.com', 'joe@gmail.com');
insert into friends values ('joe@gmail.com', 'agent7@matrix.com');
insert into friends values ('agent8@matrix.com', 'joe@gmail.com');
insert into friends values ('joe@gmail.com', 'agent8@matrix.com');
insert into friends values ('agent9@matrix.com', 'joe@gmail.com');
insert into friends values ('joe@gmail.com', 'agent9@matrix.com');
insert into friends values ('agent10@matrix.com', 'joe@gmail.com');
insert into friends values ('joe@gmail.com', 'agent10@matrix.com');


--pages(pid,cdate,title,content,creator) 
insert into pages values ('1', '01-JAN-1980', 'Calgary Flames Fans', 'Calgary Flames FTW!!!', 'admin@nhl.com'); 
insert into pages values ('2', '01-NOV-1971', 'Edmonton Oilers', 'All aboard the fail boat.', 'katz@oil.ca');
insert into pages values ('3', '01-JAN-1989', 'Calgary Osteria', 'Osteria De Medici - Fine Italian Cuisine', 'mterringo@osteria.de');
insert into pages values ('4', '03-AUG-2006', 'Through the Fire n Flames', 'Through the fire and flames we carry on!', 'dubya@whitehouse.gov');
insert into pages values ('5', '01-AUG-1988', 'Alqaeda', 'Derka derka muhammad allah jihad!', 'cheney@whitehouse.gov');
insert into pages values ('6', '11-OCT-2001', '** the Taliban n Alqaeda!', 'Get these people out of our crib!', 'dubya@whitehouse.gov');
insert into pages values ('7', '21-DEC-2012', 'Calgary in Flames', 'Shark frenzy leaves calgary in flames.', 'admin@nhl.com');


--fans(email,pid,since)
insert into fans values ('mandel@edmonton.ca', '2', '26-OCT-2004');
insert into fans values ('yukon@bear.fm', '2', '31-DEC-1999');
insert into fans values ('rafiei@prof.ca', '1', '01-JAN-1980');
insert into fans values ('moreau@oil.ca', '3', '01-JAN-2010');
insert into fans values ('mterringo@osteria.de', '1', '31-DEC-1999');
insert into fans values ('dubya@whitehouse.gov', '5', '02-AUG-1990');
insert into fans values ('agent7@matrix.com', '6', '08-SEP-2002');
insert into fans values ('heatley@sharks.com', '7', '31-DEC-1999');
insert into fans values ('yukon@bear.fm', '7', '31-DEC-1999');


--status(email,sno,text,pdate) 
insert into status values ('moreau@oil.ca', 1, 'Ate at the Osteria De Medici; great food', '01-JAN-2010');
insert into status values ('vegeta@capsule.co', 2, 'Its over 9000!!', '31-DEC-1999');
insert into status values ('barker@cbs.com', 3, 'Get lucy out of edmonton!', '24-FEB-2009');
insert into status values ('vegeta@capsule.co', 4, 'Fire that thing!', '31-DEC-1999');
insert into status values ('kyle@pwn.ca', 5, 'Geoff is leaving the show...', '28-SEP-2008');
insert into status values ('doug@pwn.ca', 6, 'fffboOOM... YEAHAHAEAAAAAA!!!!', '29-SEP-2009');
insert into status values ('agent7@matrix.com', 7, 'Is it cheap if you defend the bomb?', '01-JAN-2000');


--comments(email,cno,sno,semail,text,cdate) 
insert into comments values ('mterringo@osteria.de', 1, 1, 'moreau@oil.ca', 'Cheapskates!', '01-JAN-2010');
insert into comments values ('dubya@whitehouse.gov', 2, 1, 'moreau@oil.ca', 'My pet goat eats all the capes he wants!', '02-JAN-2010');
insert into comments values ('mterringo@osteria.de', 3, 1, 'moreau@oil.ca', 'Would he eat them with a fox?', '02-JAN-2010');
insert into comments values ('dubya@whitehouse.gov', 4, 1, 'moreau@oil.ca', 'Not in a box, etc. But, he likes them.', '03-JAN-2010');
insert into comments values ('moreau@oil.ca', 5, 1, 'moreau@oil.ca', 'Moreau agrees.', '03-JAN-2010');
insert into comments values ('jeremy@pwn.ca', 6, 1, 'moreau@oil.ca', 'This likes isnt funny at all', '03-JAN-2010');
insert into comments values ('kyle@pwn.ca', 7, 1, 'moreau@oil.ca', 'Kyle likes this', '05-JAN-2010');
insert into comments values ('goku@capsule.co', 8, 2, 'vegeta@capsule.co', 'Is that okay?', '31-DEC-1999');
insert into comments values ('tien@capsule.co', 9, 2, 'vegeta@capsule.co', 'As a matter of fact, it isnt.', '31-DEC-1999');
insert into comments values ('lucy@valleyzoo.ca', 10, 3, 'barker@cbs.com', 'The price isnt right', '25-FEB-2009');
insert into comments values ('kavna@vanaqua.org', 19, 3, 'barker@cbs.com', 'Kavna agrees.', '26-FEB-2009');
insert into comments values ('kyle@pwn.ca', 20, 3, 'barker@cbs.com', 'Kyle likes this', '26-FEB-2009');
insert into comments values ('goku@capsule.co', 11, 4, 'vegeta@capsule.co', 'But I cant!', '31-DEC-1999');
insert into comments values ('vegeta@capsule.co', 12, 4, 'vegeta@capsule.co', 'Then the whole universe is going to die!', '31-DEC-1999');
insert into comments values ('vegeta@capsule.co', 13, 4, 'vegeta@capsule.co', 'Is that okay?', '31-DEC-1999');
insert into comments values ('tien@capsule.co', 14, 4, 'vegeta@capsule.co', 'As a matter of fact...', '31-DEC-1999');
insert into comments values ('piccolo@capsule.co', 15, 4, 'vegeta@capsule.co', 'it doesnt make a bit of difference guys', '31-DEC-1999');
insert into comments values ('piccolo@capsule.co', 16, 4, 'vegeta@capsule.co', '...the balls are inert.', '31-DEC-1999');
insert into comments values ('vegeta@capsule.co', 16, 4, 'vegeta@capsule.co', 'What did you do, kick him in the ****?', '31-DEC-1999');
insert into comments values ('tien@capsule.co', 17, 4, 'vegeta@capsule.co', 'Yea, I guess I did didnt I... Did you?', '31-DEC-1999');
insert into comments values ('kyle@pwn.ca', 18, 4, 'vegeta@capsule.co', 'Kyle likes this', '05-JAN-2010');
insert into comments values ('doug@pwn.ca', 19, 7, 'agent7@matrix.com', 'well I dont know - is it cheap in an fps', '01-JAN-2000');
insert into comments values ('doug@pwn.ca', 20, 7, 'agent7@matrix.com', 'to maybe shoot the guy in the head!?', '01-JAN-2000');
insert into comments values ('doug@pwn.ca', 21, 7, 'agent7@matrix.com', 'is it cheap to maybe use your nade!?', '01-JAN-2000');
insert into comments values ('jeremy@pwn.ca', 22, 7, 'agent7@matrix.com', 'Doug... you forgot to turn capslock on', '01-JAN-2000');
insert into comments values ('doug@pwn.ca', 23, 7, 'agent7@matrix.com', '!!!****!!!', '01-JAN-2000');


--messages(mid,mdate,text,sender) 
insert into messages values (1, '10-SEP-2002', 'dirka dirka?', 'dubya@whitehouse.gov');
insert into messages values (2, '10-SEP-2002', 'Im busy; maybe later...', 'cheney@whitehouse.gov');
insert into messages values (3, '01-JAN-2010', 'Dood, wheres my new arena?', 'mandel@edmonton.ca');
insert into messages values (4, '01-JAN-2010', 'Wheres your new arena dood?', 'katz@oil.ca');
insert into messages values (5, '09-SEP-2002', 'Assalamu alaikum lets come together bro', 'joe@gmail.com');
insert into messages values (6, '22-JUN-2004', 'Im coming at your base with a rockvee...', 'jeremy@pwn.ca');
insert into messages values (7, '22-JUN-2004', 'Send your tank, cause I got frags...', 'doug@pwn.ca');
insert into messages values (8, '31-DEC-1999', '001110010001110001110 001 001011100', 'agent1@matrix.com');
insert into messages values (9, '01-JAN-2000', 'FUTURE!!!', 'agent1@matrix.com');
insert into messages values (10, '17-SEP-2002', 'Fool me once.. you cant get fooled again', 'dubya@whitehouse.gov');
insert into messages values (11, '18-SEP-2002', 'I can dance all day! Just try and hit me', 'doug@pwn.ca');


--receives(mid,email) 
insert into receives values (1, 'cheney@whitehouse.gov');
insert into receives values (2, 'dubya@whitehouse.gov');
insert into receives values (2, 'obl@cia.nwo');
insert into receives values (3, 'katz@oil.ca');
insert into receives values (4, 'mandel@edmonton.ca');
insert into receives values (5, 'agent7@matrix.com');
insert into receives values (6, 'doug@pwn.ca');
insert into receives values (7, 'jeremy@pwn.ca');
insert into receives values (8, 'agent10@matrix.com');
insert into receives values (9, 'agent10@matrix.com');
insert into receives values (10, 'agent8@matrix.com');
insert into receives values (11, 'agent7@matrix.com');
