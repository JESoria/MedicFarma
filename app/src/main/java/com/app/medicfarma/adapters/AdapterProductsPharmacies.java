package com.app.medicfarma.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.medicfarma.R;
import com.app.medicfarma.models.Product;

import java.util.List;

public class AdapterProductsPharmacies extends RecyclerView.Adapter<AdapterProductsPharmacies.ProductsViewHolder>{

    private Context context;
    private String name;
    private List<Product> products;

    public AdapterProductsPharmacies(List<Product> products, Context context){
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterProductsPharmacies.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_list_products_pharmacies,viewGroup,false);
        return new ProductsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProductsPharmacies.ProductsViewHolder productsViewHolder, int position) {
        final Product product = products.get(position);

        /*
        productsViewHolder.cvProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductsBranchOfficeActivity.class);
                intent.putExtra("idSucursal",pharmacy.getIdSucursal());
                intent.putExtra("producto",pharmacy.getProducto);
                context.startActivity(intent);
            }
        });
        */

        productsViewHolder.tvProducto.setText(product.getProducto());
        productsViewHolder.tvSucursal.setText(product.getSucursal());
        productsViewHolder.tvPrecio.setText(String.valueOf(product.getPrecio()));
        productsViewHolder.tvDireccion.setText(product.getDireccion());

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder {

        private TextView tvProducto;
        private TextView tvSucursal;
        private TextView tvPrecio;
        private TextView tvDireccion;
        private CardView cvProducts;


        public ProductsViewHolder(View itemView) {
            super(itemView);

            tvProducto = (TextView) itemView.findViewById(R.id.tvProducto);
            tvSucursal = (TextView) itemView.findViewById(R.id.tvSucursal);
            tvPrecio = (TextView) itemView.findViewById(R.id.tvPrecio);
            tvDireccion = (TextView) itemView.findViewById(R.id.tvDireccion);
            cvProducts = (CardView) itemView.findViewById(R.id.cvProductsPharmacies);


        }

    }
}
