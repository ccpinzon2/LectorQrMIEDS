package com.example.cristhianpinzon.lectorqr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.cristhianpinzon.lectorqr.Logica.Placa;
import com.example.cristhianpinzon.lectorqr.R;
import java.util.List;

public class AdapterPlacas extends BaseAdapter{

    private List<Placa> placas;
    private Context context;

    public AdapterPlacas(List<Placa> placas, Context context) {
        this.placas = placas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return placas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_item_fidelizados, parent, false);
        }

        TextView teNumero = (TextView) convertView.findViewById(R.id.teNumeroPlaca);
        TextView tePuntos = (TextView) convertView.findViewById(R.id.tePuntosPlaca);

        teNumero.setText(placas.get(position).getPlaca());
        tePuntos.setText(placas.get(position).getPuntos()+" Puntos");

        return convertView;
    }
}
