package com.ffmpeg.engine.data.repository

import com.ffmpeg.engine.data.local.FFmpegService
import com.ffmpeg.engine.domain.model.MediaInfo
import com.ffmpeg.engine.domain.repository.MediaRepository
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val ffmpegService: FFmpegService
) : MediaRepository {
    
    override suspend fun getMediaInfo(filePath: String): Result<MediaInfo> {
        return ffmpegService.probeMediaInfo(filePath)
    }
}
