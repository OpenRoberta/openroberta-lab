#!/bin/bash

nvidia-smi

python3 /home/cyberbotics/server/session_server.py /home/cyberbotics/simulation/serverConfig/session/$CONFIG.json &
python3 /home/cyberbotics/server/simulation_server.py /home/cyberbotics/simulation/serverConfig/simulation/$CONFIG.json &

tail -f /home/cyberbotics/server/log/output.log
