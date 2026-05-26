package com.odvsystem.sportcenter.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.odvsystem.sportcenter.database.DatabaseHelper
import com.odvsystem.sportcenter.model.Actividad
import com.odvsystem.sportcenter.model.Socio

class SocioRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    // ── INSERT ─────────────────────────────────────────────────
    fun insertar(socio: Socio): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("idusuario",socio.idusuario)
            put("estadohabilitacion",socio.estadohabilitacion)
            put("cuotamensual",socio.cuotamensual)
            put("carneteentregado",socio.carneteentregado)
        }
        val resultado = db.insert("socio", null, values)
        db.close()
        return resultado != -1L
    }
    // ── SELECT TODOS ───────────────────────────────────────────
    fun obtenerTodos(): List<Socio> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<Socio>()

        val cursor = db.rawQuery("SELECT * FROM socio", null)
        while (cursor.moveToNext()) {
           lista.add(cursorToSocio(cursor))
        }

        cursor.close()
        db.close()
        return lista
    }

    // ── SELECT POR ID ──────────────────────────────────────────
    fun obtenerPorId(nroSocio: Int): Socio? {
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """SELECT u.nombre,u.apellido,s.estadohabilitacion FROM usuario u 
                    INNER JOIN socio s ON u.idUsuario = s.idUsuario
                    WHERE s.nroSocio = ?""".trimIndent(),
            arrayOf(nroSocio.toString()))

        val socio = if (cursor.moveToFirst()) cursorToSocio(cursor) else null
        cursor.close()
        db.close()
        return socio
    }

    // ── SELECT POR NOMBRE ──────────────────────────────────────
    fun obtenerPorNombre(nombre: String): List<Socio> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<Socio>()

        val cursor = db.rawQuery(
            """SELECT u.nombre,u.apellido,s.estadohabilitacion FROM usuario u 
                    INNER JOIN socio s ON u.idUsuario = s.idUsuario
                    WHERE u.nombre LIKE ?""".trimIndent(),
            arrayOf("%$nombre%"))

        while (cursor.moveToNext()) {
            lista.add(cursorToSocio(cursor))
        }

        cursor.close()
        db.close()
        return lista
    }

    // ── UPDATE ─────────────────────────────────────────────────
    fun actualizar(socio: Socio): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("estadoHabilitacion",socio.estadohabilitacion)
            put("cuotaMensual",   socio.cuotamensual)
            put("carneteEntregado",   socio.carneteentregado)
        }
        val filas = db.update(
            "socio",
            values,
            "nroSocio = ? ",
            arrayOf(socio.nrosocio.toString())
        )
        db.close()
        return filas > 0
    }

    // ── DELETE ─────────────────────────────────────────────────
    fun eliminar(id: Int): Boolean {
        val db = dbHelper.writableDatabase
        val filas = db.delete(
            "socio",
            "nroSocio = ?",
            arrayOf(id.toString())
        )
        db.close()
        return filas > 0
    }

    // ── Helper: cursor → objeto ────────────────────────────────
    private fun cursorToSocio(cursor: Cursor): Socio {
        return Socio(
            nrosocio = cursor.getInt(cursor.getColumnIndexOrThrow("nrosocio")),
            idusuario   = cursor.getLong(cursor.getColumnIndexOrThrow("idusuario")),
            estadohabilitacion   = cursor.getString(cursor.getColumnIndexOrThrow("estadohabilitacion")),
            cuotamensual   = cursor.getDouble(cursor.getColumnIndexOrThrow("cuotamensual")),
            carneteentregado = cursor.getInt(cursor.getColumnIndexOrThrow("carneteentregado"))
        )
    }
}