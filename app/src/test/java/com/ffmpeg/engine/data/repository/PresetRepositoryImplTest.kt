package com.ffmpeg.engine.data.repository

import com.ffmpeg.engine.domain.model.EncodingConfig
import com.ffmpeg.engine.domain.model.Preset
import com.ffmpeg.engine.domain.model.PresetType
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PresetRepositoryImplTest {

    private lateinit var presetRepository: PresetRepositoryImpl

    @Before
    fun setup() {
        presetRepository = PresetRepositoryImpl()
    }

    @Test
    fun `getBuiltInPresets should return non-empty list`() {
        // Act
        val presets = presetRepository.getBuiltInPresets()

        // Assert
        assertTrue(presets.isNotEmpty())
    }

    @Test
    fun `getBuiltInPresets should contain fast preset`() {
        // Act
        val presets = presetRepository.getBuiltInPresets()

        // Assert
        assertTrue(presets.any { it.name.contains("Fast", ignoreCase = true) })
    }

    @Test
    fun `getBuiltInPresets should contain high quality preset`() {
        // Act
        val presets = presetRepository.getBuiltInPresets()

        // Assert
        assertTrue(presets.any { it.name.contains("High Quality", ignoreCase = true) })
    }

    @Test
    fun `getCustomPresets should return empty list initially`() {
        // Act
        val presets = presetRepository.getCustomPresets()

        // Assert
        assertTrue(presets.isEmpty())
    }

    @Test
    fun `getPresetConfig should return correct config for fast preset`() {
        // Arrange
        val preset = Preset(
            id = "fast",
            name = "Fast",
            type = PresetType.FAST,
            description = "Fast encoding"
        )

        // Act
        val config = presetRepository.getPresetConfig(preset)

        // Assert
        assertEquals("libx264", config.videoCodec)
        assertEquals("ultrafast", config.preset)
    }

    @Test
    fun `getPresetConfig should return correct config for high quality preset`() {
        // Arrange
        val preset = Preset(
            id = "high_quality",
            name = "High Quality",
            type = PresetType.HIGH_QUALITY,
            description = "High quality encoding"
        )

        // Act
        val config = presetRepository.getPresetConfig(preset)

        // Assert
        assertEquals("libx265", config.videoCodec)
        assertEquals("slow", config.preset)
    }

    @Test
    fun `getPresetConfig should return correct config for audio only preset`() {
        // Arrange
        val preset = Preset(
            id = "audio_only",
            name = "Audio Only",
            type = PresetType.AUDIO_ONLY,
            description = "Extract audio"
        )

        // Act
        val config = presetRepository.getPresetConfig(preset)

        // Assert
        assertEquals("copy", config.videoCodec)
        assertEquals("libmp3lame", config.audioCodec)
    }
}
