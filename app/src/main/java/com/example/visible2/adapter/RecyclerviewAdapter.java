package com.example.visible2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.visible2.R;

import com.example.visible2.json_obj.ranks;

import java.util.ArrayList;
import java.util.List;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {

    private List<ranks.rank_info> mDataset;

    private LayoutInflater inflater;

    public RecyclerviewAdapter(List<ranks.rank_info> users) {
        this.mDataset = users;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recyclerview, parent, false);
        ViewHolder myViewHolder=  new ViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        String name = mDataset.get(position).nickname;
        viewHolder.name.setText(name);
        String daka = String.valueOf(mDataset.get(position).credits);
        viewHolder.daka.setText(daka);
        viewHolder.mingci.setText(" " + String.valueOf(position+1));
    }

    @Override
    public int getItemCount() {
        int count;
        try{
            count = mDataset.size();
        }
        catch (Exception e){
            count = 0;
        }
        return count;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView tx;
        private TextView   name;
        private TextView   mingci;
        private TextView   word;
        private TextView   daka;
        private TextView   beijingkuang;
        public ViewHolder(View itemView) {

            super(itemView);
            tx = (ImageView) itemView.findViewById(R.id.tx);
            name = (TextView) itemView.findViewById(R.id.name);
            mingci = (TextView) itemView.findViewById(R.id.mingci);

            daka = (TextView) itemView.findViewById(R.id.daka);
            beijingkuang= (TextView) itemView.findViewById(R.id.name);
        }
    }
}
