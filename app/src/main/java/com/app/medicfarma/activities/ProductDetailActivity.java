package com.app.medicfarma.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.medicfarma.R;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.ProductDetail;
import com.app.medicfarma.ws_app.ProductDetailBridge;

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailBridge.AsyncResponse {

    ProgressBar progressBar;
    Button btnMas, btnMenos;
    TextView tvMedicamento, tvContenidoPresentacion, tvContenidoLaboratorio, tvContenidoCategoria;
    TextView tvContenidoPrecio, tvContenidoFechaVencimiento, tvContenidoIngredientes;
    EditText edtCantidad;
    AlertDialog.Builder builder;
    private int auxcantidad;
    private int idSucursalProducto;
    private int idFarmacia;
    ProductDetail productDetail = new ProductDetail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        final DbHelper mDbHelper = new DbHelper(this);

        btnMenos = (Button) findViewById(R.id.btnMenos);
        btnMas = (Button) findViewById(R.id.btnMas);
        edtCantidad = (EditText) findViewById(R.id.edtCantidad);
        tvMedicamento = (TextView) findViewById(R.id.tvMedicamento);
        tvContenidoPresentacion = (TextView) findViewById(R.id.tvContenidoPresentacion);
        tvContenidoLaboratorio = (TextView) findViewById(R.id.tvContenidoLaboratorio);
        tvContenidoCategoria = (TextView) findViewById(R.id.tvContenidoCategoria);
        tvContenidoPrecio = (TextView) findViewById(R.id.tvContenidoPrecio);
        tvContenidoFechaVencimiento = (TextView) findViewById(R.id.tvContenidoFechaVencimiento);
        tvContenidoIngredientes = (TextView) findViewById(R.id.tvContenidoIngredientes);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_product_detail);

        auxcantidad = 1;
        edtCantidad.setFocusable(false);
        edtCantidad.setCursorVisible(false);
        edtCantidad.setEnabled(false);
        edtCantidad.setText(""+auxcantidad);

        Bundle datos = getIntent().getExtras();
        idSucursalProducto = datos.getInt("idSucursalProducto");
        idFarmacia = datos.getInt("idFarmacia");

        progressBar.setVisibility(View.VISIBLE);
        startProcessProductDetail(mDbHelper,idSucursalProducto,idFarmacia);

        btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Aqui se debe comparar con las existencias
                if (productDetail.getExistencia() > auxcantidad){
                    auxcantidad++;
                    edtCantidad.setText(""+auxcantidad);
                }
            }
        });

        btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auxcantidad > 1){
                    auxcantidad --;
                    edtCantidad.setText(""+auxcantidad);
                }
            }
        });

    }

    @Override
    public void processFinish(String response, ProductDetail model) {
        try{
            progressBar.setVisibility(View.INVISIBLE);

            if(!response.equals("") && model != null){
                //Aqui van las acciones a realizar
                productDetail.setProducto(model.getProducto());
                productDetail.setPresentacion(model.getPresentacion());
                productDetail.setCategoria(model.getCategoria());
                productDetail.setLaboratorio(model.getLaboratorio());
                productDetail.setFechavencimiento(model.getFechavencimiento());
                productDetail.setPrecio(model.getPrecio());
                productDetail.setExistencia(model.getExistencia());
                productDetail.setPrincipalActivos(model.getPrincipalActivos());

                tvMedicamento.setText(""+productDetail.getProducto());
                tvContenidoPresentacion.setText(""+productDetail.getPresentacion());
                tvContenidoLaboratorio.setText(""+productDetail.getLaboratorio());
                tvContenidoCategoria.setText(""+productDetail.getCategoria());
                tvContenidoPrecio.setText("$ "+productDetail.getPrecio());
                tvContenidoFechaVencimiento.setText(productDetail.getFechavencimiento());
                tvContenidoIngredientes.setText(""+productDetail.getPrincipalActivos());
            }
            else {

                builder = new AlertDialog.Builder(ProductDetailActivity.this);
                builder.setMessage("¡Ups ocurrio un problema!")
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

        }
        catch (NullPointerException e){

            builder = new AlertDialog.Builder(ProductDetailActivity.this);
            builder.setMessage("¡Ups ocurrio un problema!")
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
    }

    public void startProcessProductDetail(DbHelper mDbHelper, int idSucursalProducto, int idFarmacia){
        progressBar.setVisibility(View.VISIBLE);
        new ProductDetailBridge(mDbHelper,progressBar,this).execute(String.valueOf(idSucursalProducto), String.valueOf(idFarmacia));
    }

}
