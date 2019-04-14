package com.app.medicfarma.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.medicfarma.R;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.UsuarioModel;
import com.app.medicfarma.ws_app.RegisterUserBridge;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Calendar;

import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;


public class RegisterActivity  extends AppCompatActivity implements RegisterUserBridge.AsyncResponse{

    private static final String TAG = "RegisterActivity";
    private static int welcome_timeout = 1000;

    TextView tvFecharegister;
    AlertDialog.Builder builder;
    Button btnregistrarregister, btncancelarregister;
    ProgressBar progressBar;
    EditText nombresregister, apellidosregister, fecha_nacimiento, correoregister,passwordregister;
    RadioGroup rdbgeneroregister;
    UsuarioModel model = new UsuarioModel();
    RadioButton rdbmasculinoregister,rdbfemeninoregister;

    String date = "sinfecha";
    DatePickerDialog.OnDateSetListener mDataSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        nombresregister = (EditText) findViewById(R.id.edtNombresRegistro);
        apellidosregister = (EditText) findViewById(R.id.edtApellidosRegistro);
        tvFecharegister = (TextView) findViewById(R.id.edtFechaNacimientoRegisto);
        correoregister =(EditText)findViewById(R.id.edtCorreoRegistro);
        rdbgeneroregister = (RadioGroup) findViewById(R.id.rdbGeneroRegistro);
        rdbmasculinoregister = (RadioButton) findViewById(R.id.rdbMasculinoRegistro);
        rdbfemeninoregister = (RadioButton) findViewById(R.id.rdbMasculinoRegistro);
        btnregistrarregister = (Button) findViewById(R.id.btnGuardarRegistro);
        btncancelarregister = (Button) findViewById(R.id.btnCancelarRegistro);
        passwordregister = (EditText) findViewById(R.id.edtPass);

        progressBar = (ProgressBar)findViewById(R.id.progressBar_registro);

        //Section Animations
        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(1)
                .playOn(btnregistrarregister);

        YoYo.with(Techniques.FadeIn)
                .duration(900)
                .repeat(0)
                .playOn(btncancelarregister);

        YoYo.with(Techniques.FadeIn)
                .duration(900)
                .repeat(0)
                .playOn(rdbgeneroregister);

        YoYo.with(Techniques.FadeIn)
                .duration(900)
                .repeat(0)
                .playOn(rdbmasculinoregister);

        YoYo.with(Techniques.FadeIn)
                .duration(900)
                .repeat(0)
                .playOn(rdbfemeninoregister);

        final DbHelper mDbHelper = new DbHelper(this);
        //End Section Animations

        btnregistrarregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResgiterByMail(mDbHelper);
            }
        });

        btncancelarregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent welcome = new Intent(RegisterActivity.this, StartActivity.class);
                startActivity(welcome);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_in);
                finish();
            }
        });

        tvFecharegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDataSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(RegisterActivity.this.getResources().getColor(R.color.transparent)));
                dialog.show();
            }
        });



        mDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String fecha = "";
                month = month+1;
                date = day+"/"+month+"/"+year;

                if(Integer.parseInt(getAge(date)) >= 14){
                    tvFecharegister.setText(date);
                    model.setAge(getAge(date));

                    if(month/10 == 0){
                        fecha = year+"-0"+month+"-"+day;
                    }else{
                        fecha = year+"-"+month+"-"+day;
                    }
                    model.setBirthday(fecha);
                }
                else{

                    builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("Su edad no es permitida para regitrarse")
                            .setTitle("Advertencia")
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
        };





    }

    @Override
    public void processFinish(String response) {
        try{
            progressBar.setVisibility(View.INVISIBLE);

            if(!response.equals("1")){

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
                Intent welcome = new Intent(RegisterActivity.this,StartActivity.class);
                startActivity(welcome);
                finish();
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


    public static String getAge(String birthday){

        int anioActual = 0;
        int mesActual = 0;
        int age;

        Calendar calendar = Calendar.getInstance();
        String[] fechArray = birthday.split("/");
        int mesCumple = Integer.valueOf(fechArray[1]);
        int anioCumple = Integer.valueOf(fechArray[2]);

        mesActual = calendar.get(MONTH);
        anioActual = calendar.get(YEAR);

        if(mesActual < mesCumple){
            age = anioActual - anioCumple - 1;
        }
        else{
            age = anioActual - anioCumple;
        }

        return String.valueOf(age);
    }


    private void ResgiterByMail(DbHelper mDbHelper){

        model.setNombres(nombresregister.getText().toString());
        model.setApellidos(apellidosregister.getText().toString());
        model.setCorreo(correoregister.getText().toString());
        model.setPassword(passwordregister.getText().toString());
        model.setEstado(true);

        if (TextUtils.isEmpty(model.getNombres())) {
            //btnregistrarregister.setEnabled(true);
            nombresregister.setError(getResources().getString(R.string.msjError_edtName));
            nombresregister.requestFocus();
        }
        else if (TextUtils.isEmpty(model.getApellidos()))
        {
            //btnregistrarregister.setEnabled(true);
            apellidosregister.setError(getResources().getString(R.string.msjError_edtLastname));
            apellidosregister.requestFocus();
        }
        else if (!rdbfemeninoregister.isChecked() && !rdbmasculinoregister.isChecked()){
            builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage("Seleccione un genero, para poder registrase")
                    .setTitle("Advertencia")
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
        else if (date.equals("sinfecha"))
        {   //btnregistrarregister.setEnabled(true);
            Toast.makeText(this, "Seleccione una fecha de nacimiento", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(model.getCorreo())){
            //btnregistrarregister.setEnabled(true);
            correoregister.setError(getResources().getString(R.string.msj_email));
            correoregister.requestFocus();
        }
        else if (TextUtils.isEmpty(model.getPassword())){
            //btnregistrarregister.setEnabled(true);
            passwordregister.setError(getResources().getString(R.string.msj_pass));
            passwordregister.requestFocus();
        }
        else {

            if (rdbfemeninoregister.isChecked()){
                model.setGenero("F");
            }
            if(rdbmasculinoregister.isChecked()){
                model.setGenero("M");
            }


            btnregistrarregister.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

            new RegisterUserBridge(mDbHelper, progressBar, this).execute(
                    model.getNombres(),
                    model.getApellidos(),
                    model.getGenero(),
                    //model.getAge(),
                    model.getBirthday(),
                    model.getCorreo(),
                    "S/F",
                    model.getPassword(),
                    String.valueOf(model.getEstado()));
        }

    }


}
