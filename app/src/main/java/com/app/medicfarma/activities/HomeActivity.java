package com.app.medicfarma.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import com.app.medicfarma.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class HomeActivity extends AppCompatActivity {

    CardView cvFarmacias, cvFarmaciaPreferida, cvEstados, cvReclamos;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cvFarmacias = (CardView) findViewById(R.id.cvFarmacias);
        cvFarmaciaPreferida = (CardView) findViewById(R.id.cvFarmaciaPreferida);
        cvEstados = (CardView) findViewById(R.id.cvEstados);
        cvReclamos = (CardView) findViewById(R.id.cvReclamos);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(1)
                .playOn(cvFarmacias);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(1)
                .playOn(cvFarmaciaPreferida);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(1)
                .playOn(cvEstados);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(1)
                .playOn(cvReclamos);

        cvFarmacias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Lista de busqueda de todas las farmacias
                Intent intent = new Intent(HomeActivity.this,ProductsPharmaciesActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cvFarmaciaPreferida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Menu de las farmacias
                Intent intent = new Intent(HomeActivity.this,PharmaciesActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cvReclamos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("¡Todavia no se ha desarrollado esta sección!")
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
        });

        cvEstados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("¡Todavia no se ha desarrollado esta sección!")
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
        });

    }
}
