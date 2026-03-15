package com.ffmpeg.engine.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ffmpeg.engine.domain.model.EncodingConfig
import com.ffmpeg.engine.domain.model.EncodingHistory
import com.ffmpeg.engine.domain.model.Preset
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ffmpeg_engine_prefs")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val gson = Gson()
    
    companion object {
        private val THEME_MODE = stringPreferencesKey("theme_mode")
        private val DEFAULT_OUTPUT_DIR = stringPreferencesKey("default_output_dir")
        private val CUSTOM_PRESETS = stringPreferencesKey("custom_presets")
        private val ENCODING_HISTORY = stringPreferencesKey("encoding_history")
        private val LAST_USED_CONFIG = stringPreferencesKey("last_used_config")
        private val SHOW_NOTIFICATIONS = booleanPreferencesKey("show_notifications")
        private val KEEP_SCREEN_ON = booleanPreferencesKey("keep_screen_on")
    }
    
    val themeMode: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME_MODE] ?: "system"
    }
    
    val defaultOutputDir: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[DEFAULT_OUTPUT_DIR] ?: ""
    }
    
    val customPresets: Flow<List<Preset>> = context.dataStore.data.map { preferences ->
        val json = preferences[CUSTOM_PRESETS] ?: "[]"
        try {
            val type = object : TypeToken<List<Preset>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    val encodingHistory: Flow<List<EncodingHistory>> = context.dataStore.data.map { preferences ->
        val json = preferences[ENCODING_HISTORY] ?: "[]"
        try {
            val type = object : TypeToken<List<EncodingHistory>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    val lastUsedConfig: Flow<EncodingConfig?> = context.dataStore.data.map { preferences ->
        val json = preferences[LAST_USED_CONFIG]
        if (json != null) {
            try {
                gson.fromJson(json, EncodingConfig::class.java)
            } catch (e: Exception) {
                null
            }
        } else null
    }
    
    val showNotifications: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[SHOW_NOTIFICATIONS] ?: true
    }
    
    val keepScreenOn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEEP_SCREEN_ON] ?: true
    }
    
    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE] = mode
        }
    }
    
    suspend fun setDefaultOutputDir(path: String) {
        context.dataStore.edit { preferences ->
            preferences[DEFAULT_OUTPUT_DIR] = path
        }
    }
    
    suspend fun saveCustomPreset(preset: Preset) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[CUSTOM_PRESETS] ?: "[]"
            val type = object : TypeToken<MutableList<Preset>>() {}.type
            val presets: MutableList<Preset> = try {
                gson.fromJson(currentJson, type) ?: mutableListOf()
            } catch (e: Exception) {
                mutableListOf()
            }
            presets.add(preset)
            preferences[CUSTOM_PRESETS] = gson.toJson(presets)
        }
    }
    
    suspend fun deleteCustomPreset(presetId: String) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[CUSTOM_PRESETS] ?: "[]"
            val type = object : TypeToken<MutableList<Preset>>() {}.type
            val presets: MutableList<Preset> = try {
                gson.fromJson(currentJson, type) ?: mutableListOf()
            } catch (e: Exception) {
                mutableListOf()
            }
            presets.removeAll { it.id == presetId }
            preferences[CUSTOM_PRESETS] = gson.toJson(presets)
        }
    }
    
    suspend fun addToHistory(history: EncodingHistory) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[ENCODING_HISTORY] ?: "[]"
            val type = object : TypeToken<MutableList<EncodingHistory>>() {}.type
            val historyList: MutableList<EncodingHistory> = try {
                gson.fromJson(currentJson, type) ?: mutableListOf()
            } catch (e: Exception) {
                mutableListOf()
            }
            historyList.add(0, history)
            // Keep only last 100 entries
            val trimmedList = historyList.take(100)
            preferences[ENCODING_HISTORY] = gson.toJson(trimmedList)
        }
    }
    
    suspend fun clearHistory() {
        context.dataStore.edit { preferences ->
            preferences[ENCODING_HISTORY] = "[]"
        }
    }
    
    suspend fun saveLastUsedConfig(config: EncodingConfig) {
        context.dataStore.edit { preferences ->
            preferences[LAST_USED_CONFIG] = gson.toJson(config)
        }
    }
    
    suspend fun setShowNotifications(show: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SHOW_NOTIFICATIONS] = show
        }
    }
    
    suspend fun setKeepScreenOn(keep: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEEP_SCREEN_ON] = keep
        }
    }
}
