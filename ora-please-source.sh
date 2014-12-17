#!/bin/bash

ora.sh --java
if [[ $? > 0 ]]; then
  if [[ "$JAVA_HOME" != "" ]]; then
    echo "trying to prefix your path with your JAVA_HOME to fix your path problem ..."
    PATH="$JAVA_HOME/bin":$PATH
    ora.sh --java
    if [[ $? > 0 ]]; then
      echo "... but this was NOT successful. You have to fix for path problem by yourself"
    else
      echo "... and this was successful"
    fi
  else
    echo "JAVA_HOME is not set. You have to fix for path problem by yourself"
  fi
fi
__commands_ora_sh=`ora.sh --help | tr [:blank:] '\n' | grep "^--"`

function __complete_ora_sh {
  COMPREPLY=()
  local cur=${COMP_WORDS[COMP_CWORD]}

  COMPREPLY=( $( compgen -f -W "$__commands_ora_sh" -- "$cur" ) )
  return 0
}

complete -F __complete_ora_sh ora.sh
