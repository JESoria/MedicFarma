package com.app.medicfarma.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.app.medicfarma.R;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.DetallePedido;
import com.app.medicfarma.models.OrdenCompra;
import com.app.medicfarma.models.Pedido;
import com.app.medicfarma.models.UsuarioModel;
import com.app.medicfarma.ws_app.OrdenCompraBridge;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class PayPalCheckActivity extends AppCompatActivity implements OrdenCompraBridge.AsyncResponse {

    AlertDialog.Builder builder;
    String direccion, telefono, tipopago;
    double montoCompra,montoCompraPayPal;
    int idSucursal;
    final DbHelper mDbHelper = new DbHelper(PayPalCheckActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_ok);

        Intent intent = getIntent();

            Bundle datos = getIntent().getExtras();
            if (datos != null) {
                direccion = datos.getString("direccion");
                telefono = datos.getString("telefono");
                montoCompra = datos.getDouble("montoCompra");
                idSucursal = datos.getInt("idSucursal");
                montoCompraPayPal = datos.getDouble("montoCompraPayPal");
                tipopago = datos.getString("tipopago");
            }

            UsuarioModel usuarioModel;
            usuarioModel = mDbHelper.cargarUsuario();

            try {
                //Generando codigo de pedido
                int cod1 = 0;
                int cod2 = 0;
                int idFarmacia = 0;
                int idSucursalProducto = 0;
                cod1 = (int) (Math.random() * 1000) + 1;
                cod2 = (int) (Math.random() * 1000) + 1;
                //Fin de codigo de pedido

                JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));


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
                pedido.setIdusuario(usuarioModel.getIdUsuario());
                pedido.setIdsucursal(idSucursalProducto);
                pedido.setDireccion(direccion);
                pedido.setTelefono(telefono);
                pedido.setCorreo(usuarioModel.getCorreo());
                pedido.setNombres(usuarioModel.getNombres());
                pedido.setApellidos(usuarioModel.getApellidos());

                if(tipopago.equals("efectivo")){
                    pedido.setMontoCompra(montoCompra);
                    pedido.setCodigoPedido("OC" + idFarmacia + idSucursalProducto + cod1 + cod2);
                }
                else{
                    //Limpiar el ultimo
                    pedido.setCodigoPedido(jsonObject.getJSONObject("response").getString("id").substring(1,jsonObject.getJSONObject("response").getString("id").length()-1));
                    pedido.setMontoCompra(montoCompraPayPal);
                }
                pedido.setEstadoPago(jsonObject.getJSONObject("response").getString("state"));
                OrdenCompra ordenCompra = new OrdenCompra();
                ordenCompra.setPedidos(pedido);
                ordenCompra.setDetallePedido(detallePedido);

                procesarPedido(jsonObject.getJSONObject("response"), mDbHelper, ordenCompra);

            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    private void procesarPedido(JSONObject response, final DbHelper  mDbHelper, OrdenCompra ordenCompra) {

        try {
            if (response.getString("state").equals("approved")){
                new OrdenCompraBridge(mDbHelper,PayPalCheckActivity.this).execute(ordenCompra);
            }else{
                builder = new AlertDialog.Builder(PayPalCheckActivity.this);
                builder.setMessage("Ups! ocurrio un problema con tu pago, se cancelara la orden, ponte en contacto con PayPal")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mDbHelper.deletePedido();
                                        Intent intent = new Intent(PayPalCheckActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void processFinish(String response) {
        try{

            if(!response.equals("") ){
                AlertDialog.Builder builder = new AlertDialog.Builder(PayPalCheckActivity.this);
                builder.setMessage("¡Pago efectuado con exito!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mDbHelper.deletePedido();
                                        Intent intent = new Intent(PayPalCheckActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();

            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(PayPalCheckActivity.this);
                builder.setMessage("Lo sentimos el pedido no se pudo efectuar, su pedido se cancelara automaticamente")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mDbHelper.deletePedido();
                                        Intent intent = new Intent(PayPalCheckActivity.this, HomeActivity.class);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(PayPalCheckActivity.this);
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
