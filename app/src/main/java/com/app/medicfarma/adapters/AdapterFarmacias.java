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
import com.app.medicfarma.models.Pharmacy;

import java.util.List;

public class AdapterFarmacias extends RecyclerView.Adapter<AdapterFarmacias.FarmaciasViewHolder> {

    private Context context;
    private String name;
    private List<Pharmacy> farmacias;

    public AdapterFarmacias(List<Pharmacy> farmacias, Context context){
        this.farmacias = farmacias;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterFarmacias.FarmaciasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_list_drugstore,viewGroup,false);
        return new FarmaciasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFarmacias.FarmaciasViewHolder farmaciasViewHolder, int position) {
        final Pharmacy pharmacy = farmacias.get(position);

        /*
        farmaciasViewHolder.cvFarmacias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BuscarProductoActivity.class);
                intent.putExtra("idFarmacia",pharmacy.getIdFarmacia());
                context.startActivity(intent);
            }
        });
        */

        farmaciasViewHolder.tvNombreFarmacia.setText(pharmacy.getNombreFarmacia());

        if (pharmacy.getIdFarmacia() == 2){
            farmaciasViewHolder.ivLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.san_nicolas));
        }
        else if (pharmacy.getIdFarmacia() == 1013){
            farmaciasViewHolder.ivLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.farmavalue));
        }
        else if (pharmacy.getIdFarmacia() == 1014){
            farmaciasViewHolder.ivLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.camila));
        }


    }

    @Override
    public int getItemCount() {
        return farmacias.size();
    }




public static class FarmaciasViewHolder extends RecyclerView.ViewHolder {

    private TextView tvNombreFarmacia;
    private CardView cvFarmacias;
    private ImageView ivLogo;

        public FarmaciasViewHolder(View itemView) {
            super(itemView);

            tvNombreFarmacia = (TextView) itemView.findViewById(R.id.tvNombreFarmacia);
            cvFarmacias = (CardView) itemView.findViewById(R.id.cvFarmacias);
            ivLogo= (ImageView) itemView.findViewById(R.id.ivLogo);

        }

    }

}
