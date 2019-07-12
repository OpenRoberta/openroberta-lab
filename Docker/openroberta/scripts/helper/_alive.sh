#!/bin/bash

# usually called by cron: check availability of an instance of openroberta-lab
# if requested or if an error is detected, send mail to a list of ops
# NOTE, THAT THE df OUTPUT IS FROM THE SYSTEM, THAT RUNS THIS COMMAND, NOT FROM THE SERVER CHECKED FOR LIVENESS

# LAB_URL: lab-url to check for liveness, e.g. https://lab.open-roberta.org
# REPORT_ALWAYS: true: send mail always, false: only if problems detected

SLEEP='sleep 1'
SMTP=smtps.iais.fraunhofer.de
PORT=25
SENDER=lab-alive@open-roberta.org
RECEIVER=( beate.jost@iais.fraunhofer.de reinhard.budde@iais.fraunhofer.de )

function generate_output {
  echo "helo $SMTP"
  $SLEEP
  
  echo "mail from: ${SENDER}"
  $SLEEP
  for mailadress in "${RECEIVER[@]}"
  do
    echo "rcpt to: ${mailadress}"
    $SLEEP
  done
  echo "data"
  $SLEEP
  echo "From: ${SENDER}"
  $SLEEP
  ASSEMBLE_TO=''
  for mailadress in "${RECEIVER[@]}"
  do
    ASSEMBLE_TO="$ASSEMBLE_TO$mailadress,"
  done
  echo "To: ${ASSEMBLE_TO}"
  $SLEEP
  if [ "$ALIVE" = true ]
  then
    echo "Subject: Server $LAB_URL $REPORT_MESSAGE >>> Server ok <<<"
  else
    echo "Subject: Server $LAB_URL $REPORT_MESSAGE >>> Problem!!! <<<"
    echo "Please check $LAB_URL !!!"
    echo " "
  fi
  echo " "
  echo "File system status of host '$HOSTNAME'"
  echo " "
  echo "$OUTPUT"
  echo " "
  $SLEEP
  echo "."
  $SLEEP
  echo "quit"
}

# check if server is alive
url=$(curl $LAB_URL/alive)
if [ $? -eq 0 ]
then
    ALIVE=true
else
    ALIVE=false
fi

OUTPUT="$(df -h)"
HOSTNAME="$(hostname)"

echo "*** checking liveness of $LAB_URL as seen by host $HOSTNAME with result alive==$ALIVE ***"

if [[ "$ALIVE" = false || "$REPORT_ALWAYS" = true ]]
then
    generate_output | sudo nc $SMTP $PORT
fi
