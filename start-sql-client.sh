# start a SQL client.
# First parameter: database URL
# - server mode: likely "jdbc:hsqldb:hsql://localhost/openroberta-db"
# - embedded mode: likely "jdbc:hsqldb:file:./db-<serverversion>/openroberta-db"

serverVersion=$(java -cp ./lib/\* de.fhg.iais.roberta.main.ServerStarter -v)

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

java -jar "lib/hsqldb-2.3.2.jar" --driver org.hsqldb.jdbc.JDBCDriver --url "$1" --user orA --password Pid