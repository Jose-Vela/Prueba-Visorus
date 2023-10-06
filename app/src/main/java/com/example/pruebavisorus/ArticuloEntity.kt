package com.example.pruebavisorus


data class ArticuloEntity(
    val data: List<ArticuloDataResponse>,
    val total: Int
)
data class ArticuloDataResponse(
    val clave: String,
    val categoria: CategoriaEntity,
    val nombre: String,
    val precios: List<Precio>,
    val activo: Boolean = true
)

data class Precio(
    val precio: Float
)
