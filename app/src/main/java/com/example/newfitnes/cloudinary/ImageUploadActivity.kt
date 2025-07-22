package com.example.newfitnes.cloudinary

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.newfitnes.content.ui.theme.NewfitnesTheme
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImageUploadActivity : ComponentActivity() {

    private var isCloudinaryInitialized = false

    companion object {
        private const val CAMERA_PERMISSION_CODE = 1001
        private const val STORAGE_PERMISSION_CODE = 1002
        private const val TAG = "ImageUpload"
    }

    private var currentPhotoPath: String = ""
    private var uploadProgressCallback: ((Float) -> Unit)? = null
    private var uploadStateCallback: ((Boolean, String, String) -> Unit)? = null

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initCloudinary()
        setContent {
            NewfitnesTheme {
                ImageUploadScreen(
                    onCameraPermissionRequest = { requestCameraPermission() },
                    onStoragePermissionRequest = { requestStoragePermission() },
                    checkCameraPermission = { checkCameraPermission() },
                    checkStoragePermission = { checkStoragePermission() },
                    onUploadImage = { uri, onProgress, onStateChange ->
                        uploadProgressCallback = onProgress
                        uploadStateCallback = onStateChange
                        uploadImageToCloudinary(uri)
                    },
                    createImageFile = { createImageFile() }
                )
            }
        }
    }

    private fun initCloudinary() {
        val config = hashMapOf(
            "cloud_name" to "deicfxzhj", // Cambiar por tu cloud name
            "api_key" to "552147737899689",       // Cambiar por tu API key
            "api_secret" to "8b0XVWGWDRPjkUPcA7RqMdYSg7Y"  // Cambiar por tu API secret
        )

        try {
            // Verificación segura de inicialización
            if (MediaManager.get().cloudinary == null) {
                MediaManager.init(this, config)
                Log.d("Cloudinary", "✅ Inicialización exitosa")
            } else {
                Log.d("Cloudinary", "ℹ️ Ya estaba inicializado")
            }
        } catch (e: Exception) {
            Log.e("Cloudinary", "❌ Error de inicialización: ${e.message}")
            Toast.makeText(
                this,
                "Error al conectar con el servicio de imágenes",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun uploadImageToCloudinary(imageUri: Uri) {
        if (!isCloudinaryInitialized) {
            Toast.makeText(this, "Cloudinary no está configurado", Toast.LENGTH_LONG).show()
            return
        }
        Log.d(TAG, "Iniciando upload de imagen: $imageUri")
        uploadStateCallback?.invoke(true, "", "")

        MediaManager.get().upload(imageUri)
            .option("resource_type", "image")
            .option("folder", "app_uploads")
            .option("use_filename", true)
            .option("unique_filename", true)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {
                    Log.d(TAG, "Upload iniciado: $requestId")
                    runOnUiThread {
                        uploadProgressCallback?.invoke(0.1f)
                    }
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    val progress = if (totalBytes > 0) bytes.toFloat() / totalBytes.toFloat() else 0f
                    runOnUiThread {
                        uploadProgressCallback?.invoke(progress)
                        Log.d(TAG, "Progreso: ${(progress * 100).toInt()}%")
                    }
                }

                override fun onSuccess(requestId: String, resultData: MutableMap<Any?, Any?>?) {
                    val imageUrl = resultData?.get("secure_url") as? String ?: ""
                    val publicId = resultData?.get("public_id") as? String ?: ""

                    runOnUiThread {
                        uploadStateCallback?.invoke(false, imageUrl, publicId)
                        Toast.makeText(this@ImageUploadActivity, "¡Imagen subida exitosamente!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Upload exitoso - URL: $imageUrl")
                        Log.d(TAG, "Public ID: $publicId")
                    }
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    runOnUiThread {
                        uploadStateCallback?.invoke(false, "", "")
                        val errorMessage = "Error al subir imagen: ${error.description}"
                        Toast.makeText(this@ImageUploadActivity, errorMessage, Toast.LENGTH_LONG).show()
                        Log.e(TAG, "Error en upload: ${error.description}")
                    }
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    Log.d(TAG, "Upload reprogramado: ${error.description}")
                }
            })
            .dispatch()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(null)

        return File.createTempFile(
            "IMG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
            Log.d(TAG, "Archivo temporal creado: $absolutePath")
        }
    }

    private fun checkCameraPermission(): Boolean {
        val hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        Log.d(TAG, "Permiso de cámara: ${if (hasPermission) "Concedido" else "Denegado"}")
        return hasPermission
    }

    private fun checkStoragePermission(): Boolean {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        val hasPermission = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        Log.d(TAG, "Permiso de almacenamiento ($permission): ${if (hasPermission) "Concedido" else "Denegado"}")
        return hasPermission
    }

    private fun requestCameraPermission() {
        Log.d(TAG, "Solicitando permiso de cámara")
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
    }

    private fun requestStoragePermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        Log.d(TAG, "Solicitando permiso de almacenamiento: $permission")
        ActivityCompat.requestPermissions(this, arrayOf(permission), STORAGE_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permiso de cámara concedido")
                    Toast.makeText(this, "Permiso de cámara concedido", Toast.LENGTH_SHORT).show()
                } else {
                    Log.w(TAG, "Permiso de cámara denegado")
                    Toast.makeText(this, "Se necesita permiso de cámara para tomar fotos", Toast.LENGTH_LONG).show()
                }
            }
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permiso de almacenamiento concedido")
                    Toast.makeText(this, "Permiso de almacenamiento concedido", Toast.LENGTH_SHORT).show()
                } else {
                    Log.w(TAG, "Permiso de almacenamiento denegado")
                    Toast.makeText(this, "Se necesita permiso de almacenamiento para acceder a las imágenes", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageUploadScreen(
    onCameraPermissionRequest: () -> Unit,
    onStoragePermissionRequest: () -> Unit,
    checkCameraPermission: () -> Boolean,
    checkStoragePermission: () -> Boolean,
    onUploadImage: (Uri, (Float) -> Unit, (Boolean, String, String) -> Unit) -> Unit,
    createImageFile: () -> File
) {
    val context = LocalContext.current
    var isUploading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableStateOf(0f) }
    var imageUrl by remember { mutableStateOf("") }
    var publicId by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var showPermissionRationale by remember { mutableStateOf(false) }

    val onProgressUpdate: (Float) -> Unit = { progress ->
        uploadProgress = progress
    }

    val onStateChange: (Boolean, String, String) -> Unit = { uploading, url, id ->
        isUploading = uploading
        if (!uploading) {
            imageUrl = url
            publicId = id
            uploadProgress = 0f
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                Log.d("Gallery", "Imagen seleccionada: $uri")
                onUploadImage(uri, onProgressUpdate, onStateChange)
            } ?: run {
                Toast.makeText(context, "No se pudo obtener la imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            photoUri?.let { uri ->
                Log.d("Camera", "Foto tomada: $uri")
                onUploadImage(uri, onProgressUpdate, onStateChange)
            } ?: run {
                Toast.makeText(context, "No se pudo obtener la foto", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.d("Camera", "Captura cancelada o falló")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Subir Imagen a Cloudinary",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Información de permisos
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "Estado de Permisos",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Icon(
                            imageVector = if (checkCameraPermission()) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = null,
                            tint = if (checkCameraPermission()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Cámara: ${if (checkCameraPermission()) "Concedido" else "Denegado"}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = if (checkStoragePermission()) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = null,
                            tint = if (checkStoragePermission()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Almacenamiento: ${if (checkStoragePermission()) "Concedido" else "Denegado"}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            // Sección de selección de imagen
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Seleccionar Fuente de Imagen",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón Galería
                    Button(
                        onClick = {
                            if (checkStoragePermission()) {
                                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                intent.type = "image/*"
                                galleryLauncher.launch(intent)
                            } else {
                                onStoragePermissionRequest()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isUploading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Seleccionar de Galería")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botón Cámara
                    FilledTonalButton(
                        onClick = {
                            if (checkCameraPermission()) {
                                try {
                                    val photoFile = createImageFile()
                                    photoUri = FileProvider.getUriForFile(
                                        context,
                                        "${context.packageName}.fileprovider",
                                        photoFile
                                    )
                                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                                        putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                                    }
                                    cameraLauncher.launch(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error al abrir la cámara: ${e.message}", Toast.LENGTH_LONG).show()
                                    Log.e("Camera", "Error opening camera", e)
                                }
                            } else {
                                onCameraPermissionRequest()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isUploading
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Tomar Foto")
                    }
                }
            }

            // Resto del código UI (progreso y resultado) permanece igual...
            if (isUploading) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Subiendo Imagen...",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LinearProgressIndicator(
                            progress = { uploadProgress },
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${(uploadProgress * 100).toInt()}%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

            // Sección de resultado
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Resultado de Subida",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (imageUrl.isNotEmpty()) {
                        OutlinedCard(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Link,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = "URL:",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = imageUrl,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }

                    if (publicId.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedCard(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Key,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = "ID Público:",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = publicId,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }

                    if (imageUrl.isEmpty() && publicId.isEmpty() && !isUploading) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f),
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = "No hay imágenes subidas aún",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Text(
                                text = "Selecciona una imagen de la galería o toma una foto",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}