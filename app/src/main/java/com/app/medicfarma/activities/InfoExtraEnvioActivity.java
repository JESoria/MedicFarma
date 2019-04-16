package com.app.medicfarma.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.medicfarma.R;

public class InfoExtraEnvioActivity extends AppCompatActivity {


    EditText edtDireccionEnvio, edtTelefono;
    Button btnSeguir, btnCancelar;
    double montoCompra = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_extra_envio);

        edtDireccionEnvio = (EditText) findViewById(R.id.edtDireccionEnvio);
        edtTelefono = (EditText) findViewById(R.id.edtTelefono);
        btnSeguir = (Button) findViewById(R.id.btnSeguir);
        btnCancelar = (Button) findViewById(R.id.btnCancelarInfoEx);

        Bundle datos = getIntent().getExtras();
        if (datos != null){
            montoCompra= datos.getDouble("montoCompra");
        }

        btnSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String direccion, telefono,tipoPago;

                Bundle data = getIntent().getExtras();
                tipoPago = data.getString("tipoPago");
                direccion = edtDireccionEnvio.getText().toString();
                telefono = edtTelefono.getText().toString();

                if(tipoPago.equals("efectivo")){
                    Intent intent = new Intent(InfoExtraEnvioActivity.this, CashCheckActivity.class);
                    intent.putExtra("direccion",direccion);
                    intent.putExtra("telefono",telefono);
                    intent.putExtra("montoCompra",montoCompra);
                    intent.putExtra("tipopago",tipoPago);
                    startActivity(intent);
                    finish();
                }
                else{
                   Intent intent = new Intent(InfoExtraEnvioActivity.this, PayPalActivity.class);
                   intent.putExtra("direccion",direccion);
                   intent.putExtra("telefono",telefono);
                   intent.putExtra("montoCompra",montoCompra);
                    intent.putExtra("tipopago",tipoPago);
                   startActivity(intent);
                   finish();
                }

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
