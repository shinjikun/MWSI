package com.indra.rover.mwsi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.MRUDao;
import com.indra.rover.mwsi.data.pojo.MRU;
import com.indra.rover.mwsi.utils.OnItemClickListener;

import java.util.List;

/**
 * Created by Indra on 8/22/2016.
 */
public class MRUListAdapter  extends RecyclerView.Adapter<MRUListAdapter.MyViewHolder>{


    private List<MRU> arryList;
    OnItemClickListener listener;
    MRUDao mruDao;

    public MRUListAdapter(List<MRU> mru, OnItemClickListener listener, MRUDao mruDao){
        this.arryList = mru;
        this.listener = listener;
        this.mruDao = mruDao;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_mru, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MRU mru = arryList.get(position);
        holder.txtMruTotal.setText(String.valueOf(mru.getCustomer_count()));
        holder.txtMruTotal.setText(String.valueOf(mru.getTotal()));
        holder.txtMRUID.setText(mru.getId());

        int undelivered = mruDao.countUnDelivered(mru.getId());
        int unread = mruDao.countUnRead(mru.getId(),"U");
        int printed = mruDao.countPrinted(mru.getId());
        holder.txtUnRead.setText(String.valueOf(unread));
        holder.txtUnDelivered.setText(String.valueOf(undelivered));
        holder.txtUnPrinted.setText(String.valueOf(printed));


        holder.bind(arryList.get(position),position, listener);

    }

    @Override
    public int getItemCount() {
        return this.arryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

         TextView txtUnRead , txtMRUID, txtUnPrinted,txtUnDelivered,txtMruTotal;
        public MyViewHolder(View itemView) {
            super(itemView);
            txtMRUID =  (TextView) itemView.findViewById(R.id.txtMRUID);
            txtUnRead = (TextView) itemView.findViewById(R.id.txtUnRead);
            txtUnPrinted = (TextView) itemView.findViewById(R.id.txtUnPrinted);
            txtUnDelivered = (TextView)itemView.findViewById(R.id.txtUnDelivered);
            txtMruTotal = (TextView)itemView.findViewById(R.id.txtMruTotal);
        }

         void bind(final MRU item,final  int position, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item ,position);
                }
            });
        }
    }
}
