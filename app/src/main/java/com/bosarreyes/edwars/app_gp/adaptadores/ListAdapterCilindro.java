package com.bosarreyes.edwars.app_gp.adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bosarreyes.edwars.app_gp.R;
import com.bosarreyes.edwars.app_gp.entidades.Cilindro;

import java.util.List;

public class ListAdapterCilindro extends ArrayAdapter<Cilindro> {

    private final List<Cilindro> list;
    private final Context context;

    public ListAdapterCilindro(Context context, List<Cilindro> list){
        super(context, R.layout.list_fila, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder{//Un miembro protegido es accesible dentro de su clase y por instancias de clases derivadas.
        protected TextView tvCantidad;
        protected TextView tvPeso;
        protected TextView tvValvula;
        protected CheckBox checkBox;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        if (convertView == null){
            LayoutInflater inflator = LayoutInflater.from(context);
            view = inflator.inflate(R.layout.list_fila,null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.tvCantidad = (TextView) view.findViewById(R.id.tvCantidad);
            viewHolder.tvPeso = (TextView) view.findViewById(R.id.tvPeso);
            viewHolder.tvValvula = (TextView) view.findViewById(R.id.tvValvula);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkbox);

            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Cilindro element = (Cilindro) viewHolder.checkBox.getTag();
                    element.setSelected(buttonView.isChecked());
                }
            });

            view.setTag(viewHolder);
            viewHolder.checkBox.setTag(list.get(position));
        }else{
            view = convertView;
            ((ViewHolder) view.getTag()).checkBox.setTag(list.get(position));
        }

        ViewHolder holder= (ViewHolder) view.getTag();
        holder.tvCantidad.setText(""+list.get(position).getCantidad());
        holder.tvPeso.setText(list.get(position).getPeso());
        holder.tvValvula.setText(list.get(position).getValvula());

        holder.checkBox.setChecked(list.get(position).isSelected());
        return view;
    }
}
