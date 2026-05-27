package com.odvsystem.sportcenter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.odvsystem.sportcenter.databinding.ActivityRegistrarSocioBinding
import com.odvsystem.sportcenter.model.Socio
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

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etDni: EditText
    private lateinit var etTelefono : EditText
    private lateinit var etCorreo : EditText
    private lateinit var etCertificado : CheckBox

    private lateinit var etActividad : EditText
    private lateinit var etCuota : EditText
    private lateinit var etFormaPago : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarSocioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        val guardar: Button= findViewById<Button>(R.id.btnGuardar)
        val limpiar: Button= findViewById<Button>(R.id.btnLimpiar)

        etNombre=findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etDni = findViewById(R.id.etDni)
        etTelefono=findViewById(R.id.etTelefono)
        etCorreo = findViewById(R.id.etEmail)
        etCertificado = findViewById(R.id.chkCerti)

        etActividad = findViewById(R.id.etActividad)
        etCuota = findViewById(R.id.etCuota)
        etFormaPago = findViewById(R.id.etFormaPago)

        binding.encabezado.setTitulo("REGISTRAR SOCIO")
        binding.encabezado.setDestino(SociosActivity::class.java)

        repo = SocioRepository(this)

        guardar.setOnClickListener {
            guardar()
            val intentar = Intent(this, SociosActivity::class.java)
            startActivity(intentar)
        }

        limpiar.setOnClickListener {
            val intentar = Intent(this, SociosActivity::class.java)
            startActivity(intentar)
        }
    }
    //funcionando ahora debo tomar los datos de la vistas
    private fun guardar(){
        val usuario : Usuario= Usuario(
            3,
            etNombre.toString(),
            etApellido.toString(),
            etDni.toString(),
            etTelefono.toString(),
            etCorreo.toString(),
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            if(etCertificado.isChecked)1 else 0)
        val repoUser = repoUsuario.insertar(usuario)

        val socio : Socio = Socio(
            1,
            if(repoUser!=-1L)repoUser else 0,
            "Al dia",
            etCuota.toString().toDouble(),
            1)
        val result = repo.insertar(socio)
        Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show()

    }
}