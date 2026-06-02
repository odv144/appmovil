package com.odvsystem.sportcenter

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.odvsystem.sportcenter.database.DatabaseHelper

class ReimpresionCarnet : AppCompatActivity() {

    private var socioActual: Map<String, String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reimpresion_carnet)

        // --- HEADER ---
        val tvTitulo: TextView = findViewById(R.id.tvTitulo)
        tvTitulo.text = "Reimpresión de Carnet"
        val btnAtras: Button = findViewById(R.id.btnAtras)
        btnAtras.setOnClickListener { finish() }

        // --- VISTAS DEL CARNET ---
        val tvNombre: TextView             = findViewById(R.id.tvNombreSocio)
        val tvDni: TextView                = findViewById(R.id.tvDniSocio)
        val tvActividad: TextView          = findViewById(R.id.tvActividad)
        val tvEstado: TextView             = findViewById(R.id.tvEstado)
        val tvMembresia: TextView          = findViewById(R.id.tvEstadoMembresia)
        val tvEtiquetaInactivo: TextView   = findViewById(R.id.tvEtiquetaInactivo)
        val tvVigencia: TextView           = findViewById(R.id.tvVigencia)
        val ivQR: android.widget.ImageView = findViewById(R.id.ivQR)

        // --- BÚSQUEDA ---
        val etBuscar: EditText = findViewById(R.id.etBuscarSocio)
        val btnBuscar: Button  = findViewById(R.id.btnBuscar)

        fun buscar() {
            val termino = etBuscar.text.toString().trim()
            if (termino.isEmpty()) {
                Toast.makeText(this, "Ingresá un número de socio o DNI", Toast.LENGTH_SHORT).show()
                return
            }

            val socio = DatabaseHelper(this).buscarSocioParaCarnet(termino)

            if (socio != null) {
                socioActual = socio

                tvNombre.text    = "${socio["apellido"]}, ${socio["nombre"]}"
                tvDni.text       = "DNI: ${socio["dni"]} · Socio Nº ${socio["nrosocio"]}"
                tvActividad.text = socio["actividades"]

                // --- VIGENCIA ---
                val vigencia = DatabaseHelper(this).obtenerVigenciaSocio(socio["nrosocio"] ?: "")
                tvVigencia.text = vigencia

                val esActivo = socio["estado"] == "activo"
                tvEstado.text    = if (esActivo) "Al día" else "Inhabilitado"
                tvMembresia.text = if (esActivo) "Activo y al día" else "Cuenta inhabilitada"
                tvMembresia.setTextColor(
                    android.graphics.Color.parseColor(if (esActivo) "#4CAF50" else "#F44336")
                )
                // Etiqueta roja
                tvEtiquetaInactivo.visibility = if (esActivo) android.view.View.GONE else android.view.View.VISIBLE

                // --- QR ---
                val contenidoQR = "SOCIO:${socio["nrosocio"]}|DNI:${socio["dni"]}|NOMBRE:${socio["apellido"]},${socio["nombre"]}|ESTADO:${socio["estado"]}"
                ivQR.setImageBitmap(generarQR(contenidoQR, 300, 300))
                ivQR.clearColorFilter()

            } else {
                socioActual      = null
                tvNombre.text    = "— Socio no encontrado —"
                tvDni.text       = ""
                tvActividad.text = "—"
                tvVigencia.text  = "—"
                tvEstado.text    = "—"
                tvMembresia.text = "—"
                tvMembresia.setTextColor(android.graphics.Color.parseColor("#4CAF50"))
                tvEtiquetaInactivo.visibility = android.view.View.GONE
                Toast.makeText(this, "No se encontró ningún socio", Toast.LENGTH_SHORT).show()
            }
        }

        btnBuscar.setOnClickListener { buscar() }

        etBuscar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) { buscar(); true } else false
        }

        // --- IMPRIMIR ---
        val btnImprimir: Button = findViewById(R.id.btnImprimirCarnet)
        btnImprimir.setOnClickListener {
            val socio = socioActual
            if (socio == null) {
                Toast.makeText(this, "Primero buscá un socio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val contenidoQR = "SOCIO:${socio["nrosocio"]}|DNI:${socio["dni"]}|NOMBRE:${socio["apellido"]},${socio["nombre"]}|ESTADO:${socio["estado"]}"
            val qrBitmap = generarQR(contenidoQR, 300, 300)
            exportarPDF(socio, qrBitmap)
        }
    }

    private fun generarQR(contenido: String, ancho: Int, alto: Int): android.graphics.Bitmap {
        val writer = com.google.zxing.qrcode.QRCodeWriter()
        val bitMatrix = writer.encode(contenido, com.google.zxing.BarcodeFormat.QR_CODE, ancho, alto)
        val bitmap = android.graphics.Bitmap.createBitmap(ancho, alto, android.graphics.Bitmap.Config.ARGB_8888)
        for (x in 0 until ancho) {
            for (y in 0 until alto) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        return bitmap
    }

    private fun exportarPDF(socio: Map<String, String>, qrBitmap: android.graphics.Bitmap) {
        val pdfDocument = android.graphics.pdf.PdfDocument()
        val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = android.graphics.Paint()

        // Fondo verde del carnet
        paint.color = android.graphics.Color.parseColor("#1B5E20")
        canvas.drawRect(40f, 40f, 555f, 400f, paint)

        // Título
        paint.color = android.graphics.Color.parseColor("#A5D6A7")
        paint.textSize = 14f
        canvas.drawText("SPORT CENTER · CARNET DE SOCIO", 60f, 80f, paint)

        // Nombre
        paint.color = android.graphics.Color.WHITE
        paint.textSize = 28f
        paint.isFakeBoldText = true
        canvas.drawText("${socio["apellido"]}, ${socio["nombre"]}", 60f, 130f, paint)

        // DNI y Nro Socio
        paint.color = android.graphics.Color.parseColor("#A5D6A7")
        paint.textSize = 16f
        paint.isFakeBoldText = false
        canvas.drawText("DNI: ${socio["dni"]} · Socio Nº ${socio["nrosocio"]}", 60f, 160f, paint)

        // Actividad / Estado
        paint.color = android.graphics.Color.WHITE
        paint.textSize = 18f
        paint.isFakeBoldText = true
        canvas.drawText("Actividad: ${socio["actividades"]}", 60f, 210f, paint)
        canvas.drawText("Estado: ${if (socio["estado"] == "activo") "Al día" else "Inhabilitado"}", 60f, 240f, paint)

        // QR
        val qrScaled = android.graphics.Bitmap.createScaledBitmap(qrBitmap, 150, 150, false)
        canvas.drawBitmap(qrScaled, 380f, 200f, null)

        pdfDocument.finishPage(page)

        // Guardar en Downloads
        val fileName = "carnet_${socio["nrosocio"]}_${socio["apellido"]}.pdf"
        val file = java.io.File(
            android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS),
            fileName
        )

        try {
            pdfDocument.writeTo(java.io.FileOutputStream(file))
            pdfDocument.close()
            Toast.makeText(this, "PDF guardado en Descargas: $fileName", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            pdfDocument.close()
            Toast.makeText(this, "Error al generar PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}