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

        const val TABLA_LOGIN = "login"
        const val COLUMN_ID="id"
        const val COLUMN_NOMBRE="nombre"
        const val COLUMN_CLAVE = "clave"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("PRAGMA foreign_keys = ON")

        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS roles(
            rolusu INTEGER NOT NULL,
            nomrol TEXT,
            PRIMARY KEY (rolusu)
            )
        """.trimIndent())

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

        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS nosocio (
                nronosocio  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                idusuario   INTEGER,
                observacion TEXT,
                FOREIGN KEY (idusuario) REFERENCES usuario(idusuario)
            )
        """.trimIndent())

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

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int){
        db?.execSQL("DROP TABLE IF EXISTS $TABLA_LOGIN")
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

    private fun insertarDatosIniciales(db: SQLiteDatabase?) {

        // ── Roles ──────────────────────────────────────────────
        db?.execSQL("INSERT OR IGNORE INTO roles VALUES (1,'Administrador')")
        db?.execSQL("INSERT OR IGNORE INTO roles VALUES (2,'Empleado')")

        // ── Credenciales ───────────────────────────────────────
        listOf("omar","cynthia","cristian","analia","jairo","admin").forEach { nombre ->
            db?.execSQL("INSERT OR IGNORE INTO credencial (nombreusu,passusu,rolusu,activo) VALUES ('$nombre','1234',1,1)")
        }

        // ── Actividades ────────────────────────────────────────
        db?.execSQL("INSERT OR IGNORE INTO actividad VALUES (1,'Fútbol 5','Partidos de fútbol 5 con árbitro',1500,2500,10,'Mañana')")
        db?.execSQL("INSERT OR IGNORE INTO actividad VALUES (2,'Natación Adultos','Clases de natación para adultos principiantes',2000,3500,15,'Tarde')")
        db?.execSQL("INSERT OR IGNORE INTO actividad VALUES (3,'Yoga','Sesiones de yoga y meditación',1800,3000,20,'Mañana')")
        db?.execSQL("INSERT OR IGNORE INTO actividad VALUES (4,'Tenis','Clases de tenis individuales y grupales',2500,4000,8,'Tarde')")
        db?.execSQL("INSERT OR IGNORE INTO actividad VALUES (5,'Gimnasio','Acceso a sala de musculación y máquinas',3000,5000,30,'Noche')")
        db?.execSQL("INSERT OR IGNORE INTO actividad VALUES (6,'Pilates','Clases de pilates con instructora certificada',2200,3800,12,'Mañana')")
        db?.execSQL("INSERT OR IGNORE INTO actividad VALUES (7,'Paddle','Alquiler de cancha de paddle por hora',1200,2000,4,'Tarde')")
        db?.execSQL("INSERT OR IGNORE INTO actividad VALUES (8,'Zumba','Clases de baile fitness con música latina',1500,2800,25,'Noche')")
        db?.execSQL("INSERT OR IGNORE INTO actividad VALUES (9,'Natación Niños','Clases de natación para niños de 6 a 12 años',1800,3200,12,'Tarde')")
        db?.execSQL("INSERT OR IGNORE INTO actividad VALUES (10,'Básquet','Entrenamientos y partidos de básquetbol',1600,2600,12,'Noche')")

        // ── Usuarios de prueba ─────────────────────────────────
        db?.execSQL("INSERT OR IGNORE INTO usuario (idusuario, nombre, apellido, dni, telefono, email, fecharegistro, certificadomedico) VALUES (1, 'Luis', 'García', '30123456', '351111111', 'luis@mail.com', '2024-01-01', 1)")
        db?.execSQL("INSERT OR IGNORE INTO usuario (idusuario, nombre, apellido, dni, telefono, email, fecharegistro, certificadomedico) VALUES (2, 'Ana', 'López', '28456789', '351222222', 'ana@mail.com', '2024-01-01', 1)")

        // ── Socios de prueba ───────────────────────────────────
        db?.execSQL("INSERT OR IGNORE INTO socio (nrosocio, idusuario, estadohabilitacion, cuotamensual, carneteentregado) VALUES (1, 1, 'activo', 3000.0, 1)")
        db?.execSQL("INSERT OR IGNORE INTO socio (nrosocio, idusuario, estadohabilitacion, cuotamensual, carneteentregado) VALUES (2, 2, 'inactivo', 3000.0, 0)")

        // ── Actividades de los socios ──────────────────────────
        db?.execSQL("INSERT OR IGNORE INTO socio_actividad (nrosocio, idactividad, fechainscripcion, estado) VALUES (1, 1, '2024-01-01', 'activo')")
        db?.execSQL("INSERT OR IGNORE INTO socio_actividad (nrosocio, idactividad, fechainscripcion, estado) VALUES (2, 3, '2024-01-01', 'activo')")
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

    fun buscarSocioParaCarnet(termino: String): Map<String, String>? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("""
            SELECT 
                s.nrosocio,
                u.nombre,
                u.apellido,
                u.dni,
                s.estadohabilitacion,
                s.cuotamensual,
                GROUP_CONCAT(a.nombre, ', ') AS actividades
            FROM socio s
            INNER JOIN usuario u ON s.idusuario = u.idusuario
            LEFT JOIN socio_actividad sa ON s.nrosocio = sa.nrosocio
            LEFT JOIN actividad a ON sa.idactividad = a.idactividad
            WHERE CAST(s.nrosocio AS TEXT) = ?
               OR u.dni = ?
            GROUP BY s.nrosocio
            LIMIT 1
        """.trimIndent(), arrayOf(termino, termino))

        return if (cursor.moveToFirst()) {
            mapOf(
                "nrosocio"    to cursor.getString(cursor.getColumnIndexOrThrow("nrosocio")),
                "nombre"      to cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                "apellido"    to cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                "dni"         to cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                "estado"      to cursor.getString(cursor.getColumnIndexOrThrow("estadohabilitacion")),
                "actividades" to (cursor.getString(cursor.getColumnIndexOrThrow("actividades")) ?: "Sin actividad")
            )
        } else null
            .also { cursor.close() }
    }
    fun obtenerVigenciaSocio(nrosocio: String): String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("""
        SELECT mes, anio
        FROM cuota
        WHERE nrosocio = ?
          AND estadopago = 1
        ORDER BY anio DESC, mes DESC
        LIMIT 1
    """.trimIndent(), arrayOf(nrosocio))

        val resultado = if (cursor.moveToFirst()) {
            val mes  = cursor.getInt(cursor.getColumnIndexOrThrow("mes")).toString().padStart(2, '0')
            val anio = cursor.getInt(cursor.getColumnIndexOrThrow("anio"))
            "$mes/$anio"
        } else {
            "Sin cuota"
        }

        cursor.close()
        return resultado
    }
}