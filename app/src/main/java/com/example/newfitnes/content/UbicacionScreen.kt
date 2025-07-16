package com.example.newfitnes.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.newfitnes.api.Ubicacion
import com.example.newfitnes.content.ui.theme.*


@Composable
fun UbicacionList(ubicaciones: List<Ubicacion>) {
    LazyColumn {
        items(ubicaciones) { ubicacion ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor  = CardBackground,
                    contentColor  = TextPrimary,
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(modifier = Modifier.padding(8.dp)) {
                    AsyncImage(
                        model = ubicacion.foto,
                        contentDescription = ubicacion.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = ubicacion.nombre, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = ubicacion.descripcion, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingUbicacionesContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = PrimaryGreen,
            modifier = Modifier.size(60.dp),
            strokeWidth = 4.dp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Cargando ubicaciones...",
            color = TextPrimary,
            fontSize = 18.sp,
            style = MaterialTheme.typography.labelLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Por favor espera un momento",
            color = TextSecondary,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 14.sp
        )
    }
}

@Composable
fun UbicacionScreen(ubicacionViewModel: UbicacionViewModel = viewModel()) {
    val ubicaciones by ubicacionViewModel.ubicaciones.collectAsState()
    val error by ubicacionViewModel.error.collectAsState()
    val isLoading by ubicacionViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        ubicacionViewModel.loadUbicaciones()
    }
    when {
        isLoading -> {
            LoadingUbicacionesContent() // <-- Aquí es donde se usa
        }
        error != null -> {
            Text(
                "Error: $error",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
        else -> {
            UbicacionList(ubicaciones)
        }
    }
}