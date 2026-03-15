FFmpeg binaries for armeabi-v7a
========================

To build FFmpeg from source for Android:

1. Install MSYS2 from https://www.msys2.org/
2. Install required tools: pacman -S make yasm sed
3. Run: cd scripts && bash build-ffmpeg.sh --arch=armeabi-v7a

Or download pre-built binaries from:
- https://github.com/nicholasflamy/ffmpeg-kit-android
- https://github.com/arthenica/ffmpeg-kit/releases

Required binaries:
- libffmpeg.so (main library)
- libffprobe.so (optional, for media info)
