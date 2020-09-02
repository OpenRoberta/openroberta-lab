#!/bin/bash

isServerNameValid ${SERVER_NAME}
if [[ "${MONTH}" == '' ]]
then
    MONTH=$(date +'%m')
    MONTH=$((10#$MONTH))
    MONTH=$(($MONTH-1))
    if [[ ${MONTH} < 1 ]]
    then
        MONTH=12
    fi
else
    MONTH=$((10#$MONTH))
fi
MONTH=$(printf "%02d" ${MONTH})
echo "generating the monthly statistics for month ${MONTH}"
YEAR=$(date +'%Y')
STATISTICS_DIR=${SERVER_DIR}/${SERVER_NAME}/admin/logging/statistics-${YEAR}
isDirectoryValid ${STATISTICS_DIR}
REPORT_DIR=${SERVER_DIR}/${SERVER_NAME}/admin/reports-${YEAR}
mkdir -p ${REPORT_DIR}

FILE=''
cd ${STATISTICS_DIR}
set ${MONTH}*
for F do
    if [[ "${FILE}" != '' ]]
    then
        echo "got more than one file for generating reports. Ignoring file: ${FILE}"
    fi
    FILE=${F}
done
if [ -r "${FILE}" ]
then
    echo "statistics generated from file ${FILE} are written to directory ${REPORT_DIR}"
    ${PYTHON} ${SCRIPT_REPORTING}/workflows-monthly.py ${STATISTICS_DIR} ${FILE} ${REPORT_DIR} textResults-${MONTH}.txt
else
    echo "file ${FILE} is not readable. No statistics could be generated"
fi

