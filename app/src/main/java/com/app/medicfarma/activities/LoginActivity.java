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
import android.widget.ProgressBar;

import com.app.medicfarma.R;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.TokenModel;
import com.app.medicfarma.models.UsuarioModel;
import com.app.medicfarma.ws_app.Login;
import com.app.medicfarma.ws_app.Token;

public class LoginActivity extends AppCompatActivity implements Login.AsyncResponse {

    AlertDialog.Builder builder;
    Button iniciar, cancelar;
    ProgressBar progressBar;
    EditText user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniciar = (Button) findViewById(R.id.btnIniciarLogin);
        cancelar = (Button) findViewById(R.id.btnCancelarLogin);
        user = (EditText) findViewById(R.id.edtUserLogin);
        pass = (EditText) findViewById(R.id.edtPassLogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_login);

        progressBar.setVisibility(View.VISIBLE);
        final DbHelper mDbHelper = new DbHelper(this);
        progressBar.setVisibility(View.INVISIBLE);

        if(mDbHelper.getAuthToken().equals("")){
            new Token(mDbHelper,progressBar).execute();

        }else{
            progressBar.setVisibility(View.INVISIBLE);
        }

        //End get token

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithEmail(mDbHelper);
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent welcome = new Intent(LoginActivity.this,StartActivity.class);
                startActivity(welcome);
                finish();
            }
        });
    }



    @Override
    public void processFinish(String response) {
        try{
            progressBar.setVisibility(View.INVISIBLE);

            if(!response.equals("")){

                Intent welcome = new Intent(LoginActivity.this,StartActivity.class);
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
            iniciar.setEnabled(true);
            iniciar.setError("¡Debe ingresar su usuario!");
            iniciar.requestFocus();
        }
        else if(TextUtils.isEmpty(model.getPassword())){
            pass.setEnabled(true);
            pass.setError("¡Debe ingresar su contraseña!");
            pass.requestFocus();
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            new Login(mDbHelper,progressBar,this).execute(model.getCorreo(), model.getPassword());
        }
    }
}
