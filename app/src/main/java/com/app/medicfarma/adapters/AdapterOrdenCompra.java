package com.app.medicfarma.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.medicfarma.R;
import com.app.medicfarma.activities.ProductDetailActivity;
import com.app.medicfarma.helpers.DbHelper;
import com.app.medicfarma.models.DetallePedido;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterOrdenCompra extends RecyclerView.Adapter<AdapterOrdenCompra.OrdenCompraViewHolder> {

    private Context context;
    private String name;
    private List<DetallePedido> detallePedido;

    final DbHelper mDbHelper = null;

    public AdapterOrdenCompra(List<DetallePedido> detallePedido, Context context){
        this.detallePedido = detallePedido;
        this.context = context;
        new DbHelper(context);
    }

    @NonNull
    @Override
    public AdapterOrdenCompra.OrdenCompraViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_list_detail_product,viewGroup,false);
        return new OrdenCompraViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrdenCompra.OrdenCompraViewHolder ordenCompraViewHolder, int position) {
        final DetallePedido detalle = detallePedido.get(position);

        ordenCompraViewHolder.tvCantidad.setText(detalle.getCantidad());
        ordenCompraViewHolder.tvProducto.setText(detalle.getProducto());
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String precio = decimalFormat.format(detalle.getPrecio());
        ordenCompraViewHolder.tvPrecio.setText("$"+precio);

        ordenCompraViewHolder.imvEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("idSucursalProducto",detalle.getIdSucursalProducto());
                intent.putExtra("idFarmacia",detalle.getIdFarmacia());
                intent.putExtra("editar",true);
                context.startActivity(intent);
            }
        });

        ordenCompraViewHolder.imvEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDbHelper.deleteDetallePedido(detalle.getIdSucursalProducto());
            }
        });
    }

    @Override
    public int getItemCount() {
            return detallePedido.size();
    }

    public static class OrdenCompraViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCantidad,tvProducto,tvPrecio;
        private CardView cvProducto;
        private ImageView imvEditar,imvEliminar;

        public OrdenCompraViewHolder(View itemView) {
            super(itemView);

            tvCantidad = (TextView) itemView.findViewById(R.id.tvCantidadls);
            tvProducto = (TextView) itemView.findViewById(R.id.tvProductols);
            tvPrecio = (TextView) itemView.findViewById(R.id.tvPreciols);
            cvProducto = (CardView) itemView.findViewById(R.id.cvListaOrdenCompra);
            imvEditar = (ImageView) itemView.findViewById(R.id.imvEditarPOrden);
            imvEliminar = (ImageView) itemView.findViewById(R.id.imvEliminarPOrden);

        }

    }
}
