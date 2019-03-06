package com.app.medicfarma.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.app.medicfarma.R;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.UsuarioModel;
import com.app.medicfarma.ws_app.RegisterUserBridge;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


public class RegisterActivity  extends AppCompatActivity implements RegisterUserBridge.AsyncResponse{

    AlertDialog.Builder builder;
    Button registrar, cancelar;
    ProgressBar progressBar;
    EditText nombres, apellidos, fecha_nacimiento, correo;
    RadioGroup rdbgenero;
    RadioButton rdbmasculino,rdbfemenino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_register);
        nombres = (EditText) findViewById(R.id.edtNombresRegistro);
        apellidos = (EditText) findViewById(R.id.edtApellidosRegistro);
        fecha_nacimiento = (EditText) findViewById(R.id.edtFechaNacimientoRegisto);
        correo =(EditText)findViewById(R.id.edtCorreoRegistro);
        rdbgenero = (RadioGroup) findViewById(R.id.rdbGeneroRegistro);
        rdbmasculino = (RadioButton) findViewById(R.id.rdbMasculinoRegistro);
        rdbfemenino = (RadioButton) findViewById(R.id.rdbMasculinoRegistro);

        registrar = (Button) findViewById(R.id.btnGuardarRegistro);
        cancelar = (Button) findViewById(R.id.btnCancelarRegistro);

        progressBar = (ProgressBar)findViewById(R.id.progressBar_registro);

        //Section Animations
        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(1)
                .playOn(registrar);

        YoYo.with(Techniques.FadeIn)
                .duration(900)
                .repeat(0)
                .playOn(cancelar);

        YoYo.with(Techniques.FadeIn)
                .duration(900)
                .repeat(0)
                .playOn(rdbgenero);

        YoYo.with(Techniques.FadeIn)
                .duration(900)
                .repeat(0)
                .playOn(rdbmasculino);

        YoYo.with(Techniques.FadeIn)
                .duration(900)
                .repeat(0)
                .playOn(rdbfemenino);

        final DbHelper mDbHelper = new DbHelper(this);
        //End Section Animations

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResgiterByMail(mDbHelper);
            }
        });
    }

    @Override
    public void processFinish(String response) {
        try{
            progressBar.setVisibility(View.INVISIBLE);

            if(!response.equals("")){

                    builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("¡Registrado con exito!")
                            .setCancelable(false)
                            .setNeutralButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                //Intent welcome = new Intent(LoginActivity.this,StartActivity.class);
                //startActivity(welcome);
                //finish();
            }
            else {

                builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage("¡No se pudo Registrar!")
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

            builder = new AlertDialog.Builder(RegisterActivity.this);
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



    private void ResgiterByMail(DbHelper mDbHelper){

        UsuarioModel model = new UsuarioModel();

        model.setNombres(nombres.getText().toString());
        model.setApellidos(apellidos.getText().toString());



        if (rdbfemenino.isChecked()){
            model.setGenero("F");
        }
        else if(rdbmasculino.isChecked()){
            model.setGenero("M");
        }

        //año/mes/dia
        model.setFechaNacimiento(fecha_nacimiento.getText().toString());
        model.setCorreo(correo.getText().toString());
        model.setFacebookId("Mail");
        model.setEstado(true);
        model.setPassword("jaja");


            progressBar.setVisibility(View.VISIBLE);
            new RegisterUserBridge(mDbHelper,progressBar,this).execute(
                    model.getNombres(),
                    model.getApellidos(),
                    model.getGenero(),
                    model.getFechaNacimiento(),
                    model.getCorreo(),
                    model.getFacebookId(),
                    model.getPassword(),
                    String.valueOf(model.getEstado()));

    }




}
