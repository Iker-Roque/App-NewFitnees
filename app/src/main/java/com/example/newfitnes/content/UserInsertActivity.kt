package com.example.newfitnes.content

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.newfitnes.content.ui.theme.NewfitnesTheme
import com.example.newfitnes.dao.DatabaseProvider
import com.example.newfitnes.dao.User
import com.example.newfitnes.ui.theme.ui.theme.*
import kotlinx.coroutines.launch

class UserInsertActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var name by remember { mutableStateOf("") }
            var exercise by remember { mutableStateOf("") }
            var intensity by remember { mutableStateOf("") }
            var duration by remember { mutableStateOf("") }
            var repetitions by remember { mutableStateOf("") }
            var sets by remember { mutableStateOf("") }
            var isLoading by remember { mutableStateOf(false) }

            NewfitnesTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = BackgroundDark
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding).padding(32.dp)) {
                        Text(
                            text = "Nuevo usuario",
                            style = MaterialTheme.typography.headlineLarge,
                            color = PrimaryGreen
                        )

                        Spacer(modifier = Modifier.padding(10.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it},
                            label = { Text("Nombre", color = TextSecondary) },
                            enabled = !isLoading,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryGreen,
                                unfocusedBorderColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = PrimaryGreen,
                                focusedLabelColor = PrimaryGreen,
                                unfocusedLabelColor = TextSecondary
                            )
                        )
                        Spacer(modifier = Modifier.padding(10.dp))

                        OutlinedTextField(
                            value = exercise,
                            onValueChange = { exercise = it},
                            label = { Text("Ejercicio", color = TextSecondary) },
                            enabled = !isLoading,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryGreen,
                                unfocusedBorderColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = PrimaryGreen,
                                focusedLabelColor = PrimaryGreen,
                                unfocusedLabelColor = TextSecondary
                            )
                        )
                        Spacer(modifier = Modifier.padding(10.dp))

                        OutlinedTextField(
                            value = intensity,
                            onValueChange = { intensity = it},
                            label = { Text("Intensidad", color = TextSecondary) },
                            enabled = !isLoading,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryGreen,
                                unfocusedBorderColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = PrimaryGreen,
                                focusedLabelColor = PrimaryGreen,
                                unfocusedLabelColor = TextSecondary
                            )
                        )
                        Spacer(modifier = Modifier.padding(10.dp))

                        OutlinedTextField(
                            value = duration,
                            onValueChange = { duration = it},
                            label = { Text("Duración (minutos)", color = TextSecondary) },
                            enabled = !isLoading,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryGreen,
                                unfocusedBorderColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = PrimaryGreen,
                                focusedLabelColor = PrimaryGreen,
                                unfocusedLabelColor = TextSecondary
                            )
                        )
                        Spacer(modifier = Modifier.padding(10.dp))

                        OutlinedTextField(
                            value = repetitions,
                            onValueChange = { repetitions = it},
                            label = { Text("Repeticiones", color = TextSecondary) },
                            enabled = !isLoading,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryGreen,
                                unfocusedBorderColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = PrimaryGreen,
                                focusedLabelColor = PrimaryGreen,
                                unfocusedLabelColor = TextSecondary
                            )
                        )
                        Spacer(modifier = Modifier.padding(10.dp))

                        OutlinedTextField(
                            value = sets,
                            onValueChange = { sets = it},
                            label = { Text("Series", color = TextSecondary) },
                            enabled = !isLoading,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryGreen,
                                unfocusedBorderColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = PrimaryGreen,
                                focusedLabelColor = PrimaryGreen,
                                unfocusedLabelColor = TextSecondary
                            )
                        )
                        Spacer(modifier = Modifier.padding(10.dp))

                        OutlinedButton(
                            onClick = {
                                // Validar que los campos no estén vacíos
                                if (name.isBlank() || exercise.isBlank() || intensity.isBlank() ||
                                    duration.isBlank() || repetitions.isBlank() || sets.isBlank()) {
                                    Toast.makeText(this@UserInsertActivity, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                                    return@OutlinedButton
                                }

                                // Validar que los campos numéricos sean válidos
                                val durationInt = duration.toIntOrNull()
                                val repetitionsInt = repetitions.toIntOrNull()
                                val setsInt = sets.toIntOrNull()

                                if (durationInt == null || repetitionsInt == null || setsInt == null) {
                                    Toast.makeText(this@UserInsertActivity, "Por favor ingresa valores numéricos válidos", Toast.LENGTH_SHORT).show()
                                    return@OutlinedButton
                                }

                                if (durationInt <= 0 || repetitionsInt <= 0 || setsInt <= 0) {
                                    Toast.makeText(this@UserInsertActivity, "Los valores numéricos deben ser mayores a 0", Toast.LENGTH_SHORT).show()
                                    return@OutlinedButton
                                }

                                isLoading = true
                                val database = DatabaseProvider.getDatabase(this@UserInsertActivity)
                                val userDao = database.userDao()

                                lifecycleScope.launch {
                                    try {
                                        val user = User(
                                            name = name,
                                            exercise = exercise,
                                            intensity = intensity,
                                            duration = durationInt,
                                            repetitions = repetitionsInt,
                                            sets = setsInt
                                        )
                                        userDao.insertUser(user)

                                        // Mostrar mensaje de éxito
                                        Toast.makeText(this@UserInsertActivity, "Usuario guardado exitosamente", Toast.LENGTH_SHORT).show()

                                        // Limpiar los campos
                                        name = ""
                                        exercise = ""
                                        intensity = ""
                                        duration = ""
                                        repetitions = ""
                                        sets = ""

                                        // OPCIÓN 1: Navegar a UserDataActivity para ver los datos
                                        kotlinx.coroutines.delay(1000) // Pequeño delay para mostrar el toast
                                        val intent = android.content.Intent(this@UserInsertActivity, UserDataActivity::class.java)
                                        startActivity(intent)
                                        finish()

                                        // OPCIÓN 2: Solo limpiar campos (mantener en la misma pantalla)
                                        // No hacer nada más

                                    } catch (e: Exception) {
                                        Toast.makeText(this@UserInsertActivity, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            },
                            enabled = !isLoading,
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isLoading) SurfaceDark else PrimaryGreen,
                                contentColor = if (isLoading) TextSecondary else TextOnPrimary
                            )
                        ) {
                            Text(text = if (isLoading) "Guardando..." else "Guardar")
                        }

                        Spacer(modifier = Modifier.padding(10.dp))

                        // Botón para ver usuarios guardados
                        OutlinedButton(
                            onClick = {
                                val intent = android.content.Intent(this@UserInsertActivity, UserDataActivity::class.java)
                                startActivity(intent)
                            },
                            enabled = !isLoading,
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = SurfaceDark,
                                contentColor = TextPrimary
                            )
                        ) {
                            Text(text = "Ver Usuarios Guardados")
                        }
                    }
                }
            }
        }
    }
}