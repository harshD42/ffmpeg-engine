# FFmpeg Engine - Technical Architecture Documentation

## Table of Contents
1. [System Overview](#system-overview)
2. [Module Diagram](#module-diagram)
3. [Sequence Diagrams](#sequence-diagrams)
4. [Data Flow](#data-flow)
5. [Component Architecture](#component-architecture)

---

## System Overview

The FFmpeg Engine is a native Android application that provides comprehensive media encoding capabilities using FFmpeg compiled from source for ARM-based mobile devices.

### Core Features
- Video transcoding (H.264, H.265/HEVC, VP8, VP9, AV1)
- Audio transcoding (AAC, MP3, Opus, Vorbis, FLAC)
- Format conversion (MP4, MKV, WebM, AVI, MOV)
- Stream copying (re-muxing without re-encoding)
- Video filters (scale, crop, rotate, flip)
- Preset management
- Real-time progress tracking

---

## Module Diagram

```mermaid
graph TB
    subgraph "Presentation Layer"
        UI[UI Layer - Jetpack Compose]
        VM[ViewModels]
        Nav[Navigation]
    end
    
    subgraph "Domain Layer"
        UC[Use Cases]
        Models[Domain Models]
        RepoInt[Repository Interfaces]
    end
    
    subgraph "Data Layer"
        RepoImpl[Repository Implementations]
        FFmpeg[FFmpeg Native Bridge]
        Prefs[Preferences DataStore]
        FileMan[File Manager]
    end
    
    subgraph "Native Layer"
        FFmpegBin[FFmpeg Binary ARM64/ARMv7]
        FFprobeBin[FFprobe Binary]
    end
    
    UI --> VM
    VM --> UC
    UC --> Models
    UC --> RepoInt
    RepoInt --> RepoImpl
    RepoImpl --> FFmpeg
    RepoImpl --> Prefs
    RepoImpl --> FileMan
    FFmpeg --> FFmpegBin
    FFmpeg --> FFprobeBin
```

---

## Sequence Diagrams

### 1. Media Encoding Flow

```mermaid
sequenceDiagram
    participant User
    participant UI as Encode Screen
    participant VM as EncodeViewModel
    participant UC as StartEncoding Use Case
    participant Repo as EncodingRepository
    participant FF as FFmpegService
    participant FFmpeg as FFmpeg Native

    User->>UI: Select input file
    UI->>VM: selectInputFile(path)
    VM->>FF: probeMediaInfo(path)
    FF->>FFmpeg: Run ffprobe
    FFmpeg-->>FF: Media information
    FF-->>VM: MediaInfo result
    VM-->>UI: Display media info
    
    User->>UI: Configure encoding settings
    UI->>VM: updateConfig(config)
    
    User->>UI: Tap "Start Encoding"
    UI->>VM: startEncoding()
    VM->>UC: execute(config)
    UC->>Repo: executeEncoding(config)
    Repo->>FF: executeEncoding(config)
    FF->>FFmpeg: Run ffmpeg command
    
    loop Progress Updates
        FFmpeg-->>FF: Statistics (time, fps, size)
        FF-->>Repo: EncodingState flow
        Repo-->>UC: EncodingState flow
        UC-->>VM: EncodingState flow
        VM-->>UI: Update progress UI
    end
    
    FFmpeg-->>FF: Completion
    FF-->>Repo: Final state
    Repo-->>UC: Completed state
    UC-->>VM: Completed state
    VM-->>UI: Show completion
```

### 2. File Selection Flow

```mermaid
sequenceDiagram
    participant User
    participant UI as Encode Screen
    participant VM as EncodeViewModel
    participant Repo as MediaRepository

    User->>UI: Tap input file card
    UI->>UI: Open file picker
    
    par File Picker Open
        User->>UI: Browse files
    and File Selected
        UI->>VM: selectInputFile(uri)
        VM->>Repo: getMediaInfo(uri)
        Repo-->>VM: MediaInfo
        VM-->>UI: Update UI with file info
    end
```

### 3. Preset Selection Flow

```mermaid
sequenceDiagram
    participant User
    participant UI as Encode Screen
    participant VM as EncodeViewModel
    participant Repo as PresetRepository

    User->>UI: Tap preset card
    UI->>VM: showPresetDialog(true)
    VM-->>UI: Show dialog
    
    UI->>VM: applyPreset(preset)
    VM->>Repo: getPresetConfig(preset)
    Repo-->>VM: EncodingConfig
    VM-->>UI: Update config display
    VM-->>UI: Update command preview
```

---

## Data Flow

### Encoding State Flow

```mermaid
stateDiagram-v2
    [*] --> IDLE
    IDLE --> PREPARING: Start encoding
    PREPARING --> ENCODING: FFmpeg running
    ENCODING --> ENCODING: Progress updates
    ENCODING --> COMPLETED: Success
    ENCODING --> CANCELLED: User cancelled
    ENCODING --> FAILED: Error occurred
    COMPLETED --> [*]
    CANCELLED --> [*]
    FAILED --> [*]
```

### Configuration Data Flow

```mermaid
flowchart LR
    subgraph Input
        A[User Selection] --> B[Preset]
        A --> C[Manual Settings]
    end
    
    subgraph Processing
        B --> D[EncodingConfig]
        C --> D
    end
    
    subgraph Output
        D --> E[FFmpeg Command]
        E --> F[Encoded File]
    end
```

---

## Component Architecture

### 1. Presentation Layer

#### MainActivity
- Entry point for the application
- Sets up Compose UI
- Handles navigation

#### EncodeScreen
- File selection UI
- Encoding configuration UI
- Progress display
- Preset selection dialog

#### EncodeViewModel
- Manages UI state
- Handles user interactions
- Coordinates with use cases

### 2. Domain Layer

#### Use Cases
- `GetMediaInfoUseCase`: Retrieves media file information
- `StartEncodingUseCase`: Initiates encoding process
- `CancelEncodingUseCase`: Cancels ongoing encoding
- `GetPresetsUseCase`: Retrieves available presets

#### Domain Models
- `MediaInfo`: Media file metadata
- `EncodingConfig`: Encoding parameters
- `EncodingState`: Current encoding status
- `Preset`: Pre-defined encoding configurations

### 3. Data Layer

#### FFmpegService
- Interface for FFmpeg operations
- Command generation
- Progress callback handling

#### MediaRepository
- File system access
- Media information parsing

#### EncodingRepository
- Encoding execution
- Progress management
- State management

### 4. Native Layer

#### FFmpeg Binary (ARM64/ARMv7)
- Compiled from FFmpeg source
- Supports all major codecs
- Lightweight build for mobile

#### FFprobe Binary
- Media information extraction
- Stream analysis

---

## File Structure

```
app/src/main/
├── java/com/ffmpeg/engine/
│   ├── FFmpegEngineApp.kt          # Application class
│   ├── di/                         # Dependency injection
│   │   └── AppModule.kt
│   ├── domain/                     # Domain layer
│   │   ├── model/                  # Domain models
│   │   ├── repository/             # Repository interfaces
│   │   └── usecase/               # Use cases
│   ├── data/                       # Data layer
│   │   ├── local/                 # Local services
│   │   └── repository/            # Repository implementations
│   └── presentation/               # Presentation layer
│       ├── MainActivity.kt
│       ├── navigation/            # Navigation
│       ├── ui/                    # UI components
│       │   ├── screens/           # Screen composables
│       │   └── theme/             # Theme
│       └── viewmodel/             # ViewModels
├── jniLibs/                       # Native libraries
│   ├── arm64-v8a/
│   │   └── libffmpeg.so
│   └── armeabi-v7a/
│       └── libffmpeg.so
└── res/                           # Resources
```

---

## Build Configuration

### Native Library Integration

The app uses native FFmpeg binaries that are:
1. Compiled from source for Android ARM
2. Placed in `jniLibs` directory
3. Loaded at runtime via System.loadLibrary()

### ABI Support
- `arm64-v8a`: Modern devices (2016+)
- `armeabi-v7a`: Older devices

---

## Error Handling

```mermaid
flowchart TD
    A[Encoding Start] --> B{FFmpeg Running?}
    B -->|Yes| C{Success?}
    B -->|No| D[Show Error]
    C -->|Yes| E[Complete]
    C -->|No| F{Cancelled?}
    F -->|Yes| G[Show Cancelled]
    F -->|No| H[Show Error]
    
    D --> I[Retry Option]
    G --> I
    H --> I
    
    I --> A
```

---

## Testing Strategy

### Unit Tests
- ViewModel logic
- Use case business logic
- Model serialization
- Command building

### Integration Tests
- Repository implementations
- FFmpeg command execution (mocked)
- File system operations

### UI Tests
- Screen interactions
- Navigation flows
- State updates
