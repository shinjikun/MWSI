package com.indra.rover.mwsi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.pojo.MRU;
import com.indra.rover.mwsi.data.pojo.Meter_Reader;
import com.indra.rover.mwsi.utils.OnItemClickListener;

import java.util.List;

/**
 * Created by Indra on 8/22/2016.
 */
public class MReaderListAdapter extends RecyclerView.Adapter<MReaderListAdapter.MyViewHolder>{


    private List<Meter_Reader> arryList;
    OnItemClickListener listener;
    public MReaderListAdapter(List<Meter_Reader> meter_reader, OnItemClickListener listener){
        this.arryList = meter_reader;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_meter_reader, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Meter_Reader mru = arryList.get(position);
        holder.txtMRUID.setText(String.valueOf(mru.getId()));
        holder.txtMRName.setText(String.valueOf(mru.getName()));
        holder.txtAssignCode.setText(String.valueOf(mru.getAssignCode()));
        holder.bind(arryList.get(position),position, listener);

    }

    @Override
    public int getItemCount() {
        return this.arryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView txtMRUID, txtMRName, txtAssignCode;
        public MyViewHolder(View itemView) {
            super(itemView);
            txtMRUID =  (TextView) itemView.findViewById(R.id.txtMRUID);
            txtMRName = (TextView) itemView.findViewById(R.id.txtMRName);
            txtAssignCode = (TextView) itemView.findViewById(R.id.txtAssignCode);

        }

        public void bind(final Meter_Reader item,final  int position, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                  //  listener.onItemClick(item ,position);
                }
            });
        }
    }
}
