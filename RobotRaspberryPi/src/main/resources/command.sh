#!/bin/bash

file=$HOME/my.pid
pid=$(<"$file")


if [[ $pid -gt 0 ]]
then 
  while $(kill $pid 2>/dev/null); do
    sleep 1
  done
fi 

program_name=$1
python3 $program_name &> out.log &
echo $!>my.pid 

exit 0

