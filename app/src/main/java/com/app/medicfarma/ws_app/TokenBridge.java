package com.app.medicfarma.ws_app;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.helpers.InternalControlDB;
import com.app.medicfarma.models.TokenModel;
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
import java.util.UUID;

/**
 * Created by Soria on 03/01/2019.
 */

public class TokenBridge extends AsyncTask<Void, Void, String> {

    private Exception exception;
    DbHelper dbHelper;
    ProgressBar progressBar;

    public TokenBridge(DbHelper mDbHelper, ProgressBar progressBar){
        this.dbHelper = mDbHelper;
        this.progressBar = progressBar;
    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(Void... voids) {
        try {

            TokenModel modelo = new TokenModel();

            modelo.setOuth_name(UUID.randomUUID().toString());

            String requestBody;
            Uri.Builder builder = new Uri.Builder();

            modelo.setSender_id("wakanda");
            modelo.setOuth_name("token");

            builder.appendQueryParameter("SENDER_ID", modelo.getSender_id());

            builder.appendQueryParameter("OUTH_NAME", modelo.getOuth_name());

            builder.appendQueryParameter("GRANT_TYPE","password");

            requestBody = builder.build().getEncodedQuery();

            URL url = new URL(WSRoutes.baseURL + ""+ WSRoutes.getToken);


            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
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
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String response) {
        try {
            //Get WS´Data
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Creando objeto JSON from Response").append("\n");
            JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
            String access_token = object.getString("access_token");
            String token_type = object.getString("token_type");
            String expires_in = object.getString("expires_in");
            String refresh_token = object.getString("refresh_token");

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(InternalControlDB.TablaToken.COLUMN_NAME_ACCESS_TOKEN, access_token);
            values.put(InternalControlDB.TablaToken.COLUMN_NAME_TOKEN_TYPE, token_type);
            values.put(InternalControlDB.TablaToken.COLUMN_NAME_REFRESH_TOKEN, refresh_token);
            values.put(InternalControlDB.TablaToken.COLUMN_NAME_EXPIRES_IN, expires_in);

            long newRowId = db.insert(InternalControlDB.TablaToken.TABLE_NAME_TOKEN, null, values);
            db.close();
            progressBar.setVisibility(View.INVISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
