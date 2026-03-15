# Contributing to FFmpeg Engine

Thank you for your interest in contributing to FFmpeg Engine!

## Development Process

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Run tests (`./gradlew test`)
5. Commit your changes (`git commit -m 'Add amazing feature'`)
6. Push to the branch (`git push origin feature/amazing-feature`)
7. Open a Pull Request

## Coding Standards

- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Write unit tests for new features
- Update documentation for API changes

## Testing

### Unit Tests
```bash
./gradlew test
```

### Debug Build
```bash
./gradlew assembleDebug
```

## Project Structure

```
app/src/
├── main/
│   ├── java/com/ffmpeg/engine/
│   │   ├── data/           # Data layer
│   │   ├── di/             # Dependency injection
│   │   ├── domain/         # Business logic
│   │   └── presentation/   # UI layer
│   └── res/               # Android resources
└── test/                  # Unit tests
```

## Building FFmpeg from Source

To build the native FFmpeg binaries for Android ARM:

1. Install Android NDK
2. Run the build script in `scripts/build-ffmpeg.sh`
3. Copy output to `app/src/main/jniLibs/`

See `docs/BUILD_FFMPEG.md` for detailed instructions.

## License

By contributing, you agree that your contributions will be licensed under the GPLv3 License.
