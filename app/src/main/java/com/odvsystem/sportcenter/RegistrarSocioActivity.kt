package com.odvsystem.sportcenter

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.odvsystem.sportcenter.databinding.ActivityRegistrarSocioBinding
import com.odvsystem.sportcenter.model.Cuota
import com.odvsystem.sportcenter.model.Socio
import com.odvsystem.sportcenter.model.SocioRegistro
import com.odvsystem.sportcenter.model.Usuario
import com.odvsystem.sportcenter.model.Vencimiento
import com.odvsystem.sportcenter.repository.CuotaRepository
import com.odvsystem.sportcenter.repository.SocioRepository
import com.odvsystem.sportcenter.repository.UsuarioRepository
import com.odvsystem.sportcenter.ui.socio.SociosActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RegistrarSocioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarSocioBinding
    private lateinit var repo : SocioRepository
    private lateinit var repoUsuario: UsuarioRepository
    private lateinit var spformaPago: Spinner
    private var socioExistente : SocioRegistro? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrarSocioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repo = SocioRepository(this)
        repoUsuario = UsuarioRepository(this)

        binding.spFormaPago.adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, listOf("Efectivo","3 Cuotas","6 Cuotas"))

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
           // val intentar = Intent(this, SociosActivity::class.java)
            //startActivity(intentar)
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
        val formaPago = binding.spFormaPago.selectedItem.toString()
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
                val idSocioGenerado = repo.insertar(socio)
                if (idSocioGenerado > 0) {
                    val repoCuota : CuotaRepository = CuotaRepository(this)
                    val calendar = Calendar.getInstance()
                    val mesActual = calendar.get(Calendar.MONTH) + 1
                    val anioActual = calendar.get(Calendar.YEAR)

                    val ok=  repoCuota.insertar(Cuota(
                        0,
                        idSocioGenerado.toInt(),
                        mesActual,
                        anioActual,
                        socio.cuotamensual,
                        repoCuota.obtenerRenovacionVencimiento(fecha.toString()),
                        fecha,
                        formaPago,
                        1))
                    if(ok){
                        val cobro :Vencimiento = Vencimiento(
                            idCuota = 0,
                            usuario.nombre,
                            fecha,
                            socio.cuotamensual.toString(),
                            repoCuota.obtenerRenovacionVencimiento(fecha.toString()),
                            "al_dia"
                        )
                        mostrarDialogoCobro(cobro,idSocioGenerado.toInt())

                    }else {
                        Toast.makeText(this, "Socio creado", Toast.LENGTH_SHORT).show()
                        finish() // Si no hay diálogo, cerramos para volver a la lista
                    }

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

        binding.chkCerti.isChecked=if(socio.certificadoMedico>0)true else false
    }
    private fun mostrarDialogoCobro(vencimiento: Vencimiento?,socio : Int) {
        val view = layoutInflater.inflate(R.layout.dialog_cobrar, null)
        vencimiento?.let {
            view.findViewById<TextView>(R.id.tvNombreSocio).text = it.nombre
            view.findViewById<TextView>(R.id.tvPeriodoCobrar).text = it.periodo
            view.findViewById<TextView>(R.id.tvMontoCobrar).text = it.monto
            view.findViewById<TextView>(R.id.tvActiSocio).text = "Completo - N° Socio: ${socio}"
        }
        val builder = AlertDialog.Builder(this)
            .setTitle(if (vencimiento != null) "Cobrar: ${vencimiento.nombre}" else "Cobrar Cuota")
            .setView(view)
            .setPositiveButton("Imprimir") { dialogInterface, _ ->
                dialogInterface.dismiss()
                val comprobante = layoutInflater.inflate(R.layout.dialog_comprobante, null)

                if (vencimiento != null) {
                    comprobante.findViewById<TextView>(R.id.tvActiSocio).text = "Completo - N° Socio: ${socio}"
                    comprobante.findViewById<TextView>(R.id.tvNombreSocioComprobante).text =
                        vencimiento.nombre
                    comprobante.findViewById<TextView>(R.id.tvPeriodoComprobante).text =
                        vencimiento.periodo
                    comprobante.findViewById<TextView>(R.id.tvMontoComprobante).text =
                        vencimiento.monto
                    comprobante.findViewById<TextView>(R.id.tvFormaPagoComprobante).text =
                        "Efectivo"
                }

                AlertDialog.Builder(this)
                    .setView(comprobante)
                    .setNegativeButton("Cerrar") { _, _ ->
                        finish()
                    }
                    .setCancelable(false)
                    .create()
                    .show()
            }
            .setNegativeButton("Cancelar"){_,_->
                finish()
            }
             .setCancelable(false)
        builder.create().show()
    }
}
