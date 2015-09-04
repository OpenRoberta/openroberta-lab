#!/bin/bash

#.sh --java

__commands_ora_sh=`ora.sh --help | tr [:blank:] '\n' | grep "^--"`

function __complete_ora_sh {
  COMPREPLY=()
  local cur=${COMP_WORDS[COMP_CWORD]}

  COMPREPLY=( $( compgen -f -W "$__commands_ora_sh" -- "$cur" ) )
  return 0
}

complete -F __complete_ora_sh ora.sh
