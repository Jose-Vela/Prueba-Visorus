package com.example.pruebavisorus


data class CategoryEntity(
    val data: List<CategoryDataResponse>,
    val total: Int
)
data class CategoryDataResponse(
    val id: Int,
    val clave: String,
    val fechaCreado: Long,
    val nombre: String,
    //val activo: Boolean = true
)

data class CategoryProvider(
    //val id: Int,
    val category: CategoryDataResponse,
    var isSelected: Boolean = false
)

data class Category(
    val clave: String,
    val nombre: String,
    val fechaCreado: Long
)
data class CategoryEntityPost(
    val data: CategoryDataResponse,
    val message: String
)