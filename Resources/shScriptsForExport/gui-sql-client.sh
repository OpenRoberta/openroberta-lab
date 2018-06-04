#!/bin/bash
# start a SQL client after exporting.

serverVersionForDb=$(java -cp ./lib/\* de.fhg.iais.roberta.main.ServerStarter --version-for-db)
hsqldbVersion='2.4.0'
hsqldbJar="lib/hsqldb-${hsqldbVersion}.jar"

echo 'gui-sql-client.sh <DB-URL>'
echo
echo "the version number used for accessing the db directory is: $serverVersionForDb"
echo '  - server mode: likely "jdbc:hsqldb:hsql://localhost/openroberta-db"'
echo "  - embedded mode: likely \"jdbc:hsqldb:file:./db-$serverVersionForDb/openroberta-db;ifexists=true\""

case "$1" in
  '') echo 'DB-URL is missing - exit 1'
	  exit 1 ;;
  *)  : ;;
esac

if [ -e ${hsqldbJar} ]
then
  echo "using version ${hsqldbVersion} of hsqldb"
else
  echo "version ${hsqldbVersion} of hsqldb does not exist - exit 12"
  exit 12
fi

java -jar "${hsqldbJar}" --driver org.hsqldb.jdbc.JDBCDriver --url "$1" --user orA --password Pid