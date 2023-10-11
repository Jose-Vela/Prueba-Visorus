package com.example.pruebavisorus

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    // Definimos las operaciones CRUD

    @GET("articulo")
    suspend fun getArticles() : Response<ArticuleEntity>

    @GET("categoria")
    suspend fun getCategories() : Response<CategoryEntity>

    @POST("categoria")
    suspend fun postCategories(@Body category: Category) : Response<CategoryEntityPost>

}