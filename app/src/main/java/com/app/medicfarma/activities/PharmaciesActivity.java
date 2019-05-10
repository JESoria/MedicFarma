package com.app.medicfarma.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.app.medicfarma.R;
import com.app.medicfarma.adapters.AdapterFarmacias;
import com.app.medicfarma.config.ConexionInternet;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.ws_app.FarmaciasBridge;
import java.util.ArrayList;

public class PharmaciesActivity extends AppCompatActivity implements FarmaciasBridge.AsyncResponse {

    private RecyclerView listaFarmacias;
    ImageView imgAtras;
    private Toolbar toolbar;
    ProgressBar progressBar;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacies);

        ConexionInternet conexionInternet = new ConexionInternet();
        conexionInternet.getStateInternet(PharmaciesActivity.this);

        progressBar = (ProgressBar) findViewById(R.id.progressBarMalo);

        final DbHelper mDbHelper = new DbHelper(PharmaciesActivity.this);

        imgAtras = (ImageView) findViewById(R.id.imgAtras);

        imgAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PharmaciesActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbarListFarmacias);
        setSupportActionBar(toolbar);

        listaFarmacias = (RecyclerView) findViewById(R.id.rvFarmacias);
        LinearLayoutManager llm = new LinearLayoutManager(PharmaciesActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaFarmacias.setLayoutManager(llm);

        progressBar.setVisibility(View.VISIBLE);
        new FarmaciasBridge(mDbHelper,progressBar,PharmaciesActivity.this).execute();

    }

    public AdapterFarmacias adaptador;

    @Override
    public void processFinish(String response, ArrayList farmacias) {
        try{
        progressBar.setVisibility(View.INVISIBLE);
        if(!response.equals("") && farmacias != null){
            adaptador = new AdapterFarmacias(farmacias,this);
            listaFarmacias.setAdapter(adaptador);

        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(PharmaciesActivity.this);
            builder.setMessage("Lo sentimos el servicio no esta disponible")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(PharmaciesActivity.this, HomeActivity.class);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PharmaciesActivity.this);
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
