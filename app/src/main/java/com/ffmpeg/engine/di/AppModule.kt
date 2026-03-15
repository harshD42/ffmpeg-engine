package com.ffmpeg.engine.di

import com.ffmpeg.engine.data.local.FFmpegService
import com.ffmpeg.engine.data.local.PreferencesManager
import com.ffmpeg.engine.data.repository.EncodingRepositoryImpl
import com.ffmpeg.engine.data.repository.MediaRepositoryImpl
import com.ffmpeg.engine.data.repository.PresetRepositoryImpl
import com.ffmpeg.engine.domain.repository.EncodingRepository
import com.ffmpeg.engine.domain.repository.MediaRepository
import com.ffmpeg.engine.domain.repository.PresetRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindMediaRepository(
        mediaRepositoryImpl: MediaRepositoryImpl
    ): MediaRepository
    
    @Binds
    @Singleton
    abstract fun bindEncodingRepository(
        encodingRepositoryImpl: EncodingRepositoryImpl
    ): EncodingRepository
    
    @Binds
    @Singleton
    abstract fun bindPresetRepository(
        presetRepositoryImpl: PresetRepositoryImpl
    ): PresetRepository
}
