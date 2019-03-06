package com.app.medicfarma.ws_app;

import android.net.Uri;
import android.os.AsyncTask;

import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.Product;

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

public class ProductosPharmaciesBridge extends AsyncTask<String, Void, String> {

    private Exception exception;
    DbHelper mDbHelper;

    public AsyncResponse delegate = null;

    public interface AsyncResponse {
        void processFinish(String response, ArrayList productos);
    }

    public ProductosPharmaciesBridge(DbHelper mDbHelper, AsyncResponse delegate){
        this.mDbHelper = mDbHelper;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... parametros) {
        try{
            String producto = parametros [0];
            String latitud = parametros[1];
            String longitud = parametros [2];


            String requestBody;
            Uri.Builder builder = new Uri.Builder();
            builder.appendQueryParameter("producto",producto);
            builder.appendQueryParameter("latitud",latitud);
            builder.appendQueryParameter("longitud",longitud);

            requestBody = builder.build().getEncodedQuery();

            URL url = new URL(WSRoutes.baseURL + ""+ WSRoutes.makeProductsPharmacies);

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

        ArrayList<Product> productosList = new ArrayList<Product>();
        Product product = null;
        JSONObject object = null;

        try{

            JSONArray jArray = new JSONArray(response);

            for(int i=0;i<jArray.length();i++){

                object = jArray.getJSONObject(i);

                product = new Product();

                product.setProducto(object.getString("producto"));
                product.setPrecio(Double.parseDouble(object.getString("precio")));
                product.setIdSucursalProducto(Integer.parseInt(object.getString("idSucursalProducto")));
                product.setIdSucursal(Integer.parseInt(object.getString("idSucursal")));
                product.setSucursal(object.getString("sucursal"));
                product.setLatitud(object.getString("latitud"));
                product.setLongitud(object.getString("longitud"));
                product.setDireccion(object.getString("direccion"));

                productosList.add(product);
            }

            delegate.processFinish(response,productosList);
        }
        catch(JSONException e){
            System.err.println(e);
            delegate.processFinish(response,productosList);
        }

    }

}
