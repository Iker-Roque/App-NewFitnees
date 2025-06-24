package com.example.newfitnes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.newfitnes.content.MiembrosActivity
import com.example.newfitnes.content.SuscripcionActivity
import com.example.newfitnes.content.EntrenadoresActivity
import com.example.newfitnes.content.RutinasActivity
import com.example.newfitnes.content.UserInsertActivity
import com.example.newfitnes.content.UsuarioActivity
import com.example.newfitnes.content.ui.theme.*
import com.example.newfitnes.ui.theme.ui.theme.NewfitnesTheme

class HomeActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val menuItems = getMenuItems()

        setContent {
            NewfitnesTheme {
                HomeScreen(menuItems) { index ->
                    navigateToScreen(index)
                }
            }
        }
    }

    private fun navigateToScreen(index: Int) {
        when(index) {
            0 -> startActivity(Intent(this, EntrenadoresActivity::class.java))
            1 -> startActivity(Intent(this, SuscripcionActivity::class.java))
            2 -> startActivity(Intent(this, RutinasActivity::class.java))
            3 -> startActivity(Intent(this, UsuarioActivity::class.java))
            4 -> startActivity(Intent(this, UserInsertActivity::class.java))
        }
    }
}

// Data class para los elementos del menú
data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val subtitle: String,
)



// Composable principal extraído para el preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeScreen(
    menuItems: List<MenuItem>,
    onMenuItemClick: (Int) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = TextPrimary,
                ),
                title = {
                    Text(
                        text = "💪 NewFitnes",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                }
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
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
                .padding(innerPadding)
        ) {
            // Header Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80",
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )

                // Overlay with welcome text
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            BlackAlpha50
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = "Bienvenido a",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = "NewFitnes Management",
                        color = PrimaryGreen,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Menu Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(menuItems.size) { index ->
                    MenuCard(
                        menuItem = menuItems[index],
                        onClick = { onMenuItemClick(index) }
                    )
                }
            }
        }
    }
}

// Composable para cada card del menú
@Composable
fun MenuCard(
    menuItem: MenuItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = menuItem.icon,
                contentDescription = menuItem.title,
                tint = PrimaryGreen,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = menuItem.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = TextPrimary
            )

            Text(
                text = menuItem.subtitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                color = TextSecondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    NewfitnesTheme {
        HomeScreen(menuItems = getMenuItems())
    }
}

// Función para obtener los items del menú (reutilizable)
fun getMenuItems() = listOf(
    MenuItem("Entrenadores", Icons.Default.AccountBox, "Elección Entrenadores"),
    MenuItem("Suscripciones", Icons.Default.CardMembership, "Membresías VIP"),
    MenuItem("Entrenamientos", Icons.Default.FitnessCenter, "Ejercicios y Rutinas"),
    MenuItem("Usuario", Icons.Default.Person, "Perfil de Usuario"),
    MenuItem("Rutinas", Icons.Default.Star, "Rutinas Personalizadas"),
    MenuItem("Configuración", Icons.Default.Settings, "Ajustes del Sistema")
)