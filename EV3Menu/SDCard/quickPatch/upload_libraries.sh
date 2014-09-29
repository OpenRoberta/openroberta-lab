ssh root@10.0.1.1 mkdir -p /home/roberta/lib
scp ../../../OpenRobertaRuntime/target/OpenRobertaRuntime-0.0.1-SNAPSHOT.jar ../../../OpenRobertaShared/target/OpenRobertaShared-0.0.1-SNAPSHOT.jar root@10.0.1.1:/home/roberta/lib
read -n1 -r -p "Press any key to continue..."