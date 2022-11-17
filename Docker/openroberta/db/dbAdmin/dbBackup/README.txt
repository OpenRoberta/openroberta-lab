the directory /data/dbAdmin is the physical counterpart
of /opt/dbAdmin declared as VOLUME in the
docker database server image openroberta/db_server:<hsqldb-version>

/data/dbAdmin (resp. /opt/dbAdmin) contains
* the log file 'ora-db.log' and
* the directory 'dbBackups' with database backups (:-)