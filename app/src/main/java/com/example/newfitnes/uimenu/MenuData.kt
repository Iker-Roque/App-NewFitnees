package com.example.newfitnes.uimenu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector




// Es un data class: modelo de cada ítem del menú
data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val subtitle: String,
)

// Función que devuelve la lista de ítems del menú
fun getMenuItems() = listOf(
    MenuItem("Entrenadores", Icons.Default.AccountBox, "Elección Entrenadores"),
    MenuItem("Suscripciones", Icons.Default.CardMembership, "Membresías VIP"),
    MenuItem("Entrenamientos", Icons.Default.FitnessCenter, "Ejercicios y Rutinas"),
    MenuItem("Usuario", Icons.Default.Person, "Perfil de Usuario"),
    MenuItem("Rutinas", Icons.Default.Star, "Elige un Rutina"),
    MenuItem("Sucursales", Icons.Default.MapsHomeWork, "Nuevas Sucursales"),
    MenuItem("Nuevo Funcion", Icons.Default.Done, "Extras"),
    MenuItem("Claudinary", Icons.Default.CloudQueue, "Sube tus Imagenes"),
    MenuItem("Nuevo apartado", Icons.Default.Info, "Proximo"),
    MenuItem("Nuevo apartado", Icons.Default.Info, "Proximo"),


)
