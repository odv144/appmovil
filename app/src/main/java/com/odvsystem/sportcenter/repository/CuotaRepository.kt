package com.odvsystem.sportcenter.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.odvsystem.sportcenter.database.DatabaseHelper
import com.odvsystem.sportcenter.model.Cuota
import com.odvsystem.sportcenter.model.Vencimiento
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
            put("fechapago", cuota.fechaPago)
            put("metodopago", cuota.metodoPago)
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
    // ── VENCIMIENTOS CON NOMBRE DE SOCIO ──────────────────────
    fun obtenerVencimientosConNombre(): List<Vencimiento> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<Vencimiento>()

        val cursor = db.rawQuery("""
    SELECT c.idcuota,
           u.apellido || ', ' || u.nombre AS nombre,
           c.mes || '/' || c.anio AS periodo,
           '$' || CAST(CAST(c.monto AS INTEGER) AS TEXT) AS monto,
           c.fechavencimiento AS vence,
           CASE 
               WHEN c.estadopago = 1 THEN 'al_dia'
               WHEN date(c.fechavencimiento) < date('now') THEN 'vencido'
               ELSE 'proximo'
           END AS estado
    FROM cuota c
    INNER JOIN socio s ON c.nrosocio = s.nrosocio
    INNER JOIN usuario u ON s.idusuario = u.idusuario
    ORDER BY c.fechavencimiento ASC
""".trimIndent(), null)

        while (cursor.moveToNext()) {
            lista.add(
                Vencimiento(
                    idCuota = cursor.getInt(0),
                    nombre  = cursor.getString(1),
                    periodo = cursor.getString(2),
                    monto   = cursor.getString(3),
                    vence   = cursor.getString(4),
                    estado  = cursor.getString(5)
                )
            )
        }

        cursor.close()
        db.close()
        return lista
    }
    fun obtenerRenovacionVencimiento(fecha:String, dias:Int = 30): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = sdf.parse(fecha)!!
        calendar.add(Calendar.DAY_OF_MONTH, dias)
        return sdf.format(calendar.time)
    }
}