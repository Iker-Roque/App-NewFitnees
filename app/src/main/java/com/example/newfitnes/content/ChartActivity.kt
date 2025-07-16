package com.example.newfitnes.content

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newfitnes.content.ui.theme.NewfitnesTheme
import com.example.newfitnes.dao.DatabaseProvider
import com.example.newfitnes.dao.User
import com.example.newfitnes.dao.UserDao
import com.example.newfitnes.ui.theme.ui.theme.*
import kotlin.math.max

class ChartActivity : ComponentActivity() {

    private lateinit var userDao: UserDao

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = DatabaseProvider.getDatabase(this)
        userDao = database.userDao()

        enableEdgeToEdge()
        setContent {
            val userList = remember { mutableStateOf(listOf<User>()) }
            val chartData = remember { mutableStateOf(listOf<ChartDataItem>()) }

            LaunchedEffect(Unit) {
                userDao.getAllUsers().collect { users ->
                    userList.value = users
                    // Crear datos para la gráfica - usando duración por usuario
                    chartData.value = users.map { user ->
                        ChartDataItem(
                            label = user.name,
                            value = user.duration.toFloat(),
                            color = getColorForIndex(users.indexOf(user))
                        )
                    }
                }
            }

            NewfitnesTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = BackgroundDark,
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    "Gráfica de Duración de Rutinas",
                                    color = TextPrimary,
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        Icons.Default.ArrowBackIosNew,
                                        contentDescription = "Volver",
                                        tint = PrimaryGreen
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = SurfaceDark
                            )
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        if (chartData.value.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No hay datos para mostrar",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextSecondary
                                )
                            }
                        } else {
                            Text(
                                text = "Duración de Rutinas por Usuario (minutos)",
                                style = MaterialTheme.typography.displaySmall,
                                color = PrimaryGreen,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            HorizontalBarChart(
                                data = chartData.value,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Datos de las Rutinas:",
                                style = MaterialTheme.typography.titleMedium,
                                color = PrimaryGreen,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            LazyColumn {
                                items(userList.value) { user ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = CardBackground
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(16.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = user.name,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = TextPrimary,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "${user.duration} min",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = PrimaryGreen
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class ChartDataItem(
    val label: String,
    val value: Float,
    val color: ComposeColor
)

@Composable
fun HorizontalBarChart(
    data: List<ChartDataItem>,
    modifier: Modifier = Modifier
) {
    val maxValue = data.maxOfOrNull { it.value } ?: 1f
    val density = LocalDensity.current

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val barHeight = (canvasHeight - 40.dp.toPx()) / data.size
        val maxBarWidth = canvasWidth * 0.6f // 60% del ancho para las barras
        val labelWidth = canvasWidth * 0.3f // 30% del ancho para las etiquetas

        data.forEachIndexed { index, item ->
            val barWidth = (item.value / maxValue) * maxBarWidth
            val y = index * barHeight + 20.dp.toPx()

            // Dibujar la barra
            drawRect(
                color = item.color,
                topLeft = androidx.compose.ui.geometry.Offset(labelWidth, y),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight * 0.7f)
            )

            // Dibujar etiqueta (nombre)
            drawContext.canvas.nativeCanvas.apply {
                val paint = android.graphics.Paint().apply {
                    color = TextPrimary.toArgb()
                    textSize = with(density) { 12.sp.toPx() }
                    isAntiAlias = true
                }

                val textBounds = Rect()
                paint.getTextBounds(item.label, 0, item.label.length, textBounds)

                drawText(
                    item.label,
                    10f,
                    y + (barHeight * 0.7f) / 2 + textBounds.height() / 2,
                    paint
                )
            }

            // Dibujar valor en la barra
            drawContext.canvas.nativeCanvas.apply {
                val paint = android.graphics.Paint().apply {
                    color = Color.WHITE
                    textSize = with(density) { 11.sp.toPx() }
                    isAntiAlias = true
                    isFakeBoldText = true
                }

                val valueText = "${item.value.toInt()} min"
                val textBounds = Rect()
                paint.getTextBounds(valueText, 0, valueText.length, textBounds)

                // Posicionar el texto en el centro de la barra
                val textX = labelWidth + barWidth / 2 - textBounds.width() / 2
                val textY = y + (barHeight * 0.7f) / 2 + textBounds.height() / 2

                drawText(
                    valueText,
                    textX,
                    textY,
                    paint
                )
            }
        }
    }
}

fun getColorForIndex(index: Int): ComposeColor {
    val colors = listOf(
        ComposeColor(0xFF4CAF50), // Verde
        ComposeColor(0xFF2196F3), // Azul
        ComposeColor(0xFFFF9800), // Naranja
        ComposeColor(0xFF9C27B0), // Púrpura
        ComposeColor(0xFFF44336), // Rojo
        ComposeColor(0xFF00BCD4), // Cian
        ComposeColor(0xFFFFEB3B), // Amarillo
        ComposeColor(0xFF607D8B)  // Gris azulado
    )
    return colors[index % colors.size]
}