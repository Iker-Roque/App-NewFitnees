package com.example.newfitnes.content

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.newfitnes.content.ui.theme.NewfitnesTheme
import com.example.newfitnes.dao.DatabaseProvider
import com.example.newfitnes.dao.User
import com.example.newfitnes.dao.UserDao
import com.example.newfitnes.ui.theme.ui.theme.*

class UserDataActivity : ComponentActivity(){

    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        val database = DatabaseProvider.getDatabase(this)
        userDao = database.userDao()

        enableEdgeToEdge()
        setContent{
            val userList = remember { mutableStateOf(listOf<User>())}

            LaunchedEffect(Unit) {
                userDao.getAllUsers().collect { users ->
                    userList.value = users
                }
            }

            NewfitnesTheme {
                Scaffold (
                    modifier = Modifier.fillMaxSize(),
                    containerColor = BackgroundDark,
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                val intent = android.content.Intent(this@UserDataActivity, UserInsertActivity::class.java)
                                startActivity(intent)
                            },
                            containerColor = PrimaryGreen,
                            contentColor = TextOnPrimary,
                            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Agregar usuario"
                            )
                        }
                    }
                ){ innerPadding ->
                    Column (modifier = Modifier.padding(innerPadding).padding(16.dp)){
                        Text(
                            text = "Usuarios Guardados",
                            style = MaterialTheme.typography.headlineLarge,
                            color = PrimaryGreen,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        if (userList.value.isEmpty()) {
                            Text(
                                text = "No hay usuarios guardados",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextSecondary
                            )
                        } else {
                            LazyColumn {
                                items(userList.value) { user ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(vertical = 8.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = CardBackground
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            Text(
                                                text = "ID: ${user.id}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = TextSecondary
                                            )

                                            Text(
                                                text = user.name,
                                                style = MaterialTheme.typography.headlineSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = PrimaryGreen,
                                                modifier = Modifier.padding(vertical = 4.dp)
                                            )

                                            Text(
                                                text = "Ejercicio: ${user.exercise}",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = TextPrimary
                                            )

                                            Text(
                                                text = "Intensidad: ${user.intensity}",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = TextPrimary
                                            )

                                            Text(
                                                text = "Duración: ${user.duration} minutos",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = Info
                                            )

                                            Text(
                                                text = "Repeticiones: ${user.repetitions}",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = Success
                                            )

                                            Text(
                                                text = "Series: ${user.sets}",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = Warning
                                            )

                                        }
                                    }
                                    Spacer(modifier = Modifier.padding(4.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}