package com.odvsystem.sportcenter.model

data class Vencimiento(
    val idCuota: Int = 0,
    val nombre:String,
    val periodo: String,
    val monto: String,
    val vence:String,
    val estado: String // "al_dia", "vencido","proximo"
)