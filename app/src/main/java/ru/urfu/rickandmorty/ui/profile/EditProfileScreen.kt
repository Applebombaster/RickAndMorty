package ru.urfu.rickandmorty.ui.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: EditProfileViewModel = viewModel()
) {
    val profile by viewModel.profile.collectAsState()
    val context = LocalContext.current
    
    // Состояние для диалога выбора
    var showImageSourceDialog by remember { mutableStateOf(false) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // Лаунчер для Галереи
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) viewModel.onAvatarChanged(uri.toString())
    }

    // Лаунчер для Камеры
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && tempCameraUri != null) {
            viewModel.onAvatarChanged(tempCameraUri.toString())
        }
    }

    // Лаунчер для разрешений (Галерея)
    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            galleryLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Нет доступа к галерее", Toast.LENGTH_SHORT).show()
        }
    }

    // Лаунчер для разрешений (Камера)
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val file = File(context.cacheDir, "images").apply { mkdirs() }
            val newFile = File(file, "avatar_${System.currentTimeMillis()}.jpg")
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", newFile)
            tempCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Нет доступа к камере", Toast.LENGTH_SHORT).show()
        }
    }

    // Функция проверки разрешений для Галереи
    fun openGallery() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            galleryLauncher.launch("image/*")
        } else {
            galleryPermissionLauncher.launch(permission)
        }
    }

    // Функция проверки разрешений для Камеры
    fun openCamera() {
        val permission = Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            val file = File(context.cacheDir, "images").apply { mkdirs() }
            val newFile = File(file, "avatar_${System.currentTimeMillis()}.jpg")
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", newFile)
            tempCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            cameraPermissionLauncher.launch(permission)
        }
    }

    // Диалог выбора
    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Откуда взять фото?") },
            confirmButton = {
                TextButton(onClick = { 
                    showImageSourceDialog = false
                    openCamera() 
                }) { Text("Камера") }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showImageSourceDialog = false
                    openGallery() 
                }) { Text("Галерея") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактирование") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Аватарка
            if (profile.avatarUri.isNotEmpty()) {
                AsyncImage(
                    model = profile.avatarUri,
                    contentDescription = "Аватар",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .clickable { showImageSourceDialog = true }
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .clickable { showImageSourceDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Нажмите\nдля фото", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = profile.fullName,
                onValueChange = viewModel::onNameChanged,
                label = { Text("ФИО") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = profile.jobTitle,
                onValueChange = viewModel::onJobChanged,
                label = { Text("Должность") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = profile.resumeUrl,
                onValueChange = viewModel::onResumeChanged,
                label = { Text("Ссылка на резюме (URL)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { viewModel.saveProfile(onSaved) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Готово")
            }
        }
    }
}
