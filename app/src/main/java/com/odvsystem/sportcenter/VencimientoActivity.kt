package com.odvsystem.sportcenter

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.odvsystem.sportcenter.databinding.ActivityRegistrarSocioBinding
import com.odvsystem.sportcenter.databinding.ActivityVencimientoBinding

data class Vencimiento(
    val nombre:String,
    val periodo: String,
    val monto: String,
    val vence:String,
    val estado: String // "al_dia, "vencido","proximo"
    )
class VencimientoActivity : AppCompatActivity() {
    public lateinit var binding: ActivityVencimientoBinding
    private val datos = listOf(
        Vencimiento("García, Luis",   "Abr 25", "$8.500",  "10/04", "al_dia"),
        Vencimiento("López, Marcos",  "Mar 25", "$11.000", "31/03", "vencido"),
        Vencimiento("Rodríguez, C.",  "Abr 25", "$7.500",  "15/04", "proximo"),
        Vencimiento("Martínez, Ana",  "Abr 25", "$9.000",  "20/04", "al_dia"),
        Vencimiento("Flores, Pedro",  "Mar 25", "$8.000",  "28/03", "vencido")
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVencimientoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
       // creo que no se necesita la linea de abajo
     //   setContentView(R.layout.activity_vencimiento)
        binding.encabezado.setTitulo("DATOS DE COBRANZAS")
        binding.encabezado.setDestino(MenuActivity::class.java)

        val vencidos = datos.count { it.estado == "vencido" }
        val proximos = datos.count { it.estado == "proximo" }
        val alDia    = datos.count { it.estado == "al_dia" }

        findViewById<TextView>(R.id.tvVencidos).text = "$vencidos vencidos"
        findViewById<TextView>(R.id.tvProximos).text = "$proximos próximos"
        findViewById<TextView>(R.id.tvAlDia).text    = "$alDia al día"

        // RecyclerView
        val recycler = findViewById<RecyclerView>(R.id.recyclerVencimientos)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = VencimientoAdapter(datos) { socio ->
            Toast.makeText(this, "Seleccionado: ${socio.nombre}", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnAtras).setOnClickListener { finish() }

        findViewById<Button>(R.id.btnCobrarCuota).setOnClickListener {
            // Lógica de cobro
        }
    }
}
