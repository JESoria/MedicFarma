package com.app.medicfarma.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.app.medicfarma.R;
import com.app.medicfarma.helpers.DbHelper;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class InfoExtraEnvioActivity extends AppCompatActivity {


    EditText edtDireccionEnvio, edtTelefono;
    Button btnSeguir, btnCancelar;
    double montoCompra = 0.00;
    int idSucursal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_extra_envio);

        final DbHelper mDbHelper = new DbHelper(InfoExtraEnvioActivity.this);

        edtDireccionEnvio = (EditText) findViewById(R.id.edtDireccionEnvio);
        edtTelefono = (EditText) findViewById(R.id.edtTelefono);
        btnSeguir = (Button) findViewById(R.id.btnSeguir);
        btnCancelar = (Button) findViewById(R.id.btnCancelarInfoEx);

        Bundle datos = getIntent().getExtras();
        if (datos != null){
            montoCompra= datos.getDouble("montoCompra");
            idSucursal = datos.getInt("idSucursal");
        }

        btnSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tipoPago;
                Bundle data = getIntent().getExtras();
                tipoPago = data.getString("tipoPago");

                procesarPago(tipoPago);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoExtraEnvioActivity.this);
                builder.setMessage("¿Estas seguro de cancelar su orden?")
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
                                        Intent intent = new Intent(InfoExtraEnvioActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void procesarPago(String tipoPago){

        if(TextUtils.isEmpty(edtDireccionEnvio.getText().toString())){
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(1)
                    .playOn(edtDireccionEnvio);
            edtDireccionEnvio.setEnabled(true);
            edtDireccionEnvio.setError("¡Debe ingresar la dirección de envio!");
            edtDireccionEnvio.requestFocus();
        }
        else if(TextUtils.isEmpty(edtTelefono.getText().toString())){
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(1)
                    .playOn(edtTelefono);
            edtTelefono.setEnabled(true);
            edtTelefono.setError("¡Debe ingresar su telefono de contacto!");
            edtTelefono.requestFocus();
        }
        else{
            if(tipoPago.equals("efectivo")){
                Intent intent = new Intent(InfoExtraEnvioActivity.this, CashCheckActivity.class);
                intent.putExtra("direccion",edtDireccionEnvio.getText().toString());
                intent.putExtra("telefono",edtTelefono.getText().toString());
                intent.putExtra("idSucursal",idSucursal);
                intent.putExtra("montoCompra",montoCompra);
                intent.putExtra("tipopago",tipoPago);
                startActivity(intent);
                finish();
            }
            else{
                Intent intent = new Intent(InfoExtraEnvioActivity.this, PayPalActivity.class);
                intent.putExtra("direccion",edtDireccionEnvio.getText().toString());
                intent.putExtra("telefono",edtTelefono.getText().toString());
                intent.putExtra("idSucursal",idSucursal);
                intent.putExtra("montoCompra",montoCompra);
                intent.putExtra("tipopago",tipoPago);
                startActivity(intent);
                finish();
            }

        }
    }
}
