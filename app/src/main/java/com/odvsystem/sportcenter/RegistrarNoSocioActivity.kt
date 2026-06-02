package com.odvsystem.sportcenter

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.odvsystem.sportcenter.databinding.ActivityRegistrarNoSocioBinding
import com.odvsystem.sportcenter.ui.nosocio.NoSociosActivity

class RegistrarNoSocioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarNoSocioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_no_socio)
        binding = ActivityRegistrarNoSocioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.encabezado.setTitulo("Registro No Socio")
        binding.encabezado.setDestino(NoSociosActivity::class.java)


        val limpiar: Button= findViewById<Button>(R.id.btnLimpiar)
        val cobro: Button= findViewById<Button>(R.id.btnCobrar)
        cobro.setOnClickListener {
            val cobro=layoutInflater.inflate(R.layout.dialog_cobrar,null)

            val dialog = AlertDialog.Builder(this)
                .setTitle("COMPROBANTE DE PAGO DIARIO")
                .setView(cobro)
                .setPositiveButton("Imprimir Pago Diario"){_,_->
                    val comprobante = layoutInflater.inflate(R.layout.dialog_comprobante, null)
                    val dialogComprobante = AlertDialog.Builder(this)
                        .setView(comprobante)
                        .setPositiveButton("Cerrar"){_,_->}
                        .create()
                    dialogComprobante.show()

                }
                .create()
            dialog.show()
        }

    }
}