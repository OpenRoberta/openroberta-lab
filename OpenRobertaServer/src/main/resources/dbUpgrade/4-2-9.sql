SET DATABASE TRANSACTION CONTROL MVCC;
SET DATABASE SQL UNIQUE NULLS FALSE;
commit;

-- remove all account duplications from the data base. Obviously dangerous.
delete
from USER
where ID in (
    select u2.ID
    from USER u1,
         USER u2
    where u1.USERGROUP_ID is null
      and u2.USERGROUP_ID is null
      and u1.ACCOUNT = u2.ACCOUNT
      and u1.ID < u2.ID
);
commit;

create index userEmailIdx on USER (EMAIL);
commit;