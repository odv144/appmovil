package com.odvsystem.sportcenter.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ){
    companion object{
        const val DATABASE_NAME="deportivo.db"
        const val DATABASE_VERSION =1

        //TABLA
        const val TABLA_LOGIN = "login"

        //COLUMNAS
        const val COLUMN_ID="id"
        const val COLUMN_NOMBRE="nombre"
        const val COLUMN_CLAVE = "clave"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("PRAGMA foreign_keys = ON")
        //ROLES
        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS roles(
            rolusu INTEGER NOT NULL,
            nomrol TEXT,
            PRIMARY KEY (rolusu)
            )
        """.trimIndent())
        //USUARIO
        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS usuario (
            idusuario INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
            nombre TEXT,
            apellido TEXT,
            dni TEXT,
            telefono TEXT,
            email TEXT,
            fecharegistro TEXT,
            certificadomedico INTEGER
            )
        """.trimIndent())

        //CREDENCIALES
        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS credencial(
            codusu INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
            nombreusu TEXT,
            passusu TEXT,
            rolusu INTEGER,
            activo INTEGER DEFAULT 1,
            FOREIGN KEY (rolusu) REFERENCES roles(rolusu)
            )
        """.trimIndent())
        //SOCIO
        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS socio(
            nrosocio INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
            idusuario INTEGER,
            estadohabilitacion TEXT NOT NULL DEFAULT 'activo',
            cuotamensual REAL,
            carneteentregado INTEGER,
            FOREIGN KEY (idusuario) REFERENCES usuario(idusuario)
            )
        """.trimIndent())
        //ACTIVIDAD
        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS actividad(
             idactividad     INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
             nombre          TEXT,
             descripcion     TEXT,
             tarifasocio     REAL,
             tarifanosocio   REAL,
             cupomaximo      INTEGER,
             turno           TEXT
            )
        """.trimIndent())
        //CUOTA
        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS cuota(
            idcuota INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
            nrosocio INTEGER,
            mes INTEGER,
            anio INTEGER,
            monto REAL,
            fechavencimiento TEXT,
            fechapago TEXT,
            metodopago TEXT,
            estadopago INTEGER DEFAULT 0,
            FOREIGN KEY (nrosocio) REFERENCES socio(nrosocio)
            )
        """.trimIndent())
        //NO SOCIO
        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS nosocio (
                nronosocio  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                idusuario   INTEGER,
                observacion TEXT,
                FOREIGN KEY (idusuario) REFERENCES usuario(idusuario)
            )
        """.trimIndent())
        //SOCIO - ACTIVIDAD
        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS socio_actividad (
                idinscripcion   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                nrosocio        INTEGER NOT NULL,
                idactividad     INTEGER NOT NULL,
                fechainscripcion TEXT NOT NULL,
                estado          TEXT,
                UNIQUE (nrosocio, idactividad),
                FOREIGN KEY (nrosocio)    REFERENCES socio(nrosocio),
                FOREIGN KEY (idactividad) REFERENCES actividad(idactividad)
            )
        """.trimIndent())
        //NOSOCIO ACTIVIDAD
        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS nosocio_actividad (
                id          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                nronosocio  INTEGER NOT NULL,
                idactividad INTEGER NOT NULL,
                FOREIGN KEY (nronosocio)  REFERENCES nosocio(nronosocio),
                FOREIGN KEY (idactividad) REFERENCES actividad(idactividad)
            )
        """.trimIndent())
        insertarDatosIniciales(db)

    }



    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion:Int,
        newVersion: Int
    ){
        db?.execSQL("DROP TABLE IF EXISTS $TABLA_LOGIN") //tabla de login vieja
        db?.execSQL("DROP TABLE IF EXISTS nosocio_actividad")
        db?.execSQL("DROP TABLE IF EXISTS socio_actividad")
        db?.execSQL("DROP TABLE IF EXISTS cuota")
        db?.execSQL("DROP TABLE IF EXISTS nosocio")
        db?.execSQL("DROP TABLE IF EXISTS socio")
        db?.execSQL("DROP TABLE IF EXISTS actividad")
        db?.execSQL("DROP TABLE IF EXISTS credencial")
        db?.execSQL("DROP TABLE IF EXISTS usuario")
        db?.execSQL("DROP TABLE IF EXISTS roles")
        onCreate(db)
     }
    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        if (!db.isReadOnly) db.execSQL("PRAGMA foreign_keys = ON")
    }
    // ── Datos iniciales ────────────────────────────────────────
    private fun insertarDatosIniciales(db: SQLiteDatabase?) {

        // roles
        db?.execSQL("INSERT INTO roles VALUES (1,'Administrador')")
        db?.execSQL("INSERT INTO roles VALUES (2,'Empleado')")

        // credenciales
        listOf("omar","cynthia","cristian","analia","jairo","admin").forEachIndexed { i, nombre ->
            db?.execSQL("INSERT INTO credencial (nombreusu,passusu,rolusu,activo) VALUES ('$nombre','1234',1,1)")
        }

        // actividades
        db?.execSQL("INSERT INTO actividad VALUES (1,'Fútbol 5','Partidos de fútbol 5 con árbitro',1500,2500,10,'Mañana')")
        db?.execSQL("INSERT INTO actividad VALUES (2,'Natación Adultos','Clases de natación para adultos principiantes',2000,3500,15,'Tarde')")
        db?.execSQL("INSERT INTO actividad VALUES (3,'Yoga','Sesiones de yoga y meditación',1800,3000,20,'Mañana')")
        db?.execSQL("INSERT INTO actividad VALUES (4,'Tenis','Clases de tenis individuales y grupales',2500,4000,8,'Tarde')")
        db?.execSQL("INSERT INTO actividad VALUES (5,'Gimnasio','Acceso a sala de musculación y máquinas',3000,5000,30,'Noche')")
        db?.execSQL("INSERT INTO actividad VALUES (6,'Pilates','Clases de pilates con instructora certificada',2200,3800,12,'Mañana')")
        db?.execSQL("INSERT INTO actividad VALUES (7,'Paddle','Alquiler de cancha de paddle por hora',1200,2000,4,'Tarde')")
        db?.execSQL("INSERT INTO actividad VALUES (8,'Zumba','Clases de baile fitness con música latina',1500,2800,25,'Noche')")
        db?.execSQL("INSERT INTO actividad VALUES (9,'Natación Niños','Clases de natación para niños de 6 a 12 años',1800,3200,12,'Tarde')")
        db?.execSQL("INSERT INTO actividad VALUES (10,'Básquet','Entrenamientos y partidos de básquetbol',1600,2600,12,'Noche')")
    }
    fun ingresoLogin(usuario: String, pass: String): String? {
        val db = this.readableDatabase

        val cursor = db.rawQuery("""
            SELECT r.nomrol
            FROM credencial c
            INNER JOIN roles r ON c.rolusu = r.rolusu
            WHERE c.nombreusu = ?
              AND c.passusu = ?
              AND c.activo = 1
        """.trimIndent(), arrayOf(usuario, pass))

        var rol: String? = null
        if (cursor.moveToFirst()) {
            rol = cursor.getString(cursor.getColumnIndexOrThrow("nomrol"))
        }

        cursor.close()
        db.close()
        return rol
    }
}

