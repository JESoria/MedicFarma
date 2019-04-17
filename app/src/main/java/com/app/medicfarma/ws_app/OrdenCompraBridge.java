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

    public OrdenCompraBridge.AsyncResponse delegate = null;

    public interface AsyncResponse {
        void processFinish(String response);
    }

    public OrdenCompraBridge(DbHelper mDbHelper, OrdenCompraBridge.AsyncResponse delegate){
        this.mDbHelper = mDbHelper;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(OrdenCompra ...ordenCompra) {
        try{
            Gson gson = new Gson();
            String jsonString = gson.toJson(ordenCompra[0]);

            String requestBody;
            Uri.Builder builder = new Uri.Builder();
            builder.appendQueryParameter("data",jsonString );
            requestBody = builder.build().getEncodedQuery();

            URL url = new URL(WSRoutes.baseURL +""+ WSRoutes.makeOrder);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            urlConnection.setRequestProperty("Authorization",mDbHelper.getAuthToken());

            OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));

            writer.write(requestBody);
            writer.flush();
            writer.close();
            outputStream.close();

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
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
            delegate.processFinish(response);

        } catch (Exception e) {
            e.printStackTrace();
            delegate.processFinish(response);
        }

    }
}
