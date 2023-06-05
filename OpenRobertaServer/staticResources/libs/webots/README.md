# Webots Libraries

## Prerequisites

1. Install rollup
```bash
npm install -g rollup
```

2. Clone webots
```bash
git clone https://github.com/cyberbotics/webots.git
```

## Get libraries
```bash
cd <target-directory> #openroberta-lab/OpenRobertaServer/staticResources/js/libs/webots
./get-webots.sh <webots-repo> #where you installed webots
```

> If you want a different version of webots dependencies on web, edit WEBOTS_VERSION in `get-webots.sh`.