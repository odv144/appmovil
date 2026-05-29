package com.odvsystem.sportcenter.model

data class Vencimiento(
    val nombre:String,
    val periodo: String,
    val monto: String,
    val vence:String,
    val estado: String // "al_dia", "vencido","proximo"
)