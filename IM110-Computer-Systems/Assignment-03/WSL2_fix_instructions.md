# Get Linux Kernel Trace Signatures in WSL2

> [!Important]
>
> This fix is required to get the kernel trace signatures for the full version of `trace` in WSL2, as the kernel source code is not installed by default.

In `trace-system-calls.cc` (line 354) this path does not exist in WSL2:

```cpp
static const string kKernelSourceCodeDirectory = "/usr/src/linux-source-3.13.0/linux-source-3.13.0";
```

**How to fix it:**

1. **Make sure you have the latest version of the GitLab starter code, as there are some fixes**
2. Find WSL2 kernel version: `uname -r`, note the number before "-microsoft-standard-WSL2", e.g. `5.15.167.4`
3. Go to https://github.com/microsoft/WSL2-Linux-Kernel/tags
4. Find the tag that contains the kernel version from step 2, e.g. `linux-msft-wsl-5.15.167.4`
5. Set env variable for the tag: `TAG_NAME=<tag-name>` and replace `<tag-name>` with the tag from step 4, e.g. `TAG_NAME=linux-msft-wsl-5.15.167.4`
6. Create and switch to directory to download the kernel source code: `mkdir -p ~/WSL2_kernel && cd ~/WSL2_kernel`
7. Download the kernel source code: `wget "https://github.com/microsoft/WSL2-Linux-Kernel/archive/refs/tags/${TAG_NAME}.tar.gz"` (~200MB)
8. Unzip the downloaded file (and delete the tar file afterwards): `tar -xzf ${TAG_NAME}.tar.gz && rm ${TAG_NAME}.tar.gz` (May take a few seconds)
9. Check the name of the unzipped directory: `ls -l` and note the name of the unzipped directory, e.g. `WSL2-Linux-Kernel-linux-msft-wsl-5.15.167.4`
10. Move the unzipped directory to `/usr/src/`: `sudo mv WSL2-Linux-Kernel-${TAG_NAME} /usr/src/`
11. Confirm the directory was moved correctly: `ls /usr/src/` - Check if the directory name appears in the list
12. Cleanup: Delete the temporary directory: `cd ~ && rm -rf ~/WSL2_kernel`
13. In `trace-system-calls.cc` (file from assignment, line 354) change the kernel source code path to the following: `static const string kKernelSourceCodeDirectory = "/usr/src/<KERNEL_NAME_HERE>"`.  
    It should look similar to this (**Adjust the kernel name, see step 9!**):

    ```cpp
    static const string kKernelSourceCodeDirectory = "/usr/src/WSL2-Linux-Kernel-linux-msft-wsl-5.15.167.4";
    ```

14. Navigate to the assignment directory
15. Make sure that no cached trace signarures exist: `rm -f .trace_signatures.txt`
16. Compile the code: `make`, if that doesn't work try `make CXX=g++`
