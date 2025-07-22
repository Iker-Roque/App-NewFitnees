package com.example.newfitnes.nuevafuncion

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.newfitnes.content.ui.theme.BackgroundBlack
import com.example.newfitnes.content.ui.theme.BackgroundDark
import com.example.newfitnes.content.ui.theme.CardBackground
import com.example.newfitnes.content.ui.theme.TextPrimary
import com.example.newfitnes.ui.theme.ui.theme.NewfitnesTheme
import com.example.newfitnes.ui.theme.ui.theme.*
import kotlinx.coroutines.launch

enum class Screen {
    Workouts, Progress, Nutrition, Community, Calculator, Settings
}

data class NavigationItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

data class WorkoutCategory(
    val name: String,
    val description: String,
    val exercises: Int,
    val duration: String
)

data class NutritionTip(
    val title: String,
    val description: String,
    val calories: String
)

@OptIn(ExperimentalMaterial3Api::class)
class ExtrasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewfitnesTheme {

                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                var currentScreen by remember { mutableStateOf(Screen.Workouts) }

                val navigationItems = listOf(
                    NavigationItem(Screen.Workouts, "Rutinas", Icons.Filled.FitnessCenter),
                    NavigationItem(Screen.Progress, "Progreso", Icons.Filled.Timeline),
                    NavigationItem(Screen.Nutrition, "Nutrición", Icons.Filled.Restaurant),
                    NavigationItem(Screen.Community, "Comunidad", Icons.Filled.Group),
                    NavigationItem(Screen.Calculator, "Calculadoras", Icons.Filled.Calculate),
                    NavigationItem(Screen.Settings, "Configuración", Icons.Filled.Settings)
                )

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            drawerContainerColor = SurfaceDark, // Fondo del drawer personalizado
                            drawerContentColor = TextPrimary    // Color del contenido
                        ) {
                            Spacer(Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Filled.FitnessCenter,
                                    contentDescription = null,
                                    tint = PrimaryGreen,
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Gym Extras",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = DividerColor
                            )

                            navigationItems.forEach { item ->
                                NavigationDrawerItem(
                                    icon = {
                                        Icon(
                                            item.icon,
                                            contentDescription = item.label,
                                            tint = if (item.screen == currentScreen) TextOnPrimary else TextPrimary
                                        )
                                    },
                                    label = {
                                        Text(
                                            item.label,
                                            color = if (item.screen == currentScreen) TextOnPrimary else TextPrimary
                                        )
                                    },
                                    selected = item.screen == currentScreen,
                                    colors = NavigationDrawerItemDefaults.colors(
                                        selectedContainerColor = PrimaryGreen,
                                        unselectedContainerColor = SurfaceDark,
                                        selectedIconColor = TextOnPrimary,
                                        unselectedIconColor = TextPrimary,
                                        selectedTextColor = TextOnPrimary,
                                        unselectedTextColor = TextPrimary
                                    ),
                                    onClick = {
                                        scope.launch { drawerState.close() }
                                        currentScreen = item.screen
                                        Toast.makeText(
                                            context,
                                            "Navegando a ${item.label}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                )
                            }

                            Spacer(Modifier.weight(1f))
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = DividerColor)

                            NavigationDrawerItem(
                                icon = {
                                    Icon(
                                        Icons.Filled.ExitToApp,
                                        contentDescription = "Salir",
                                        tint = Error // Color rojo para salir
                                    )
                                },
                                label = {
                                    Text(
                                        "Volver al Menú Principal",
                                        color = TextPrimary
                                    )
                                },
                                selected = false,
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedContainerColor = SurfaceDark,
                                    unselectedIconColor = Error,
                                    unselectedTextColor = TextPrimary
                                ),
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    Toast.makeText(context, "Volviendo...", Toast.LENGTH_SHORT).show()
                                    finish()
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = BackgroundDark,
                                    titleContentColor = TextPrimary,
                                    navigationIconContentColor = PrimaryGreen  // Color del icono hamburguesa
                                ),
                                title = {
                                    Text(
                                        when(currentScreen) {
                                            Screen.Workouts -> "Rutinas de Entrenamiento"
                                            Screen.Progress -> "Mi Progreso"
                                            Screen.Nutrition -> "Guía Nutricional"
                                            Screen.Community -> "Comunidad Fitness"
                                            Screen.Calculator -> "Calculadoras Fitness"
                                            Screen.Settings -> "Configuración"
                                        }
                                    )
                                },
                                navigationIcon = {
                                    IconButton(
                                        onClick = {
                                            scope.launch {
                                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Menu,
                                            contentDescription = "Abrir menú",
                                            tint = PrimaryGreen // Color verde lima para el icono hamburguesa
                                        )
                                    }
                                }
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .padding(innerPadding)
                                .background(Brush.verticalGradient(colors = listOf(BackgroundDark, BackgroundBlack)))
                                .fillMaxSize()
                        ) {
                            when (currentScreen) {
                                Screen.Workouts -> WorkoutsContent()
                                Screen.Progress -> ProgressContent()
                                Screen.Nutrition -> NutritionContent()
                                Screen.Community -> CommunityContent()
                                Screen.Calculator -> CalculatorContent()
                                Screen.Settings -> SettingsContent()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutsContent() {
    val workoutCategories = listOf(
        WorkoutCategory("Pecho y Tríceps", "Rutina completa de empuje", 8, "45-60 min"),
        WorkoutCategory("Espalda y Bíceps", "Rutina completa de jalón", 10, "50-65 min"),
        WorkoutCategory("Piernas", "Cuádriceps, glúteos y pantorrillas", 12, "60-75 min"),
        WorkoutCategory("Hombros", "Deltoides y trapecio", 8, "40-50 min"),
        WorkoutCategory("Cardio HIIT", "Alta intensidad", 6, "20-30 min"),
        WorkoutCategory("Abdominales", "Core y estabilidad", 10, "25-35 min")
    )

    LazyColumn(
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        BackgroundDark,
                        BackgroundBlack
                    )
                )
            )
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Elige tu rutina de hoy",
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(workoutCategories) { workout ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor  = CardBackground,
                    contentColor  = TextPrimary,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)

            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                workout.name,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                workout.description,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Light,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(
                            Icons.Filled.FitnessCenter,
                            contentDescription = null,
                            tint = PrimaryGreen,
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "${workout.exercises} ejercicios",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Text(
                            workout.duration,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Tu Progreso",
            style = MaterialTheme.typography.headlineSmall,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor  = CardBackground,
                contentColor  = TextPrimary,
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Estadísticas de la Semana", fontFamily = Poppins, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("5", style = MaterialTheme.typography.headlineMedium, color = PrimaryGreen, fontFamily = Poppins, fontWeight = FontWeight.Bold)
                        Text("Entrenamientos", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("285", style = MaterialTheme.typography.headlineMedium, color = PrimaryGreen, fontFamily = Poppins, fontWeight = FontWeight.Bold)
                        Text("Minutos", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("1,450", style = MaterialTheme.typography.headlineMedium, color = PrimaryGreen, fontFamily = Poppins, fontWeight = FontWeight.Bold)
                        Text("Calorías", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor  = CardBackground,
                contentColor  = TextPrimary,
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Récords Personales",fontFamily = Poppins, fontWeight = FontWeight.Light, color = TextPrimary)
                Spacer(Modifier.height(8.dp))
                listOf(
                    "Press Banca: 85kg",
                    "Sentadilla: 120kg",
                    "Peso Muerto: 140kg",
                    "Press Militar: 55kg"
                ).forEach { record ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(record, fontFamily = Poppins, color = TextPrimary)
                        Icon(Icons.Filled.TrendingUp, contentDescription = null, tint = Success)
                    }
                }
            }
        }
    }
}

@Composable
fun NutritionContent() {
    val nutritionTips = listOf(
        NutritionTip("Proteína Post-Entreno", "Batido de proteína con plátano", "250 cal"),
        NutritionTip("Desayuno Energético", "Avena con frutas y frutos secos", "400 cal"),
        NutritionTip("Almuerzo Balanceado", "Pollo con arroz integral y vegetales", "550 cal"),
        NutritionTip("Snack Saludable", "Yogur griego con nueces", "180 cal")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Guía Nutricional",
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(nutritionTips) { tip ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CardBackground)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Restaurant,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(tip.title,fontFamily = Poppins, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(tip.description, fontFamily = Poppins, fontWeight = FontWeight.Light, color = TextSecondary)
                    }
                    Text(
                        tip.calories,
                        color = PrimaryGreen,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CommunityContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Filled.Group,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = PrimaryGreen
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Comunidad Fitness",
            style = MaterialTheme.typography.headlineMedium,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Conecta con otros atletas, comparte tu progreso y mantente motivado",
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            color = TextSecondary
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { /* Implementar funcionalidad */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryGreen,
                contentColor = TextOnPrimary
            )
        ) {
            Text("Próximamente",fontFamily = Poppins, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CalculatorContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Calculadoras Fitness",
            style = MaterialTheme.typography.headlineSmall,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        listOf(
            "IMC (Índice de Masa Corporal)" to Icons.Filled.Scale,
            "Calorías Diarias" to Icons.Filled.LocalFireDepartment,
            "Porcentaje de Grasa Corporal" to Icons.Filled.Analytics,
            "1RM (Repetición Máxima)" to Icons.Filled.FitnessCenter,
            "Macronutrientes" to Icons.Filled.PieChart
        ).forEach { (title, icon) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CardBackground)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Normal,
                        color = TextPrimary
                    )
                    Icon(
                        Icons.Filled.ArrowForward,
                        contentDescription = "Ir a calculadora",
                        tint = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Configuración",
            style = MaterialTheme.typography.headlineSmall,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        listOf(
            "Perfil de Usuario" to Icons.Filled.Person,
            "Notificaciones" to Icons.Filled.Notifications,
            "Unidades (kg/lb)" to Icons.Filled.Scale,
            "Sincronización" to Icons.Filled.Sync,
            "Privacidad" to Icons.Filled.Security,
            "Soporte" to Icons.Filled.Help
        ).forEach { (setting, icon) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CardBackground)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(icon, contentDescription = null, tint = PrimaryGreen)
                    Spacer(Modifier.width(16.dp))
                    Text(setting, modifier = Modifier.weight(1f), fontFamily = Poppins,fontWeight = FontWeight.Normal, color = TextPrimary)
                    Icon(Icons.Filled.ArrowForward, contentDescription = "Configurar", tint = TextSecondary)
                }
            }
        }
    }
}