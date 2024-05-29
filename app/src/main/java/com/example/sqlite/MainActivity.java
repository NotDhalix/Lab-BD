package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText et_nombre;
    EditText et_correo;
    Button guardar;
    Button borrar;
    Button actualizar;
    Button adelante;
    Button atras;
    Cursor c;
    AlertDialog.Builder alertDialogBuilder;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_nombre = findViewById(R.id.et1);
        et_correo = findViewById(R.id.et2);
        guardar = findViewById(R.id.bt_insert);
        borrar = findViewById(R.id.bt_delete);
        actualizar = findViewById(R.id.bt_update);
        adelante = findViewById(R.id.bt_adelante);
        atras = findViewById(R.id.bt_atras);

        ClaseSQLiteBD1 csql = new ClaseSQLiteBD1(this, "Estudiantes.db", null, 1);
        db = csql.getWritableDatabase();

        c = db.rawQuery("SELECT * FROM contacto", null);

        alertDialogBuilder = new AlertDialog.Builder(this);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put("NOMBRE", et_nombre.getText().toString());
                nuevoRegistro.put("CORREO", et_correo.getText().toString());

                db.insert("contacto", null, nuevoRegistro);
                Toast.makeText(getApplicationContext(), "Contacto añadido correctamente", Toast.LENGTH_LONG).show();
                et_nombre.setText("");
                et_correo.setText("");
                refreshCursor();
            }
        });

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBuilder.setTitle("Confirmación");
                alertDialogBuilder.setMessage("¿Está seguro de eliminar el registro?")
                        .setCancelable(false)
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String ID = String.valueOf(c.getInt(0));
                                db.delete("contacto", "_id=?", new String[]{ID});
                                Toast.makeText(getApplicationContext(), "Registro eliminado", Toast.LENGTH_LONG).show();
                                et_nombre.setText("");
                                et_correo.setText("");
                                refreshCursor();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).create().show();
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBuilder.setTitle("Confirmación");
                alertDialogBuilder.setMessage("¿Está seguro de actualizar el registro?")
                        .setCancelable(false)
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                ContentValues nuevoRegistro = new ContentValues();
                                nuevoRegistro.put("NOMBRE", et_nombre.getText().toString());
                                nuevoRegistro.put("CORREO", et_correo.getText().toString());
                                String ID = String.valueOf(c.getInt(0));
                                int cant = db.update("contacto", nuevoRegistro, "_id = ?", new String[]{ID});
                                if (cant == 1) {
                                    Toast.makeText(getApplicationContext(), "Datos modificados correctamente", Toast.LENGTH_SHORT).show();
                                    refreshCursor();
                                    et_nombre.setText("");
                                    et_correo.setText("");
                                } else {
                                    Toast.makeText(getApplicationContext(), "No existe un contacto con dicho documento", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).create().show();
            }
        });

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!c.isFirst()) {
                    c.moveToPrevious();
                } else {
                    c.moveToLast();
                }
                updateEditTexts();
            }
        });

        adelante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!c.isLast()) {
                    c.moveToNext();
                } else {
                    c.moveToFirst();
                }
                updateEditTexts();
            }
        });

        if (c.moveToFirst()) {
            updateEditTexts();
        }
    }

    private void updateEditTexts() {
        if (c != null && c.getCount() > 0) {
            et_nombre.setText(c.getString(1));
            et_correo.setText(c.getString(2));
        }
    }

    private void refreshCursor() {
        c = db.rawQuery("SELECT * FROM contacto", null);
        if (c.moveToFirst()) {
            updateEditTexts();
        }
    }
}
