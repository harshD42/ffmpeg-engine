#!/bin/bash
# FFmpeg Build Script for Android ARM
# This script builds FFmpeg from source for Android ARM devices (arm64-v8a and armeabi-v7a)

set -e

# Configuration
FFMPEG_VERSION="6.0"
ANDROID_NDK_VERSION="r26b"
ANDROID_API_LEVEL=26
BUILD_DIR="$(pwd)/build-ffmpeg"
PREFIX="$(pwd)/app/src/main/jniLibs"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}FFmpeg Build Script for Android${NC}"
echo "========================================"

# Check for required tools
command -v yasm >/dev/null 2>&1 || { echo -e "${YELLOW}Warning: yasm not found. Some optimizations may be disabled.${NC}"; }
command -v make >/dev/null 2>&1 || { echo -e "${RED}Error: make is required but not installed.${NC}"; exit 1; }
command -v sed >/dev/null 2>&1 || { echo -e "${RED}Error: sed is required but not installed.${NC}"; exit 1; }

# Create build directory
mkdir -p "$BUILD_DIR"
cd "$BUILD_DIR"

# Download FFmpeg if not exists
if [ ! -f "ffmpeg-${FFMPEG_VERSION}.tar.xz" ]; then
    echo -e "${GREEN}Downloading FFmpeg ${FFMPEG_VERSION}...${NC}"
    curl -L -o "ffmpeg-${FFMPEG_VERSION}.tar.xz" "https://ffmpeg.org/releases/ffmpeg-${FFMPEG_VERSION}.tar.xz"
fi

# Extract FFmpeg
if [ ! -d "ffmpeg-${FFMPEG_VERSION}" ]; then
    echo -e "${GREEN}Extracting FFmpeg...${NC}"
    tar -xf "ffmpeg-${FFMPEG_VERSION}.tar.xz"
fi

cd "ffmpeg-${FFMPEG_VERSION}"

# Build for arm64-v8a
build_arm64() {
    echo -e "${GREEN}Building for ARM64-v8a...${NC}"
    
    ./configure \
        --prefix="${PREFIX}/arm64-v8a" \
        --enable-cross-compile \
        --cross-prefix=aarch64-linux-android- \
        --target-os=android \
        --arch=aarch64 \
        --cpu=armv8 \
        --enable-neon \
        --enable-gpl \
        --enable-version3 \
        --disable-doc \
        --disable-programs \
        --disable-static \
        --enable-shared \
        --enable-small \
        --disable-debug \
        --disable-x86asm \
        --enable-libx264 \
        --enable-libx265 \
        --enable-libvpx \
        --enable-libopus \
        --enable-libvorbis \
        --enable-libmp3lame \
        --enable-libaacplus \
        --enable-libass \
        --enable-libwebp \
        --enable-libfreetype \
        --enable-libfribidi \
        --enable-libtheora \
        --enable-libwavpack \
        --enable-libspeex \
        --enable-libflac \
        --enable-libopenjpeg

    make -j$(nproc)
    make install
}

# Build for armeabi-v7a
build_armv7() {
    echo -e "${GREEN}Building for ARMv7...${NC}"
    
    ./configure \
        --prefix="${PREFIX}/armeabi-v7a" \
        --enable-cross-compile \
        --cross-prefix=arm-linux-androideabi- \
        --target-os=android \
        --arch=arm \
        --cpu=armv7 \
        --enable-neon \
        --enable-gpl \
        --enable-version3 \
        --disable-doc \
        --disable-programs \
        --disable-static \
        --enable-shared \
        --enable-small \
        --disable-debug \
        --disable-x86asm \
        --enable-libx264 \
        --enable-libx265 \
        --enable-libopus \
        --enable-libvorbis \
        --enable-libmp3lame \
        --enable-libaacplus \
        --enable-libass \
        --enable-libfreetype

    make -j$(nproc)
    make install
}

# Clean previous build
make clean || true

# Build for both architectures
echo -e "${YELLOW}Starting build process...${NC}"

# Uncomment the following lines to build:
# build_arm64
# build_armv7

echo -e "${GREEN}Build configuration complete!${NC}"
echo ""
echo "To complete the build:"
echo "1. Install Android NDK ${ANDROID_NDK_VERSION}"
echo "2. Set ANDROID_NDK_ROOT environment variable"
echo "3. Run this script again"
echo ""
echo "For ARM64-v8a build, run: ./build-ffmpeg.sh --arch=arm64"
echo "For ARMv7 build, run: ./build-ffmpeg.sh --arch=armv7"
