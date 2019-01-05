package com.app.medicfarma.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.app.medicfarma.R;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.UsuarioModel;
import com.app.medicfarma.ws_app.Login;

public class LoginActivity extends AppCompatActivity implements Login.AsyncResponse {


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

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithEmail();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void loginWithEmail(){

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
            //actions if there are not empty fields
        }
    }

    @Override
    public void processFinish(String response) {

    }
}
