package com.app.medicfarma.ws_app;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.Incidencias;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Soria on 04/01/2018.
 */

public class ListIncidenciasBridge extends AsyncTask<String, Void, String> {

    private Exception exception;
    DbHelper mDbHelper;
    ProgressBar progressBar;

    public AsyncResponse delegate = null;

    public interface AsyncResponse {
        void processFinish(String response, ArrayList<Incidencias> incidencias);
    }

    public ListIncidenciasBridge(DbHelper mDbHelper, ProgressBar progressBar, AsyncResponse delegate){
        this.mDbHelper = mDbHelper;
        this.progressBar = progressBar;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... parametros) {
        try{
            String idUsuario = parametros[0];

            String requestBody;
            Uri.Builder builder = new Uri.Builder();
            builder.appendQueryParameter("ID_USUARIO",idUsuario);
            requestBody = builder.build().getEncodedQuery();

            URL url = new URL(WSRoutes.baseURL +""+ WSRoutes.makePedidos);

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

        ArrayList<Incidencias> incidencias = new ArrayList<Incidencias>();
        Incidencias incidencia;
        JSONObject object = null;

        try {
            JSONArray jArray = new JSONArray(response);

            for (int i = 0; i < jArray.length(); i++) {
                object = jArray.getJSONObject(i);
                incidencia = new Incidencias();
                incidencia.setIdPedido(Integer.parseInt(object.getString("idPedido")));
                incidencia.setCodidoPedido(object.getString("codigoPedido"));
                incidencia.setDireccion(object.getString("direccion"));
                incidencia.setLatitud(object.getString("latitud"));
                incidencia.setLongitud(object.getString("longitud"));
                incidencia.setSucursal(object.getString("sucursal"));
                incidencia.setTelefono(object.getString("telefono"));
                incidencia.setMontoCompra(Double.parseDouble(object.getString("montoCompra")));
                incidencias.add(incidencia);
            }

            delegate.processFinish(response, incidencias);
        } catch (JSONException e) {
            System.err.println(e);
            delegate.processFinish(response, incidencias);
        }
    }
}