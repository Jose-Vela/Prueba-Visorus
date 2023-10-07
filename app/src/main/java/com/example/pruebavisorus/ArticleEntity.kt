package com.example.pruebavisorus


data class ArticuleEntity(
    val data: List<ArticleDataResponse>,
    val total: Int
)
data class ArticleDataResponse(
    val clave: String,
    val categoria: CategoryEntity,
    val nombre: String,
    val precios: List<Precio>,
    val activo: Boolean = true
)

data class Precio(
    val precio: Float
)
