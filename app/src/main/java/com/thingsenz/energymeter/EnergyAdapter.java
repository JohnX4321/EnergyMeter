package com.thingsenz.energymeter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.thingsenz.energymeter.database.EnergyModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EnergyAdapter extends RecyclerView.Adapter<EnergyAdapter.ViewHolder> {

    private Context context;
    private List<EnergyModel> energyModelList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView energy, power, dot, timestamp;

        public ViewHolder(View view) {
            super(view);
            energy = view.findViewById(R.id.energy);
            dot = view.findViewById(R.id.dot);
            power = view.findViewById(R.id.power);
            timestamp = view.findViewById(R.id.timestamp);
        }
    }




    public EnergyAdapter(Context context,List<EnergyModel> energyModelList) {
        this.context=context;
        this.energyModelList=energyModelList;
        setHasStableIds(true);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_energy_row,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int pos) {
        EnergyModel energyModel=energyModelList.get(pos);
        holder.power.setText(energyModel.getPower());
        holder.energy.setText(energyModel.getEnergy());

        holder.dot.setText(Html.fromHtml("&#8226;"));
        holder.timestamp.setText(formatDate(energyModel.getTimestamp()));
    }


    @Override
    public int getItemCount() {
        return energyModelList.size();
    }



    private String formatDate(String timestamp) {
        try{
            SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date=format1.parse(timestamp);
            return format1.format(date);
        } catch (ParseException e) {

        }
        return "";
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
