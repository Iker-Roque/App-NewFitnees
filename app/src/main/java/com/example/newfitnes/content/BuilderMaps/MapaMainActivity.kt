// MapActivity.kt
package com.example.miapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ciudadId = intent.getIntExtra("ciudad_id", 0)
        val nombre = intent.getStringExtra("nombre") ?: ""
        val latitud = intent.getDoubleExtra("latitud", 0.0)
        val longitud = intent.getDoubleExtra("longitud", 0.0)
        val descripcion = intent.getStringExtra("descripcion") ?: ""
        val imageUrl = intent.getStringExtra("imageUrl") ?: ""

        setContent {
            MaterialTheme {
                MapScreen(
                    nombre = nombre,
                    latitud = latitud,
                    longitud = longitud,
                    descripcion = descripcion,
                    imageUrl = imageUrl,
                    onBackClick = { finish() },
                    onCircleClick = {
                        val intent = Intent(this, DetailActivity::class.java).apply {
                            putExtra("nombre", nombre)
                            putExtra("descripcion", descripcion)
                            putExtra("imageUrl", imageUrl)
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    nombre: String,
    latitud: Double,
    longitud: Double,
    descripcion: String,
    imageUrl: String,
    onBackClick: () -> Unit,
    onCircleClick: () -> Unit
) {
    var showMarkerInfo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mapa - $nombre",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Simulación del mapa con gradiente
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF87CEEB), // Sky blue
                                Color(0xFF98D8E8),
                                Color(0xFFF0F8FF)  // Alice blue
                            )
                        )
                    )
            )

            // Círculo verde semitransparente clickeable
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = (-20).dp, y = 30.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Green.copy(alpha = 0.3f))
                        .clickable { onCircleClick() }
                        .border(2.dp, Color.Green.copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Zona de\nInfluencia",
                        color = Color.Green.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Marcador principal
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = (-20).dp, y = 30.dp)
                    .clickable { showMarkerInfo = !showMarkerInfo }
            ) {
                // Pin del marcador
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Marcador",
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(4.dp, CircleShape),
                    tint = Color.Red
                )

                // Información del marcador (tooltip)
                if (showMarkerInfo) {
                    Card(
                        modifier = Modifier
                            .offset(x = 50.dp, y = (-40).dp)
                            .width(200.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = nombre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Lat: ${String.format("%.4f", latitud)}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Lng: ${String.format("%.4f", longitud)}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Marcadores adicionales simulados
            repeat(5) { index ->
                val angle = (index * 72) * Math.PI / 180 // 72 grados entre cada marcador
                val radius = 150.dp
                val offsetX = (cos(angle) * radius.value).dp
                val offsetY = (sin(angle) * radius.value).dp

                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Marcador secundario",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(x = offsetX, y = offsetY)
                        .size(24.dp)
                        .alpha(0.6f),
                    tint = Color.Blue
                )
            }

            // Información en la parte inferior
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Información de Ubicación",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = descripcion,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Latitud:",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = String.format("%.6f", latitud),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Column {
                            Text(
                                text = "Longitud:",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = String.format("%.6f", longitud),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onCircleClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Ver Detalles Completos")
                    }
                }
            }

            // Instrucciones
            Card(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .width(180.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "Instrucciones:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "• Toca el marcador rojo para ver info\n• Toca el círculo verde para ver detalles",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}