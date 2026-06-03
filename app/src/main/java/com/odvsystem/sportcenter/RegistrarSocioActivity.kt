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
    private fun guardar() {
        val nombre = binding.etNombre.text.toString().trim()
        val apellido = binding.etApellido.text.toString().trim()
        val dni = binding.etDni.text.toString().trim()
        val telefono = binding.etTelefono.text.toString().trim()
        val formaPago = binding.spFormaPago.selectedItem.toString()
        val email = binding.etEmail.text.toString().trim()
        val cuota = binding.etCuota.text.toString().toDoubleOrNull() ?: 0.0
        val certificado = if (binding.chkCerti.isChecked) 1 else 0
        val fechaBD = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty()) {
            Toast.makeText(this, "Complete nombre, apellido y DNI", Toast.LENGTH_SHORT).show()
            return
        }

        if (cuota <= 0) {
            Toast.makeText(this, "Ingrese una cuota válida", Toast.LENGTH_SHORT).show()
            return
        }

        val idUsuarioActual = socioExistente?.idUsuario?.toInt()

        if (repoUsuario.existeDni(dni, idUsuarioActual)) {
            Toast.makeText(
                this,
                "Ya existe un usuario registrado con ese DNI",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (socioExistente != null) {
            // EDITAR
            val usuario = Usuario(
                socioExistente!!.idUsuario.toInt(),
                nombre,
                apellido,
                dni,
                telefono,
                email,
                fechaBD,
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
            } else {
                Toast.makeText(this, "Error al actualizar socio", Toast.LENGTH_SHORT).show()
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
                fechaBD,
                certificado
            )

            val idUsuarioGenerado: Long = repoUsuario.insertar(usuario)

            if (idUsuarioGenerado <= 0) {
                Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                return
            }

            val socio = Socio(
                0,
                idUsuarioGenerado.toInt(),
                "Al dia",
                cuota,
                1
            )

            val idSocioGenerado = repo.insertar(socio)

            if (idSocioGenerado <= 0) {
                Toast.makeText(this, "Error al registrar socio", Toast.LENGTH_SHORT).show()
                return
            }
            val repoCuota = CuotaRepository(this)
            val calendar = Calendar.getInstance()
            val mesActual = calendar.get(Calendar.MONTH) + 1
            val anioActual = calendar.get(Calendar.YEAR)
            val periodo = "${mesActual.toString().padStart(2, '0')}/$anioActual"
            val fechaVencimientoBD = repoCuota.obtenerRenovacionVencimiento(fechaBD)
            val fechaVencimientoMostrar = convertirFechaArgentina(fechaVencimientoBD)
            val ok = repoCuota.insertar(
                Cuota(
                    0,
                    idSocioGenerado.toInt(),
                    mesActual,
                    anioActual,
                    socio.cuotamensual,
                    fechaVencimientoBD,
                    fechaBD,
                    formaPago,
                    1
                )
            )

            if (ok) {
                val cobro = Vencimiento(
                    0,
                    "$apellido, $nombre",
                    periodo,
                    socio.cuotamensual.toString(),
                    fechaVencimientoMostrar,
                    "al_dia"
                )

                mostrarDialogoCobro(cobro, idSocioGenerado.toInt())

            } else {
                Toast.makeText(this, "Socio creado, pero no se pudo registrar la cuota", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun convertirFechaArgentina(fechaBD: String): String {
        return try {
            val formatoEntrada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formatoSalida = SimpleDateFormat("dd-MM-yyyy", Locale("es", "AR"))

            val fechaParseada = formatoEntrada.parse(fechaBD)

            if (fechaParseada != null) {
                formatoSalida.format(fechaParseada)
            } else {
                fechaBD
            }
        } catch (e: Exception) {
            fechaBD
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
