package com.example.newfitnes.content.dataclass

data class Entrenador(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val experiencia: String,
    val cualidades: List<String>,
    val calificacion: Float,
    val imagenResId: Int? = null
)
