package com.example.visible2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LoginActivity extends AppCompatActivity {

    private ImageView bookIconImageView;
    private TextView bookITextView;
    private ProgressBar loadingProgressBar;
    private RelativeLayout rootView, afterAnimationView;

    private Button logButton;
    private TextInputEditText userNameEdit;
    private TextInputEditText passWordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getCheckFromServer("g", "G");
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        initViews();

        new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                rootView.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.colorBackground));
                bookIconImageView.setImageResource(R.drawable.ic_flash_on_yellow_48dp);
            }

            @Override
            public void onFinish() {
                bookIconImageView.setVisibility(GONE);
                loadingProgressBar.setVisibility(GONE);
                bookITextView.setVisibility(GONE);
                afterAnimationView.setVisibility(VISIBLE);
            }
        }.start();
    }

    private void initViews() {
        bookIconImageView = findViewById(R.id.bookIconImageView);
        bookITextView = findViewById(R.id.bookITextView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        rootView = findViewById(R.id.login_activity);
        afterAnimationView = findViewById(R.id.afterAnimationView);
        logButton = findViewById(R.id.loginButton);

        userNameEdit = findViewById(R.id.emailEditText);
        passWordEdit = findViewById(R.id.passwordEditText);

        logButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if (getCheckFromServer(userNameEdit.getText().toString(), passWordEdit.getText().toString())){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    //startActivity(intent);
                }
            }
        });
    }

    public boolean getCheckFromServer (String username, String password) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.7.209.104:8888/api/user";

        RequestBody requestBody=new FormBody.Builder()
                .add("oldPassword","111111")
                .add("newPassword","111111")
                .build();

        Request request = new Request.Builder()
                                    .url(url)
                                    .post(requestBody)
                                    .addHeader("content-type", "application/json")
                                    .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showWarnSweetDialog("服务器错误");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (res.equals("0"))
                        {
                            showWarnSweetDialog("无此账号,请先注册");
                        }
                        else if(res.equals("1"))
                        {
                            showWarnSweetDialog("密码不正确");
                        }
                        else{
                            showWarnSweetDialog(res);
                        }
                    }
                });
            }
        });
        return true;
    }

    public boolean registFromServer (String username, String password) {
        return true;
    }

    private void showWarnSweetDialog(String info)
    {
        SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(info);
        pDialog.setCancelable(true);
        pDialog.show();
    }
}
