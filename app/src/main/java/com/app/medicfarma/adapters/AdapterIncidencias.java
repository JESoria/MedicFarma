package com.app.medicfarma.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.medicfarma.R;
import com.app.medicfarma.activities.IncidenciaActivity;
import com.app.medicfarma.activities.ProductDetailActivity;
import com.app.medicfarma.models.Incidencias;
import com.app.medicfarma.models.Product;

import java.util.List;

public class AdapterIncidencias extends RecyclerView.Adapter<AdapterIncidencias.IncidenciasViewHolder>{

    private Context context;
    private List<Incidencias> incidencias;

    public AdapterIncidencias(List<Incidencias> incidencias, Context context){
        this.incidencias = incidencias;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterIncidencias.IncidenciasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_list_incidencias,viewGroup,false);
        return new IncidenciasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterIncidencias.IncidenciasViewHolder incidenciasViewHolder, int position) {
        final Incidencias incidencia = incidencias.get(position);

        incidenciasViewHolder.tvSucursal.setText(incidencia.getSucursal());
        incidenciasViewHolder.tvCodigoPedido.setText(incidencia.getCodidoPedido());
        incidenciasViewHolder.tvDireccion.setText(incidencia.getDireccion());
        incidenciasViewHolder.tvTelefono.setText(incidencia.getTelefono());
        incidenciasViewHolder.tvMontoCompra.setText("$ "+ String.valueOf(incidencia.getMontoCompra()));

        incidenciasViewHolder.cvPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IncidenciaActivity.class);
                intent.putExtra("idPedido",incidencia.getIdPedido());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return incidencias.size();
    }

    public static class IncidenciasViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSucursal;
        private TextView tvCodigoPedido;
        private TextView tvMontoCompra;
        private TextView tvDireccion;
        private TextView tvTelefono;
        private CardView cvPedido;

        public IncidenciasViewHolder(View itemView) {
            super(itemView);
            tvSucursal = (TextView) itemView.findViewById(R.id.tvSucursal_incidencia);
            tvCodigoPedido = (TextView) itemView.findViewById(R.id.tvCodigo_incidencia);
            tvDireccion = (TextView) itemView.findViewById(R.id.tvDireccion_incidencia);
            tvTelefono = (TextView) itemView.findViewById(R.id.tvTelefono_incidencia);
            tvMontoCompra = (TextView) itemView.findViewById(R.id.tvMonto_incidencia);
            cvPedido = (CardView) itemView.findViewById(R.id.cvIncidencia);
        }

    }
}
