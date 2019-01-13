package com.app.medicfarma.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.app.medicfarma.R;
import java.util.Arrays;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.UsuarioModel;
import com.app.medicfarma.ws_app.Token;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONException;
import org.json.JSONObject;

public class StartActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton loginButton;
    ProgressDialog mDialog;
    Button iniciarSesion, crearCuenta;
    ProgressBar progressBar;

    //Interfaz callbackManager.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);

        iniciarSesion = (Button) findViewById(R.id.btnIniciarSesion);
        crearCuenta = (Button) findViewById(R.id.btnCrearCuenta);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_start);

        //Get token if it's not
        final DbHelper mDbHelper = new DbHelper(this);

        progressBar.setVisibility(View.VISIBLE);

        if(mDbHelper.getAuthToken().equals("")){
            new Token(mDbHelper,progressBar).execute();

        }else{
            progressBar.setVisibility(View.INVISIBLE);
        }

        //End get token

        //Administrador de devoluciones de llamada que gestione las respuestas de inicio de sesión.
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email","user_gender"));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                mDialog=new ProgressDialog(StartActivity.this);
                mDialog.setMessage("Iniciando sesión.......");
                mDialog.show();

                String accesstoken = loginResult.getAccessToken().getToken();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        mDialog.dismiss();
                        getData(object,mDbHelper);

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
                // cancel actions
            }

            @Override
            public void onError(FacebookException exception) {
                // Error actions
                Toast.makeText(StartActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        if (AccessToken.getCurrentAccessToken() != null) {
            AccessToken.getCurrentAccessToken().getUserId();
        }

        //End Login with Facebook

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        crearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void getData(JSONObject object, DbHelper mDbHelper){
        try {

            UsuarioModel model = new UsuarioModel();

            model.setCorreo(object.getString("email"));
            model.setPassword("");
            model.setNombres(object.getString("first_name"));
            model.setApellidos(object.getString("last_name"));
            model.setGenero(formatoGenero(object.getString("gender")));
            model.setFechaNacimiento(object.getString("birthday"));
            model.setFacebookId(Integer.parseInt(object.getString("id")));

            progressBar.setVisibility(View.VISIBLE);

            /*
            new Register(mDbHelper,progressBar,this).execute(
                    model.getCorreo(),
                    model.getPassword(),
                    model.getNombres(),
                    model.getApellidos(),
                    model.getGenero(),
                    model.getFechaNacimiento(),
                    model.getFacebookId()
            );
            */
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static String formatoGenero(String gender){

        if(gender.equals("male")){
            gender = "m";
        }
        else{
            gender = "f";
        }

        return gender;
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
