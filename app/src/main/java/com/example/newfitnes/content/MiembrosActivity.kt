package com.example.newfitnes.content

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newfitnes.content.ui.theme.*

class MiembrosActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NewfitnesTheme {
                MiembrosScreen(
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
fun MiembrosScreen(
    onBackClick: () -> Unit = {}
) {
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
                            "Miembros",
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.labelMedium,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            BackgroundDark,
                            SurfaceDark
                        )
                    )
                )
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Aquí puedes agregar el contenido específico de Miembros
            Text(
                text = "Pantalla de Miembros",
                color = TextPrimary,
                fontSize = 24.sp,
                style = MaterialTheme.typography.labelMedium,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Aquí irá el diseño para gestionar miembros",
                color = TextSecondary,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MiembrosScreenPreview() {
    NewfitnesTheme {
        MiembrosScreen()
    }
}