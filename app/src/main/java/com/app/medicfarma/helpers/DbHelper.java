package com.app.medicfarma.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.medicfarma.models.DetallePedido;
import com.app.medicfarma.models.Pedido;
import com.app.medicfarma.models.TokenModel;
import com.app.medicfarma.models.UsuarioModel;


public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "medicfarma.db";

    public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(InternalControlDB.SQL_Token.CREATE_TABLE_TOKEN);
        db.execSQL(InternalControlDB.SQL_Usuario.CREATE_TABLE_USUARIO);
        db.execSQL(InternalControlDB.SQL_Pedido.CREATE_TABLE_PEDIDO);
        db.execSQL(InternalControlDB.SQL_DetallePedido.CREATE_TABLE_DETALLE_PEDIDO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(InternalControlDB.SQL_Token.DELETE_TABLE_TOKEN);
        db.execSQL(InternalControlDB.SQL_Usuario.DELETE_USUARIOS);
        db.execSQL(InternalControlDB.SQL_Pedido.DELETE_PEDIDO);
        db.execSQL(InternalControlDB.SQL_DetallePedido.DELETE_DETALLE_PEDIDO);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //---------------------------------CRUD PEDIDO---------------------------------------

    public long insertPedido(Pedido Pedido) {

        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();

        values.put(InternalControlDB.TablaPedido.COLUMN_NAME_CODIGO_PEDIDO, Pedido.getCodigoPedido());
        values.put(InternalControlDB.TablaPedido.COLUMN_NAME_ID_USUARIO, Pedido.getIdusuario());
        values.put(InternalControlDB.TablaPedido.COLUMN_NAME_ID_SUCURSAL, Pedido.getIdsucursal());
        values.put(InternalControlDB.TablaPedido.COLUMN_NAME_DIRECCION, Pedido.getDireccion());
        values.put(InternalControlDB.TablaPedido.COLUMN_NAME_TELEFONO, Pedido.getTelefono());
        values.put(InternalControlDB.TablaPedido.COLUMN_NAME_MONTO_COMPRA, Pedido.getMontoCompra());
        values.put(InternalControlDB.TablaPedido.COLUMN_NAME_ESTADO_PAGO, Pedido.getEstadoPago());


        long rowID = db.insert(InternalControlDB.TablaPedido.TABLE_NAME_PEDIDO, null,values);

        db.close();

        return rowID;
    }

    public long insertDetallePedido(DetallePedido detallePedido) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(InternalControlDB.TablaDetallePedido.COLUMN_NAME_ID_PRODUCTO, detallePedido.getIdSucursalProducto());
        values.put(InternalControlDB.TablaDetallePedido.COLUMN_NAME_ID_FARMACIA,detallePedido.getIdFarmacia());
        values.put(InternalControlDB.TablaDetallePedido.COLUMN_NAME_CANTIDAD, detallePedido.getCantidad());
        values.put(InternalControlDB.TablaDetallePedido.COLUMN_NAME_PRODUCTO,detallePedido.getProducto());
        values.put(InternalControlDB.TablaDetallePedido.COLUMN_NAME_PRECIO,detallePedido.getPrecio());
        long rowID = db.insert(InternalControlDB.TablaDetallePedido.TABLE_NAME_DETALLE_PEDIDO, null,values);
        db.close();
        return rowID;
    }

    public void actualizarDetallePedido(DetallePedido detallePedido) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(InternalControlDB.TablaDetallePedido.COLUMN_NAME_ID_PRODUCTO, detallePedido.getIdSucursalProducto());
        values.put(InternalControlDB.TablaDetallePedido.COLUMN_NAME_ID_FARMACIA,detallePedido.getIdFarmacia());
        values.put(InternalControlDB.TablaDetallePedido.COLUMN_NAME_CANTIDAD, detallePedido.getCantidad());
        values.put(InternalControlDB.TablaDetallePedido.COLUMN_NAME_PRODUCTO,detallePedido.getProducto());
        values.put(InternalControlDB.TablaDetallePedido.COLUMN_NAME_PRECIO,detallePedido.getPrecio());
        String where = InternalControlDB.TablaDetallePedido.COLUMN_NAME_ID_PRODUCTO + "= ?";
        db.update(InternalControlDB.TablaDetallePedido.TABLE_NAME_DETALLE_PEDIDO, values, where, new String[]{String.valueOf(detallePedido.getIdSucursalProducto())});
        db.close();
    }

    public void deletePedido(){
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL(InternalControlDB.SQL_Pedido.DELETE_PEDIDO);
        db.execSQL(InternalControlDB.SQL_DetallePedido.DELETE_DETALLE_PEDIDO);

        db.execSQL(InternalControlDB.SQL_Pedido.CREATE_TABLE_PEDIDO);
        db.execSQL(InternalControlDB.SQL_DetallePedido.CREATE_TABLE_DETALLE_PEDIDO);

        db.close();
    }

    public void deleteDetallePedido(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String where = InternalControlDB.TablaDetallePedido.COLUMN_NAME_ID_PRODUCTO + "= ?";
        db.delete(InternalControlDB.TablaDetallePedido.TABLE_NAME_DETALLE_PEDIDO,where ,new String[]{String.valueOf(id)});
        db.close();
    }

    //---------------------------------FIN CRUD PEDIDO-----------------------------------------------


    public UsuarioModel cargarUsuario() {

        int idUsuario;
        String nombres;
        String apellidos;
        String genero;
        String fechaNacimiento;
        String correo;
        String facebookId;
        String Birthday;

        SQLiteDatabase db = this.getReadableDatabase();
        //definiendo el row
        String[] projection = {
                InternalControlDB.TablaUsuario.COLUMN_NAME_ID_USUARIO,
                InternalControlDB.TablaUsuario.COLUMN_NAME_NOMBRES,
                InternalControlDB.TablaUsuario.COLUMN_NAME_APELLIDOS,
                InternalControlDB.TablaUsuario.COLUMN_NAME_GENERO,
                InternalControlDB.TablaUsuario.COLUMN_NAME_FECHA_NACIMIENTO,
                InternalControlDB.TablaUsuario.COLUMN_NAME_CORREO,
                InternalControlDB.TablaUsuario.COLUMN_NAME_FACEBOOK_ID,
                InternalControlDB.TablaUsuario.COLUMN_NAME_FECHA_NACIMIENTO
        };
        String selection = null;
        String[] selectionArgs = null;
        Cursor c = db.query(
                InternalControlDB.TablaUsuario.TABLE_NAME_USUARIO,// The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // sort type
        );
        if(c.getCount()>0){
            c.moveToFirst();

            idUsuario = c.getInt(c.getColumnIndexOrThrow(InternalControlDB.TablaUsuario.COLUMN_NAME_ID_USUARIO));
            nombres = c.getString(c.getColumnIndexOrThrow(InternalControlDB.TablaUsuario.COLUMN_NAME_NOMBRES));
            apellidos = c.getString(c.getColumnIndexOrThrow(InternalControlDB.TablaUsuario.COLUMN_NAME_APELLIDOS));
            genero = c.getString(c.getColumnIndexOrThrow(InternalControlDB.TablaUsuario.COLUMN_NAME_GENERO));
            fechaNacimiento = c.getString(c.getColumnIndexOrThrow(InternalControlDB.TablaUsuario.COLUMN_NAME_FECHA_NACIMIENTO));
            correo = c.getString(c.getColumnIndexOrThrow(InternalControlDB.TablaUsuario.COLUMN_NAME_CORREO));
            facebookId = c.getString(c.getColumnIndexOrThrow(InternalControlDB.TablaUsuario.COLUMN_NAME_FACEBOOK_ID));
            Birthday = c.getString(c.getColumnIndexOrThrow(InternalControlDB.TablaUsuario.COLUMN_NAME_FECHA_NACIMIENTO));


            return new UsuarioModel(idUsuario, "n/a", nombres, apellidos, genero, fechaNacimiento, correo, facebookId, true, "n/a", Birthday);
        }else{
            return null;
        }
    }

    public void deleteUsuario(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(InternalControlDB.TablaUsuario.TABLE_NAME_USUARIO,null,null);
    }

    public  Cursor listadoProductos(){

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                InternalControlDB.TablaDetallePedido.COLUMN_NAME_ID_PRODUCTO,
                InternalControlDB.TablaDetallePedido.COLUMN_NAME_ID_FARMACIA,
                InternalControlDB.TablaDetallePedido.COLUMN_NAME_CANTIDAD,
                InternalControlDB.TablaDetallePedido.COLUMN_NAME_PRODUCTO,
                InternalControlDB.TablaDetallePedido.COLUMN_NAME_PRECIO
        };
        String selection = null;
        String[] selectionArgs = null;
        Cursor c = db.query(
                InternalControlDB.TablaDetallePedido.TABLE_NAME_DETALLE_PEDIDO,// The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // sort type
        );
        if(c.getCount()>0){
            c.moveToFirst();
            //c.moveToNext();
            return  c;
        }else{
            return null;
        }

    }


    public boolean estadoOrden(){
        SQLiteDatabase db = this.getReadableDatabase();
        //definiendo el row
        String[] projection = {
                InternalControlDB.TablaDetallePedido._ID,
                InternalControlDB.TablaDetallePedido.COLUMN_NAME_ID_PRODUCTO,
                InternalControlDB.TablaDetallePedido.COLUMN_NAME_ID_FARMACIA,
                InternalControlDB.TablaDetallePedido.COLUMN_NAME_CANTIDAD,
                InternalControlDB.TablaDetallePedido.COLUMN_NAME_PRODUCTO,
                InternalControlDB.TablaDetallePedido.COLUMN_NAME_PRECIO
        };
        String selection = null;
        String[] selectionArgs = null;
        Cursor c = db.query(
                InternalControlDB.TablaDetallePedido.TABLE_NAME_DETALLE_PEDIDO,    // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // sort type
        );
        if(c.getCount()>0){
            c.moveToFirst();

            return true;
        }else{
            return false;
        }

    }



    //Method to get token
    public String getAuthToken(){
        SQLiteDatabase db = this.getReadableDatabase();
        //definiendo el row
        String[] projection = {
                InternalControlDB.TablaToken._ID,
                InternalControlDB.TablaToken.COLUMN_NAME_ACCESS_TOKEN,
                InternalControlDB.TablaToken.COLUMN_NAME_TOKEN_TYPE,
                InternalControlDB.TablaToken.COLUMN_NAME_EXPIRES_IN,
                InternalControlDB.TablaToken.COLUMN_NAME_REFRESH_TOKEN
        };
        String selection = null;
        String[] selectionArgs = null;
        Cursor c = db.query(
                InternalControlDB.TablaToken.TABLE_NAME_TOKEN,    // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // sort type
        );
        if(c.getCount()>0){
            c.moveToFirst();

            String oAuth =
                    c.getString(c.getColumnIndexOrThrow(InternalControlDB.TablaToken.COLUMN_NAME_TOKEN_TYPE))+" "+
                    c.getString(c.getColumnIndexOrThrow(InternalControlDB.TablaToken.COLUMN_NAME_ACCESS_TOKEN));

            return  oAuth;
        }else{
            return "";
        }

    }

    public TokenModel tokenModel(){

        String refresh_token;

        SQLiteDatabase db = this.getReadableDatabase();
        //definiendo el row
        String[] projection = {
                InternalControlDB.TablaToken._ID,
                InternalControlDB.TablaToken.COLUMN_NAME_ACCESS_TOKEN,
                InternalControlDB.TablaToken.COLUMN_NAME_TOKEN_TYPE,
                InternalControlDB.TablaToken.COLUMN_NAME_REFRESH_TOKEN,
                InternalControlDB.TablaToken.COLUMN_NAME_EXPIRES_IN
        };
        String selection = null;
        String[] selectionArgs = null;
        Cursor c = db.query(
                InternalControlDB.TablaToken.TABLE_NAME_TOKEN,    // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // sort type
        );
        if(c.getCount()>0){
            c.moveToFirst();
            refresh_token = c.getString(c.getColumnIndexOrThrow(InternalControlDB.TablaToken.COLUMN_NAME_REFRESH_TOKEN));
            return  new TokenModel(refresh_token);
        }else{
            return null;
        }

    }



}
