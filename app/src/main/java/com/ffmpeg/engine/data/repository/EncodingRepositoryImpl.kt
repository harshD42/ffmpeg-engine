package com.ffmpeg.engine.data.repository

import com.ffmpeg.engine.data.local.FFmpegService
import com.ffmpeg.engine.domain.model.EncodingConfig
import com.ffmpeg.engine.domain.model.EncodingState
import com.ffmpeg.engine.domain.repository.EncodingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EncodingRepositoryImpl @Inject constructor(
    private val ffmpegService: FFmpegService
) : EncodingRepository {
    
    override fun executeEncoding(config: EncodingConfig): Flow<EncodingState> {
        return ffmpegService.executeEncoding(config)
    }
    
    override fun cancelEncoding() {
        ffmpegService.cancelEncoding()
    }
    
    override fun getFFmpegVersion(): String {
        return ffmpegService.getFFmpegVersion()
    }
}
