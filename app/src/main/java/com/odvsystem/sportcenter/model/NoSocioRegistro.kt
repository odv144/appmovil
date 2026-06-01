package com.odvsystem.sportcenter.model

data class NoSocioRegistro(
    val nronosocio: Int,
    val idusuario: Int,
    val nombre: String,
    val apellido: String,
    val dni: String,
    val telefono: String,
    val correo: String,
    val observacion: String
)
