FROM --platform=linux/amd64 ubuntu:22.04

# Install necessary packages
RUN apt-get update && apt-get install -y \
    build-essential \
    gcc \
    g++ \
    make \
    gdb \
    valgrind \
    vim \
    nano \
    strace \
    ltrace \
    linux-source \
    git \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy project files
COPY . /app/

# Default command when the container starts
CMD ["bash"]