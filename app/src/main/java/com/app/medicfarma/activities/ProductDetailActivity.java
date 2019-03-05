package com.app.medicfarma.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.medicfarma.R;

public class ProductDetailActivity extends AppCompatActivity {


    Button btnMas, btnMenos;
    EditText edtCantidad;
    private int cantidad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        btnMenos = (Button) findViewById(R.id.btnMenos);
        btnMas = (Button) findViewById(R.id.btnMas);
        edtCantidad = (EditText) findViewById(R.id.edtCantidad);

        cantidad = 1;
        edtCantidad.setFocusable(false);
        edtCantidad.setCursorVisible(false);
        edtCantidad.setEnabled(false);
        edtCantidad.setText(""+cantidad);

        btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Aqui se debe comparar con las existencias
                cantidad++;
                edtCantidad.setText(""+cantidad);
            }
        });

        btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cantidad > 1){
                    cantidad --;
                    edtCantidad.setText(""+cantidad);
                }
            }
        });

    }



}
