package com.example.newfitnes.api

import retrofit2.Call
import retrofit2.http.GET


interface ApiServices {
    @GET("rutinas.php")
    fun getRutinas(): Call<List<Rutina>>

    @GET("fitnes-api/api/ubicaciones.php")
    fun getUbicaciones(): Call<List<Ubicacion>>
}