ssh root@10.0.1.1 mkdir -p /home/roberta/lib
scp ../OpenRobertaRuntime/target/OpenRobertaRuntime-1.0.0-SNAPSHOT.jar ../OpenRobertaShared/target/OpenRobertaShared-1.0.0-SNAPSHOT.jar lib/json-20140107.jar root@10.0.1.1:/home/roberta/lib
read -n1 -r -p "Press any key to continue..."