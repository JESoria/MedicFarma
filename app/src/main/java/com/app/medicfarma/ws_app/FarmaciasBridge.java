package com.app.medicfarma.ws_app;

import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.Pharmacy;

import org.json.JSONArray;
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
import java.util.ArrayList;

public class FarmaciasBridge extends AsyncTask<Void, Void, String> {

    private Exception exception;
    DbHelper mDbHelper;
    ProgressBar progressBar;

    public AsyncResponse delegate = null;

    public interface AsyncResponse {
        void processFinish(String response, ArrayList farmacias);
    }

    public FarmaciasBridge(DbHelper mDbHelper, AsyncResponse delegate){
        this.mDbHelper = mDbHelper;
        this.progressBar = progressBar;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try{

            String requestBody;
            requestBody = "";

            URL url = new URL(WSRoutes.baseURL +""+ WSRoutes.makeDrugstoresList);

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

        ArrayList<Pharmacy> farmaciasList = new ArrayList<Pharmacy>();
        Pharmacy pharmacy = null;
        JSONObject object = null;

        try{

            JSONArray jArray = new JSONArray(response);

            for(int i=0;i<jArray.length();i++){

                object = jArray.getJSONObject(i);

                pharmacy = new Pharmacy();

                pharmacy.setIdFarmacia(Integer.parseInt(object.getString("idFarmacia")));
                pharmacy.setNombreFarmacia(object.getString("nombreFarmacia"));

                farmaciasList.add(pharmacy);
            }

            delegate.processFinish(response,farmaciasList);
        }
        catch(JSONException e){
            System.err.println(e);
            delegate.processFinish(response,farmaciasList);
        }

    }

}
