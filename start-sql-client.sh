#!/bin/bash

# start a SQL client.
# First parameter: database URL
# - server mode: likely "jdbc:hsqldb:hsql://localhost/openroberta-db"
# - embedded mode: likely "jdbc:hsqldb:file:./db-<serverversion>/openroberta-db"

serverVersion=$(java -cp ./lib/\* de.fhg.iais.roberta.main.ServerStarter -v)
hsqldbVersion='2.3.3'
hsqldbJar="lib/hsqldb-${hsqldbVersion}.jar"

echo 'start-sql-client.sh <DB-URL>'
echo '  - server mode: likely "jdbc:hsqldb:hsql://localhost/openroberta-db"'
echo "  - embedded mode: likely \"jdbc:hsqldb:file:./db-$serverVersion/openroberta-db\""
echo
echo "the version of this installation is: $serverVersion"

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