package com.app.medicfarma.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.app.medicfarma.R;
import java.util.Arrays;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.UsuarioModel;
import com.app.medicfarma.ws_app.RegisterUserBridge;
import com.app.medicfarma.ws_app.TokenBridge;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONException;
import org.json.JSONObject;

public class StartActivity extends AppCompatActivity implements RegisterUserBridge.AsyncResponse {

    CallbackManager callbackManager;
    LoginButton loginButton;
    ProgressDialog mDialog;
    Button iniciarSesion, crearCuenta, botonfacebook;
    ProgressBar progressBar;
    AlertDialog.Builder builder;
    Animation downtoup,uptodown;
    ImageView LogodeLogin;
    TextView tvStartactivity;
    boolean userLogeado;

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

        uptodown= AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup=AnimationUtils.loadAnimation(this,R.anim.downtoup);

        iniciarSesion = (Button) findViewById(R.id.btnIniciarSesion);
        crearCuenta = (Button) findViewById(R.id.btnCrearCuenta);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_start);
        LogodeLogin = (ImageView) findViewById(R.id.logodelogin) ;
        tvStartactivity = (TextView) findViewById(R.id.tvStartactivity);
        botonfacebook = (Button) findViewById(R.id.login_button);

        LogodeLogin.setAnimation(uptodown);
        tvStartactivity.setAnimation(downtoup);
        iniciarSesion.setAnimation(downtoup);
        crearCuenta.setAnimation(downtoup);
        botonfacebook.setAnimation(uptodown);
        //Get token if it's not
        final DbHelper mDbHelper = new DbHelper(this);

        progressBar.setVisibility(View.VISIBLE);

        //El token se debe pedir en la primera pantalla
        if(mDbHelper.getAuthToken().equals("")){
            new TokenBridge(mDbHelper,progressBar).execute();

        }else{
            progressBar.setVisibility(View.INVISIBLE);
        }
        //End get token

        userLogeado = mDbHelper.usuarioLogeado();

        if (userLogeado){
            Intent intent = new Intent(StartActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }

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
            Intent intent = new Intent(StartActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }

        //End LoginBridge with Facebook

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
                Intent intent = new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
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
            model.setFechaNacimiento(formatDate(object.getString("birthday")));
            model.setFacebookId(object.getString("id"));
            model.setEstado("1");
            model.setPassword("123");

            progressBar.setVisibility(View.VISIBLE);

            new RegisterUserBridge(mDbHelper,progressBar,this).execute(
                    model.getNombres(),
                    model.getApellidos(),
                    model.getGenero(),
                    model.getFechaNacimiento(),
                    model.getCorreo(),
                    model.getFacebookId(),
                    model.getPassword(),
                    String.valueOf(model.getEstado())
            );

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

    public static String formatDate(String birthday){

        String fecha  = "";

        String[] fechArray = birthday.split("/");

        fecha = fechArray[2]+"-"+fechArray[0]+"-"+fechArray[1];

        return fecha;
    }

    @Override
    public void processFinish(String response) {
        try{
            progressBar.setVisibility(View.INVISIBLE);

            if(!response.equals("")){
                //Aqui sea hace la llamada al menu de la aplicación
                Intent welcome = new Intent(StartActivity.this,HomeActivity.class);
                startActivity(welcome);
                finish();
            }
            else {
                LoginManager.getInstance().logOut();
                builder = new AlertDialog.Builder(StartActivity.this);
                builder.setMessage("¡Ups ha ocurrido un error!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        }
        catch (NullPointerException e){
            LoginManager.getInstance().logOut();
            builder = new AlertDialog.Builder(StartActivity.this);
            builder.setMessage("Pongase en contacto con soporte tecnico")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

}
