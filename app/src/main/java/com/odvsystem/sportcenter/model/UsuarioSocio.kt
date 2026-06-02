package com.odvsystem.sportcenter.model

data class UsuarioSocio(
    val nrosocio:Int,
    val idusuario: Long,
    val nombre: String,
    val apellido: String,
    val dni: String,
    val estadohabilitacion: String,
    val cuotamensual: Double,
)
