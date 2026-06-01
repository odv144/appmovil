package com.odvsystem.sportcenter.model

data class Cuota(
    val idCuota: Int = 0,
    val nroSocio: Int,
    val mes: Int,
    val anio: Int,
    val monto: Double,
    val fechaVencimiento: String,
    val fechaPago: String? = null,
    val metodoPago: String? = null,
    val estadoPago: Int = 0
)