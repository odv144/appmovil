package com.odvsystem.sportcenter.model

data class Usuario(
    val idusuario: Int,
    val nombre: String,
    val apellido: String,
    val dni: String,
    val telefono: String,
    val email: String,
    val fecharegistro: String,
    val certificadoMedico: Int
)
