package com.odvsystem.sportcenter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.odvsystem.sportcenter.ui.actividad.ActividadActivity
import com.odvsystem.sportcenter.ui.nosocio.NoSociosActivity
import com.odvsystem.sportcenter.ui.socio.SociosActivity
import com.odvsystem.sportcenter.ui.vencimiento.VencimientoActivity
import android.widget.TextView
import com.odvsystem.sportcenter.repository.CuotaRepository
import com.odvsystem.sportcenter.repository.SocioRepository
import com.odvsystem.sportcenter.repository.NoSocioRepository

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        // Repositorios
        val socioRepo = com.odvsystem.sportcenter.repository.SocioRepository(this)
        val nosocioRepo = com.odvsystem.sportcenter.repository.NoSocioRepository(this)
        val cuotaRepo = CuotaRepository(this)

        // Contadores
        val tvTotalSocios = findViewById<TextView>(R.id.tvTotalSocios)
        val tvVisitasHoy = findViewById<TextView>(R.id.tvVisitasHoy)
        val tvSociosVencidos = findViewById<TextView>(R.id.tvSociosVencidos)

        tvTotalSocios.text = socioRepo.obtenerTodos().size.toString()
        tvVisitasHoy.text = nosocioRepo.obtenerTodos().size.toString()
        tvSociosVencidos.text = cuotaRepo.obtenerVencimientos().count {
            it.estadoPago == 0
        }.toString()

        val socios: LinearLayout = findViewById<LinearLayout>(R.id.menu_Socio)
        val nosocio: LinearLayout = findViewById<LinearLayout>(R.id.menu_NoSocio)
        val vencimiento: LinearLayout=findViewById<LinearLayout>(R.id.menu_Vencimientos)
        val acti : LinearLayout=findViewById<LinearLayout>(R.id.menu_actividades)
        val reimpresion: LinearLayout=findViewById<LinearLayout>(R.id.menu_reimpresion)
        val cerrarSesion: Button = findViewById(R.id.btnCerrarSesion)

        socios.setOnClickListener {
            Toast.makeText(this,"presionando boton de socio", Toast.LENGTH_LONG).show()
            val intentar = Intent(this, SociosActivity::class.java)
            startActivity(intentar)
        }
        nosocio.setOnClickListener{
            val intentar = Intent(this, NoSociosActivity::class.java)
            startActivity(intentar)
        }
        vencimiento.setOnClickListener {
            val intentar = Intent(this, VencimientoActivity::class.java)
            startActivity(intentar)
        }
        acti.setOnClickListener {
            val intentar = Intent(this, ActividadActivity::class.java)
            startActivity(intentar)
        }
        /*
        acti.setOnClickListener {
            val intentar = Intent(this, ActividadesActivity::class.java)
            startActivity(intentar)
        }
        */
        reimpresion.setOnClickListener {
            val intentar = Intent(this, ReimpresionCarnet::class.java)
            startActivity(intentar)
        }

        cerrarSesion.setOnClickListener {
            val intentar = Intent(this, MainActivity::class.java)
            startActivity(intentar)
        }
    }
}