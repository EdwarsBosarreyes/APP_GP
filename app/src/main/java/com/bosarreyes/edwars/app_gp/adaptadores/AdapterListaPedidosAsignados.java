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
import com.bosarreyes.edwars.app_gp.entidades.InformacionPedido;

import java.util.List;

public class AdapterListaPedidosAsignados extends ArrayAdapter<InformacionPedido> {
    private final List<InformacionPedido> list;
    private final Context context;

    public AdapterListaPedidosAsignados(List<InformacionPedido> list, Context context) {
        super(context, R.layout.lista_pedidos_asignados, list);
        this.list = list;
        this.context = context;
    }

    static class ViewHolder{//Un miembro protegido es accesible dentro de su clase y por instancias de clases derivadas.
        protected TextView tvNumero;
        protected TextView tvNombre;
        protected TextView tvApellido;
        protected TextView tvFecha;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        if (convertView == null){
            LayoutInflater inflator = LayoutInflater.from(context);
            view = inflator.inflate(R.layout.lista_pedidos_asignados,null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.tvNumero = (TextView) view.findViewById(R.id.txNumero);
            viewHolder.tvNombre = (TextView) view.findViewById(R.id.txNombre);
            viewHolder.tvApellido = (TextView) view.findViewById(R.id.txApellido);
            viewHolder.tvFecha = (TextView) view.findViewById(R.id.tvHora);
            view.setTag(viewHolder);
        }else{
            view = convertView;
        }

        ViewHolder holder= (ViewHolder) view.getTag();
        holder.tvNumero.setText(""+list.get(position).getNumero());
        holder.tvNombre.setText(list.get(position).getNombre().toString());
        holder.tvApellido.setText(list.get(position).getApellido().toString());
        holder.tvFecha.setText(list.get(position).getFecha().toString());
        return view;
    }
}
