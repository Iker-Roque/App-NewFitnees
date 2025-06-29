package com.example.newfitnes.content

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.example.newfitnes.api.Rutina
import com.example.newfitnes.api.ApiServices
import com.example.newfitnes.ui.theme.ui.theme.NewfitnesTheme
import com.example.newfitnes.ui.theme.ui.theme.*


import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class RutinasActivity : ComponentActivity() {

    private val BASE_URL = "https://fixed.alwaysdata.net/fitness-api/api/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            NewfitnesTheme {
                RutinasScreen()
            }
        }
    }

    @Composable
    fun RutinasScreen() {
        var rutinas by remember { mutableStateOf<List<Rutina>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        // Lanzar petición Retrofit una vez
        LaunchedEffect(Unit) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiServices::class.java)

            apiService.getRutinas().enqueue(object : Callback<List<Rutina>> {
                override fun onResponse(call: Call<List<Rutina>>, response: Response<List<Rutina>>) {
                    if (response.isSuccessful) {
                        rutinas = response.body() ?: emptyList()
                    } else {
                        errorMessage = "Error en la respuesta"
                    }
                    isLoading = false
                }

                override fun onFailure(call: Call<List<Rutina>>, t: Throwable) {
                    errorMessage = "Fallo en la conexión: ${t.message}"
                    isLoading = false
                }
            })
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = BackgroundDark
        ) {
            when {
                isLoading -> {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = PrimaryGreen,
                                modifier = Modifier.size(60.dp),
                                strokeWidth = 4.dp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Cargando rutinas...",
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Por favor espera un momento",
                                color = TextSecondary,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                errorMessage != null -> {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = errorMessage ?: "Error desconocido",
                                color = Error,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Verifica tu conexión a internet",
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Rutinas de Ejercicio",
                            style = MaterialTheme.typography.headlineLarge,
                            color = PrimaryGreen,
                            modifier = Modifier.padding(30.dp)
                        )

                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(rutinas) { rutina ->
                                RutinaCard(rutina)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun RutinaCard(rutina: Rutina) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardBackground
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = rutina.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = PrimaryGreen
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = rutina.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Nivel con color según la dificultad
                val nivelColor = when (rutina.nivel.lowercase()) {
                    "principiante", "básico", "fácil" -> Success
                    "intermedio", "medio" -> Warning
                    "avanzado", "difícil", "experto" -> Error
                    else -> Info
                }

                Text(
                    text = "Nivel: ${rutina.nivel}",
                    style = MaterialTheme.typography.bodySmall,
                    color = nivelColor
                )
            }
        }
    }
}