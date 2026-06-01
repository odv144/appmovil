package com.odvsystem.sportcenter

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.odvsystem.sportcenter.databinding.ActivityNuevaActividadBinding
import com.odvsystem.sportcenter.model.Actividad
import com.odvsystem.sportcenter.repository.ActividadRepository
import com.odvsystem.sportcenter.ui.actividad.ActividadActivity

class NuevaActividad : AppCompatActivity() {
    private lateinit var binding: ActivityNuevaActividadBinding
    private lateinit var repo: ActividadRepository
    // Si es null → nueva actividad / Si tiene valor → editar
    private var actividadExistente: Actividad? = null
    // Vistas
    private lateinit var etNombreActividad: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etTarifaDiaria: EditText
    private lateinit var etTarifaMensual: EditText
    private lateinit var etCupo: EditText
    private lateinit var spinnerTurno: Spinner
    private val turnos = listOf("Mañana", "Tarde", "Noche")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nueva_actividad)

        // --- 1. CONFIGURACIÓN DEL HEADER ---
        val tvTitulo: TextView = findViewById(R.id.tvTitulo)
        tvTitulo.text = "Nueva Actividad"

        val btnAtras: Button = findViewById(R.id.btnAtras)
        btnAtras.setOnClickListener {
            finish() // Cierra la pantalla y vuelve atrás
        }

        // --- 2. CONFIGURACIÓN DE LOS BOTONES DE LA CARD ---

        // Botón GUARDAR
        val btnGuardar: Button = findViewById(R.id.btnGuardar)
        btnGuardar.setOnClickListener {
            // Por ahora solo cerramos la pantalla,
            // pero acá es donde después se validará que los campos no estén vacíos.
            finish()
        }

        // Botón LIMPIAR
        binding.btnLimpiar.setOnClickListener {
            limpiarCampos()
        }
    }

    // Función extra para que el código sea más ordenado
    private fun limpiarCampos() {
        findViewById<EditText>(R.id.etNombreActividad).text.clear()
        findViewById<EditText>(R.id.etDescripcion).text.clear()
        //findViewById<EditText>(R.id.etTurno).text.clear()
        findViewById<EditText>(R.id.etCupo).text.clear()
        findViewById<EditText>(R.id.etTarifaDiaria).text.clear()
        findViewById<EditText>(R.id.etTarifaMensual).text.clear()
    }
    // Rellena los campos cuando es edición
    private fun rellenarCampos(actividad: Actividad) {
        etNombreActividad.setText(actividad.nombre)
        etDescripcion.setText(actividad.descripcion)
        etTarifaMensual.setText(actividad.tarifaSocio.toString())
        etTarifaDiaria.setText(actividad.tarifaNoSocio.toString())
        etCupo.setText(actividad.cupoMaximo.toString())

        // Seleccionar el turno correcto en el Spinner
        val index = turnos.indexOf(actividad.turno)
        if (index >= 0) spinnerTurno.setSelection(index)
    }
    private fun guardar() {
        // Validar campos vacíos
        if (!validarCampos()) return
       val actividad = Actividad(
            idActividad   = actividadExistente?.idActividad ?: 0,
            nombre        = etNombreActividad.text.toString().trim(),
            descripcion   = etDescripcion.text.toString().trim(),
            tarifaSocio   = etTarifaMensual.text.toString().toDouble(),
            tarifaNoSocio = etTarifaDiaria.text.toString().toDouble(),
            cupoMaximo    = etCupo.text.toString().toInt(),
            turno         = spinnerTurno.selectedItem.toString()
        )

        if (actividadExistente != null) {
            // EDITAR
            val ok = repo.actualizar(actividad)
            if (ok) {
                Toast.makeText(this, "Actividad actualizada", Toast.LENGTH_SHORT).show()
                finish() // vuelve a la lista y onResume() la recarga
            }
        } else {
            // NUEVO
            val ok = repo.insertar(actividad)
            if (ok) {
                Toast.makeText(this, "Actividad creada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun validarCampos(): Boolean {
        if (etNombreActividad.text.isBlank()) {
            etNombreActividad.error = "Ingresá un nombre"
            return false
        }
        if (etDescripcion.text.isBlank()) {
            etDescripcion.error = "Ingresá una descripción"
            return false
        }
        if (etTarifaMensual.text.isBlank()) {
            etTarifaMensual.error = "Ingresá la tarifa"
            return false
        }
        if (etTarifaDiaria.text.isBlank()) {
            etTarifaDiaria.error = "Ingresá la tarifa"
            return false
        }
        if (etCupo.text.isBlank()) {
            etCupo.error = "Ingresá el cupo"
            return false
        }
        return true
    }
}