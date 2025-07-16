package com.example.newfitnes.ui.theme.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.newfitnes.R

// 1. Define la familia de fuentes Poppins
val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)

)

// 2. Define estilos con esa fuente
val Typography: Typography
    get() = Typography(
        bodyLarge = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        displaySmall = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        ),
        titleLarge = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        ),
        labelLarge = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )



        // Puedes seguir agregando estilos: displaySmall, headlineMedium, etc.
    )