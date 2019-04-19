package com.app.medicfarma.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.app.medicfarma.R;
import com.app.medicfarma.helpers.DbHelper;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.login.LoginManager;
import static com.app.medicfarma.adapters.AdapterProductsPharmacies.idf;
import static com.app.medicfarma.adapters.AdapterProductsPharmacies.ids;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    CardView cvFarmacias, cvFarmaciaPreferida,cvReclamos;
    AlertDialog.Builder builder;
    Toolbar toolbar;
    final DbHelper mDbHelper = new DbHelper(this);
    public static boolean estadoOrden;
    int idF,idS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cvFarmacias = (CardView) findViewById(R.id.cvFarmacias);
        cvFarmaciaPreferida = (CardView) findViewById(R.id.cvFarmaciaPreferida);
        cvReclamos = (CardView) findViewById(R.id.cvReclamos);

        toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(cvFarmacias);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(cvFarmaciaPreferida);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .repeat(0)
                .playOn(cvReclamos);

        cvFarmacias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Lista de busqueda de todas las farmacias
                Intent intent = new Intent(HomeActivity.this,ProductsPharmaciesActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cvFarmaciaPreferida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Menu de las farmacias
                Intent intent = new Intent(HomeActivity.this,PharmaciesActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cvReclamos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,IncidenciasListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        estadoOrden = mDbHelper.estadoOrden();

        if (estadoOrden){

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setMessage("Tiene una orden pendiente ¿Deseas seguir con la orden?")
                    .setCancelable(false)
                    .setNegativeButton("No, gracias",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mDbHelper.deletePedido();
                                    dialog.cancel();
                                }
                            })
                    .setPositiveButton("Si, por favor",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    idF = idf;
                                    idS = ids;
                                    Intent intent = new Intent(HomeActivity.this,OrderDetailActivity.class);
                                    intent.putExtra("idFarmacia",idF);
                                    intent.putExtra("idSucursal",idS);
                                    startActivity(intent);
                                    finish();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.itemLoginHome) {
            mDbHelper.deleteUsuario();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(HomeActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.acerca_de) {
            Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.sesion) {
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(HomeActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
