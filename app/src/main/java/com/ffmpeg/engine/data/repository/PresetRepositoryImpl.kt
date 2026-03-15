package com.ffmpeg.engine.data.repository

import com.ffmpeg.engine.data.local.PreferencesManager
import com.ffmpeg.engine.domain.model.EncodingConfig
import com.ffmpeg.engine.domain.model.Preset
import com.ffmpeg.engine.domain.repository.PresetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PresetRepositoryImpl @Inject constructor(
    private val preferencesManager: PreferencesManager
) : PresetRepository {
    
    override fun getBuiltInPresets(): List<Preset> {
        return listOf(
            Preset(
                id = "fast",
                name = "Fast (Smallest Size)",
                config = EncodingConfig(
                    videoCodec = "libx264",
                    preset = "ultrafast",
                    crf = 28,
                    audioCodec = "aac",
                    audioBitrate = "96k"
                ),
                isBuiltIn = true
            ),
            Preset(
                id = "medium",
                name = "Medium (Balanced)",
                config = EncodingConfig(
                    videoCodec = "libx264",
                    preset = "medium",
                    crf = 23,
                    audioCodec = "aac",
                    audioBitrate = "128k"
                ),
                isBuiltIn = true
            ),
            Preset(
                id = "slow",
                name = "Slow (High Quality)",
                config = EncodingConfig(
                    videoCodec = "libx264",
                    preset = "slow",
                    crf = 20,
                    audioCodec = "aac",
                    audioBitrate = "192k"
                ),
                isBuiltIn = true
            ),
            Preset(
                id = "high_quality",
                name = "High Quality (H.265)",
                config = EncodingConfig(
                    videoCodec = "libx265",
                    preset = "slow",
                    crf = 18,
                    audioCodec = "aac",
                    audioBitrate = "256k"
                ),
                isBuiltIn = true
            ),
            Preset(
                id = "web_optimized",
                name = "Web Optimized",
                config = EncodingConfig(
                    videoCodec = "libx264",
                    preset = "medium",
                    crf = 23,
                    audioCodec = "aac",
                    audioBitrate = "128k",
                    outputFormat = "mp4"
                ),
                isBuiltIn = true
            ),
            Preset(
                id = "audio_only",
                name = "Audio Only (MP3)",
                config = EncodingConfig(
                    videoCodec = "none",
                    audioCodec = "libmp3lame",
                    audioBitrate = "192k",
                    outputFormat = "mp3"
                ),
                isBuiltIn = true
            ),
            Preset(
                id = "copy_streams",
                name = "Copy Streams (No Re-encode)",
                config = EncodingConfig(
                    copyVideo = true,
                    copyAudio = true
                ),
                isBuiltIn = true
            )
        )
    }
    
    override fun getCustomPresets(): Flow<List<Preset>> {
        return preferencesManager.customPresets
    }
    
    override suspend fun saveCustomPreset(preset: Preset) {
        preferencesManager.saveCustomPreset(preset)
    }
    
    override suspend fun deleteCustomPreset(presetId: String) {
        preferencesManager.deleteCustomPreset(presetId)
    }
}
