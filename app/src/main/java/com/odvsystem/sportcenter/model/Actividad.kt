package com.odvsystem.sportcenter.model

data class Actividad (
    val idActividad: Int=0,
    val nombre: String,
    val descripcion: String,
    val tarifaSocio: Double,
    val tarifaNoSocio: Double,
    val cupoMaximo: Int,
    val turno:String
)