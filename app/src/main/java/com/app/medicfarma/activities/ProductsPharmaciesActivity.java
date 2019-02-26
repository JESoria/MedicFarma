package com.app.medicfarma.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.app.medicfarma.R;
import com.app.medicfarma.adapters.AdapterProducts;
import com.app.medicfarma.adapters.AdapterProductsPharmacies;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.Product;
import com.app.medicfarma.ws_app.ProductosPharmacies;

import java.util.ArrayList;

public class ProductsPharmaciesActivity extends AppCompatActivity implements ProductosPharmacies.AsyncResponse {

    Toolbar toolbar;
    private RecyclerView listaProductos;
    Product product = new Product();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_pharmacies);

        final DbHelper mDbHelper = new DbHelper(ProductsPharmaciesActivity.this);

        toolbar = (Toolbar) findViewById(R.id.toolbarMenuPharmacies);
        setSupportActionBar(toolbar);

        listaProductos = (RecyclerView) findViewById(R.id.rvProducts);
        LinearLayoutManager llm = new LinearLayoutManager(ProductsPharmaciesActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaProductos.setLayoutManager(llm);

    }

    public AdapterProductsPharmacies adaptador;

    @Override
    public void processFinish(String response, ArrayList productos) {

    }
}
