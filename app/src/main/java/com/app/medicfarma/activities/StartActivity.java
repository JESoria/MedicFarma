package com.app.medicfarma.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.app.medicfarma.R;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class StartActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton loginButton;
    ProgressDialog mDialog;

    //Interfaz callbackManager.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_star);

        //Administrador de devoluciones de llamada que gestione las respuestas de inicio de sesión.
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email","user_gender"));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                mDialog=new ProgressDialog(StartActivity.this);
                mDialog.setMessage("Obteniendo datos.......");
                mDialog.show();

                String accesstoken = loginResult.getAccessToken().getToken();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        mDialog.dismiss();
                        getData(object);

                    }
                });

                //request graph  api
                Bundle parameters =new Bundle();
                parameters.putString("fields","id,first_name,last_name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(StartActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        if (AccessToken.getCurrentAccessToken() != null) {
            //AccessToken.getCurrentAccessToken().getUserId();
        }
    }

    private void getData(JSONObject object){
        try {

            System.out.print(
                    "Datos de Facebook : \n "+
                            object.getString("id")+"\n"+
                            object.getString("first_name")+"\n"+
                            object.getString("last_name")+"\n"+
                            object.getString("email")+"\n"+
                            object.getString("gender")+"\n"+
                            object.getString("birthday")+"\n"+"holis");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

/*
    private void printKeyHash() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.app.medicfarma", PackageManager.GET_SIGNATURES);

            for (Signature signature:info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("AQUI KeyHash:", Base64.encodeToString(md.digest(),Base64.DEFAULT));
                System.out.print("AQUI KeyHash:" + Base64.encodeToString(md.digest(),Base64.DEFAULT));

            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
*/

}
