package com.example.newfitnes.content

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newfitnes.content.DataClass.*
import com.example.newfitnes.content.ui.theme.NewfitnesTheme
import com.example.newfitnes.content.ui.theme.*

class EntrenadoresActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NewfitnesTheme  {
                EntrenadoresScreen(
                    onBackClick = {
                        finish() // Regresa a la pantalla anterior
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntrenadoresScreen(
    onBackClick: () -> Unit = {}
) {
    // Lista de entrenadores de ejemplo
    val entrenadores = remember {
        listOf(
            Entrenador(
                id = 1,
                nombre = "Carlos Mendoza",
                especialidad = "Entrenamiento Funcional",
                experiencia = "5 años de experiencia",
                cualidades = listOf("Intensivo", "Motivador", "Disciplinado", "Profesional"),
                calificacion = 4.8f
            ),
            Entrenador(
                id = 2,
                nombre = "Ana García",
                especialidad = "Yoga y Pilates",
                experiencia = "7 años de experiencia",
                cualidades = listOf("Amable", "Paciente", "Flexible", "Comprensiva"),
                calificacion = 4.9f
            ),
            Entrenador(
                id = 3,
                nombre = "Miguel Torres",
                especialidad = "Musculación",
                experiencia = "3 años de experiencia",
                cualidades = listOf("Intensivo", "Técnico", "Exigente", "Resultados"),
                calificacion = 4.6f
            ),
            Entrenador(
                id = 4,
                nombre = "Sofia Ruiz",
                especialidad = "Cardio y Fitness",
                experiencia = "4 años de experiencia",
                cualidades = listOf("Energética", "Amable", "Divertida", "Motivadora"),
                calificacion = 4.7f
            )
        )
    }

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
                            Icons.Filled.Person,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Entrenadores",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Volver",
                            tint = TextPrimary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            BackgroundDark,
                            BackgroundBlack
                        )
                    )
                )
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(entrenadores) { entrenador ->
                EntrenadorCard(entrenador = entrenador)
            }
        }
    }
}

@Composable
fun EntrenadorCard(entrenador: Entrenador) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceDark
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header con imagen y datos básicos
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Imagen del entrenador (usando icono por defecto)
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    PrimaryGreen,
                                    PrimaryGreen.copy(alpha = 0.8f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "Foto del entrenador",
                        tint = TextOnPrimary,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Información básica
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = entrenador.nombre,
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = entrenador.especialidad,
                        color = PrimaryGreen,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = entrenador.experiencia,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    // Calificación
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = "Calificación",
                            tint = Warning,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${entrenador.calificacion}",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Divisor
            HorizontalDivider(
                color = DividerColor,
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Cualidades
            Text(
                text = "Cualidades:",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Chips de cualidades
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                entrenador.cualidades.take(2).forEach { cualidad ->
                    CualidadChip(cualidad = cualidad)
                }
            }

            if (entrenador.cualidades.size > 2) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    entrenador.cualidades.drop(2).forEach { cualidad ->
                        CualidadChip(cualidad = cualidad)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de acción
            Button(
                onClick = { /* Acción para seleccionar entrenador */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Filled.FitnessCenter,
                    contentDescription = null,
                    tint = TextOnPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Seleccionar Entrenador",
                    color = TextOnPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CualidadChip(cualidad: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = PrimaryGreen.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            PrimaryGreen.copy(alpha = 0.5f)
        )
    ) {
        Text(
            text = cualidad,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = PrimaryGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EntrenadoresScreenPreview() {
    NewfitnesTheme  {
        EntrenadoresScreen()
    }
}