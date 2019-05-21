package com.example.visible2.adapter;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.visible2.MainActivity;
import com.example.visible2.R;
import com.example.visible2.fragment.TaskFragment;
import com.example.visible2.json_obj.tasks;
import com.example.visible2.utils.MyCountDownTimer;
import com.example.visible2.utils.Task;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private TextView time_tv;
    private MyCountDownTimer mc;
    private TaskFragment homeFragment;
    private List<tasks.task_info> mDataset;
    private Calendar c;
    ColorStateList[] task_colors = new ColorStateList[] {
            MainActivity.getAppContext().getColorStateList(R.color.task_color1),
            MainActivity.getAppContext().getColorStateList(R.color.task_color2),
            MainActivity.getAppContext().getColorStateList(R.color.task_color3),
            MainActivity.getAppContext().getColorStateList(R.color.task_color4),
            MainActivity.getAppContext().getColorStateList(R.color.task_color5),
            MainActivity.getAppContext().getColorStateList(R.color.task_color6)};

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView TaskName;
        public TextView Date;
        public TextView Duration;
        public TextView TaskIcon;
        public Button SettingButton;
        public Button StartButton;
        public TaskViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.textView);
            TaskName = v.findViewById(R.id.task_name);
            TaskIcon = v.findViewById(R.id.task_icon);
            Date = v.findViewById(R.id.date);
            Duration = v.findViewById(R.id.duration);
            StartButton = v.findViewById(R.id.task_start);
            SettingButton = v.findViewById(R.id.task_setting);
        }
    }


    public TaskAdapter(List<tasks.task_info> myDataset, TaskFragment homefragment) {
        homeFragment = homefragment;
        mDataset = myDataset;
    }

    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_recycler_item_view, parent, false);

        TaskViewHolder vh = new TaskViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, final int position) {
        holder.textView.setBackgroundTintList(task_colors[0]);
        holder.TaskName.setText(mDataset.get(position).content);
        if (mDataset.get(position).state == 0) {
            holder.TaskIcon.setBackground(MainActivity.getAppContext().getResources().getDrawable(R.drawable.ic_not_complete_yellow_24dp));
        }
        else {
            holder.TaskIcon.setBackground(MainActivity.getAppContext().getResources().getDrawable(R.drawable.ic_complete_green_24dp));
        }
        holder.Date.setText("Date: " + mDataset.get(position).dates);
        holder.StartButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mDataset.get(position).state == 0) {
                    homeFragment.set_gone();
                    homeFragment.current_task_index = position;
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        int size;
        try {
            size = mDataset.size();
        }
        catch (Exception e) {
            size = 0;
        }

        return size;
    }
}
