package com.example.pruebavisorus

data class CategoriaEntity(
    val clave: String,
    val fechaCreado: Long,
    val nombre: String,
    val activo: Boolean = true
)