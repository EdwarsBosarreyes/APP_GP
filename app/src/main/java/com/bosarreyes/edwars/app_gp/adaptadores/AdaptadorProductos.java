package com.bosarreyes.edwars.app_gp.adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bosarreyes.edwars.app_gp.R;
import com.bosarreyes.edwars.app_gp.entidades.Producto;

import java.util.ArrayList;

public class AdaptadorProductos extends RecyclerView.Adapter<AdaptadorProductos.ViewHolderProductos> {

    ArrayList<Producto> listaProductos;

    public AdaptadorProductos(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public ViewHolderProductos onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_productos,null,false);
        return new ViewHolderProductos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProductos holder, int position) {
        holder.peso.setText(listaProductos.get(position).getPeso());
        holder.valvula.setText(listaProductos.get(position).getValvula());
        holder.precio.setText(listaProductos.get(position).getPrecio());
        holder.imagenProducto.setImageResource(listaProductos.get(position).getImagen());

    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public class ViewHolderProductos extends RecyclerView.ViewHolder {

        TextView peso,valvula,precio;
        ImageView imagenProducto;

        public ViewHolderProductos(@NonNull View itemView) {
            super(itemView);
            peso = itemView.findViewById(R.id.peso);
            valvula = itemView.findViewById(R.id.valvulas);
            precio = itemView.findViewById(R.id.precio);
            imagenProducto = itemView.findViewById(R.id.imagenProducto);
        }
    }
}
