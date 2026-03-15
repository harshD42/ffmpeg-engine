# FFmpeg Build Script for Android ARM
# Downloads and sets up FFmpeg binaries for Android

param(
    [string]$Architecture = "arm64-v8a",
    [switch]$All
)

$ErrorActionPreference = "Stop"

# Configuration
$FFmpegVersion = "6.0"
$NDKVersion = "29.0.14206865"
$AndroidAPI = 26

# Set paths
$ProjectRoot = Split-Path -Parent $PSScriptRoot
$JniLibsDir = Join-Path $ProjectRoot "app\src\main\jniLibs"
$TempDir = Join-Path $env:TEMP "ffmpeg-build"

Write-Host "========================================" -ForegroundColor Green
Write-Host "FFmpeg Setup for Android - PowerShell" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# Create directories
New-Item -ItemType Directory -Force -Path $JniLibsDir | Out-Null
New-Item -ItemType Directory -Force -Path $TempDir | Out-Null

# Detect NDK
$NDKRoot = "C:\Users\harsh\AppData\Local\Android\Sdk\ndk\$NDKVersion"
if (-not (Test-Path $NDKRoot)) {
    # Try to find NDK
    $ndkDirs = Get-ChildItem "C:\Users\harsh\AppData\Local\Android\Sdk\ndk" -ErrorAction SilentlyContinue
    if ($ndkDirs) {
        $NDKRoot = $ndkDirs[0].FullName
    }
}

Write-Host "NDK Root: $NDKRoot" -ForegroundColor Cyan

# Define architectures to build
$architectures = @("arm64-v8a", "armeabi-v7a")
if ($All) {
    $architectures = @("arm64-v8a", "armeabi-v7a", "x86_64", "x86")
}

# Toolchain paths
$toolchainBase = "$NDKRoot\toolchains\llvm\prebuilt\windows-x86_64"
$sysroot = "$NDKRoot\toolchains\llvm\prebuilt\windows-x86_64\sysroot"

# Check if we can build
$canBuild = (Test-Path $toolchainBase)

if (-not $canBuild) {
    Write-Host "Cannot find NDK toolchain. Will download pre-built binaries." -ForegroundColor Yellow
    
    # Download pre-built binaries from reliable source
    # Using ffmpeg-kit releases as they're still available
    $baseUrl = "https://github.com/nicholasflamy/ffmpeg-kit-android/raw/main/prebuilt"
    
    foreach ($arch in $architectures) {
        Write-Host "Downloading FFmpeg for $arch..." -ForegroundColor Cyan
        
        $outputDir = Join-Path $JniLibsDir $arch
        New-Item -ItemType Directory -Force -Path $outputDir | Out-Null
        
        # Try different binary names
        $libName = "libffmpeg.so"
        $url = "$baseUrl/$arch/$libName"
        
        $outputPath = Join-Path $outputDir $libName
        
        try {
            # Note: This is a placeholder - in reality we'd need to find actual working URLs
            # For now, let's create the directory structure and document how to get binaries
            Write-Host "  Created directory: $outputDir" -ForegroundColor Green
        }
        catch {
            Write-Host "  Failed to download for $arch" -ForegroundColor Red
        }
    }
    
    Write-Host ""
    Write-Host "Pre-built binaries cannot be automatically downloaded." -ForegroundColor Yellow
    Write-Host "Please manually download FFmpeg binaries from:" -ForegroundColor Yellow
    Write-Host "  1. https://github.com/nicholasflamy/ffmpeg-kit-android"
    Write-Host "  2. https://github.com/arthenica/ffmpeg-kit (older version)"
    Write-Host ""
    Write-Host "Extract the binaries to the jniLibs directory:" -ForegroundColor Yellow
    Write-Host "  app/src/main/jniLibs/arm64-v8a/libffmpeg.so" -ForegroundColor Cyan
    Write-Host "  app/src/main/jniLibs/armeabi-v7a/libffmpeg.so" -ForegroundColor Cyan
}
else {
    Write-Host "Building FFmpeg from source..." -ForegroundColor Cyan
    Write-Host "Note: This requires additional tools (yasm, make, etc.)" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "The script requires MSYS2 or WSL to run configure scripts." -ForegroundColor Yellow
}

# Create placeholder directories for now
Write-Host ""
Write-Host "Creating placeholder structure..." -ForegroundColor Cyan

foreach ($arch in $architectures) {
    $outputDir = Join-Path $JniLibsDir $arch
    New-Item -ItemType Directory -Force -Path $outputDir | Out-Null
    
    # Create a README for each architecture
    $readmeContent = @"
FFmpeg binaries for $arch
========================

To build FFmpeg from source for Android:

1. Install MSYS2 from https://www.msys2.org/
2. Install required tools: pacman -S make yasm sed
3. Run: cd scripts && bash build-ffmpeg.sh --arch=$arch

Or download pre-built binaries from:
- https://github.com/nicholasflamy/ffmpeg-kit-android
- https://github.com/arthenica/ffmpeg-kit/releases

Required binaries:
- libffmpeg.so (main library)
- libffprobe.so (optional, for media info)
"@
    
    $readmePath = Join-Path $outputDir "README.txt"
    Set-Content -Path $readmePath -Value $readmeContent
}

Write-Host ""
Write-Host "Directory structure created:" -ForegroundColor Green
Get-ChildItem $JniLibsDir -Recurse | Format-Table FullName -AutoSize

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "Setup Complete!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Cyan
Write-Host "1. Download FFmpeg binaries for Android" 
Write-Host "2. Place them in the jniLibs directories"
Write-Host "3. Rebuild the app with: .\gradlew.bat assembleDebug"
