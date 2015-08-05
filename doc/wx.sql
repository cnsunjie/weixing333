create table AdminUser
(
   Id                   int(11) not null AUTO_INCREMENT,
   Account              varchar(32) NOT NULL default '',
   Name                 varchar(32) NOT NULL default '',
   Mobile               varchar(30) NOT NULL default '',
   Area                 int(11) not null default 0,
   Roles                varchar(256) NOT NULL default '',
   Pwd                  varchar(32) NOT NULL default '',
   CreateTime           timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   primary key (Id),
   UNIQUE KEY (`Account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into AdminUser(Account, Name, Mobile, Area, Roles, Pwd) values('admin', 'admin', '134', 1, '1,2,3,4,', 'admin');

create table Shop
(
   Id                   int(11) not null AUTO_INCREMENT,
   Name                 varchar(100) NOT NULL default '',
   Contact              varchar(32) NOT NULL default '',
   Tel                  varchar(30) NOT NULL default '',
   Area                 int(11) not null default 0,
   Category             int(11) not null default 0,
   CreateTime           timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   primary key (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;