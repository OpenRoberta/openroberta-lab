ssh root@10.116.20.143 mkdir -p /home/roberta/lib
scp ../../OpenRobertaRuntime/target/OpenRobertaRuntime-0.0.1-SNAPSHOT.jar ../../OpenRobertaShared/target/OpenRobertaShared-0.0.1-SNAPSHOT.jar ../../EV3Menu/lib/json-20140107.jar root@10.116.20.143:/home/roberta/lib
read -n1 -r -p "Press any key to continue..."