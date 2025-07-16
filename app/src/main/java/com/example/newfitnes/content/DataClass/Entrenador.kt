package com.example.newfitnes.content.DataClass

data class Entrenador(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val experiencia: String,
    val cualidades: List<String>,
    val calificacion: Float,
    val imageRes: Int? = null // Usaremos null para usar icono por defecto
)
