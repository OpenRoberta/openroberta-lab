# essential configuration variables. Be very carefull!

INAME=''                # the (short, alphanumeric) name of this installation
BASE_VERSION='3'        # the version of the docker base image, FROM which all server images are derived from
DATABASE_SERVER_PORT='' # one database server for all jetty server. It is listening on this port. Usually this is '9001'
DOCKER_NETWORK_NAME=''  # the network for cooperation between jetty and database server. Usually this is 'ora-net'

SERVERS=''              # these servers can run (used in start-all and stop-all), e.g. 'test dev dev4'
AUTODEPLOY=''           # these servers are deployed when new commits hit the git repo. See auto-deploy and cron, e.g. 'dev dev4'
DATABASES=''            # these databases are served by the database server, e.g. 'test dev dev4'

# only needed, if the alive function is used. This function can send mail if problems are detected
ALIVE_ACTIVE=true                                               # set to false if not needed
ALIVE_MAIL_SMTP_SERVER='smtps.iais.fraunhofer.de'               # smtp server
ALIVE_MAIL_SMTP_PORT='25'                                       # smtp port
ALIVE_MAIL_SENDER='lab-alive@open-roberta.org '                 # the name for the sender of the mail
ALIVE_MAIL_RECEIVER=( 'name-1@address-1' 'name-2@address-2' )   # list of receivers for the mail, white-space separated