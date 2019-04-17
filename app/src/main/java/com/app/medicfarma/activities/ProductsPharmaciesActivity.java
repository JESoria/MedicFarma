package com.app.medicfarma.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.app.medicfarma.R;
import com.app.medicfarma.adapters.AdapterProductsPharmacies;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.Product;
import com.app.medicfarma.ws_app.ProductosPharmaciesBridge;
import com.facebook.login.LoginManager;

import java.util.ArrayList;

public class ProductsPharmaciesActivity extends AppCompatActivity implements ProductosPharmaciesBridge.AsyncResponse {

    Toolbar toolbar;
    ImageView imgAtras;
    private RecyclerView listaProductos;
    Product product = new Product();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_pharmacies);

        progressBar = (ProgressBar) findViewById(R.id.progressBarProductosFarmacias);
        imgAtras = (ImageView) findViewById(R.id.imgAtras);

        imgAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsPharmaciesActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final DbHelper mDbHelper = new DbHelper(ProductsPharmaciesActivity.this);

        toolbar = (Toolbar) findViewById(R.id.toolbarMenuPharmacies);
        setSupportActionBar(toolbar);

        listaProductos = (RecyclerView) findViewById(R.id.rvProductsPharmacies);
        LinearLayoutManager llm = new LinearLayoutManager(ProductsPharmaciesActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaProductos.setLayoutManager(llm);
    }

    public AdapterProductsPharmacies adaptador;

    public boolean onCreateOptionsMenu(Menu menu){
        final DbHelper mDbHelper = new DbHelper(this);
        getMenuInflater().inflate(R.menu.menu_buscar,menu);
        final MenuItem searchItem = menu.findItem(R.id.itemBuscar);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("¿Que buscas?");
        //searchView.setBackgroundColor(getResources().getColor(R.color.blanco));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                product.setProducto(query);
                //Aqui se le debe enviar las coordenadas del lugar donde este el dispositivo
                progressBar.setVisibility(View.VISIBLE);
                new ProductosPharmaciesBridge(mDbHelper,progressBar,ProductsPharmaciesActivity.this).execute(product.getProducto(),"13.700515","-89.201563");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.itemLogin) {
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(ProductsPharmaciesActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(String response, ArrayList productos) {
        try{
            progressBar.setVisibility(View.INVISIBLE);
            if(productos.size() > 0){
                adaptador = new AdapterProductsPharmacies(productos,this);
                listaProductos.setAdapter(adaptador);
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductsPharmaciesActivity.this);
                builder.setMessage("Lo sentimos el medicamento "+ product.getProducto() +" no se encuentra en su ubicación")
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
            AlertDialog.Builder builder = new AlertDialog.Builder(ProductsPharmaciesActivity.this);
            builder.setMessage("Sin servicio")
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
