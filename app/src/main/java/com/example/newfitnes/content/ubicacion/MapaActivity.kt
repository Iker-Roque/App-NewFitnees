package com.example.newfitnes.content.ubicacion

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newfitnes.content.ui.theme.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*


class MapaActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Obtener datos del gym del Intent
        val gymName = intent.getStringExtra("gym_name") ?: "Gym"
        val gymAddress = intent.getStringExtra("gym_address") ?: ""
        val gymLatitude = intent.getDoubleExtra("gym_latitude", 0.0)
        val gymLongitude = intent.getDoubleExtra("gym_longitude", 0.0)

        setContent {
            NewfitnesTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = BackgroundDark,
                                titleContentColor = TextPrimary,
                            ),
                            title = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Filled.LocationOn,
                                        contentDescription = null,
                                        tint = PrimaryGreen,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        gymName,
                                        fontSize = 18.sp,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = TextPrimary,
                                        maxLines = 1
                                    )
                                }
                            },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        Icons.Filled.ArrowBackIosNew,
                                        contentDescription = "Volver",
                                        tint = PrimaryGreen
                                    )
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                abrirGoogleMaps(gymLatitude, gymLongitude, gymName)
                            },
                            containerColor = PrimaryGreen,
                            contentColor = BackgroundDark
                        ) {
                            Icon(
                                imageVector = Icons.Default.Directions,
                                contentDescription = "Direcciones"
                            )
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = BackgroundDark
                    ) {
                        MapScreen(
                            gymName = gymName,
                            gymAddress = gymAddress,
                            latitude = gymLatitude,
                            longitude = gymLongitude
                        )
                    }
                }
            }
        }
    }

    private fun abrirGoogleMaps(latitude: Double, longitude: Double, gymName: String = "") {
        try {
            // Intentar abrir Google Maps con navegación
            val navigationUri = "google.navigation:q=$latitude,$longitude"
            val navigationIntent = Intent(Intent.ACTION_VIEW, Uri.parse(navigationUri))
            navigationIntent.setPackage("com.google.android.apps.maps")

            if (navigationIntent.resolveActivity(packageManager) != null) {
                startActivity(navigationIntent)
                return
            }

            // Si no funciona la navegación, intentar abrir ubicación en Google Maps
            val mapsUri = "geo:$latitude,$longitude?q=$latitude,$longitude($gymName)"
            val mapsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapsUri))
            mapsIntent.setPackage("com.google.android.apps.maps")

            if (mapsIntent.resolveActivity(packageManager) != null) {
                startActivity(mapsIntent)
                return
            }

            // Si Google Maps no está instalado, abrir en el navegador
            val webUri = "https://www.google.com/maps/dir/?api=1&destination=$latitude,$longitude"
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webUri))
            startActivity(webIntent)

        } catch (e: Exception) {
            // Fallback: abrir en el navegador web
            val webUri = "https://www.google.com/maps/@$latitude,$longitude,15z"
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webUri))
            startActivity(webIntent)
        }
    }
}

@Composable
fun MapScreen(
    gymName: String,
    gymAddress: String,
    latitude: Double,
    longitude: Double
) {
    val gymLocation = LatLng(latitude, longitude)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(gymLocation, 15f)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Card con información del gym
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = SurfaceDark
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = gymName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                if (gymAddress.isNotEmpty()) {
                    Text(
                        text = gymAddress,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Mapa
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                compassEnabled = true,
                myLocationButtonEnabled = false
            ),
            properties = MapProperties(
                mapType = MapType.NORMAL
            )
        ) {
            Marker(
                state = MarkerState(position = gymLocation),
                title = gymName,
                snippet = gymAddress
            )
        }
    }
}