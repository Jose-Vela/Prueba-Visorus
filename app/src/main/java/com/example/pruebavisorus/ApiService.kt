package com.example.pruebavisorus

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    // Definimos las operaciones CRUD

    @GET("articulo")
    suspend fun getArticles() : Response<ArticuleEntity>

    @GET("categoria")
    suspend fun getCategories() : Response<CategoryEntity>

}