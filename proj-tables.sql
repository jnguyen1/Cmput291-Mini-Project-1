-- Prepared by Davood Rafiei for CMPUT 291 / Winter 2010
-- We will drop the tables in case they exist from previous runs
drop table receives;
drop table messages;
drop table comments;
drop table status;
drop table fans;
drop table pages;
drop table friend_requests;
drop table friends;
drop table users;

-- Create the tables
create table users (
      email         char(25),
      name          char(16),
      city          char(12),
      gender        char(1),
      pwd           char(4),
      primary key (email)
);
create table friends (
      email         char(25),
      femail        char(25),
      primary key (email,femail),
      foreign key (email) references users,
      foreign key (femail) references users
);

create table friend_requests (
      email         char(25),
      femail        char(25),
      checked       char(1),
      primary key (email,femail),
      foreign key (email) references users,
      foreign key (femail) references users
);

create table pages (
      pid           char(6),
      cdate         date,
      title         char(25),
      content       char(40),
      creator       char(25) not null,
      primary key (pid),
      foreign key (creator) references users
);
create table fans ( 
      email         char(25),
      pid           char(6),
      since         date, 
      primary key (email,pid), 
      foreign key (email) references users,
      foreign key (pid) references pages
);
create table status (
      email         char(25),
      sno           int,
      text          char(40),
      pdate         date,
      primary key (email,sno),
      foreign key (email) references users on delete cascade
);
create table comments (
      email         char(25),
      cno           int,
      sno           int not null,
      semail        char(25) not null,
      text          char(40),
      cdate         date,
      primary key (email,cno),
      foreign key (email) references users on delete cascade,
      foreign key (semail,sno) references status
);
create table messages (
      mid           int,
      mdate         date,
      text          char(40),
      sender        char(25),
      primary key (mid),
      foreign key (sender) references users
);
create table receives (
      mid           int,
      email         char(25),
      primary key (mid,email),
      foreign key (mid) references messages,
      foreign key (email) references users
);
