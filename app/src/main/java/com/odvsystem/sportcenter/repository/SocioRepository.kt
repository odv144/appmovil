package com.odvsystem.sportcenter.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.odvsystem.sportcenter.database.DatabaseHelper
import com.odvsystem.sportcenter.model.Socio
import com.odvsystem.sportcenter.model.SocioRegistro

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
    fun obtenerTodos(): List<SocioRegistro> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<SocioRegistro>()

        val cursor = db.rawQuery(
            """SELECT * 
            FROM usuario u 
            INNER JOIN socio s ON u.idUsuario = s.idUsuario""".trimIndent(),arrayOf())
        while (cursor.moveToNext()) {
           lista.add(cursorToSocio(cursor))
        }

        cursor.close()
        db.close()
        return lista
    }

    // ── SELECT POR ID ──────────────────────────────────────────
    fun obtenerPorId(nroSocio: Int): SocioRegistro? {
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """SELECT * FROM usuario u 
                    INNER JOIN socio s ON u.idUsuario = s.idUsuario
                    WHERE s.nroSocio = ?""".trimIndent(),
            arrayOf(nroSocio.toString()))

        val socio = if (cursor.moveToFirst()) cursorToSocio(cursor) else null
        cursor.close()
        db.close()
        return socio
    }
    fun obtenerSocioPorNro(nroSocio: Int): SocioRegistro? {
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery("""
        SELECT 
            s.nrosocio,
            u.idusuario,
            u.nombre,
            u.apellido,
            u.dni,
            s.estadohabilitacion,
            u.telefono,
            u.email,
            u.certificadomedico,
            s.cuotamensual,
            c.metodopago    AS formapago
        FROM socio s
        INNER JOIN usuario u ON s.idusuario = u.idusuario
        LEFT JOIN cuota c ON s.nrosocio = c.nrosocio
            AND c.mes  = strftime('%m', 'now')
            AND c.anio = strftime('%Y', 'now')
        WHERE s.nrosocio = ?
    """.trimIndent(), arrayOf(nroSocio.toString()))

        var resultado: SocioRegistro? = null

        if (cursor.moveToFirst()) {
            resultado = cursorToSocio(cursor)
        }

        cursor.close()
        db.close()
        return resultado
    }
    // ── SELECT POR NOMBRE ──────────────────────────────────────
    fun obtenerPorNombre(nombre: String): List<SocioRegistro> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<SocioRegistro>()

        val cursor = db.rawQuery(
            """SELECT * FROM usuario u 
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
            put("idusuario", socio.idusuario)
            put("carneteentregado", socio.carneteentregado)
            put("estadohabilitacion", socio.estadohabilitacion)
            put("cuotamensual", socio.cuotamensual)
        }
        val filas = db.update(
            "socio",
            values,
            "nrosocio = ?",
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
    private fun cursorToSocio(cursor: Cursor): SocioRegistro {
        val idxFormaPago = cursor.getColumnIndex("formapago")
        return SocioRegistro(
            nrosocio           = cursor.getInt(cursor.getColumnIndexOrThrow("nrosocio")),
            idUsuario          = cursor.getInt(cursor.getColumnIndexOrThrow("idusuario")),
            nombre             = cursor.getString(cursor.getColumnIndexOrThrow("nombre")) ?: "",
            apellido           = cursor.getString(cursor.getColumnIndexOrThrow("apellido")) ?: "",
            dni                = cursor.getString(cursor.getColumnIndexOrThrow("dni")) ?: "",
            estadohabilitacion = cursor.getString(cursor.getColumnIndexOrThrow("estadohabilitacion")) ?: "activo",
            telefono           = cursor.getString(cursor.getColumnIndexOrThrow("telefono")) ?: "",
            correo             = cursor.getString(cursor.getColumnIndexOrThrow("email")) ?: "",
            certificadoMedico  = cursor.getInt(cursor.getColumnIndexOrThrow("certificadomedico")),
            cuotamensual       = cursor.getDouble(cursor.getColumnIndexOrThrow("cuotamensual")),
            formaPago          = if (idxFormaPago != -1) (cursor.getString(idxFormaPago) ?: "") else ""
        )
    }
}