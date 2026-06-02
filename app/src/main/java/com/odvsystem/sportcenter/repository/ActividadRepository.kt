package com.odvsystem.sportcenter.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.odvsystem.sportcenter.database.DatabaseHelper
import com.odvsystem.sportcenter.model.Actividad

class ActividadRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)
    // ── INSERT ─────────────────────────────────────────────────
    fun insertar(actividad: Actividad): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre",        actividad.nombre)
            put("descripcion",   actividad.descripcion)
            put("tarifasocio",   actividad.tarifaSocio)
            put("tarifanosocio", actividad.tarifaNoSocio)
            put("cupomaximo",    actividad.cupoMaximo)
            put("turno",         actividad.turno)
        }
        val resultado = db.insert("actividad", null, values)
        db.close()
        return resultado != -1L
    }

    // ── SELECT TODOS ───────────────────────────────────────────
    fun obtenerTodos(): List<Actividad> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<Actividad>()

        val cursor = db.rawQuery("SELECT * FROM actividad", null)
        while (cursor.moveToNext()) {
            lista.add(cursorToActividad(cursor))
        }

        cursor.close()
        db.close()
        return lista
    }

    // ── SELECT POR ID ──────────────────────────────────────────
    fun obtenerPorId(id: Int): Actividad? {
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM actividad WHERE idactividad = ?",
            arrayOf(id.toString())
        )

        val actividad = if (cursor.moveToFirst()) cursorToActividad(cursor) else null
        cursor.close()
        db.close()
        return actividad
    }

    // ── SELECT POR NOMBRE ──────────────────────────────────────
    fun obtenerPorNombre(nombre: String): List<Actividad> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<Actividad>()

        val cursor = db.rawQuery(
            "SELECT * FROM actividad WHERE nombre LIKE ?",
            arrayOf("%$nombre%")
        )
        while (cursor.moveToNext()) {
            lista.add(cursorToActividad(cursor))
        }

        cursor.close()
        db.close()
        return lista
    }

    // ── UPDATE ─────────────────────────────────────────────────
    fun actualizar(actividad: Actividad): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre",        actividad.nombre)
            put("descripcion",   actividad.descripcion)
            put("tarifasocio",   actividad.tarifaSocio)
            put("tarifanosocio", actividad.tarifaNoSocio)
            put("cupomaximo",    actividad.cupoMaximo)
            put("turno",         actividad.turno)
        }
        val filas = db.update(
            "actividad",
            values,
            "idactividad = ?",
            arrayOf(actividad.idActividad.toString())
        )
        db.close()
        return filas > 0
    }

    // ── DELETE ─────────────────────────────────────────────────
    fun eliminar(id: Int): Boolean {
        val db = dbHelper.writableDatabase
        val filas = db.delete(
            "actividad",
            "idactividad = ?",
            arrayOf(id.toString())
        )
        db.close()
        return filas > 0
    }

    // ── Helper: cursor → objeto ────────────────────────────────
    private fun cursorToActividad(cursor: Cursor): Actividad {
        return Actividad(
            idActividad   = cursor.getInt(cursor.getColumnIndexOrThrow("idactividad")),
            nombre        = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
            descripcion   = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
            tarifaSocio   = cursor.getDouble(cursor.getColumnIndexOrThrow("tarifasocio")),
            tarifaNoSocio = cursor.getDouble(cursor.getColumnIndexOrThrow("tarifanosocio")),
            cupoMaximo    = cursor.getInt(cursor.getColumnIndexOrThrow("cupomaximo")),
            turno         = cursor.getString(cursor.getColumnIndexOrThrow("turno"))
        )
    }
}