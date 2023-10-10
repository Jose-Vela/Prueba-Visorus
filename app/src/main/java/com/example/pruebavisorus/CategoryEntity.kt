package com.example.pruebavisorus


data class CategoryEntity(
    val data: List<CategoryDataResponse>,
    val total: Int
)
data class CategoryDataResponse(
    val clave: String,
    val fechaCreado: Long,
    val nombre: String,
    val activo: Boolean = true
)

data class CategoryProvider(
    var nombre: String,
    var isSelected: Boolean = false
)