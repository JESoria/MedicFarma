package com.app.medicfarma.ws_app;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.ProductDetail;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Soria on 05/03/2019.
 */

public class ProductDetailBridge extends AsyncTask<String, Void, String> {

    private Exception exception;
    DbHelper mDbHelper;
    ProgressBar progressBar;

    public AsyncResponse delegate = null;

    public interface AsyncResponse {
        void processFinish(String response,ProductDetail productDetail);
    }

    public ProductDetailBridge(DbHelper mDbHelper, ProgressBar progressBar, AsyncResponse delegate){
        this.mDbHelper = mDbHelper;
        this.progressBar = progressBar;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... parametros) {
        try{
            String idSucursalProducto = parametros[0];
            String idFarmacia = parametros[1];

            String requestBody;
            Uri.Builder builder = new Uri.Builder();
            builder.appendQueryParameter("idSucursalProducto",idSucursalProducto);
            builder.appendQueryParameter("idFarmacia",idFarmacia);
            requestBody = builder.build().getEncodedQuery();

            URL url = new URL(WSRoutes.baseURL +""+ WSRoutes.makeProductDetail);

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

        ProductDetail productDetail = new ProductDetail();

        try {
            JSONObject object = (JSONObject) new JSONTokener(response).nextValue();

            productDetail.setProducto(object.getString("producto"));
            productDetail.setPresentacion(object.getString("presentacion"));
            productDetail.setFechavencimiento(object.getString("fechaVencimiento"));
            productDetail.setLaboratorio(object.getString("laboratorio"));
            productDetail.setPrincipalActivos(object.getString("principiosActivos"));
            productDetail.setCategoria(object.getString("categoria"));
            productDetail.setPrecio(Double.parseDouble(object.getString("precio")));
            productDetail.setExistencia(Integer.parseInt(object.getString("existencia")));

            delegate.processFinish(response,productDetail);

        } catch (JSONException e) {
            System.err.println(e);
            delegate.processFinish(response,productDetail);
        }

    }
}