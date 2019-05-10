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
import android.widget.Button;
import android.widget.ProgressBar;
import com.app.medicfarma.R;
import com.app.medicfarma.adapters.AdapterProductsSpecificBranchOffice;
import com.app.medicfarma.config.ConexionInternet;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.Product;
import com.app.medicfarma.ws_app.ProductosSpecificSucursalBridge;
import com.facebook.login.LoginManager;
import java.util.ArrayList;

public class ProductsSpecificBranchOfficeActivity extends AppCompatActivity implements ProductosSpecificSucursalBridge.AsyncResponse{

    Toolbar toolbar;
    private RecyclerView listaProductos;
    Product product = new Product();
    int idFarmacia;
    int idSucursal;
    ProgressBar progressBar;
    Button verOrden,cancelarOrden;
    AlertDialog.Builder builder;
    boolean connected;
    final DbHelper mDbHelper = new DbHelper(ProductsSpecificBranchOfficeActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_specific_branch_office);

        ConexionInternet conexionInternet = new ConexionInternet();
        conexionInternet.getStateInternet(ProductsSpecificBranchOfficeActivity.this);

        if(!connected){
            builder = new AlertDialog.Builder(ProductsSpecificBranchOfficeActivity.this);
            builder.setMessage("¡Ups! debes conectarte a Internet, la aplicación no funcionará correctamente")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Intent intent = new Intent(ProductsSpecificBranchOfficeActivity.this,HomeActivity.class);
                                    //startActivity(intent);
                                    //finish();
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBarFarmaciaEspecifica);

        toolbar = (Toolbar) findViewById(R.id.toolbarMenuBranchOffice);
        setSupportActionBar(toolbar);

        listaProductos = (RecyclerView) findViewById(R.id.rvProducts);
        LinearLayoutManager llm = new LinearLayoutManager(ProductsSpecificBranchOfficeActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaProductos.setLayoutManager(llm);

        verOrden = (Button) findViewById(R.id.btnVerOrdenLst);
        cancelarOrden = (Button) findViewById(R.id.btnCancelarOrdenLst);

        Bundle datos = getIntent().getExtras();
        idFarmacia = datos.getInt("idFarmacia");
        idSucursal = datos.getInt("idSucursal");

        verOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mDbHelper.estadoOrden()){
                    Intent intent = new Intent(ProductsSpecificBranchOfficeActivity.this, OrderDetailActivity.class);
                    intent.putExtra("idFarmacia",idFarmacia);
                    intent.putExtra("idSucursal",idSucursal);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(ProductsSpecificBranchOfficeActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        cancelarOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ProductsSpecificBranchOfficeActivity.this);
                builder.setMessage("¿Estas seguro de cancelar tu orden?")
                        .setCancelable(false)
                        .setNegativeButton("Ups, No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Acciones false
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mDbHelper.deletePedido();
                                        Intent intent = new Intent(ProductsSpecificBranchOfficeActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    public AdapterProductsSpecificBranchOffice adaptador;

    public boolean onCreateOptionsMenu(Menu menu){
        final DbHelper mDbHelper = new DbHelper(this);
        getMenuInflater().inflate(R.menu.menu_buscar,menu);
        final MenuItem searchItem = menu.findItem(R.id.itemBuscar);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("¿Que buscas?");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                product.setProducto(query);
                progressBar.setVisibility(View.VISIBLE);
                new ProductosSpecificSucursalBridge(mDbHelper, progressBar,ProductsSpecificBranchOfficeActivity.this).execute(product.getProducto(),String.valueOf(idFarmacia),String.valueOf(idSucursal));
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
            mDbHelper.deleteUsuario();
            Intent intent = new Intent(ProductsSpecificBranchOfficeActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(String response, ArrayList productos) {
        try{
            InputMethodManager imm = (InputMethodManager) getSystemService(ProductsSpecificBranchOfficeActivity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_HIDDEN);

            progressBar.setVisibility(View.INVISIBLE);
            if(productos.size() > 0){
                adaptador = new AdapterProductsSpecificBranchOffice(productos,this);
                listaProductos.setAdapter(adaptador);

            }
            else if (response.equals("1")){
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductsSpecificBranchOfficeActivity.this);
                builder.setMessage("Lo sentimos el medicamento "+ product.getProducto() +" no se encuentra disponible")
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
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductsSpecificBranchOfficeActivity.this);
                builder.setMessage("Lo sentimos el medicamento "+ product.getProducto() +" no se encuentra en la Sucursal")
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
            AlertDialog.Builder builder = new AlertDialog.Builder(ProductsSpecificBranchOfficeActivity.this);
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
