package com.app.medicfarma.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.medicfarma.R;

public class PaymentTypeActivity extends AppCompatActivity {

    Button btnEfectivo, btnPayPal, btnCancelar;
    double montoCompra = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_type);

        btnEfectivo = (Button) findViewById(R.id.btnEfectivo);
        btnPayPal = (Button) findViewById(R.id.btnPaypal);
        btnCancelar = (Button) findViewById(R.id.btnCancelarTipo);

        Bundle datos = getIntent().getExtras();

        if (datos != null){
            montoCompra= datos.getDouble("montoCompra");
        }

        btnEfectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentTypeActivity.this, InfoExtraEnvioActivity.class);
                intent.putExtra("montoCompra",montoCompra);
                intent.putExtra("tipoPago","efectivo");
                startActivity(intent);
                finish();
            }
        });

        btnPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentTypeActivity.this, InfoExtraEnvioActivity.class);
                intent.putExtra("montoCompra",montoCompra);
                intent.putExtra("tipoPago","paypal");
                startActivity(intent);
                finish();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
