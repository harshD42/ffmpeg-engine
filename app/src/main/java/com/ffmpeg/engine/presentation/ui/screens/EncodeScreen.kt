package com.ffmpeg.engine.presentation.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ffmpeg.engine.domain.model.EncodingStatus
import com.ffmpeg.engine.domain.model.MediaInfo
import com.ffmpeg.engine.presentation.ui.theme.AudioCodecColor
import com.ffmpeg.engine.presentation.ui.theme.VideoCodecColor
import com.ffmpeg.engine.presentation.viewmodel.EncodeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncodeScreen(
    viewModel: EncodeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    val inputFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            val path = it.path ?: ""
            viewModel.selectInputFile(path)
        }
    }
    
    val outputFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/*")
    ) { uri: Uri? ->
        uri?.let {
            val path = it.path ?: ""
            viewModel.setOutputPath(path)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FFmpeg Engine") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Input File Selection
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { inputFileLauncher.launch(arrayOf("video/*", "audio/*", "image/*")) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.VideoFile,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Input File",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = uiState.inputPath.ifBlank { "Tap to select a media file" },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null
                        )
                    }
                }
            }
            
            // Media Info Display
            uiState.mediaInfo?.let { info ->
                item {
                    MediaInfoCard(info = info)
                }
            }
            
            // Output File Selection
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val extension = uiState.config.outputFormat
                        outputFileLauncher.launch("output.$extension")
                    },
                    enabled = uiState.inputPath.isNotBlank()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.SaveAlt,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Output File",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = uiState.outputPath.ifBlank { "Tap to select output location" },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null
                        )
                    }
                }
            }
            
            // Preset Selection
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.showPresetDialog(true) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Encoding Preset",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Tap to select preset",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null
                        )
                    }
                }
            }
            
            // Encoding Settings
            item {
                EncodingSettingsCard(
                    viewModel = viewModel,
                    uiState = uiState
                )
            }
            
            // Command Preview
            if (uiState.commandPreview.isNotBlank()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Command Preview",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = uiState.commandPreview,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.Monospace
                                    ),
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }
            }
            
            // Encoding Progress
            val encodingState = uiState.encodingState
            if (encodingState.status != EncodingStatus.IDLE) {
                item {
                    EncodingProgressCard(
                        progress = encodingState.progress,
                        status = encodingState.status,
                        speed = encodingState.speed,
                        time = encodingState.formattedTime,
                        onCancel = { viewModel.cancelEncoding() }
                    )
                }
            } else {
                // Start Encoding Button
                item {
                    Button(
                        onClick = { viewModel.startEncoding() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = uiState.inputPath.isNotBlank() && 
                                 uiState.outputPath.isNotBlank() &&
                                 encodingState.status == EncodingStatus.IDLE
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Start Encoding")
                    }
                }
            }
        }
    }
    
    // Preset Dialog
    if (uiState.showPresetDialog) {
        PresetSelectionDialog(
            builtInPresets = uiState.builtInPresets,
            customPresets = uiState.customPresets,
            onPresetSelected = { viewModel.applyPreset(it) },
            onDismiss = { viewModel.showPresetDialog(false) }
        )
    }
    
    // Error Snackbar
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // Show snackbar
            viewModel.clearError()
        }
    }
}

@Composable
fun MediaInfoCard(info: MediaInfo) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Media Information",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(label = "Duration", value = info.formattedDuration)
                InfoItem(label = "Size", value = info.formattedSize)
                InfoItem(label = "Format", value = info.format.uppercase())
            }
            
            // Video Streams
            if (info.videoStreams.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Video Streams",
                    style = MaterialTheme.typography.labelMedium,
                    color = VideoCodecColor
                )
                info.videoStreams.forEach { stream ->
                    Text(
                        text = "${stream.codec.uppercase()} - ${stream.resolution}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            
            // Audio Streams
            if (info.audioStreams.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Audio Streams",
                    style = MaterialTheme.typography.labelMedium,
                    color = AudioCodecColor
                )
                info.audioStreams.forEach { stream ->
                    Text(
                        text = "${stream.codec.uppercase()} - ${stream.channelsDescription}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun EncodingSettingsCard(
    viewModel: EncodeViewModel,
    uiState: com.ffmpeg.engine.presentation.viewmodel.EncodeUiState
) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Encoding Settings",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }
            }
            
            AnimatedVisibility(visible = expanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Output Format
                    OutlinedTextField(
                        value = uiState.config.outputFormat,
                        onValueChange = { viewModel.updateConfig { copy(outputFormat = it) } },
                        label = { Text("Output Format") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Video Codec
                    OutlinedTextField(
                        value = uiState.config.videoCodec,
                        onValueChange = { viewModel.updateConfig { copy(videoCodec = it) } },
                        label = { Text("Video Codec") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Video Bitrate
                    OutlinedTextField(
                        value = uiState.config.videoBitrate,
                        onValueChange = { viewModel.updateConfig { copy(videoBitrate = it) } },
                        label = { Text("Video Bitrate") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Audio Codec
                    OutlinedTextField(
                        value = uiState.config.audioCodec,
                        onValueChange = { viewModel.updateConfig { copy(audioCodec = it) } },
                        label = { Text("Audio Codec") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Audio Bitrate
                    OutlinedTextField(
                        value = uiState.config.audioBitrate,
                        onValueChange = { viewModel.updateConfig { copy(audioBitrate = it) } },
                        label = { Text("Audio Bitrate") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Resolution
                    OutlinedTextField(
                        value = uiState.config.resolution,
                        onValueChange = { viewModel.updateConfig { copy(resolution = it) } },
                        label = { Text("Resolution (e.g., 1920x1080)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun EncodingProgressCard(
    progress: Float,
    status: EncodingStatus,
    speed: String,
    time: String,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = when (status) {
                    EncodingStatus.PREPARING -> "Preparing..."
                    EncodingStatus.ENCODING -> "Encoding..."
                    EncodingStatus.COMPLETED -> "Completed"
                    EncodingStatus.CANCELLED -> "Cancelled"
                    EncodingStatus.FAILED -> "Failed"
                    else -> ""
                },
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            CircularProgressIndicator(
                progress = progress,
                modifier = Modifier.size(100.dp),
                strokeWidth = 8.dp,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = time, style = MaterialTheme.typography.titleMedium)
                    Text(text = "Time", style = MaterialTheme.typography.bodySmall)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = speed, style = MaterialTheme.typography.titleMedium)
                    Text(text = "Speed", style = MaterialTheme.typography.bodySmall)
                }
            }
            
            if (status == EncodingStatus.ENCODING) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = onCancel,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Stop, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
fun PresetSelectionDialog(
    builtInPresets: List<com.ffmpeg.engine.domain.model.Preset>,
    customPresets: List<com.ffmpeg.engine.domain.model.Preset>,
    onPresetSelected: (com.ffmpeg.engine.domain.model.Preset) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Preset") },
        text = {
            LazyColumn {
                if (builtInPresets.isNotEmpty()) {
                    item {
                        Text(
                            text = "Built-in Presets",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(builtInPresets) { preset ->
                        ListItem(
                            headlineContent = { Text(preset.name) },
                            modifier = Modifier.clickable { onPresetSelected(preset) }
                        )
                    }
                }
                
                if (customPresets.isNotEmpty()) {
                    item {
                        Text(
                            text = "Custom Presets",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(customPresets) { preset ->
                        ListItem(
                            headlineContent = { Text(preset.name) },
                            modifier = Modifier.clickable { onPresetSelected(preset) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
