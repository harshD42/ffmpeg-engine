package com.ffmpeg.engine.domain.model

import java.io.File

/**
 * Represents a media file with its metadata
 */
data class MediaInfo(
    val file: File,
    val path: String,
    val name: String,
    val size: Long,
    val duration: Long = 0L,
    val videoStreams: List<VideoStream> = emptyList(),
    val audioStreams: List<AudioStream> = emptyList(),
    val subtitleStreams: List<SubtitleStream> = emptyList(),
    val metadata: Map<String, String> = emptyMap(),
    val format: String = ""
) {
    val formattedSize: String
        get() = formatFileSize(size)

    val formattedDuration: String
        get() = formatDuration(duration)

    companion object {
        fun formatFileSize(size: Long): String {
            return when {
                size >= 1_073_741_824 -> String.format("%.2f GB", size / 1_073_741_824.0)
                size >= 1_048_576 -> String.format("%.2f MB", size / 1_048_576.0)
                size >= 1024 -> String.format("%.2f KB", size / 1024.0)
                else -> "$size B"
            }
        }

        fun formatDuration(durationMs: Long): String {
            val seconds = (durationMs / 1000) % 60
            val minutes = (durationMs / (1000 * 60)) % 60
            val hours = durationMs / (1000 * 60 * 60)
            return if (hours > 0) {
                String.format("%02d:%02d:%02d", hours, minutes, seconds)
            } else {
                String.format("%02d:%02d", minutes, seconds)
            }
        }
    }
}

data class VideoStream(
    val index: Int,
    val codec: String,
    val codecLong: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val bitrate: Long = 0L,
    val frameRate: String = "",
    val pixelFormat: String = "",
    val duration: Long = 0L
) {
    val resolution: String
        get() = if (width > 0 && height > 0) "${width}x${height}" else "N/A"
}

data class AudioStream(
    val index: Int,
    val codec: String,
    val codecLong: String = "",
    val sampleRate: Int = 0,
    val channels: Int = 0,
    val channelLayout: String = "",
    val bitrate: Long = 0L,
    val duration: Long = 0L
) {
    val channelsDescription: String
        get() = when (channels) {
            1 -> "Mono"
            2 -> "Stereo"
            6 -> "5.1"
            8 -> "7.1"
            else -> "$channels channels"
        }
}

data class SubtitleStream(
    val index: Int,
    val codec: String,
    val codecLong: String = "",
    val language: String = "",
    val title: String = ""
)
