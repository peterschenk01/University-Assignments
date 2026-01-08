## Setup with OSX

## Issues while using standard docker image with platform linux/amd64

You will probably encounter the following issue or sth similar when developing in a docker container on OSX with an M Chip:

```bash
./trace ls
syscall(-1) = -1
```

Where `-1` indicates an error occurred. The `syscall` to `ptrace` failed with an input/output error, which can be verified using `perror`.

While visiting several forums and github discussions(most of them are linked [here](https://forums.docker.com/t/sys-ptrace-capability-for-linux-amd64/138482/2)) and trying out several workarounds, i found out
that this issue can be solved using colima.

## Workaround using colima

1. Install colima

The architecture we are using for colima needs qemu to be installed. You can install colima and qemu using homebrew:

```bash
$ brew install colima
$ brew install qemu
```

2. Start colima with qemu

```bash
$ colima start --cpu 2 --memory 4 --arch x86_64
```

3. Build the image

```bash
$ docker compose build
```

4. Run the image

```bash
$ docker compose up
```

5. Connect to container via cli (or even better use the vscode remote container extension)

```bash
$ docker exec -it cs110_assignment03 bash
$ ./trace ls
```

---
You need to do the following steps to correctly setup `linux-source` for `trace-system-calls.cc`
1. Unpack the linux-source tarball inside `/usr/src` (you have to check which version you are using, the following example uses 5.4.0):

```bash
$ cd /usr/src/linux-source-5.4.0/
$ tar -xvf linux-source-5.4.0.tar.bz2
```

Note: This can take a while, so be patient.

2. Update the path to the linux-source in trace-system-calls.cc
```cpp
static const string kKernelSourceCodeDirectory = "/usr/src/linux-source-5.4.0/linux-source-5.4.0";
```

---

You can stop colima using:

```bash
$ colima stop
```
