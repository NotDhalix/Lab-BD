package com.example.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class ClaseSQLiteBD1 extends SQLiteOpenHelper {
    public static final String DataBaseName = "Estudiantes.db";
    public static final int dbversion = 1;

    public ClaseSQLiteBD1(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DataBaseName, factory, dbversion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Se ejecuta la primera vez para crear la BD
        db.execSQL("CREATE TABLE contacto (_id INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE TEXT, CORREO TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se ejecuta cuando hay una nueva versión de la BD
        db.execSQL("DROP TABLE IF EXISTS contacto;");
        onCreate(db);
    }
}
