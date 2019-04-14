package com.app.medicfarma.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.app.medicfarma.R;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.DetallePedido;
import com.app.medicfarma.models.ProductDetail;
import com.app.medicfarma.ws_app.ProductDetailBridge;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailBridge.AsyncResponse {

    ProgressBar progressBar;
    Button btnMas, btnMenos,btnAgregar,btnCancelar;
    TextView tvMedicamento, tvContenidoPresentacion, tvContenidoLaboratorio, tvContenidoCategoria;
    TextView tvContenidoPrecio, tvContenidoFechaVencimiento, tvContenidoIngredientes;
    EditText edtCantidad;
    ImageView imgMedicina;
    AlertDialog.Builder builder;
    private int auxcantidad;
    private int idSucursalProducto;
    private int idSucursal;
    private int idFarmacia;
    public static boolean estadoOrden;
    ProductDetail productDetail = new ProductDetail();
    final DbHelper mDbHelper = new DbHelper(this);
    private boolean editar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

//Componentes------------------------------------------------------------------------
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
        btnAgregar = (Button) findViewById(R.id.btnAgregarProductoDetalle);
        btnCancelar = (Button) findViewById(R.id.btnCancelaProductoDetalle);
        imgMedicina = (ImageView) findViewById(R.id.imgMedicina);
//Componentes------------------------------------------------------------------------
        editar = false;
        auxcantidad = 1;
        edtCantidad.setFocusable(false);
        edtCantidad.setCursorVisible(false);
        edtCantidad.setEnabled(false);
        edtCantidad.setText(""+auxcantidad);

        Bundle datos = getIntent().getExtras();
        idSucursalProducto = datos.getInt("idSucursalProducto");
        idFarmacia = datos.getInt("idFarmacia");
        idSucursal = datos.getInt("idSucursal");
        editar = datos.getBoolean("editar");
        estadoOrden = mDbHelper.estadoOrden();

        if (editar){
            btnAgregar.setText("Actualizar");
        }

        startProcessProductDetail(mDbHelper,idSucursalProducto,idFarmacia);

        btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarProducto();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(estadoOrden){
                    //Regresa a pantalla de busqueda por sucursal especifica
                    Intent intent = new Intent(ProductDetailActivity.this,ProductsSpecificBranchOfficeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(ProductDetailActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
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
                habilitar();
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

    public void agregarProducto(){

        DetallePedido detallePedido = new DetallePedido();
        detallePedido.setIdSucursalProducto(idSucursalProducto);
        detallePedido.setIdFarmacia(idFarmacia);
        detallePedido.setCantidad(Integer.parseInt(edtCantidad.getText().toString()));
        detallePedido.setProducto(productDetail.getProducto());
        detallePedido.setPrecio(productDetail.getPrecio());

        if (editar){
            mDbHelper.actualizarDetallePedido(detallePedido);
        }
        else {
            mDbHelper.insertDetallePedido(detallePedido);
        }

        Intent intent = new Intent(ProductDetailActivity.this,ProductsSpecificBranchOfficeActivity.class);
        intent.putExtra("idFarmacia",idFarmacia);
        intent.putExtra("idSucursal",idSucursal);
        startActivity(intent);
        finish();
    }

    public void startProcessProductDetail(DbHelper mDbHelper, int idSucursalProducto, int idFarmacia){
        deshabilitar();
        progressBar.setVisibility(View.VISIBLE);
        new ProductDetailBridge(mDbHelper,progressBar,this).execute(String.valueOf(idSucursalProducto), String.valueOf(idFarmacia));
    }

    public void deshabilitar(){
        btnMenos.setEnabled(false);
        btnMas.setEnabled(false);
        edtCantidad.setEnabled(false);
        tvMedicamento.setEnabled(false);
        tvContenidoPresentacion.setEnabled(false);
        tvContenidoLaboratorio.setEnabled(false);
        tvContenidoCategoria.setEnabled(false);
        tvContenidoPrecio.setEnabled(false);
        tvContenidoFechaVencimiento.setEnabled(false);
        tvContenidoIngredientes.setEnabled(false);
        btnAgregar.setEnabled(false);
        btnCancelar.setEnabled(false);
        imgMedicina.setEnabled(false);
    };

    public void habilitar(){
        btnMenos.setEnabled(true);
        btnMas.setEnabled(true);
        edtCantidad.setEnabled(true);
        tvMedicamento.setEnabled(true);
        tvContenidoPresentacion.setEnabled(true);
        tvContenidoLaboratorio.setEnabled(true);
        tvContenidoCategoria.setEnabled(true);
        tvContenidoPrecio.setEnabled(true);
        tvContenidoFechaVencimiento.setEnabled(true);
        tvContenidoIngredientes.setEnabled(true);
        btnAgregar.setEnabled(true);
        btnCancelar.setEnabled(true);
        imgMedicina.setEnabled(true);

        //Animacion
        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(imgMedicina);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(btnMenos);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(btnMas);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(edtCantidad);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(tvMedicamento);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(tvContenidoPresentacion);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(tvContenidoLaboratorio);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(tvContenidoCategoria);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(tvContenidoPrecio);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(tvContenidoFechaVencimiento);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(tvContenidoIngredientes);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(btnAgregar);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(btnCancelar);
        //End Animacion
    };

}
