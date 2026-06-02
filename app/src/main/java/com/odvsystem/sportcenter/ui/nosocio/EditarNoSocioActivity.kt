package com.odvsystem.sportcenter.ui.nosocio

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.odvsystem.sportcenter.ui.nosocio.NoSociosActivity
import com.odvsystem.sportcenter.databinding.ActivityEditarNoSocioBinding
import com.odvsystem.sportcenter.model.NoSocio
import com.odvsystem.sportcenter.model.Usuario
import com.odvsystem.sportcenter.repository.NoSocioRepository
import com.odvsystem.sportcenter.repository.UsuarioRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditarNoSocioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarNoSocioBinding
    private lateinit var noSocioRepository: NoSocioRepository
    private lateinit var usuarioRepository: UsuarioRepository

    private var nroNoSocio: Int = -1
    private var idUsuario: Int = -1
    private var fechaRegistro: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditarNoSocioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noSocioRepository = NoSocioRepository(this)
        usuarioRepository = UsuarioRepository(this)

        binding.encabezado.setTitulo("Editar No Socio")
        binding.encabezado.setDestino(NoSociosActivity::class.java)

        nroNoSocio = intent.getIntExtra("nronosocio", -1)

        if (nroNoSocio == -1) {
            Toast.makeText(this, "No se recibió el no socio", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        cargarDatos()

        binding.btnGuardarNoSocio.setOnClickListener {
            guardarCambios()
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun cargarDatos() {
        val noSocio = noSocioRepository.obtenerPorId(nroNoSocio)

        if (noSocio == null) {
            Toast.makeText(this, "No se encontró el no socio", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        idUsuario = noSocio.idusuario
        fechaRegistro = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        binding.etDni.setText(noSocio.dni)
        binding.etNombre.setText(noSocio.nombre)
        binding.etApellido.setText(noSocio.apellido)
        binding.etTelefono.setText(noSocio.telefono)
        binding.etEmail.setText(noSocio.correo)
        binding.etObservacion.setText(noSocio.observacion)
    }

    private fun guardarCambios() {
        val dni = binding.etDni.text.toString().trim()
        val nombre = binding.etNombre.text.toString().trim()
        val apellido = binding.etApellido.text.toString().trim()
        val telefono = binding.etTelefono.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val observacion = binding.etObservacion.text.toString().trim()
        val certificado = if (binding.chkCerti.isChecked) 1 else 0

        if (dni.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            Toast.makeText(this, "Complete DNI, nombre y apellido", Toast.LENGTH_SHORT).show()
            return
        }

        val usuario = Usuario(
            idusuario = idUsuario,
            nombre = nombre,
            apellido = apellido,
            dni = dni,
            telefono = telefono,
            email = email,
            fecharegistro = fechaRegistro,
            certificadoMedico = certificado
        )

        val noSocio = NoSocio(
            nronosocio = nroNoSocio,
            idusuario = idUsuario,
            observacion = observacion
        )

        val usuarioActualizado = usuarioRepository.actualizar(usuario)
        val noSocioActualizado = noSocioRepository.actualizar(noSocio)

        if (usuarioActualizado && noSocioActualizado) {
            Toast.makeText(this, "No socio actualizado", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "No se pudo actualizar", Toast.LENGTH_SHORT).show()
        }
    }
}