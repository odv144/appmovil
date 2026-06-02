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
        const val DATABASE_VERSION = 8

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
            FOREIGN KEY (rolusu) REFERENCES roles(rolusu) ON DELETE CASCADE
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
            FOREIGN KEY (idusuario) REFERENCES usuario(idusuario) ON DELETE CASCADE
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
            UNIQUE(nrosocio, mes, anio),
            FOREIGN KEY (nrosocio) REFERENCES socio(nrosocio) ON DELETE CASCADE
            )
        """.trimIndent())
        //NO SOCIO
        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS nosocio (
                nronosocio  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                idusuario   INTEGER,
                observacion TEXT,
                FOREIGN KEY (idusuario) REFERENCES usuario(idusuario) ON DELETE CASCADE
            )
        """.trimIndent())
        //PAGO NO SOCIO
        db?.execSQL("""
        CREATE TABLE IF NOT EXISTS pago_nosocio(
            idpago INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
            nronosocio INTEGER NOT NULL,
            idactividad INTEGER,
            monto REAL,
            fechapago TEXT,
            metodopago TEXT,
            FOREIGN KEY (nronosocio) REFERENCES nosocio(nronosocio) ON DELETE CASCADE,
            FOREIGN KEY (idactividad) REFERENCES actividad(idactividad) ON DELETE SET NULL
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
                FOREIGN KEY (nrosocio)    REFERENCES socio(nrosocio) ON DELETE CASCADE,
                FOREIGN KEY (idactividad) REFERENCES actividad(idactividad) ON DELETE CASCADE
            )
        """.trimIndent())
        //NOSOCIO ACTIVIDAD
        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS nosocio_actividad (
                id          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                nronosocio  INTEGER NOT NULL,
                idactividad INTEGER NOT NULL,
                FOREIGN KEY (nronosocio)  REFERENCES nosocio(nronosocio) ON DELETE CASCADE,
                FOREIGN KEY (idactividad) REFERENCES actividad(idactividad) ON DELETE CASCADE
            )
        """.trimIndent())
        insertarDatosIniciales(db)

    }



    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion:Int,
        newVersion: Int
    ){

        db?.execSQL("DROP TABLE IF EXISTS nosocio_actividad")
        db?.execSQL("DROP TABLE IF EXISTS socio_actividad")
        db?.execSQL("DROP TABLE IF EXISTS cuota")
        db?.execSQL("DROP TABLE IF EXISTS pago_nosocio")
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
        db?.execSQL("INSERT OR IGNORE INTO usuario (idusuario, nombre, apellido, dni, telefono, email, fecharegistro, certificadomedico) VALUES (3, 'Carlos', 'Gómez', '32123456', '351333333', 'carlos@mail.com', '2024-01-01', 1)")
        db?.execSQL("INSERT OR IGNORE INTO usuario (idusuario, nombre, apellido, dni, telefono, email, fecharegistro, certificadomedico) VALUES (4, 'Marta', 'Sánchez', '35123456', '351444444', 'marta@mail.com', '2024-01-01', 1)")


        // ── Socios de prueba ───────────────────────────────────
        db?.execSQL("INSERT OR IGNORE INTO socio (nrosocio, idusuario, estadohabilitacion, cuotamensual, carneteentregado) VALUES (1, 1, 'activo', 3000.0, 1)")
        db?.execSQL("INSERT OR IGNORE INTO socio (nrosocio, idusuario, estadohabilitacion, cuotamensual, carneteentregado) VALUES (2, 2, 'inactivo', 3000.0, 0)")
        db?.execSQL("INSERT OR IGNORE INTO socio (nrosocio, idusuario, estadohabilitacion, cuotamensual, carneteentregado) VALUES (3, 3, 'activo', 3500.0, 1)")
        db?.execSQL("INSERT OR IGNORE INTO socio (nrosocio, idusuario, estadohabilitacion, cuotamensual, carneteentregado) VALUES (4, 4, 'activo', 4000.0, 1)")


        // ── Actividades de los socios ──────────────────────────
        db?.execSQL("INSERT OR IGNORE INTO socio_actividad (nrosocio, idactividad, fechainscripcion, estado) VALUES (1, 1, '2024-01-01', 'activo')")
        db?.execSQL("INSERT OR IGNORE INTO socio_actividad (nrosocio, idactividad, fechainscripcion, estado) VALUES (2, 3, '2024-01-01', 'activo')")


        // ── Cuotas de prueba (ESTADOS: Al día, Vencido, Próximo) ──
        db?.execSQL("DELETE FROM cuota")
        // Socio 1: PAGADA (Verde)
        db?.execSQL("INSERT INTO cuota VALUES (1,1,4,2025,8500,'2025-04-10',null,null,1)")
        // Socio 2: VENCIDA (Rojo) - Fecha en el pasado
        db?.execSQL("INSERT INTO cuota VALUES (2,2,3,2025,11000,'2024-01-01',null,null,0)")
        // Socio 3: PRÓXIMO (!) - Fecha en el futuro
        db?.execSQL("INSERT INTO cuota VALUES (3,3,5,2025,7500,'2026-12-31',null,null,0)")
        // Socio 4: VENCIDA (Rojo) - Fecha en el pasado
        db?.execSQL("INSERT INTO cuota VALUES (4,4,3,2025,8000,'2024-05-20',null,null,0)")
        db?.execSQL("INSERT INTO cuota VALUES (2,2,3,2025,11000,'2025-03-31',null,null,0)")

        // nosocios de prueba
        db?.execSQL("INSERT INTO usuario VALUES (5,'Pérez','Juan','40111222','1133334444','juan@mail.com','2025-04-01',1)")
        db?.execSQL("INSERT INTO nosocio VALUES (1,5,'Visitante eventual')")
        db?.execSQL("INSERT INTO nosocio_actividad VALUES (1,1,1)")
        db?.execSQL("INSERT INTO pago_nosocio VALUES (1,1,1,2500,'2025-04-01','Efectivo')")
        // más nosocios de prueba
        db?.execSQL("INSERT INTO usuario VALUES (6,'Martínez','Laura','40222333','1134445555','laura@mail.com','2025-04-03',1)")
        db?.execSQL("INSERT INTO nosocio VALUES (2,6,'Viene ocasionalmente a clases de yoga')")

        db?.execSQL("INSERT INTO usuario VALUES (7,'Sosa','Diego','40333444','1135556666','diego@mail.com','2025-04-05',1)")
        db?.execSQL("INSERT INTO nosocio VALUES (3,7,'Participa a veces en fútbol 5')")

        db?.execSQL("INSERT INTO usuario VALUES (8,'Ramírez','Camila','40444555','1136667777','camila@mail.com','2025-04-08',1)")
        db?.execSQL("INSERT INTO nosocio VALUES (4,8,'Asiste ocasionalmente a natación')")

        db?.execSQL("INSERT INTO usuario VALUES (9,'Torres','Nicolás','40555666','1137778888','nicolas@mail.com','2025-04-10',1)")
        db?.execSQL("INSERT INTO nosocio VALUES (5,9,'Usa el gimnasio algunos días')")

        db?.execSQL("INSERT INTO usuario VALUES (10,'Acosta','Sofía','40666777','1138889999','sofia@mail.com','2025-04-12',1)")
        db?.execSQL("INSERT INTO nosocio VALUES (6,10,'Realiza pilates de forma eventual')")

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