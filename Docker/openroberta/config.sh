# essential configuration variables. Be very carefull!

INAME=i1 # the (short, alphanumeric) name of this installation
DATABASE_SERVER_PORT=9001 # one database server for all jetty server. It is listening on this port
DOCKER_NETWORK_NAME=ora-net # the network for cooperation between jetty and database server

SERVERS='test dev1' # these servers can run (see startAll)
AUTODEPLOY='test'   # these servers are deployed when new commits hit the git repo (see autoDeploy and cron)
DATABASES='test dev dev1' # these databases are served by the database server
