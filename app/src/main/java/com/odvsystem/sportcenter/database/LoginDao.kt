package com.odvsystem.sportcenter.database

import android.content.ContentValues
import android.content.Context
import com.odvsystem.sportcenter.model.Login

class LoginDao(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    //INSERT
    fun insertar(login: Login):Long{
        val db=dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_NOMBRE,login.nombre)
            put(DatabaseHelper.COLUMN_CLAVE,login.clave)
        }
        val resultado = db.insert(DatabaseHelper.TABLA_LOGIN,null,values)
        db.close()
        return resultado
    }
    //SELECT TODOS
    fun obtenerLogin():List<Login>{
        val lista = mutableListOf<Login>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLA_LOGIN}",null
        )
        if(cursor.moveToFirst()){
            do{
                val login = Login(
                    id= cursor.getInt(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)
                    ),
                    nombre = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOMBRE)
                    ),
                    clave  = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLAVE)
                    )

                )
                lista.add(login)
            }while(cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
    //SELECCIONAR UN USUARIO SEGUN LOGIN
    fun obtenerLoginUsuario(nombre : String):Login?{
        //val lista = mutableListOf<Login>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLA_LOGIN} WHERE ${DatabaseHelper.COLUMN_NOMBRE} = ?",arrayOf(nombre)
        )
        var  login : Login?=null

        if(cursor.moveToFirst()){

                login = Login(
                    id= cursor.getInt(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                    nombre = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOMBRE)),
                    clave  = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLAVE))

                )

        }
        cursor.close()
        db.close()
        return login
    }


}