package com.ffmpeg.engine.domain.model

/**
 * Represents encoding configuration parameters
 */
data class EncodingConfig(
    val inputPath: String = "",
    val outputPath: String = "",
    val outputFormat: String = "mp4",
    
    // Video settings
    val videoCodec: String = "libx264",
    val videoBitrate: String = "2000k",
    val resolution: String = "",
    val frameRate: String = "",
    val pixelFormat: String = "yuv420p",
    val preset: String = "medium",
    val crf: Int = 23,
    val copyVideo: Boolean = false,
    
    // Audio settings
    val audioCodec: String = "aac",
    val audioBitrate: String = "128k",
    val sampleRate: String = "44100",
    val audioChannels: Int = 2,
    val copyAudio: Boolean = false,
    
    // Filters
    val filters: List<VideoFilter> = emptyList(),
    
    // Trimming
    val startTime: String = "",
    val endTime: String = "",
    
    // Custom command
    val customCommand: String = "",
    val useCustomCommand: Boolean = false
)

/**
 * Video/Audio filter configuration
 */
data class VideoFilter(
    val type: FilterType,
    val value: String,
    val enabled: Boolean = true
)

enum class FilterType(val displayName: String, val ffmpegName: String) {
    SCALE("Scale", "scale"),
    CROP("Crop", "crop"),
    ROTATE("Rotate", "rotate"),
    FLIP_H("Flip Horizontal", "hflip"),
    FLIP_V("Flip Vertical", "vflip"),
    PAD("Pad", "pad"),
    DEINTERLACE("Deinterlace", "yadif"),
    DENOISE("Denoise", "hqdn3d"),
    SHARPNESS("Sharpness", "unsharp"),
    BRIGHTNESS("Brightness", "eq"),
    CONTRAST("Contrast", "eq"),
    SATURATION("Saturation", "eq"),
    VOLUME("Volume", "volume"),
    CUSTOM("Custom", "")
}

object Codecs {
    val VIDEO_CODECS = listOf(
        "libx264" to "H.264 / AVC",
        "libx265" to "H.265 / HEVC",
        "libvpx" to "VP8",
        "libvpx-vp9" to "VP9",
        "libaom-av1" to "AV1",
        "mpeg4" to "MPEG-4 Part 2",
        "h264_nvenc" to "H.264 (NVIDIA NVENC)",
        "hevc_nvenc" to "H.265 (NVIDIA NVENC)",
        "copy" to "Copy (no re-encode)",
        "none" to "No Video"
    )
    
    val AUDIO_CODECS = listOf(
        "aac" to "AAC",
        "libmp3lame" to "MP3",
        "libopus" to "Opus",
        "libvorbis" to "Vorbis",
        "libflac" to "FLAC",
        "pcm_s16le" to "PCM 16-bit",
        "ac3" to "AC3",
        "copy" to "Copy (no re-encode)",
        "none" to "No Audio"
    )
    
    val FORMATS = listOf(
        "mp4" to "MP4 (MPEG-4 Part 14)",
        "mkv" to "Matroska (MKV)",
        "webm" to "WebM",
        "avi" to "AVI",
        "mov" to "QuickTime (MOV)",
        "mp3" to "MP3 Audio",
        "aac" to "AAC Audio",
        "ogg" to "OGG Audio",
        "wav" to "WAV Audio",
        "gif" to "GIF Animation",
        "webp" to "WebP"
    )
    
    val PRESETS = listOf(
        "ultrafast" to "Ultra Fast",
        "superfast" to "Super Fast",
        "veryfast" to "Very Fast",
        "faster" to "Faster",
        "fast" to "Fast",
        "medium" to "Medium",
        "slow" to "Slow",
        "slower" to "Slower",
        "veryslow" to "Very Slow"
    )
}
