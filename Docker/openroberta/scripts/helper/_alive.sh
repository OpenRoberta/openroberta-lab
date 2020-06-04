#!/bin/bash

# usually called by cron: check availability of an instance of openroberta-lab
# if requested or if an error is detected, send mail to a list of ops
# NOTE, THAT THE df OUTPUT IS FROM THE SYSTEM, THAT RUNS THIS COMMAND, NOT FROM THE SERVER CHECKED FOR LIVENESS

# LAB_URL: lab-url to check for liveness, e.g. https://lab.open-roberta.org
# REPORT_ALWAYS: true: send mail always, false: only if problems detected
# REPORT_MESSAGE: added to the subject: line

SLEEP='sleep 1'

isDefined ALIVE_MAIL_SMTP_SERVER
isDefined ALIVE_MAIL_SMTP_PORT
isDefined ALIVE_MAIL_SENDER
isDefined ALIVE_MAIL_RECEIVER
isDefined REPORT_ALWAYS
isDefined REPORT_MESSAGE

function generate_output {
  echo "helo ${ALIVE_MAIL_SMTP_SERVER}"
  ${SLEEP}
  
  echo "mail from: ${ALIVE_MAIL_SENDER}"
  ${SLEEP}
  for mailadress in "${ALIVE_MAIL_RECEIVER[@]}"
  do
    echo "rcpt to: ${mailadress}"
    ${SLEEP}
  done
  echo "data"
  ${SLEEP}
  echo "From: ${ALIVE_MAIL_SENDER}"
  ${SLEEP}
  ASSEMBLE_TO=''
  for mailadress in "${ALIVE_MAIL_RECEIVER[@]}"
  do
    ASSEMBLE_TO="${ASSEMBLE_TO}${mailadress},"
  done
  echo "To: ${ASSEMBLE_TO}"
  ${SLEEP}
  if [ "${ALIVE}" = true ]
  then
    echo "Subject: Server ${LAB_URL} ${REPORT_MESSAGE} >>> Server ok <<<"
  else
    echo "Subject: Server ${LAB_URL} ${REPORT_MESSAGE} >>> Problem!!! <<<"
    echo "Please check ${LAB_URL} !!!"
    echo " "
  fi
  echo " "
  echo "File system status of host '${HOSTNAME}'"
  echo " "
  echo "${OUTPUT}"
  echo " "
  ${SLEEP}
  echo "."
  ${SLEEP}
  echo "quit"
}

# check if server is alive
url=$(curl ${LAB_URL}/alive)
if [ $? -eq 0 ]
then
    ALIVE=true
else
    ALIVE=false
fi

OUTPUT="$(df -h)"
HOSTNAME="$(hostname)"

echo '******************** '"${DATE}: checking liveness of ${LAB_URL}, host ${HOSTNAME} with result alive==${ALIVE}"' ********************'

if [[ "${ALIVE}" = false || "${REPORT_ALWAYS}" = true ]]
then
    generate_output | sudo nc ${ALIVE_MAIL_SMTP_SERVER} ${ALIVE_MAIL_SMTP_PORT} >/tmp/smtpOutputForDebugging
fi
