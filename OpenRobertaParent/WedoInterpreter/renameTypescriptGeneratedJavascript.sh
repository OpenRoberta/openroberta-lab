#!/bin/bash

BASE=$(pwd)
OUTDIR="$BASE/jsGenerated/node_modules"

if [ -d "$OUTDIR" ]
then
	echo "tool for the renaming of generated javascript started and is working in $OUTDIR"
	cd "$OUTDIR"
	if [ -f "runStackMachineJson.js" ]
	then
		echo "new generated javascript files found - this is ok: Renaming starts"
		rm interpreter.*.js
		set *.js
		for F do mv $F interpreter.$F; done
		echo "renaming successfully done"
	else
		echo "no new generated javascript files found - this is ok: nothing to do"
	fi
else
	echo "$OUTDIR is no valid node_modules directory - error: nothing done"
fi