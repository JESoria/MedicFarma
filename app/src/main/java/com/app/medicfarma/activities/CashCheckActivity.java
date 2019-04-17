package com.app.medicfarma.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.app.medicfarma.R;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.DetallePedido;
import com.app.medicfarma.models.OrdenCompra;
import com.app.medicfarma.models.Pedido;
import com.app.medicfarma.models.UsuarioModel;
import com.app.medicfarma.ws_app.OrdenCompraBridge;

import static com.app.medicfarma.adapters.AdapterProductsPharmacies.ids;
import java.util.ArrayList;

public class CashCheckActivity extends AppCompatActivity implements OrdenCompraBridge.AsyncResponse {

    String direccion, telefono;
    double montoCompra;
    int idSucursal;
    final DbHelper mDbHelper = new DbHelper(CashCheckActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_check);


        Bundle datos = getIntent().getExtras();
        if (datos != null) {
            direccion = datos.getString("direccion");
            telefono = datos.getString("telefono");
            montoCompra = datos.getDouble("montoCompra");
            idSucursal = datos.getInt("idSucursal");
        }

        UsuarioModel usuarioModel;
        usuarioModel = mDbHelper.cargarUsuario();

            //Generando codigo de pedido
        int cod1 = 0;
        int cod2 = 0;
        int idFarmacia = 0;
        int idSucursalProducto = 0;
        cod1 = (int) (Math.random() * 1000) + 1;
        cod2 = (int) (Math.random() * 1000) + 1;
            //Fin de codigo de pedido

        ArrayList<DetallePedido> detallePedido;

        Cursor listado = mDbHelper.listadoProductos();

        detallePedido = new ArrayList<>();

        do {
            detallePedido.add(new DetallePedido(listado.getInt(0), listado.getInt(1), listado.getInt(2), listado.getString(3), listado.getDouble(4)));
        } while (listado.moveToNext());

        DetallePedido detail;
        detail = detallePedido.get(0);
        idFarmacia = detail.getIdFarmacia();
        idSucursalProducto = idSucursal;

        Pedido pedido = new Pedido();
        pedido.setCodigoPedido("OC" + idFarmacia + idSucursalProducto + cod1 + cod2);
        pedido.setIdusuario(usuarioModel.getIdUsuario());
        pedido.setIdsucursal(idSucursalProducto);
        pedido.setDireccion(direccion);
        pedido.setTelefono(telefono);
        pedido.setMontoCompra(montoCompra);
        pedido.setEstadoPago("No pagado");

        OrdenCompra ordenCompra = new OrdenCompra();
        ordenCompra.setPedidos(pedido);
        ordenCompra.setDetallePedido(detallePedido);

        procesarPedido(mDbHelper, ordenCompra);

    }

    private void procesarPedido(DbHelper  mDbHelper, OrdenCompra ordenCompra) {
        new OrdenCompraBridge(mDbHelper,CashCheckActivity.this).execute(ordenCompra);
    }


    @Override
    public void processFinish(String response) {
        try{

            if(!response.equals("") ){
                AlertDialog.Builder builder = new AlertDialog.Builder(CashCheckActivity.this);
                builder.setMessage("¡Pedido enviado con exito!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                mDbHelper.deletePedido();
                Intent intent = new Intent(CashCheckActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(CashCheckActivity.this);
                builder.setMessage("Lo sentimos el pedido no se pudo efectuar, su pedido se cancelara automaticamente")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mDbHelper.deletePedido();
                                        Intent intent = new Intent(CashCheckActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        }
        catch (NullPointerException e){
            AlertDialog.Builder builder = new AlertDialog.Builder(CashCheckActivity.this);
            builder.setMessage("Sin servicio")
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
}
