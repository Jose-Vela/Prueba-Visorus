package com.example.pruebavisorus


data class ArticuleEntity(
    val data: List<ArticleDataResponse>,
    val total: Int
)
data class ArticleDataResponse(
    val id: Int,
    val clave: String,
    val categoria: CategoryDataResponse, // CategoryDataResponse,
    val nombre: String,
    val precios: List<Precio>,
    val activo: Boolean = true
)

data class Precio(
    val id: Int,
    val precio: Float
)
