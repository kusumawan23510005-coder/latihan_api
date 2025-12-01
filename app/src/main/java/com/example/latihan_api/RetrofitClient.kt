package com.example.latihan_api

import com.example.latihan_api.repository.CatatanRepository
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {
    private const val Base_URL = "http://192.168.1.120:8000/api"

    val catatanRepository: CatatanRepository by lazy {
    Retrofit.Builder()
        .baseUrl(Base_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CatatanRepository::class.java)
    }

}