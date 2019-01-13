package com.app.medicfarma.activities;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.app.medicfarma.R;

public class RegisterActivity extends AppCompatActivity {

    AlertDialog.Builder builder;
    Button iniciar, cancelar;
    ProgressBar progressBar;
    EditText user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
