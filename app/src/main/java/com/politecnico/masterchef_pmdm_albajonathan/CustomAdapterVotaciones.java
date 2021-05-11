package com.politecnico.masterchef_pmdm_albajonathan;

// @Author - Alba Orbegozo / Jonathan Lopez - PMDM Masterchef - CI Politecnico Estella

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.politecnico.materchef_pmdm_albajonathan.R;
import java.util.ArrayList;

public class CustomAdapterVotaciones extends RecyclerView.Adapter<CustomAdapterVotaciones.MyViewHolder> {

    ArrayList<String> equipos;
    Context context;
    NumberPicker presentacion, servicio, sabor, imagen, triptico;

    public CustomAdapterVotaciones(Context context, ArrayList<String> equipo) {
        this.context = context;
        this.equipos = equipo;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout_votaciones, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        holder.equipo.setText(equipos.get(position));
        presentacion.setMinValue(0);
        presentacion.setMaxValue(100);
        servicio.setMinValue(0);
        servicio.setMaxValue(100);
        sabor.setMinValue(0);
        sabor.setMaxValue(100);
        imagen.setMinValue(0);
        imagen.setMaxValue(100);
        triptico.setMinValue(0);
        triptico.setMaxValue(100);
    }

    @Override
    public int getItemCount() {
        return equipos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView equipo;// init the item view's


        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            equipo = (TextView) itemView.findViewById(R.id.idEquipo);
            presentacion = itemView.findViewById(R.id.presentacionNP);
            servicio = itemView.findViewById(R.id.servicioNP);
            sabor = itemView.findViewById(R.id.saborNP);
            imagen = itemView.findViewById(R.id.imagenNP);
            triptico = itemView.findViewById(R.id.tripticoNP);
        }
    }
}