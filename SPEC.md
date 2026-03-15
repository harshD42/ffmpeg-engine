# FFmpeg Engine Android App - Technical Specification

## 1. Project Overview

**Project Name:** FFmpegEngine
**Package Name:** com.ffmpeg.engine
**Type:** Android Native Application

**Core Functionality:** A full-featured FFmpeg encoding engine for Android that provides comprehensive video/audio encoding capabilities, mirroring FFmpeg command-line functionality. Users can select input files, configure encoding parameters using an intuitive UI or direct command input, and process media files with full control over all FFmpeg features.

## 2. Technology Stack & Choices

### Framework & Language
- **Language:** Kotlin 1.9.x
- **Min SDK:** 26 (Android 8.0)
- **Target SDK:** 34 (Android 14)
- **Compile SDK:** 34

### Key Libraries/Dependencies
- **FFmpeg:** FFmpeg-Kit Full-GPL (provides complete FFmpeg functionality)
- **UI:** Jetpack Compose with Material Design 3
- **DI:** Hilt (Dagger Hilt)
- **Async:** Kotlin Coroutines + Flow
- **Navigation:** Compose Navigation
- **File Picker:** Activity Result API
- **Permissions:** Accompanist Permissions
- **Image Loading:** Coil Compose

### State Management
- ViewModel + StateFlow for UI state
- Repository pattern for data
- Use Cases for business logic

### Architecture Pattern
- Clean Architecture (3-layer)
  - Presentation Layer (Compose UI + ViewModels)
  - Domain Layer (Use Cases + Models)
  - Data Layer (Repositories + Data Sources)

## 3. Feature List

### Core Features
1. **File Management**
   - Browse and select input media files (video, audio, images)
   - Display file metadata (codec, duration, resolution, bitrate)
   - Choose output location and filename

2. **FFmpeg Command Builder**
   - Visual parameter builder for common operations
   - Support for all FFmpeg codecs (video/audio)
   - Filter chain builder (scale, crop, trim, overlay, etc.)
   - Custom command input for advanced users

3. **Encoding Operations**
   - Video transcoding (change codec, resolution, bitrate)
   - Audio transcoding (extract audio, change codec, sample rate)
   - Format conversion (MP4, MKV, AVI, WebM, MP3, AAC, etc.)
   - Video/audio extraction
   - Stream copying (re-muxing without re-encoding)
   - Concatenation of multiple files
   - Batch processing queue

4. **Progress & Monitoring**
   - Real-time encoding progress
   - Encoding speed and ETA display
   - Detailed encoding logs
   - Cancel/pause encoding operations

5. **Preset Management**
   - Built-in encoding presets (Fast, Medium, Quality, Custom)
   - Save/load custom presets
   - Preset sharing via JSON

### Additional Features
6. **Media Information**
   - Full FFprobe information display
   - Stream details (video, audio, subtitle)
   - Metadata display

7. **History**
   - Encoding history with logs
   - Re-run previous commands
   - Favorite presets

## 4. UI/UX Design Direction

### Overall Visual Style
- Material Design 3 (Material You)
- Dark/Light theme support with dynamic colors
- Clean, professional, technical aesthetic
- Monospace fonts for command display

### Color Scheme
- Primary: Deep Purple (#6750A4)
- Secondary: Teal (#03DAC6)
- Surface: Adaptive based on system theme
- Accent colors for different codec types (video=blue, audio=green, subtitle=orange)

### Layout Approach
- Bottom navigation with 4 main tabs:
  1. **Encode** - Main encoding interface
  2. **Presets** - Preset management
  3. **History** - Past encoding jobs
  4. **Settings** - App configuration

### Key UI Components
- Floating Action Button for quick encode
- Bottom sheets for parameter configuration
- Expandable cards for stream details
- Terminal-style log viewer
- Progress rings with percentage
- Drag-and-drop file selection

### Interaction Patterns
- Swipe gestures for quick actions
- Long-press for context menus
- Pull-to-refresh for file lists
- Snackbar for action feedback
