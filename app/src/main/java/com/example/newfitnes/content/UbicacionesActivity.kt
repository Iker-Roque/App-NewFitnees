package com.example.newfitnes.content


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.newfitnes.api.ApiServices
import com.example.newfitnes.ui.theme.ui.theme.NewfitnesTheme

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UbicacionesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://fixed.alwaysdata.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiServices::class.java)
        val ubicacionViewModel = UbicacionViewModel(apiService)

        setContent {
            MaterialTheme {
                Surface {
                    UbicacionScreen(ubicacionViewModel)
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    NewfitnesTheme {

    }
}