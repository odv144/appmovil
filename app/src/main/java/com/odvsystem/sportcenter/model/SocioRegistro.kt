package com.odvsystem.sportcenter.model

import android.widget.CheckBox
import android.widget.EditText

data class SocioRegistro(
    var nrosocio: Int,
    var idUsuario : Int,
     var nombre: String,
     var apellido: String,
     var dni: String,
    var estadohabilitacion: String,
     var telefono : String,
     var correo : String,
     var certificadoMedico : Int,
     var cuotamensual : Double,
     var formaPago : String
)
