package com.example.visible2.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import mobi.upod.timedurationpicker.TimeDurationPickerDialogFragment;
import mobi.upod.timedurationpicker.TimeDurationPicker;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.example.visible2.MainActivity;
import com.example.visible2.R;
import com.example.visible2.adapter.TaskAdapter;
import com.example.visible2.json_obj.tasks;
import com.example.visible2.utils.Task;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.visible2.LoginActivity.JSON;


public class TaskFragment extends Fragment {

    private View root_view;
    private View time_counter_view;
    public TextView time_tv;
    private Button add_button;
    private Button complete_button;
    private Button startCount;
    private Button quit;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public TaskAdapter adapter;

    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;

    private final String host_getTasks = "http://kyrie.top:8888/api/task/";
    private String host_post_task_url = host_getTasks;
    private String current_id;
    private String current_content;
    private String current_date;
    public static String current_nickname;
    public static int current_credits;
    public int current_task_index;
    private Calendar c;
    private MyCountDownTimer mc;
    private HashMap<String, String> m_map = new HashMap<String, String>() {
        {
            put("1", "Jan");
            put("2", "Feb");
            put("3", "Mar");
            put("4", "Apr");
            put("5", "May");
            put("6", "June");
            put("7", "July");
            put("8", "Aug");
            put("9", "Sep");
            put("10", "Oct");
            put("11", "Nov");
            put("12", "Dec");
        }
    };

    public List<tasks.task_info> mDataset;

    public void set_gone(){
        add_button.setVisibility(GONE);
        complete_button.setVisibility(GONE);
        time_counter_view.setVisibility(VISIBLE);
        time_counter_view.invalidate();
    }

    public void set_appear() {
        add_button.setVisibility(VISIBLE);
        complete_button.setVisibility(VISIBLE);
        time_counter_view.setVisibility(GONE);
    }

    public void feed_dataset(List<tasks.task_info> dataset) {
        mDataset = dataset;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        current_id = getActivity().getIntent().getStringExtra("id");
        current_credits = getActivity().getIntent().getIntExtra("credits", 0);
        current_nickname = getActivity().getIntent().getStringExtra("nickname");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        /** load the root view (fragment_task.xml) **/
        View rootView = inflater.inflate(R.layout.fragment_task, container, false);
        root_view = rootView;
        time_tv = rootView.findViewById(R.id.show_time);
        time_counter_view = rootView.findViewById(R.id.time_counter_layout);
        time_counter_view.setVisibility(GONE);

        complete_button = rootView.findViewById(R.id.complete_button);
        add_button = rootView.findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSettingWindow(v);
            }
        });

        startCount = rootView.findViewById(R.id.duration_setting);
        startCount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis());
                int hour = 0;
                int minute = 0;
                new TimePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        c.setTimeInMillis(System.currentTimeMillis());
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.set(Calendar.SECOND, 0);
                        c.set(Calendar.MILLISECOND, 0);
                        //Toast.makeText(MainActivity.this, c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE), Toast.LENGTH_SHORT).show();
                        mc = new MyCountDownTimer(1000 * 60 * c.get(Calendar.MINUTE)+1000 * 60 *60* c.get(Calendar.HOUR_OF_DAY), 1000);//传参
                        mc.start();//获得时间传参
                    }
                }, hour, minute, true).show();
            }
        });

        quit = rootView.findViewById(R.id.stop);
        quit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mc.cancel();
                time_tv.setText("00:00");
                adapter.notifyDataSetChanged();
                set_appear();
            }
        });

        /** load the recycler view from root view **/
        recyclerView = rootView.findViewById(R.id.task_recycler_view);
        recyclerView.setHasFixedSize(true);

        /** specify the layout manager **/
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        /** specify the adapter **/
        mDataset = ((MainActivity) getActivity()).get_dataset();
        adapter = new TaskAdapter(mDataset, this);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0 
        getActivity().getWindow().setAttributes(lp);
    }


    private void initSettingWindow(View v) {
        View view =  LayoutInflater.from(MainActivity.getAppContext()).inflate(R.layout.task_setting, null, false);

        Button closeButton = view.findViewById(R.id.exit_button);
        final Button dateButton = view.findViewById(R.id.date_setting);
        Button confirmButton = view.findViewById(R.id.confirm_button);

        final TextView dateContent = view.findViewById(R.id.date_content);
        final TextView taskName = view.findViewById(R.id.task_name);

        final PopupWindow popWindow = new PopupWindow(view);
        popWindow.setWidth(1000);
        popWindow.setHeight(1000);
        popWindow.setFocusable(true);

        popWindow.showAtLocation(v, Gravity.CENTER_HORIZONTAL, 0, 200);
        backgroundAlpha(0.5f);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundAlpha(1.0f);
                popWindow.dismiss();
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar ca = Calendar.getInstance();
                int mYear = ca.get(Calendar.YEAR);
                int mMonth = ca.get(Calendar.MONTH);
                int mDay = ca.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                final String data =  m_map.get(String.valueOf(month+1)) + "-" + dayOfMonth;
                                dateContent.setText(data);
                            }
                        },
                        mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String taskname = taskName.getText().toString();
                String date = dateContent.getText().toString();
                tasks.task_info new_task = new tasks.task_info();
                current_content = taskname;
                current_date = date;
                new_task._id = current_id;
                new_task.content = taskname;
                new_task.dates = date;
                new_task.state = 0;
                mDataset.add(new_task);
                adapter.notifyDataSetChanged();
                backgroundAlpha(1.0f);
                popWindow.dismiss();
                new Thread(post_task).start();
            }
        });
    }


    public Runnable post_task = new Runnable() {
        @Override
        public void run() {
            String response_string = "";
            JSONObject login = new JSONObject();
            try {
                login.put("owner", current_id);
                login.put("content", current_content);
                login.put("dates", current_date);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            OkHttpClient okHttpClient = new OkHttpClient();
            String json = login.toString();
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(host_post_task_url)
                    .post(requestBody)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    response_string = response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler handler_time = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.d("cal", val);
            time_tv.setText(val);
        }
    };

    public Runnable put_task = new Runnable() {
        @Override
        public void run() {
            String response_string = "";
            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody build = new FormBody.Builder().build();

            Request request = new Request.Builder()
                    .url(host_getTasks + mDataset.get(current_task_index)._id)
                    .put(build)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    response_string = response.body().string();
                    Log.d("put", response_string);
                }
            } catch (IOException e) {
                Log.d("put", "fail");
                e.printStackTrace();
            }
        }
    };


    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            mDataset.get(current_task_index).state = 1;
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", "Done!");
            msg.setData(data);
            handler_time.sendMessage(msg);
            current_credits ++;
            new Thread(put_task).start();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", getTime(millisUntilFinished));
            msg.setData(data);
            handler_time.sendMessage(msg);
        }

        public String getTime(long time) {
            int totalSeconds = (int) (time / 1000);
            int seconds = totalSeconds % 60;
            int minutes = (totalSeconds / 60) % 60;
            int hours = totalSeconds / 3600;
            return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
        }
    }
}
