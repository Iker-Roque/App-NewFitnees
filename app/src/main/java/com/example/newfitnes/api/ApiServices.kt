package com.example.newfitnes.api

import retrofit2.Call
import retrofit2.http.GET


interface ApiServices {
    @GET("rutinas.php")
    fun getRutinas(): Call<List<Rutina>>

    @GET("ubicacion.php")
    fun getUbicaciones(): Call<List<Ubicacion>>
}