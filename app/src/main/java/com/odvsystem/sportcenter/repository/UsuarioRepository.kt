package com.odvsystem.sportcenter.repository
import android.content.ContentValues
import android.content.Context

import com.odvsystem.sportcenter.database.DatabaseHelper
import com.odvsystem.sportcenter.model.Socio
import com.odvsystem.sportcenter.model.Usuario

class UsuarioRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)


    // ── INSERT ─────────────────────────────────────────────────
    fun insertar(usuario: Usuario): Long{
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre",usuario.nombre)
            put("apellido",usuario.apellido)
            put("dni",usuario.dni)
            put("telefono",usuario.telefono)
            put("email", usuario.email)
            put("fechaRegistro",usuario.fecharegistro)
            put("certificadoMedico",usuario.certificadoMedico)
        }
        val resultado = db.insert("usuario", null, values)
        db.close()
        return resultado
    }
}
