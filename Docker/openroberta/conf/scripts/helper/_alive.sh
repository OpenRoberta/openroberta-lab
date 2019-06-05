#!/bin/bash

# TODO: make it more general, refactor
# to be called by cron: check availability of https://lab.open-roberta.org
# if requested or if an error is detected, send mail to Beate & Reinhard
# NOTE, THAT THE df OUTPUT IS FROM THE SYSTEM, THAT RUNS THIS COMMAND, NOT FROM THE SERVER CHECKED FOR LIVENESS

# LAB_URL: lab-url to check for liveness, e.g. lab.open-roberta.org
# REPORT_ALWAYS: false: only if error detected, otherwise: send mail always

function generate_output {
  echo "helo localhost"
  sleep 1
  echo "mail from: lab-alive@open-roberta.org"
  sleep 1
  echo "rcpt to: beate.jost@iais.fraunhofer.de"
  echo "rcpt to: reinhard.budde@iais.fraunhofer.de"
  sleep 1
  echo "data"
  sleep 1
  echo "From: lab-alive@open-roberta.org"
  sleep 1
  echo "To: beate.jost@iais.fraunhofer.de"
  sleep 1
  echo "To: reinhard.budde@iais.fraunhofer.de"
  sleep 1
  if [ "$ALIVE" = true ]
  then
    echo "Subject: $LAB_URL: >>> Server ok <<<"
  else
    echo "Subject: $LAB_URL: >>> Problem!!! <<<"
    echo "Please check $LAB_URL !!!"
    echo " "
  fi
  echo " "
  echo "File system status of host '$HOSTNAME'"
  echo " "
  echo "$OUTPUT"
  echo " "
  sleep 1
  echo "."
  sleep 1
  echo "quit"
}

# check if server is alive
url=$(curl https://$LAB_URL/alive)
if [ $? -eq 0 ]
then
    ALIVE=true
else
    ALIVE=false
fi
OUTPUT="$(df -h)"
HOSTNAME="$(hostname)"

if [[ "$ALIVE" = false || "$REPORT_ALWAYS" = true ]]
then
    generate_output | sudo nc smtps.iais.fraunhofer.de 25
fi
