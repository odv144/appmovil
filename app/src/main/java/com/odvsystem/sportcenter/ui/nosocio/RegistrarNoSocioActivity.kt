package com.odvsystem.sportcenter.ui.nosocio

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.odvsystem.sportcenter.ui.nosocio.NoSociosActivity
import com.odvsystem.sportcenter.databinding.ActivityRegistrarNoSocioBinding
import com.odvsystem.sportcenter.model.NoSocio
import com.odvsystem.sportcenter.model.Usuario
import com.odvsystem.sportcenter.repository.NoSocioRepository
import com.odvsystem.sportcenter.repository.UsuarioRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegistrarNoSocioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarNoSocioBinding
    private lateinit var usuarioRepository: UsuarioRepository
    private lateinit var noSocioRepository: NoSocioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegistrarNoSocioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarioRepository = UsuarioRepository(this)
        noSocioRepository = NoSocioRepository(this)

        binding.encabezado.setTitulo("Registro No Socio")
        binding.encabezado.setDestino(NoSociosActivity::class.java)

        binding.btnCobrar.setOnClickListener {
            registrarNoSocio()
        }

        binding.btnLimpiar.setOnClickListener {
            limpiarCampos()
        }
    }

    private fun registrarNoSocio() {
        val dni = binding.etDni.text.toString().trim()
        val nombre = binding.etNombre.text.toString().trim()
        val apellido = binding.etApellido.text.toString().trim()
        val telefono = binding.etTelefono.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val actividad = binding.etActividad.text.toString().trim()
        val cuota = binding.etCuota.text.toString().trim()
        val formaPago = binding.etFormaPago.text.toString().trim()
        val certificado = if (binding.chkCerti.isChecked) 1 else 0

        if (dni.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            Toast.makeText(this, "Complete DNI, nombre y apellido", Toast.LENGTH_SHORT).show()
            return
        }

        val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val usuario = Usuario(
            idusuario = 0,
            nombre = nombre,
            apellido = apellido,
            dni = dni,
            telefono = telefono,
            email = email,
            fecharegistro = fechaActual,
            certificadoMedico = certificado
        )

        val idUsuarioGenerado = usuarioRepository.insertar(usuario)

        if (idUsuarioGenerado <= 0) {
            Toast.makeText(this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show()
            return
        }

        val observacion = if (actividad.isNotEmpty()) {
            "Realiza ocasionalmente: $actividad"
        } else {
            "Visitante eventual"
        }

        val noSocio = NoSocio(
            nronosocio = 0,
            idusuario = idUsuarioGenerado.toInt(),
            observacion = observacion
        )

        val resultadoNoSocio = noSocioRepository.insertar(noSocio)

        if (resultadoNoSocio > 0) {
            mostrarComprobante(nombre, apellido, dni, actividad, cuota, formaPago)
        } else {
            Toast.makeText(this, "No se pudo registrar el no socio", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarComprobante(
        nombre: String,
        apellido: String,
        dni: String,
        actividad: String,
        cuota: String,
        formaPago: String
    ) {
        AlertDialog.Builder(this)
            .setTitle("COMPROBANTE DE PAGO DIARIO")
            .setMessage("""
                Visitante: $apellido, $nombre
                DNI: $dni
                Actividad: $actividad
                Tarifa: $$cuota
                Forma de pago: $formaPago
            """.trimIndent())
            .setPositiveButton("Cerrar") { _, _ ->
                val intent = Intent(this, NoSociosActivity::class.java)
                startActivity(intent)
                finish()
            }
            .show()
    }

    private fun limpiarCampos() {
        binding.etDni.text.clear()
        binding.etNombre.text.clear()
        binding.etApellido.text.clear()
        binding.etTelefono.text.clear()
        binding.etEmail.text.clear()
        binding.etActividad.text.clear()
        binding.etCuota.text.clear()
        binding.etFormaPago.text.clear()
        binding.chkCerti.isChecked = false
    }
}