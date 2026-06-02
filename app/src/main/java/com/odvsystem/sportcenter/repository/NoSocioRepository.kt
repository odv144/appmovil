package com.odvsystem.sportcenter.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.odvsystem.sportcenter.database.DatabaseHelper
import com.odvsystem.sportcenter.model.NoSocio
import com.odvsystem.sportcenter.model.NoSocioRegistro

class NoSocioRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun insertar(noSocio: NoSocio): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("idusuario", noSocio.idusuario)
            put("observacion", noSocio.observacion)
        }

        val resultado = db.insert("nosocio", null, values)
        db.close()
        return resultado
    }

    fun obtenerTodos(): List<NoSocioRegistro> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<NoSocioRegistro>()

        val cursor = db.rawQuery(
            """
            SELECT *
            FROM usuario u
            INNER JOIN nosocio n ON u.idusuario = n.idusuario
            """.trimIndent(),
            null
        )

        while (cursor.moveToNext()) {
            lista.add(cursorToNoSocio(cursor))
        }

        cursor.close()
        db.close()
        return lista
    }

    fun obtenerPorId(nroNoSocio: Int): NoSocioRegistro? {
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT *
            FROM usuario u
            INNER JOIN nosocio n ON u.idusuario = n.idusuario
            WHERE n.nronosocio = ?
            """.trimIndent(),
            arrayOf(nroNoSocio.toString())
        )

        val resultado = if (cursor.moveToFirst()) cursorToNoSocio(cursor) else null

        cursor.close()
        db.close()
        return resultado
    }

    fun obtenerFiltro(texto: String): List<NoSocioRegistro> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<NoSocioRegistro>()

        val cursor = db.rawQuery(
            """
            SELECT *
            FROM usuario u
            INNER JOIN nosocio n ON u.idusuario = n.idusuario
            WHERE u.nombre LIKE ? OR u.apellido LIKE ? OR u.dni LIKE ?
            """.trimIndent(),
            arrayOf("%$texto%", "%$texto%", "%$texto%")
        )

        while (cursor.moveToNext()) {
            lista.add(cursorToNoSocio(cursor))
        }

        cursor.close()
        db.close()
        return lista
    }

    fun actualizar(noSocio: NoSocio): Boolean {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("idusuario", noSocio.idusuario)
            put("observacion", noSocio.observacion)
        }

        val filas = db.update(
            "nosocio",
            values,
            "nronosocio = ?",
            arrayOf(noSocio.nronosocio.toString())
        )

        db.close()
        return filas > 0
    }

    fun eliminar(nroNoSocio: Int): Boolean {
        val db = dbHelper.writableDatabase

        val filas = db.delete(
            "nosocio",
            "nronosocio = ?",
            arrayOf(nroNoSocio.toString())
        )

        db.close()
        return filas > 0
    }

    private fun cursorToNoSocio(cursor: Cursor): NoSocioRegistro {
        return NoSocioRegistro(
            nronosocio = cursor.getInt(cursor.getColumnIndexOrThrow("nronosocio")),
            idusuario = cursor.getInt(cursor.getColumnIndexOrThrow("idusuario")),
            nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")) ?: "",
            apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")) ?: "",
            dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")) ?: "",
            telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")) ?: "",
            correo = cursor.getString(cursor.getColumnIndexOrThrow("email")) ?: "",
            observacion = cursor.getString(cursor.getColumnIndexOrThrow("observacion")) ?: ""
        )
    }
}