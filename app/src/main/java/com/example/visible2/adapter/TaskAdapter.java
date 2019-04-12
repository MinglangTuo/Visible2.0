package com.example.visible2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.visible2.R;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private String[] mDataset;

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TaskViewHolder(View v) {
            super(v);
            textView =  v.findViewById(R.id.textView);
        }
    }

    public TaskAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_recycler_item_view, parent, false);

        TaskViewHolder vh = new TaskViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mDataset[position]);

    }
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
