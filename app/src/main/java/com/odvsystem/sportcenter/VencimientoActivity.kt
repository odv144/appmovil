package com.odvsystem.sportcenter

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.odvsystem.sportcenter.databinding.ActivityVencimientoBinding
import com.odvsystem.sportcenter.model.Vencimiento
import com.odvsystem.sportcenter.repository.CuotaRepository

class VencimientoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVencimientoBinding
    private lateinit var cuotaRepository: CuotaRepository
    private var datos: List<Vencimiento> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVencimientoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // 1. Inicializamos el repositorio y traemos los datos reales
        cuotaRepository = CuotaRepository(this)
        datos = cuotaRepository.obtenerVencimientosConNombre()

        // 2. Configuramos el encabezado
        binding.encabezado.setTitulo("Vencimientos")
        binding.encabezado.setDestino(MenuActivity::class.java)

        // 3. Calculamos los contadores dinámicamente desde la base de datos
        actualizarContadores()

        // 4. Configuramos el RecyclerView con los datos reales
        val recycler = findViewById<RecyclerView>(R.id.recyclerVencimientos)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = VencimientoAdapter(datos) { socio ->
            mostrarDialogoCobro(socio)
        }

        // 5. Botón de cobro general
        findViewById<Button>(R.id.btnCobrarCuota).setOnClickListener {
            mostrarDialogoCobro(null)
        }
    }

    private fun actualizarContadores() {
        val vencidos = datos.count { it.estado == "vencido" }
        val proximos = datos.count { it.estado == "proximo" }
        val alDia    = datos.count { it.estado == "al_dia" }

        findViewById<TextView>(R.id.tvVencidos).text = "$vencidos vencidos"
        findViewById<TextView>(R.id.tvProximos).text = "$proximos próximos"
        findViewById<TextView>(R.id.tvAlDia).text    = "$alDia al día"
    }

    private fun mostrarDialogoCobro(vencimiento: Vencimiento?) {
        val view = layoutInflater.inflate(R.layout.dialog_cobrar, null)
        vencimiento?.let {
            view.findViewById<TextView>(R.id.tvNombreSocio).text = it.nombre
            view.findViewById<TextView>(R.id.tvPeriodoCobrar).text = it.periodo
            view.findViewById<TextView>(R.id.tvMontoCobrar).text = it.monto
        }
        val builder = AlertDialog.Builder(this)
            .setTitle(if (vencimiento != null) "Cobrar: ${vencimiento.nombre}" else "Cobrar Cuota")
            .setView(view)
            .setPositiveButton("Imprimir") { _, _ ->
                Toast.makeText(this, "Comprobante enviado a imprimir", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("Cancelar", null)
        builder.create().show()
    }
}

