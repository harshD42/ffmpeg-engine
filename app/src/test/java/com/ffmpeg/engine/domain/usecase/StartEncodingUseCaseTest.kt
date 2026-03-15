package com.ffmpeg.engine.domain.usecase

import com.ffmpeg.engine.domain.model.EncodingConfig
import com.ffmpeg.engine.domain.model.EncodingState
import com.ffmpeg.engine.domain.model.EncodingStatus
import com.ffmpeg.engine.domain.repository.EncodingRepository
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class StartEncodingUseCaseTest {

    private lateinit var encodingRepository: EncodingRepository
    private lateinit var startEncodingUseCase: StartEncodingUseCase

    @Before
    fun setup() {
        encodingRepository = mock()
        startEncodingUseCase = StartEncodingUseCase(encodingRepository)
    }

    @Test
    fun `execute should call repository executeEncoding with config`() {
        // Arrange
        val config = EncodingConfig(
            inputPath = "/storage/input.mp4",
            outputPath = "/storage/output.mp4",
            videoCodec = "libx264",
            audioCodec = "aac"
        )
        
        val expectedState = EncodingState(
            outputPath = config.outputPath,
            status = EncodingStatus.COMPLETED
        )
        
        whenever(encodingRepository.executeEncoding(config))
            .thenReturn(flowOf(expectedState))

        // Act
        val result = startEncodingUseCase.execute(config)

        // Assert
        verify(encodingRepository).executeEncoding(config)
    }

    @Test
    fun `execute should emit encoding states from repository`() {
        // Arrange
        val config = EncodingConfig(
            inputPath = "/storage/input.mp4",
            outputPath = "/storage/output.mp4"
        )
        
        val encodingState = EncodingState(
            outputPath = config.outputPath,
            status = EncodingStatus.ENCODING,
            progress = 0.5f
        )
        
        whenever(encodingRepository.executeEncoding(config))
            .thenReturn(flowOf(encodingState))

        // Act
        val result = startEncodingUseCase.execute(config)

        // Assert
        // The flow should emit the encoding state
        result.collect { state ->
            assert(state.status == EncodingStatus.ENCODING)
            assert(state.progress == 0.5f)
        }
    }
}
