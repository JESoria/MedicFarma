package com.app.medicfarma.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.medicfarma.R;
import com.app.medicfarma.adapters.AdapterOrdenCompra;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.DetallePedido;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity {

    TextView tvTotal;
    String monto;
    double montoCompra = 0.00;
    Button procesarOrden,cancelarOrden;
    ImageView agregarProducto;
    int idFarmacia;
    int idSucursal;

    private RecyclerView listaProductos;
    ImageView imgAtras;
    private Toolbar toolbar;
    public AdapterOrdenCompra adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        final DbHelper mDbHelper = new DbHelper(OrderDetailActivity.this);

        imgAtras = (ImageView) findViewById(R.id.imgAtras);

        imgAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this,ProductsSpecificBranchOfficeActivity.class);
                intent.putExtra("idFarmacia",idFarmacia);
                intent.putExtra("idSucursal",idSucursal);
                startActivity(intent);
                finish();
            }
        });

        tvTotal = (TextView) findViewById(R.id.tvTotalDetalleOrden);
        procesarOrden = (Button) findViewById(R.id.btnProcesarOrden);
        cancelarOrden = (Button) findViewById(R.id.btnCancelarOrdenDetalle);
        agregarProducto = (ImageView) findViewById(R.id.imvAgregarPOrden);

        Bundle datos = getIntent().getExtras();
        idFarmacia = datos.getInt("idFarmacia");
        idSucursal = datos.getInt("idSucursal");
        boolean eliminar = datos.getBoolean("eliminar");

        if (eliminar){
            Intent intent = new Intent(OrderDetailActivity.this,OrderDetailActivity.class);
            intent.putExtra("idFarmacia",idFarmacia);
            intent.putExtra("idSucursal",idSucursal);
            startActivity(intent);
            finish();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbarOrder);
        setSupportActionBar(toolbar);

        listaProductos = (RecyclerView) findViewById(R.id.rvDetalleOrder);
        LinearLayoutManager llm = new LinearLayoutManager(OrderDetailActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaProductos.setLayoutManager(llm);

        cargarListaProductos(mDbHelper);

        agregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailActivity.this,ProductsSpecificBranchOfficeActivity.class);
                intent.putExtra("idFarmacia",idFarmacia);
                intent.putExtra("idSucursal",idSucursal);
                startActivity(intent);
                finish();
            }
        });

        procesarOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailActivity.this, PaymentTypeActivity.class);
                intent.putExtra("montoCompra",montoCompra);
                startActivity(intent);
                finish();
            }
        });

        cancelarOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDbHelper.deletePedido();
                Intent intent = new Intent(OrderDetailActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void cargarListaProductos(DbHelper mDbHelper){

        ArrayList<DetallePedido> detallePedido;

        Cursor listado = mDbHelper.listadoProductos();

        detallePedido = new ArrayList<>();

        do{
            detallePedido.add(new DetallePedido(listado.getInt(0), listado.getInt(1), listado.getInt(2), listado.getString(3), listado.getDouble(4)));
            montoCompra = montoCompra + listado.getDouble(4);
        } while (listado.moveToNext());


        if (detallePedido != null){
            adaptador = new AdapterOrdenCompra(detallePedido,OrderDetailActivity.this);
            listaProductos.setAdapter(adaptador);
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            String monto = decimalFormat.format(montoCompra);
            tvTotal.setText("Total compra: $"+monto);
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
            builder.setMessage("Ups! :V ocurrio un problema")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }


    }

}
