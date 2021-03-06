package com.app.medicfarma.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.app.medicfarma.R;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.UsuarioModel;
import com.app.medicfarma.ws_app.LoginBridge;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class LoginActivity extends AppCompatActivity implements LoginBridge.AsyncResponse {

    AlertDialog.Builder builder;
    Button iniciar, cancelar;
    ProgressBar progressBar;
    EditText user, pass;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniciar = (Button) findViewById(R.id.btnIniciarLogin);
        cancelar = (Button) findViewById(R.id.btnCancelarLogin);
        user = (EditText) findViewById(R.id.edtUserLogin);
        pass = (EditText) findViewById(R.id.edtPassLogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_login);
        linearLayout = (LinearLayout) findViewById(R.id.pantallaLogin);
        //Section Animations
        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(iniciar);

        YoYo.with(Techniques.FadeIn)
                .duration(900)
                .repeat(0)
                .playOn(cancelar);

        YoYo.with(Techniques.FadeIn)
                .duration(900)
                .repeat(0)
                .playOn(user);

        YoYo.with(Techniques.FadeIn)
                .duration(900)
                .repeat(0)
                .playOn(pass);
        //End Section Animations

        //Para efectos de pruebas
        //user.setText("ing_soria@hotmail.com");
        //pass.setText("123");

        final DbHelper mDbHelper = new DbHelper(this);

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithEmail(mDbHelper);
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void processFinish(String response) {
        try{
            progressBar.setVisibility(View.INVISIBLE);

            if(!response.equals("")){
                //Aqui la llamada al menu de la aplicación
                YoYo.with(Techniques.ZoomOutUp)
                        .duration(500)
                        .repeat(0)
                        .playOn(linearLayout);
                Intent welcome = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(welcome);
                finish();
            }
            else {
                iniciar.setEnabled(true);
                builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("¡Sus credenciales son incorrectas!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();

                pass.setText("");
                pass.requestFocus();
            }

        }
        catch (NullPointerException e){
            iniciar.setEnabled(true);
            builder = new AlertDialog.Builder(LoginActivity.this);
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

    private void loginWithEmail(DbHelper mDbHelper){

        UsuarioModel model = new UsuarioModel();

        model.setCorreo(user.getText().toString());
        model.setPassword(pass.getText().toString());

        if(TextUtils.isEmpty(model.getCorreo())){
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(1)
                    .playOn(user);
            user.setEnabled(true);
            user.setError("¡Debe ingresar su usuario!");
            user.requestFocus();
        }
        else if(TextUtils.isEmpty(model.getPassword())){
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(1)
                    .playOn(pass);
            pass.setEnabled(true);
            pass.setError("¡Debe ingresar su contraseña!");
            pass.requestFocus();
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            new LoginBridge(mDbHelper,progressBar,this).execute(model.getCorreo(), model.getPassword());
        }
    }



}
