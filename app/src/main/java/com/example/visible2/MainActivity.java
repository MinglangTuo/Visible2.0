package com.example.visible2;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import com.example.visible2.json_obj.ranks;
import com.example.visible2.json_obj.tasks;
import com.example.visible2.utils.Task;
import com.google.gson.Gson;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.example.visible2.fragment.MeFragment;
import com.example.visible2.fragment.RankFragment;
import com.example.visible2.fragment.TaskFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity  {

    private Fragment currentFragment = new Fragment();
    private Fragment taskFragment;
    private Fragment rankFragment;
    private Fragment meFragment;

    private final String host_getTasks = "http://kyrie.top:8888/api/task/";
    private final String rank_url = "http://kyrie.top:8888/api/user/rank";
    private String host_getTasks_url;
    private String current_id;
    private String current_nickname;
    private int current_credits;

    private static Context context;

    public List<tasks.task_info> mDataset;
    public List<ranks.rank_info> rDataset;
    

    private Handler handler_tasks = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Gson gson = new Gson();
            mDataset = gson.fromJson(val, tasks.class).get_task();
            TaskFragment frg = (TaskFragment) getSupportFragmentManager().findFragmentByTag(taskFragment.getClass().getName());
            frg.feed_dataset(mDataset);
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        }
    };

    public Runnable get_tasks = new Runnable() {
        @Override
        public void run() {
            String response_string = "";
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(host_getTasks_url)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    response_string = response.body().string();
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", response_string);
                    msg.setData(data);
                    handler_tasks.sendMessage(msg);
                } else {
                    Log.d("myapp", "fail");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public Runnable get_rank = new Runnable() {
        @Override
        public void run() {
            String response_string = "";
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(rank_url)
                    .get()
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    response_string = response.body().string();
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", response_string);
                    msg.setData(data);
                    handler_rank.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler handler_rank = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Gson gson = new Gson();
            rDataset = gson.fromJson(val, ranks.class).get_rank();
            //Log.d("check", String.valueOf(rDataset.size()));
            //RankFragment frg = (RankFragment) getSupportFragmentManager().findFragmentByTag(rankFragment.getClass().getName());
            //frg.feed_dataset(rDataset);
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchFragment(taskFragment);
                    return true;
                case R.id.navigation_dashboard:
                    switchFragment(rankFragment);
                    return true;
                case R.id.navigation_notifications:
                    switchFragment(meFragment);
                    return true;
            }
            return false;
        }
    };

    public List<tasks.task_info> get_dataset(){
        return mDataset;
    }
    public List<ranks.rank_info> get_rDataset() {return rDataset;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.context = getApplicationContext();
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        current_id = getIntent().getStringExtra("id");
        current_credits = getIntent().getIntExtra("credits", 0);
        current_nickname = getIntent().getStringExtra("nickname");

        host_getTasks_url = host_getTasks + current_id;

        taskFragment = new TaskFragment();
        rankFragment = new RankFragment();
        meFragment = new MeFragment();
        currentFragment = taskFragment;

        Thread thread = new Thread(get_tasks);
        thread.start();
        new Thread(get_rank).start();

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, currentFragment, taskFragment.getClass().getName()).commit();
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public static Context getAppContext() {
        return MainActivity.context;
    }


    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()){
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction
                    .add(R.id.fragment, targetFragment, targetFragment.getClass().getName())
                    .commit();
        } else {
            transaction
                    .hide(currentFragment)
                    .show(targetFragment)
                    .commit();
        }
        currentFragment = targetFragment;
    }
}
