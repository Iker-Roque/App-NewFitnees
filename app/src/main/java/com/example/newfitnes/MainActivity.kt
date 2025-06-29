package com.example.newfitnes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newfitnes.content.ui.theme.*
import com.example.newfitnes.content.ui.theme.NewfitnesTheme

// MainActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NewfitnesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundDark
                ) {
                    ActivationScreen(
                        onStartTraining = {
                            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivationScreen(
    onStartTraining: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "NewFitnes",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "SUI",
                            fontSize = 12.sp,
                            color = PrimaryGreen,
                            letterSpacing = 2.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            BackgroundDark,
                            SurfaceDark,
                            CardBackground
                        )
                    )
                )
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Spacer para empujar el contenido hacia el centro
            Spacer(modifier = Modifier.weight(1f))

            // Icono del corredor
            Card(
                modifier = Modifier
                    .size(120.dp),
                shape = RoundedCornerShape(60.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardBackground
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.imagen_corredor),
                        contentDescription = "Running icon",
                        modifier = Modifier.size(170.dp),
                        tint = PrimaryGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Título
            Text(
                text = "Activate Entrenaz",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Botón de empezar
            Button(
                onClick = onStartTraining,
                modifier = Modifier
                    .width(160.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = TextOnPrimary
                ),
                shape = RoundedCornerShape(24.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "Inicio",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextOnPrimary
                )
            }

            // Spacer para balancear el layout
            Spacer(modifier = Modifier.weight(1.5f))

            // Texto de copyright en la parte inferior
            Text(
                text = "© 2024 NewFitnes - Todos los derechos reservados",
                fontSize = 12.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivationScreenPreview() {
    NewfitnesTheme {
        ActivationScreen()
    }
}