package com.app.medicfarma.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import com.app.medicfarma.R;
import com.app.medicfarma.adapters.AdapterFarmacias;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.ws_app.Farmacias;
import java.util.ArrayList;

public class ListDrugStoreActivity extends AppCompatActivity implements Farmacias.AsyncResponse,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private RecyclerView listaFarmacias;
    ImageView imgAtras;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_drug_store);

        final DbHelper mDbHelper = new DbHelper(ListDrugStoreActivity.this);

        imgAtras = (ImageView) findViewById(R.id.imgAtras);

        imgAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDrugStoreActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ActivityCompat.requestPermissions(ListDrugStoreActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        toolbar = (Toolbar) findViewById(R.id.toolbarListFarmacias);
        setSupportActionBar(toolbar);

        listaFarmacias = (RecyclerView) findViewById(R.id.rvFarmacias);
        LinearLayoutManager llm = new LinearLayoutManager(ListDrugStoreActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaFarmacias.setLayoutManager(llm);


        new Farmacias(mDbHelper,ListDrugStoreActivity.this).execute();
    }

    public AdapterFarmacias adaptador;

    @Override
    public void processFinish(String response, ArrayList farmacias) {
        try{

            if(!response.equals("") && farmacias != null){
                adaptador = new AdapterFarmacias(farmacias,this);
                listaFarmacias.setAdapter(adaptador);

            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(ListDrugStoreActivity.this);
                builder.setMessage("Lo sentimos el servicio no esta disponible")
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
        catch (NullPointerException e){
            AlertDialog.Builder builder = new AlertDialog.Builder(ListDrugStoreActivity.this);
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
