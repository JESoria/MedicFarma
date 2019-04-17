package com.app.medicfarma.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.app.medicfarma.R;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.ws_app.IncidenciaBridge;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class IncidenciaActivity extends AppCompatActivity implements IncidenciaBridge.AsyncResponse {

    EditText incidencia, telefono;
    Button procesar,cancelar;
    int idPedido;
    ProgressBar progressBar;
    ImageView imgAtras;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencia);

        incidencia = (EditText) findViewById(R.id.edtIncidencia);
        telefono = (EditText) findViewById(R.id.edtTelefonoIncidencia);
        procesar = (Button) findViewById(R.id.btnProcesarIncidencia);
        cancelar = (Button) findViewById(R.id.btnCancelarIncidencia);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_incidencia);


        imgAtras = (ImageView) findViewById(R.id.imgAtras);

        imgAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IncidenciaActivity.this, IncidenciasListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbarIncidencia);
        setSupportActionBar(toolbar);

        final DbHelper mDbHelper = new DbHelper(IncidenciaActivity.this);

        Bundle datos = getIntent().getExtras();
        idPedido = datos.getInt("idPedido");

        procesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                procesarIncidencia(mDbHelper);
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(IncidenciaActivity.this);
                builder.setMessage("¿Estas seguro de cancelar tu reclamo?")
                        .setCancelable(false)
                        .setNegativeButton("Ups, No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Acciones false
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mDbHelper.deletePedido();
                                        Intent intent = new Intent(IncidenciaActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void procesarIncidencia(DbHelper mDbHelper){

        if(TextUtils.isEmpty(incidencia.getText().toString())){
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(1)
                    .playOn(incidencia);
            incidencia.setEnabled(true);
            incidencia.setError("¡Debe ingresar su incoveniente!");
            incidencia.requestFocus();
        }
        else if(TextUtils.isEmpty(telefono.getText().toString())){
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(1)
                    .playOn(telefono);
            telefono.setEnabled(true);
            telefono.setError("¡Debe ingresar su telefono de contacto!");
            telefono.requestFocus();
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            new IncidenciaBridge(mDbHelper,progressBar,this).execute(String.valueOf(idPedido),incidencia.getText().toString(),telefono.getText().toString());
        }
    }

    @Override
    public void processFinish(String response) {
        try{
            progressBar.setVisibility(View.INVISIBLE);
            if(!response.equals("")){
                AlertDialog.Builder builder = new AlertDialog.Builder(IncidenciaActivity.this);
                builder.setMessage("Tu reclamo ha sido procesado, la farmacia te contactará")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(IncidenciaActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(IncidenciaActivity.this);
                builder.setMessage("Ups! ocurrio un problema, no se pudo realizar tu reclamo")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(IncidenciaActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        catch (NullPointerException e){
            AlertDialog.Builder builder = new AlertDialog.Builder(IncidenciaActivity.this);
            builder.setMessage("Sin servicio")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
