package com.example.newfitnes.content.ubicacion

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.newfitnes.api.Ubicacion
import com.example.newfitnes.content.ui.theme.*


@Composable
fun UbicacionList(
    ubicaciones: List<Ubicacion>,
    onUbicacionClick: (Ubicacion) -> Unit
) {
    LazyColumn {
        items(ubicaciones) { ubicacion ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable { onUbicacionClick(ubicacion) },
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor  = CardBackground,
                    contentColor  = TextPrimary,
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ubicacion.foto,
                        contentDescription = ubicacion.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = ubicacion.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = ubicacion.descripcion,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Ver en mapa",
                        tint = PrimaryGreen,
                        modifier = Modifier.size(24.dp)
                    )
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
    val context = LocalContext.current
    val ubicaciones by ubicacionViewModel.ubicaciones.collectAsState()
    val error by ubicacionViewModel.error.collectAsState()
    val isLoading by ubicacionViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        ubicacionViewModel.loadUbicaciones()
    }

    when {
        isLoading -> {
            LoadingUbicacionesContent()
        }
        error != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Error: $error",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { ubicacionViewModel.retry() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen
                    )
                ) {
                    Text("Reintentar")
                }
            }
        }
        else -> {
            UbicacionList(
                ubicaciones = ubicaciones,
                onUbicacionClick = { ubicacion ->
                    val intent = Intent(context, MapaActivity::class.java).apply {
                        putExtra("gym_name", ubicacion.nombre)
                        putExtra("gym_address", ubicacion.descripcion) // o ubicacion.direccion si existe
                        // Ajusta estas propiedades según tu modelo Ubicacion
                        putExtra("gym_latitude", ubicacion.latitud)
                        putExtra("gym_longitude", ubicacion.longitud)
                    }
                    context.startActivity(intent)
                }
            )
        }
    }
}