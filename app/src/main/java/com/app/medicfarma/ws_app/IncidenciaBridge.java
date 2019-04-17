package com.app.medicfarma.ws_app;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import com.app.medicfarma.helpers.DbHelper;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class IncidenciaBridge extends AsyncTask<String, Void, String> {

    private Exception exception;
    DbHelper mDbHelper;
    ProgressBar progressBar;

    public AsyncResponse delegate = null;

    public interface AsyncResponse {
        void processFinish(String response);
    }

    public IncidenciaBridge(DbHelper mDbHelper, ProgressBar progressBar, AsyncResponse delegate){
        this.mDbHelper = mDbHelper;
        this.progressBar = progressBar;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... parametros) {
        try{
            String idPedido = parametros[0];
            String incidencia = parametros[1];
            String telefono = parametros[2];


            String requestBody;
            Uri.Builder builder = new Uri.Builder();
            builder.appendQueryParameter("idPedido",idPedido);
            builder.appendQueryParameter("incidencia",incidencia);
            builder.appendQueryParameter("telefono",telefono);
            requestBody = builder.build().getEncodedQuery();

            URL url = new URL(WSRoutes.baseURL +""+ WSRoutes.makeIncidencia);

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