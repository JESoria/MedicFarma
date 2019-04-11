package com.app.medicfarma.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.medicfarma.models.DetallePedido;
import com.app.medicfarma.models.Pedido;
import com.app.medicfarma.models.TokenModel;


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
        System.out.println(InternalControlDB.SQL_Token.DELETE_TABLE_TOKEN);
        System.out.println(InternalControlDB.SQL_Usuario.DELETE_USUARIOS);
        System.out.println(InternalControlDB.SQL_Pedido.DELETE_PEDIDO);
        System.out.println(InternalControlDB.SQL_DetallePedido.DELETE_DETALLE_PEDIDO);

        db.execSQL(InternalControlDB.SQL_Token.DELETE_TABLE_TOKEN);
        db.execSQL(InternalControlDB.SQL_Usuario.DELETE_USUARIOS);
        db.execSQL(InternalControlDB.SQL_Pedido.DELETE_PEDIDO);
        db.execSQL(InternalControlDB.SQL_DetallePedido.DELETE_DETALLE_PEDIDO);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //--------------------------------------------------------------------------------

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

        values.put(InternalControlDB.TablaDetallePedido.COLUMN_NAME_ID_PRODUCTO, detallePedido.getIdproducto());
        values.put(InternalControlDB.TablaDetallePedido.COLUMN_NAME_CANTIDAD, detallePedido.getCantidad());

        long rowID = db.insert(InternalControlDB.TablaDetallePedido.TABLE_NAME_DETALLE_PEDIDO, null,values);

        db.close();

        return rowID;
    }

    public void actualizarDetallePedido(DetallePedido detallePedido) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(InternalControlDB.TablaDetallePedido.COLUMN_NAME_ID_PRODUCTO, detallePedido.getIdproducto());
        values.put(InternalControlDB.TablaDetallePedido.COLUMN_NAME_CANTIDAD, detallePedido.getCantidad());
        String where = InternalControlDB.TablaDetallePedido.COLUMN_NAME_ID_PRODUCTO + "= ?";
        db.update(InternalControlDB.TablaDetallePedido.TABLE_NAME_DETALLE_PEDIDO, values, where, new String[]{String.valueOf(detallePedido.getIdproducto())});
        db.close();
    }

    public void deletePedido(){
        SQLiteDatabase db = this.getReadableDatabase();
        String where = InternalControlDB.TablaPedido._ID+ "= ?";
        db.delete(InternalControlDB.TablaPedido.TABLE_NAME_PEDIDO,"" ,new String[]{String.valueOf(1)});
        db.close();
    }

    public void deleteDetallePedido(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String where = InternalControlDB.TablaDetallePedido.COLUMN_NAME_ID_PRODUCTO + "= ?";
        db.delete(InternalControlDB.TablaDetallePedido.TABLE_NAME_DETALLE_PEDIDO,where ,new String[]{String.valueOf(id)});
        db.close();
    }

    //--------------------------------------------------------------------------------

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
