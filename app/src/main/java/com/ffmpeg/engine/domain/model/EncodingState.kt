package com.ffmpeg.engine.domain.model

import java.util.UUID

/**
 * Represents the current state of an encoding operation
 */
data class EncodingState(
    val sessionId: String = UUID.randomUUID().toString(),
    val status: EncodingStatus = EncodingStatus.IDLE,
    val progress: Float = 0f,
    val currentFrame: Long = 0L,
    val fps: Float = 0f,
    val speed: String = "",
    val size: Long = 0L,
    val bitrate: String = "",
    val time: Long = 0L,
    val outputPath: String = "",
    val logs: String = "",
    val errorMessage: String? = null,
    val startTime: Long = 0L,
    val endTime: Long = 0L
) {
    val formattedProgress: String
        get() = String.format("%.1f%%", progress * 100)

    val formattedTime: String
        get() = MediaInfo.formatDuration(time)

    val formattedSize: String
        get() = MediaInfo.formatFileSize(size)

    val duration: Long
        get() = if (endTime > 0) endTime - startTime else 0L
}

enum class EncodingStatus {
    IDLE,
    PREPARING,
    ENCODING,
    CANCELLED,
    COMPLETED,
    FAILED
}

/**
 * Represents a saved preset
 */
data class Preset(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val config: EncodingConfig,
    val isBuiltIn: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastUsedAt: Long? = null
)

/**
 * Represents an encoding history entry
 */
data class EncodingHistory(
    val id: String = UUID.randomUUID().toString(),
    val inputPath: String,
    val outputPath: String,
    val command: String,
    val config: EncodingConfig,
    val status: EncodingStatus,
    val startTime: Long,
    val endTime: Long = 0L,
    val outputSize: Long = 0L,
    val errorMessage: String? = null,
    val logs: String = ""
) {
    val duration: Long
        get() = if (endTime > 0) endTime - startTime else 0L

    val formattedDuration: String
        get() = MediaInfo.formatDuration(duration)
}
