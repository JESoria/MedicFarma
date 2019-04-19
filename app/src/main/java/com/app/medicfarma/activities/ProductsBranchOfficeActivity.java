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
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.app.medicfarma.R;
import com.app.medicfarma.adapters.AdapterProducts;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.Product;
import com.app.medicfarma.ws_app.ProductosSucursalBridge;
import com.facebook.login.LoginManager;

import java.util.ArrayList;

public class ProductsBranchOfficeActivity extends AppCompatActivity implements ProductosSucursalBridge.AsyncResponse{

    Toolbar toolbar;
    ImageView imgAtras;
    private RecyclerView listaProductos;
    Product product = new Product();
    int idFarmacia;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_branch_office);

        progressBar = (ProgressBar) findViewById(R.id.progressBarSucursal);

        imgAtras = (ImageView) findViewById(R.id.imgAtras);

        imgAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsBranchOfficeActivity.this, PharmaciesActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final DbHelper mDbHelper = new DbHelper(ProductsBranchOfficeActivity.this);

        toolbar = (Toolbar) findViewById(R.id.toolbarMenuBranchOffice);
        setSupportActionBar(toolbar);

        listaProductos = (RecyclerView) findViewById(R.id.rvProducts);
        LinearLayoutManager llm = new LinearLayoutManager(ProductsBranchOfficeActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaProductos.setLayoutManager(llm);

        Bundle datos = getIntent().getExtras();
        idFarmacia = datos.getInt("idFarmacia");
    }

    public AdapterProducts adaptador;

    public boolean onCreateOptionsMenu(Menu menu){
        final DbHelper mDbHelper = new DbHelper(this);
        getMenuInflater().inflate(R.menu.menu_buscar,menu);
        final MenuItem searchItem = menu.findItem(R.id.itemBuscar);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("¿Que buscas?");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //Aqui debe recibir las coordenadas del dispositivo
                product.setProducto(query);
                progressBar.setVisibility(View.VISIBLE);
                new ProductosSucursalBridge(mDbHelper,progressBar,ProductsBranchOfficeActivity.this).execute(String.valueOf(idFarmacia),"13.700515","-89.201563",product.getProducto());
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
            Intent intent = new Intent(ProductsBranchOfficeActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(String response, ArrayList productos) {
        try{
            InputMethodManager imm = (InputMethodManager) getSystemService(ProductsBranchOfficeActivity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_HIDDEN);

            progressBar.setVisibility(View.INVISIBLE);
            if(productos.size() > 0){
                adaptador = new AdapterProducts(productos,this);
                listaProductos.setAdapter(adaptador);

            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductsBranchOfficeActivity.this);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(ProductsBranchOfficeActivity.this);
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
