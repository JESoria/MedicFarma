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
import com.app.medicfarma.adapters.AdapterIncidencias;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.Incidencias;
import com.app.medicfarma.ws_app.ListIncidenciasBridge;
import java.util.ArrayList;

public class IncidenciasListActivity extends AppCompatActivity implements ListIncidenciasBridge.AsyncResponse{

    private RecyclerView listaPedidos;
    ImageView imgAtras;
    private Toolbar toolbar;
    ProgressBar progressBar;
    int idUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencias_list);

        progressBar = (ProgressBar) findViewById(R.id.progressBarIncidenciasList);

        final DbHelper mDbHelper = new DbHelper(IncidenciasListActivity.this);

        idUsuario = mDbHelper.cargarUsuario().getIdUsuario();

        imgAtras = (ImageView) findViewById(R.id.imgAtras);

        imgAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IncidenciasListActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbarListIncidencias);
        setSupportActionBar(toolbar);

        listaPedidos = (RecyclerView) findViewById(R.id.rvIncidenciasList);
        LinearLayoutManager llm = new LinearLayoutManager(IncidenciasListActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaPedidos.setLayoutManager(llm);

        progressBar.setVisibility(View.VISIBLE);
        new ListIncidenciasBridge(mDbHelper,progressBar,IncidenciasListActivity.this).execute(String.valueOf(idUsuario));
    }

    public AdapterIncidencias adaptador;

    @Override
    public void processFinish(String response, ArrayList<Incidencias> incidencias) {
        try{
            progressBar.setVisibility(View.INVISIBLE);
            if(!response.equals("") && incidencias != null){
                adaptador = new AdapterIncidencias(incidencias,this);
                listaPedidos.setAdapter(adaptador);
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(IncidenciasListActivity.this);
                builder.setMessage("No hay pedido realizados o ya realizo reclamos a sus pedidos")
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
            AlertDialog.Builder builder = new AlertDialog.Builder(IncidenciasListActivity.this);
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
