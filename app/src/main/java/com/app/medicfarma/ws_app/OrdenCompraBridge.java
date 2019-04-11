package com.app.medicfarma.ws_app;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.helpers.InternalControlDB;
import com.app.medicfarma.models.OrdenCompra;
import com.app.medicfarma.models.UsuarioModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class OrdenCompraBridge extends AsyncTask<OrdenCompra, Void, String> {

    private Exception exception;
    DbHelper mDbHelper;
    ProgressBar progressBar;

    public LoginBridge.AsyncResponse delegate = null;

    public interface AsyncResponse {
        void processFinish(String response);
    }

    public OrdenCompraBridge(DbHelper mDbHelper, ProgressBar progressBar, LoginBridge.AsyncResponse delegate){
        this.mDbHelper = mDbHelper;
        this.progressBar = progressBar;
        this.delegate = delegate;
    }

    private void setPostRequestContent(HttpURLConnection conn,
                                       String jsonString) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonString);
        writer.flush();
        writer.close();
        os.close();
    }

    @Override
    protected String doInBackground(OrdenCompra ...ordenCompra) {
        try{

            Gson gson = new Gson();
            String jsonString = gson.toJson(ordenCompra[0]);

            String requestBody;
            Uri.Builder builder = new Uri.Builder();
            //builder.appendQueryParameter("correo",email);
            requestBody = builder.build().getEncodedQuery();

            URL url = new URL(WSRoutes.baseURL +""+ WSRoutes.makeLogin);//Cambiar

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            urlConnection.setRequestProperty("Authorization",mDbHelper.getAuthToken());

/*
            OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));

            writer.write(requestBody);
            writer.flush();
            writer.close();
            outputStream.close();

*/
            //Se debe crear dos clases, pedido y detalle pedido en el ws

            setPostRequestContent(urlConnection, jsonString);
            urlConnection.connect();

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();

                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }catch(Exception e){
            System.err.println(e);
            return "";
        }

    }


    protected void onPostExecute(String response) {

        try {
            JSONObject object = (JSONObject) new JSONTokener(response).nextValue();

            UsuarioModel model = new UsuarioModel();

            model.setIdUsuario(Integer.parseInt(object.getString("iD_USUARIO")));
            model.setNombres(object.getString("nombres"));
            model.setApellidos(object.getString("apellidos"));
            model.setGenero(object.getString("genero"));
            model.setFechaNacimiento(object.getString("fechA_NACIMIENTO"));
            model.setCorreo(object.getString("correo"));
            model.setFacebookId(object.getString("facebooK_ID"));

            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(InternalControlDB.TablaUsuario.COLUMN_NAME_ID_USUARIO, model.getIdUsuario());
            values.put(InternalControlDB.TablaUsuario.COLUMN_NAME_NOMBRES, model.getNombres());
            values.put(InternalControlDB.TablaUsuario.COLUMN_NAME_APELLIDOS, model.getApellidos());
            values.put(InternalControlDB.TablaUsuario.COLUMN_NAME_GENERO, model.getGenero());
            values.put(InternalControlDB.TablaUsuario.COLUMN_NAME_FECHA_NACIMIENTO, model.getFechaNacimiento());
            values.put(InternalControlDB.TablaUsuario.COLUMN_NAME_CORREO, model.getCorreo());
            values.put(InternalControlDB.TablaUsuario.COLUMN_NAME_FACEBOOK_ID, model.getFacebookId());


            long newRowId = db.insert(InternalControlDB.TablaUsuario.TABLE_NAME_USUARIO, null, values);
            db.close();
            delegate.processFinish(response);

        } catch (JSONException e) {
            e.printStackTrace();
            delegate.processFinish(response);
        }

    }
}
