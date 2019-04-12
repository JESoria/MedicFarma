package com.app.medicfarma.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.app.medicfarma.R;
import com.app.medicfarma.config.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class PayPalActivity extends AppCompatActivity {

    double montoCompra = 0.00;
    private static final int REQUEST_CODE_PAYMENT = 7171;
    AlertDialog.Builder builder;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(PayPalActivity.this, PayPalService.class));
        super.onDestroy();

        Intent intent = new Intent(PayPalActivity.this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        Bundle datos = getIntent().getExtras();
        if (datos != null){
            montoCompra= datos.getDouble("montoCompra");
        }

        procesarPago();

    }

    private void procesarPago() {

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(montoCompra)),"USD","Titular: Soria",PayPalPayment
                .PAYMENT_INTENT_SALE);

        Intent intent = new Intent(PayPalActivity.this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PAYMENT){
            if (resultCode == Activity.RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null){
                    try {
                        String paymentDetail = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(PayPalActivity.this, PagoOKActivity.class)
                                .putExtra("PaymentDetails",paymentDetail)
                                .putExtra("PaymentAmount",montoCompra));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(resultCode == Activity.RESULT_CANCELED){
                //Posiblemente de problema, si las interfacez de PayPal son modales
                //Ya que no pueden haber dos modales abiertas
                builder = new AlertDialog.Builder(PayPalActivity.this);
                builder.setMessage("Cancelado")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(PayPalActivity.this,HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
            else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                builder = new AlertDialog.Builder(PayPalActivity.this);
                builder.setMessage("Invalida")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(PayPalActivity.this,HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }

    }

}
