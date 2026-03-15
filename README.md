# FFmpeg Engine

A powerful Android application that brings FFmpeg's complete encoding capabilities to your mobile device. Convert, transcode, and process media files with an intuitive interface.

## Features

- **Full FFmpeg Integration**: Access all FFmpeg features directly from your Android device
- **Visual Command Builder**: Configure encoding parameters through an intuitive UI or use custom commands
- **Media Information**: View detailed stream information (video, audio, subtitles)
- **Preset Management**: Use built-in presets or create custom encoding presets
- **Encoding History**: Track all your encoding jobs with detailed logs
- **Modern UI/UX**: Material Design 3 with dark/light theme support

## Supported Operations

- Video transcoding (H.264, H.265, VP8, VP9, AV1)
- Audio transcoding (AAC, MP3, Opus, Vorbis, FLAC)
- Format conversion (MP4, MKV, WebM, AVI, MOV, etc.)
- Stream copying (re-muxing without re-encoding)
- Video filters (scale, crop, rotate, flip, etc.)
- Audio extraction and manipulation

## Screenshots

[Add screenshots here]

## Architecture

This project follows **Clean Architecture** principles with three main layers:

- **Presentation Layer**: Jetpack Compose UI with Material Design 3
- **Domain Layer**: Use cases and business logic
- **Data Layer**: Repositories and FFmpeg integration

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: Clean Architecture + MVVM
- **DI**: Hilt (Dagger)
- **FFmpeg**: FFmpeg-Kit Full-GPL
- **Navigation**: Compose Navigation
- **State Management**: StateFlow + ViewModel

## Project Structure

```
app/
├── src/main/
│   ├── java/com/ffmpeg/engine/
│   │   ├── data/                    # Data layer
│   │   │   ├── local/              # Local data sources
│   │   │   └── repository/         # Repository implementations
│   │   ├── di/                     # Dependency injection modules
│   │   ├── domain/                 # Domain layer
│   │   │   ├── model/              # Domain models
│   │   │   ├── repository/         # Repository interfaces
│   │   │   └── usecase/            # Use cases
│   │   └── presentation/           # Presentation layer
│   │       ├── navigation/         # Navigation components
│   │       ├── ui/                 # UI components
│   │       │   ├── components/     # Reusable UI components
│   │       │   ├── screens/        # Screen composables
│   │       │   └── theme/         # Theme and styling
│   │       └── viewmodel/         # ViewModels
│   └── res/                        # Android resources
├── build.gradle.kts                # App-level build config
└── proguard-rules.pro              # ProGuard rules
```

## Building the Project

### Prerequisites

- Android Studio Arctic Fox or newer
- JDK 17 or higher
- Android SDK 34

### Build Commands

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew test

# Clean build
./gradlew clean
```

## Installation

Download the APK from the [Releases](https://github.com/yourusername/ffmpeg-engine/releases) page and install it on your Android device.

### Requirements

- Android 8.0 (API 26) or higher
- ARM64-v8a or ARMv7 processor (most modern devices)

## FFmpeg-Kit

This app uses [FFmpeg-Kit](https://github.com/arthenica/ffmpeg-kit), a powerful FFmpeg build for Android, iOS, macOS, and Linux. FFmpeg-Kit brings the complete functionality of FFmpeg to mobile devices.

## Contributing

Contributions are welcome! Please read our [contributing guidelines](CONTRIBUTING.md) first.

## License

This project is licensed under the GPLv3 License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [FFmpeg](https://ffmpeg.org/) - The industry-standard multimedia framework
- [FFmpeg-Kit](https://github.com/arthenica/ffmpeg-kit) - FFmpeg for mobile platforms
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern Android UI toolkit
