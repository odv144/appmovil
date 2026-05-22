package com.odvsystem.sportcenter.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.odvsystem.sportcenter.database.DatabaseHelper
import com.odvsystem.sportcenter.model.Cuota

class CuotaRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    // ── SELECT VENCIDOS Y PROXIMOS ─────────────────────────────
    fun obtenerVencimientos(): List<Cuota> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<Cuota>()

        val cursor = db.rawQuery(
            "SELECT * FROM cuota WHERE estadopago = 0 ORDER BY fechavencimiento ASC",
            null
        )
        while (cursor.moveToNext()) {
            lista.add(cursorToCuota(cursor))
        }

        cursor.close()
        db.close()
        return lista
    }

    // ── SELECT POR SOCIO ───────────────────────────────────────
    fun obtenerPorSocio(nroSocio: Int): List<Cuota> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<Cuota>()

        val cursor = db.rawQuery(
            "SELECT * FROM cuota WHERE nrosocio = ?",
            arrayOf(nroSocio.toString())
        )
        while (cursor.moveToNext()) {
            lista.add(cursorToCuota(cursor))
        }

        cursor.close()
        db.close()
        return lista
    }

    // ── MARCAR COMO PAGADA ─────────────────────────────────────
    fun marcarComoPagada(idCuota: Int, metodoPago: String, fechaPago: String): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("estadopago", 1)
            put("metodopago", metodoPago)
            put("fechapago", fechaPago)
        }
        val filas = db.update(
            "cuota",
            values,
            "idcuota = ?",
            arrayOf(idCuota.toString())
        )
        db.close()
        return filas > 0
    }

    // ── INSERT ─────────────────────────────────────────────────
    fun insertar(cuota: Cuota): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nrosocio", cuota.nroSocio)
            put("mes", cuota.mes)
            put("anio", cuota.anio)
            put("monto", cuota.monto)
            put("fechavencimiento", cuota.fechaVencimiento)
            put("estadopago", cuota.estadoPago)
        }
        val resultado = db.insert("cuota", null, values)
        db.close()
        return resultado != -1L
    }

    // ── Helper: cursor → objeto ────────────────────────────────
    private fun cursorToCuota(cursor: Cursor): Cuota {
        return Cuota(
            idCuota          = cursor.getInt(cursor.getColumnIndexOrThrow("idcuota")),
            nroSocio         = cursor.getInt(cursor.getColumnIndexOrThrow("nrosocio")),
            mes              = cursor.getInt(cursor.getColumnIndexOrThrow("mes")),
            anio             = cursor.getInt(cursor.getColumnIndexOrThrow("anio")),
            monto            = cursor.getDouble(cursor.getColumnIndexOrThrow("monto")),
            fechaVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("fechavencimiento")),
            fechaPago        = cursor.getString(cursor.getColumnIndexOrThrow("fechapago")),
            metodoPago       = cursor.getString(cursor.getColumnIndexOrThrow("metodopago")),
            estadoPago       = cursor.getInt(cursor.getColumnIndexOrThrow("estadopago"))
        )
    }
}