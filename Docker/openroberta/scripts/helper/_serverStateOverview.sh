NGINX_LOG='/data/openroberta-lab/nginx/logs/access.log'
AUTORESTART_LOG='/data/openroberta-lab/logs/autorestart.txt'

(   cd /proc
    for pid in [0-9]*
    do
        echo "$(ls /proc/${pid}/fd/ | wc -l) ${pid}"
    done \
    | sort -rn -k1 | head -5 \
    | while read -r fdcount pid
      do
        command=$(ps -o cmd -p "${pid}" -hc)
        printf "pid: %5d fd: %5d cmd: %s\n" "${pid}" "${fdcount}" "${command}"
        case "${command}" in
          *java*) pstree ${pid} ;;
          *)      ;;
        esac
      done \
)

echo
if [ -e ${NGINX_LOG} ]
then
    echo '110er'
    cat ${NGINX_LOG} | fgrep '110: Connection timed out'
else
    echo "no 110er, because file ${NGINX_LOG} not found"
fi

echo
if [ -e ${AUTORESTART_LOG} ]
then
    tail -15 ${AUTORESTART_LOG}
else
    echo "no auto restart info, because file ${AUTORESTART_LOG} not found"
fi
