package com.odvsystem.sportcenter

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.odvsystem.sportcenter.databinding.ActivityRegistrarSocioBinding
import com.odvsystem.sportcenter.model.Socio
import com.odvsystem.sportcenter.model.SocioRegistro
import com.odvsystem.sportcenter.model.Usuario
import com.odvsystem.sportcenter.repository.SocioRepository
import com.odvsystem.sportcenter.repository.UsuarioRepository
import com.odvsystem.sportcenter.ui.socio.SociosActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegistrarSocioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarSocioBinding
    private lateinit var repo : SocioRepository
    private lateinit var repoUsuario: UsuarioRepository

    private var socioExistente : SocioRegistro? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrarSocioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repo = SocioRepository(this)
        repoUsuario = UsuarioRepository(this)

        val nroSocio = intent.getIntExtra("nroSocio", -1)

        if (nroSocio != -1) {
            // MODO EDITAR → buscar en BD y rellenar campos
            binding.encabezado.setTitulo("Editar Socio")
            socioExistente = repo.obtenerSocioPorNro(nroSocio)
            socioExistente?.let { rellenarCampos(it) }
        } else {
            // MODO NUEVO → título vacío
            binding.encabezado.setTitulo("Registrar Nuevo Socio")
        }

        // Configuracion del boton de Atraz del encabezado
        binding.encabezado.setDestino(SociosActivity::class.java)

        binding.btnGuardar.setOnClickListener {
            guardar()
            val intentar = Intent(this, SociosActivity::class.java)
            startActivity(intentar)
        }

        binding.btnLimpiar.setOnClickListener {
            val intentar = Intent(this, SociosActivity::class.java)
            startActivity(intentar)
        }
    }

    //funcionando ahora debo tomar los datos de la vistas
    private fun guardar(){
        val nombre = binding.etNombre.text.toString()
        val apellido = binding.etApellido.text.toString()
        val dni = binding.etDni.text.toString()
        val telefono = binding.etTelefono.text.toString()
        val email = binding.etEmail.text.toString()
        val cuota = binding.etCuota.text.toString().toDoubleOrNull() ?: 0.0
        val certificado = if(binding.chkCerti.isChecked) 1 else 0
        val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        if (socioExistente != null) {
            // EDITAR
            val usuario = Usuario(
                socioExistente!!.idUsuario.toInt(),
                nombre,
                apellido,
                dni,
                telefono,
                email,
                fecha,
                certificado
            )
            repoUsuario.actualizar(usuario)

            val socio = Socio(
                socioExistente!!.nrosocio,
                socioExistente!!.idUsuario,
                "Al dia",
                cuota,
                1
            )

            val resultado = repo.actualizar(socio)
            if (resultado) {
                Toast.makeText(this, "Socio actualizado correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            // NUEVO
            val usuario = Usuario(
                0,
                nombre,
                apellido,
                dni,
                telefono,
                email,
                fecha,
                certificado
            )
            val idUsuarioGenerado : Long= repoUsuario.insertar(usuario)

            if (idUsuarioGenerado > 0) {
                val socio = Socio(
                    0,
                    idUsuarioGenerado.toInt(),
                    "Al dia",
                    cuota,
                    1
                )
                val result = repo.insertar(socio)
                if (result) {
                    Toast.makeText(this, "Socio Nuevo Creado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun rellenarCampos(socio : SocioRegistro){
        binding.etNombre.setText(socio.nombre)
        binding.etApellido.setText(socio.apellido)
        binding.etDni.setText(socio.dni)
        binding.etTelefono.setText(socio.telefono)
        binding.etEmail.setText(socio.correo)
       // binding.etActividad.setText(socio.actividad)//tengo que leer tabla cuota
        binding.etCuota.setText(socio.cuotamensual.toString())
        binding.etFormaPago.setText(socio.formaPago)
        binding.chkCerti.isChecked=if(socio.certificadoMedico>0)true else false
    }
}
