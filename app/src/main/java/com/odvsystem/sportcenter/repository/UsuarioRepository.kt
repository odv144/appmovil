package com.odvsystem.sportcenter.repository
import android.content.ContentValues
import android.content.Context

import com.odvsystem.sportcenter.database.DatabaseHelper
import com.odvsystem.sportcenter.model.Actividad
import com.odvsystem.sportcenter.model.Socio
import com.odvsystem.sportcenter.model.Usuario

class UsuarioRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)


    // ── INSERT ─────────────────────────────────────────────────
    fun insertar(usuario: Usuario): Long{
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", usuario.nombre)
            put("apellido", usuario.apellido)
            put("dni", usuario.dni)
            put("telefono", usuario.telefono)
            put("email", usuario.email)
            put("fecharegistro", usuario.fecharegistro)
            put("certificadomedico", usuario.certificadoMedico)
        }
        val resultado = db.insert("usuario", null, values)
        db.close()
        return resultado
    }

    fun actualizar(usuario: Usuario): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", usuario.nombre)
            put("apellido", usuario.apellido)
            put("dni", usuario.dni)
            put("telefono", usuario.telefono)
            put("email", usuario.email)
            put("fecharegistro", usuario.fecharegistro)
            put("certificadomedico", usuario.certificadoMedico)
        }
        val filas = db.update(
            "usuario",
            values,
            "idusuario = ?",
            arrayOf(usuario.idusuario.toString())
        )
        db.close()
        return filas > 0
    }

    // ── DELETE ─────────────────────────────────────────────────
    fun eliminar(id: Int): Boolean {
        val db = dbHelper.writableDatabase
        val filas = db.delete(
            "usuario",
            "idusuario = ?",
            arrayOf(id.toString())
        )
        db.close()
        return filas > 0
    }

}
