package com.app.medicfarma.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.medicfarma.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PagoOKActivity extends AppCompatActivity {

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_ok);

        Intent intent = getIntent();

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            verDetalles(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void verDetalles(JSONObject response, String monto) {
        try {

            builder = new AlertDialog.Builder(PagoOKActivity.this);
            builder.setMessage(response.getString("id")+"\n"+response.getString("state")+"\n"+"$"+monto)
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(PagoOKActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();


        }catch (JSONException e){
            e.printStackTrace();
        }
    }

}
