package com.example.newfitnes.content

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newfitnes.content.ui.theme.BackgroundDark
import com.example.newfitnes.content.ui.theme.NewfitnesTheme
import com.example.newfitnes.content.ui.theme.PrimaryGreen
import com.example.newfitnes.content.ui.theme.TextPrimary
import com.example.newfitnes.ui.theme.ui.theme.*

class SuscripcionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewfitnesTheme {

                SuscripcionScreen()

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuscripcionScreen(
    onBackClick: () -> Unit = {}
) {
    var selectedPlan by remember { mutableStateOf("premium") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark),
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
                            "Planes De Suscripcion",
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
                            tint = PrimaryGreen
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark)
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Header con beneficios premium
                HeaderSection()
            }

            item {
                // Plan Básico
                PlanCard(
                    title = "Plan Básico",
                    subtitle = "Acceso limitado",
                    price = "Gratis",
                    features = listOf(
                        "3 entrenamientos por semana",
                        "Rutinas básicas",
                        "Seguimiento básico"
                    ),
                    isSelected = selectedPlan == "basic",
                    isRecommended = false,
                    onSelect = { selectedPlan = "basic" }
                )
            }

            item {
                // Plan Premium
                PlanCard(
                    title = "Plan Premium",
                    subtitle = "Acceso completo",
                    price = "$9.99/mes",
                    features = listOf(
                        "Entrenamientos ilimitados",
                        "Rutinas personalizadas",
                        "Seguimiento avanzado",
                        "Nutrición personalizada",
                        "Sin anuncios"
                    ),
                    isSelected = selectedPlan == "premium",
                    isRecommended = true,
                    onSelect = { selectedPlan = "premium" }
                )
            }

            item {
                // Plan Pro
                PlanCard(
                    title = "Plan Pro",
                    subtitle = "Para atletas",
                    price = "$19.99/mes",
                    features = listOf(
                        "Todo del Plan Premium",
                        "Entrenador personal virtual",
                        "Análisis de rendimiento",
                        "Videos HD sin límite",
                        "Soporte prioritario"
                    ),
                    isSelected = selectedPlan == "pro",
                    isRecommended = false,
                    onSelect = { selectedPlan = "pro" }
                )
            }

            item {
                // Botón de suscripción
                Button(
                    onClick = { /* Acción de suscripción */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "SUSCRIBIRSE AHORA",
                        color = TextOnPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                // Footer con garantía
                FooterSection()
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(PrimaryGreen, PrimaryGreen.copy(alpha = 0.8f))
                ),
                shape = RoundedCornerShape(20.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = TextOnPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¡Desbloquea tu potencial!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextOnPrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Accede a entrenamientos personalizados y alcanza tus metas fitness",
                fontSize = 16.sp,
                color = TextOnPrimary.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun PlanCard(
    title: String,
    subtitle: String,
    price: String,
    features: List<String>,
    isSelected: Boolean,
    isRecommended: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isSelected) {
                    Modifier.border(
                        2.dp,
                        PrimaryGreen,
                        RoundedCornerShape(16.dp)
                    )
                } else {
                    Modifier
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SurfaceDark else CardBackground
        ),
        shape = RoundedCornerShape(16.dp),
        onClick = onSelect
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                if (isRecommended) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "RECOMENDADO",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextOnPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = price,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )

            Spacer(modifier = Modifier.height(20.dp))

            features.forEach { feature ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = feature,
                        color = TextPrimary,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isSelected) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Plan seleccionado",
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FooterSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Security,
                contentDescription = null,
                tint = PrimaryGreen,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Garantía de 7 días o te devolvemos tu dinero",
                color = TextSecondary,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Cancela cuando quieras • Sin compromisos",
            color = TextSecondary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun SuscripcionScreenPreview() {
    NewfitnesTheme {
        SuscripcionScreen()
    }
}