package com.odvsystem.sportcenter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.odvsystem.sportcenter.database.DatabaseHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val dbHelper = DatabaseHelper(this)

        // DatabaseHelper(this).writableDatabase.close()

        setContentView(R.layout.activity_main)
        val boton: Button = findViewById(R.id.btnLogin)

        boton.setOnClickListener{
            val nombre = findViewById<EditText>(R.id.EtUser).text.toString()
            val clave = findViewById<EditText>(R.id.EtClave).text.toString()


            if(nombre.isEmpty()){
                Toast.makeText(this,"Debe Ingresar las credenciales",Toast.LENGTH_SHORT).show()
            }else{
                val login = dbHelper.ingresoLogin(nombre,clave)
                val intentar = Intent(this, MenuActivity::class.java)
                if(login !=null)
                {
                    when (login){
                        "Administrador"-> startActivity(intentar)
                        "Empleado"->startActivity(intentar)
                    }
                }else {
                    Toast.makeText(this, "CREDENCIALES INVALIDAS", Toast.LENGTH_LONG).show()
                }
            }

        }

    }
}