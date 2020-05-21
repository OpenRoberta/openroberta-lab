# essential configuration variables. Be very carefull!

INAME=''                # the (short, alphanumeric) name of this installation
DATABASE_SERVER_PORT='' # one database server for all jetty server. It is listening on this port. Usually this is '9001'
DOCKER_NETWORK_NAME=''  # the network for cooperation between jetty and database server. Usually this is 'ora-net'

SERVERS=''              # these servers can run (used in start-all and stop-all), e.g. 'test dev dev4'
SERVERSXMX='-Xmx1G'     # maximum of heap size for the jetty server. CURRENTLY UNUSED. Production server needs 1G, raspi 1G or 500M
DATABASES=''            # these databases are served by the database server, e.g. 'test dev dev4'
DATABASEXMX='-Xmx4G'    # maximum of heap size for the database server. Production server needs 4G, raspi 1G or 500M
AUTODEPLOY=''           # these servers are deployed when new commits hit the git repo. See auto-deploy and cron, e.g. 'dev dev4'

# for operating purposes
ALIVE_ACTIVE=true                                               # set to false if not needed
  ALIVE_MAIL_SMTP_SERVER='smtps.iais.fraunhofer.de'               # smtp server
  ALIVE_MAIL_SMTP_PORT='25'                                       # smtp port
  ALIVE_MAIL_SENDER='lab-alive@open-roberta.org '                 # the name for the sender of the mail
  ALIVE_MAIL_RECEIVER=( 'name-1@address-1' 'name-2@address-2' )   # list of receivers for the mail, white-space separated

PYTHON=/usr/bin/python3 # for log file evaluation