(   cd /proc
    for pid in [0-9]*
    do
        echo "$(ls /proc/$pid/fd/ | wc -l) $pid"
    done \
    | sort -rn -k1 | head -5 \
    | while read -r fdcount pid
      do
        command=$(ps -o cmd -p "$pid" -hc)
        printf "pid: %5d fd: %5d cmd: %s\n" "$pid" "$fdcount" "$command"
        case "$command" in
          *java*) pstree $pid ;;
          *)      ;;
        esac
      done \
)
echo
echo '110er'
cat /data/log/nginx/error.log | fgrep '110: Connection timed out'
echo
tail -15 /data/openroberta-lab/logs/autorestart.txt
