package com.ffmpeg.engine.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ffmpeg.engine.domain.model.EncodingConfig
import com.ffmpeg.engine.domain.model.EncodingHistory
import com.ffmpeg.engine.domain.model.EncodingState
import com.ffmpeg.engine.domain.model.EncodingStatus
import com.ffmpeg.engine.domain.model.MediaInfo
import com.ffmpeg.engine.domain.model.Preset
import com.ffmpeg.engine.domain.repository.EncodingRepository
import com.ffmpeg.engine.domain.repository.MediaRepository
import com.ffmpeg.engine.domain.repository.PresetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class EncodeUiState(
    val inputPath: String = "",
    val outputPath: String = "",
    val mediaInfo: MediaInfo? = null,
    val config: EncodingConfig = EncodingConfig(),
    val encodingState: EncodingState = EncodingState(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val builtInPresets: List<Preset> = emptyList(),
    val customPresets: List<Preset> = emptyList(),
    val showPresetDialog: Boolean = false,
    val showSettingsSheet: Boolean = false,
    val commandPreview: String = ""
)

@HiltViewModel
class EncodeViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val encodingRepository: EncodingRepository,
    private val presetRepository: PresetRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(EncodeUiState())
    val uiState: StateFlow<EncodeUiState> = _uiState.asStateFlow()
    
    private var encodingJob: Job? = null
    
    init {
        loadPresets()
    }
    
    private fun loadPresets() {
        viewModelScope.launch {
            val builtIn = presetRepository.getBuiltInPresets()
            presetRepository.getCustomPresets().collect { custom ->
                _uiState.update { it.copy(
                    builtInPresets = builtIn,
                    customPresets = custom
                )}
            }
        }
    }
    
    fun selectInputFile(path: String) {
        _uiState.update { it.copy(inputPath = path, isLoading = true) }
        probeMediaInfo(path)
    }
    
    private fun probeMediaInfo(path: String) {
        viewModelScope.launch {
            mediaRepository.getMediaInfo(path).fold(
                onSuccess = { info ->
                    _uiState.update { it.copy(
                        mediaInfo = info,
                        isLoading = false,
                        config = it.config.copy(inputPath = path)
                    )}
                    updateCommandPreview()
                },
                onFailure = { error ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = error.message
                    )}
                }
            )
        }
    }
    
    fun setOutputPath(path: String) {
        _uiState.update { it.copy(
            outputPath = path,
            config = it.config.copy(outputPath = path)
        )}
        updateCommandPreview()
    }
    
    fun updateConfig(update: EncodingConfig.() -> EncodingConfig) {
        _uiState.update { it.copy(
            config = it.config.update()
        )}
        updateCommandPreview()
    }
    
    fun applyPreset(preset: Preset) {
        _uiState.update { it.copy(
            config = preset.config.copy(
                inputPath = it.config.inputPath,
                outputPath = it.config.outputPath
            ),
            showPresetDialog = false
        )}
        updateCommandPreview()
    }
    
    fun showPresetDialog(show: Boolean) {
        _uiState.update { it.copy(showPresetDialog = show) }
    }
    
    fun showSettingsSheet(show: Boolean) {
        _uiState.update { it.copy(showSettingsSheet = show) }
    }
    
    fun startEncoding() {
        val config = _uiState.value.config
        if (config.inputPath.isBlank() || config.outputPath.isBlank()) {
            _uiState.update { it.copy(error = "Please select input and output files") }
            return
        }
        
        encodingJob = viewModelScope.launch {
            encodingRepository.executeEncoding(config).collect { state ->
                _uiState.update { it.copy(encodingState = state) }
                
                if (state.status == EncodingStatus.COMPLETED || state.status == EncodingStatus.FAILED) {
                    saveToHistory(state)
                }
            }
        }
    }
    
    fun cancelEncoding() {
        encodingRepository.cancelEncoding()
        encodingJob?.cancel()
    }
    
    private fun saveToHistory(state: EncodingState) {
        viewModelScope.launch {
            val config = _uiState.value.config
            val history = EncodingHistory(
                inputPath = config.inputPath,
                outputPath = config.outputPath,
                command = _uiState.value.commandPreview,
                config = config,
                status = state.status,
                startTime = state.startTime,
                endTime = state.endTime,
                outputSize = state.size,
                errorMessage = state.errorMessage,
                logs = state.logs
            )
            // Save to history repository
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
    
    private fun updateCommandPreview() {
        val config = _uiState.value.config
        if (config.inputPath.isBlank()) {
            _uiState.update { it.copy(commandPreview = "") }
            return
        }
        
        val command = buildString {
            append("ffmpeg -y -i \"${config.inputPath}\"")
            
            if (config.startTime.isNotBlank()) append(" -ss ${config.startTime}")
            if (config.endTime.isNotBlank()) append(" -to ${config.endTime}")
            
            if (!config.copyVideo) {
                if (config.videoCodec.isNotBlank() && config.videoCodec != "none") {
                    append(" -c:v ${config.videoCodec}")
                    if (config.videoCodec.startsWith("libx264") || config.videoCodec.startsWith("libx265")) {
                        append(" -preset ${config.preset}")
                        append(" -crf ${config.crf}")
                    }
                    if (config.videoBitrate.isNotBlank()) append(" -b:v ${config.videoBitrate}")
                    if (config.pixelFormat.isNotBlank()) append(" -pix_fmt ${config.pixelFormat}")
                }
            } else {
                append(" -c:v copy")
            }
            
            if (!config.copyAudio) {
                if (config.audioCodec.isNotBlank() && config.audioCodec != "none") {
                    append(" -c:a ${config.audioCodec}")
                    if (config.audioBitrate.isNotBlank()) append(" -b:a ${config.audioBitrate}")
                    if (config.sampleRate.isNotBlank()) append(" -ar ${config.sampleRate}")
                    if (config.audioChannels > 0) append(" -ac ${config.audioChannels}")
                }
            } else {
                append(" -c:a copy")
            }
            
            if (config.resolution.isNotBlank()) append(" -vf scale=${config.resolution}")
            
            append(" \"${config.outputPath}\"")
        }
        
        _uiState.update { it.copy(commandPreview = command) }
    }
}
