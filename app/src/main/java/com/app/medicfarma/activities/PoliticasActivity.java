package com.app.medicfarma.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.app.medicfarma.R;

public class PoliticasActivity extends AppCompatActivity {

    ImageView imgAtras;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politicas);

        imgAtras = (ImageView) findViewById(R.id.imgAtras);

        imgAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PoliticasActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbarPoliticas);
        setSupportActionBar(toolbar);
    }
}
