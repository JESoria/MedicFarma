package com.app.medicfarma.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.app.medicfarma.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class AboutActivity extends AppCompatActivity {

    Button regresar;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        regresar = (Button) findViewById(R.id.btnRegresar);
        logo = (ImageView) findViewById(R.id.imgLogoAbout);

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Wobble)
                        .duration(1000)
                        .repeat(0)
                        .playOn(regresar);
                Intent intent = new Intent(AboutActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(logo);
    }
}
