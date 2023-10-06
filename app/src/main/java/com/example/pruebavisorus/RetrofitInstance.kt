package com.example.pruebavisorus

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        fun getRetrofitInstance() : Retrofit {
            return Retrofit
                .Builder()
                .baseUrl("http://visorus.ddnsking.com:8091/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}