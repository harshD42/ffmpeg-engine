package com.ffmpeg.engine.domain.repository

import com.ffmpeg.engine.domain.model.EncodingConfig
import com.ffmpeg.engine.domain.model.EncodingHistory
import com.ffmpeg.engine.domain.model.EncodingState
import com.ffmpeg.engine.domain.model.MediaInfo
import com.ffmpeg.engine.domain.model.Preset
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    suspend fun getMediaInfo(filePath: String): Result<MediaInfo>
}

interface EncodingRepository {
    fun executeEncoding(config: EncodingConfig): Flow<EncodingState>
    fun cancelEncoding()
    fun getFFmpegVersion(): String
}

interface PresetRepository {
    fun getBuiltInPresets(): List<Preset>
    fun getCustomPresets(): Flow<List<Preset>>
    suspend fun saveCustomPreset(preset: Preset)
    suspend fun deleteCustomPreset(presetId: String)
}

interface HistoryRepository {
    fun getHistory(): Flow<List<EncodingHistory>>
    suspend fun addToHistory(history: EncodingHistory)
    suspend fun clearHistory()
}

interface PreferencesRepository {
    val themeMode: Flow<String>
    val defaultOutputDir: Flow<String>
    val showNotifications: Flow<Boolean>
    val keepScreenOn: Flow<Boolean>
    
    suspend fun setThemeMode(mode: String)
    suspend fun setDefaultOutputDir(path: String)
    suspend fun setShowNotifications(show: Boolean)
    suspend fun setKeepScreenOn(keep: Boolean)
}
