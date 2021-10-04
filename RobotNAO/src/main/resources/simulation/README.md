# NAO Simulation
## Development Notes
## Docker

> Webots currently only support nvidia GPUs, test if you have setup you GPU correclty with `nvidia-smi`
> To use the GPU inside docker make sure, you have `nvidia-docker` setup (see [webots instructions](https://www.cyberbotics.com/doc/guide/installation-procedure?version=develop#run-webots-in-docker-with-gui) and [instructions by nvidia](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/install-guide.html) ) 

### Settings up a local webots-server
**Build Image**
```bash
docker build -t webots -f Docker/local/Dockerfile .
```

**Run Image (with GPU support)**
```bash
docker run --gpus=all -p2000-2100:2000-2100 -it webots
```

**Run Image (without GPU support)**
```bash
docker run -p2000-2100:2000-2100 -it webots
```

### Developing a local webots-server
**Build Image**
```bash
docker build -t webots -f Docker/local/Dockerfile .
```

**Run Image (with GPU support)**
```bash
docker run --gpus=all -p2000-2100:2000-2100 -v .:/home/cyberbotics/simulation -it webots /bin/bash
```

**Run Image (without GPU support)**
```bash
docker run -p2000-2100:2000-2100 -v .:/home/cyberbotics/simulation -it webots /bin/bash
```