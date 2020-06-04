#!/bin/bash

exit 0 # not ready

echo 'create statistics from the Open Roberta Lab and mail or log them'
echo 'sendStats.sh -o console -d 2019-06'
echo ''
echo 'parameter:'
echo ' -o [mail|console]              # default: mail'
echo ' -d <JJJJ-MM>                   # default: last month'

SLEEP='sleep 1'
SMTP=smtps.iais.fraunhofer.de
PORT=25
SENDER=lab-stats@open-roberta.org
RECEIVER=( beate.jost@iais.fraunhofer.de reinhard.budde@iais.fraunhofer.de thorsten.leimbach@iais.fraunhofer.de )

OUTPUT="mail" # default: send mail
YEAR_MONTH=$(date --date="-1 month" +"%Y-%m") # default, e.g. 2019-12

while [ "$1" != "" ]; do
    case $1 in
        -o ) OUTPUT=$2 ;;
        -d ) YEAR_MONTH=$2 ;;
        *)   break ;;
    esac
    shift; shift
done

ARR_YM=(${YEAR_MONTH//-/ })
YEAR=${ARR_YM[0]}
MONTH=$(echo ${ARR_YM[1]} | sed 's/^0*//')

function generateStats {
  STATS_USER='$(./admin.sh --sql "SELECT count(*) FROM user WHERE year(created)=${YEAR} AND month(created)=${MONTH}" 2>/dev/null | sed -n -e "s/^.*>>> //p")'
  echo "Number of new accounts: ${STATS_USER}"

function generateMail {
  echo "helo localhost"
  ${SLEEP}
  echo "mail from: ${SENDER}"
  ${SLEEP}
  for mailadress in "${RECEIVER[@]}"
  do
    echo "rcpt to: ${mailadress}"
    ${SLEEP}
  done
  echo "data"
  ${SLEEP}
  echo "From: ${SENDER}"
  ${SLEEP}
  for mailadress in "${RECEIVER[@]}"
  do
    var=${var}${mailadress},
  done
  echo "To: ${var}"
  ${SLEEP}
  echo "Subject: Statistics of the OpenRoberta Lab from month ${MONTH} ${YEAR}"
  echo " "
  generateStats
  echo "."
  ${SLEEP}
  echo "quit"
}

function generateStats {
  STATS_USER='$(./admin.sh --sql "SELECT count(*) FROM user WHERE year(created)=${YEAR} AND month(created)=${MONTH}" 2>/dev/null | sed -n -e "s/^.*>>> //p")'
  echo "Number of new accounts: ${STATS_USER}"

case "${OUTPUT}" in
   mail)      generateMail | sudo nc ${SMTP} ${PORT} ;;
   console)   generateStats ;;
esac

