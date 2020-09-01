#!/bin/bash

exit 0 # not ready

# this is a standalone script!

# Hilfsfunktion zum senden an ${RECV} über den smtp server ${SMTP}
function ms {
  sleep 1; echo "helo ${SMTP}"
  sleep 1; echo "mail from: lab-alive@open-roberta.org"
  sleep 1; echo "rcpt to: ${RECV}"
  sleep 1; echo "data"
  sleep 1; echo "From: lab-alive@open-roberta.org"
  sleep 1; echo "To: ${RECV}"
  sleep 1; echo "Hello at $(date)"
  sleep 1; echo "."
  sleep 1; echo "quit"
}

# interne Mail über smtps.iais.fraunhofer.de
SMTP=smtps.iais.fraunhofer.de; \
RECV=reinhard.budde@iais.fraunhofer.de; \
ms | nc ${SMTP} 25

# externe Mail direkt über postfix auf me-roblab-prod
SMTP=me-roblab-prod; \
RECV=reinhard.w.budde@gmail.com; \
ms | nc ${SMTP} 25


echo "
send mail. Use the environment variables as shown below:

export SMTP=smtps.iais.fraunhofer.de
export SENDER=lab@open-roberta.org
export RECEIVERS='reinhard.budde@iais.fraunhofer.de reinhard.budde@iais.fraunhofer.de>'
export SUBJECT='Hello!'
export TEXT='This is mail for you

Regards
Reinhard'
export TRANSFER_TOOL=curl or nc
"

SCRIPT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd .. && pwd )"
CWD=$(pwd)
echo "script directory is ${SCRIPT}, working directory is ${CWD}"
source ${SCRIPT}/helper/__defs.sh

SLEEP='sleep 1'

isDefined SMTP
isDefined SENDER
isDefined RECEIVERS
isDefined SUBJECT
isDefined TEXT

function netcatMail {
  ${SLEEP}; echo "helo ${SMTP}"
  ${SLEEP}; echo "mail from: ${SENDER}"
  ${SLEEP}; echo "rcpt to: ${RECEIVERS}"
  ${SLEEP}; echo "data"
  ${SLEEP}; echo "From: ${SENDER}"
  ${SLEEP}; echo "To: ${RECEIVERS}"
  ${SLEEP}; echo "Subject: ${SUBJECT}"
  ${SLEEP}; echo "Date: $(date)"
  ${SLEEP}; echo "${TEXT}"
  ${SLEEP}; echo "."
  ${SLEEP}; echo "quit"
}

function curlMail { 
  curl smtp://${SMTP} --mail-from ${SENDER} --mail-rcpt "${RECEIVERS}" <<.EOF
From: ${SENDER}
To: ${RECEIVERS}
Subject: ${SUBJECT}
Date: $(date)
${TEXT}
.EOF
}

if [[ "${TRANSFER_TOOL}" = 'curl' ]]
then
  curlMail
elif [[ "${TRANSFER_TOOL}" = 'nc' ]]
then
  netcatMail | nc ${SMTP} 25
else
  echo "invalid transfer tool: ${TRANSFER_TOOL}"
  exit 12
fi
