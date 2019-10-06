package com.bosarreyes.edwars.app_gp.adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bosarreyes.edwars.app_gp.R;
import com.bosarreyes.edwars.app_gp.entidades.Cilindro;

import java.util.List;

public class ListaAdapterPedido extends ArrayAdapter<Cilindro> {

    private final List<Cilindro> list;
    private final Context context;

    public ListaAdapterPedido(Context context, List<Cilindro> list){
        super(context, R.layout.list_fila2, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder{//Un miembro protegido es accesible dentro de su clase y por instancias de clases derivadas.
        protected TextView tvCantidad;
        protected TextView tvPeso;
        protected TextView tvValvula;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        if (convertView == null){
            LayoutInflater inflator = LayoutInflater.from(context);
            view = inflator.inflate(R.layout.list_fila2,null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.tvCantidad = (TextView) view.findViewById(R.id.tvCantidad);
            viewHolder.tvPeso = (TextView) view.findViewById(R.id.tvPeso);
            viewHolder.tvValvula = (TextView) view.findViewById(R.id.tvValvula);
            view.setTag(viewHolder);
        }else{
            view = convertView;
        }

        ViewHolder holder= (ViewHolder) view.getTag();
        holder.tvCantidad.setText(""+list.get(position).getCantidad());
        holder.tvPeso.setText(list.get(position).getPeso());
        holder.tvValvula.setText(list.get(position).getValvula());
        return view;
    }
}
