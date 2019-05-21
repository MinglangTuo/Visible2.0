package com.example.visible2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangePass extends Activity {
        private AbsoluteLayout re;
        private Button confirm_button;
        private EditText forpass;
        private EditText newpass;
        private EditText confirmpass;

        private TextView wrongpass;
        private TextView wrongconfirm;

        boolean formerpasswordfail = true;
        boolean comfirmfail = true;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_changepass);

            initView();
        }

        private void initView() {
            forpass = findViewById(R.id.forpass);
            newpass = findViewById(R.id.newpass);
            confirmpass = findViewById(R.id.confirmpass);
            confirm_button = findViewById(R.id.confirm_button);
            wrongpass = findViewById(R.id.wrongpass);
            wrongconfirm = findViewById(R.id.wrongcomfirm);

            re = findViewById(R.id.re);
            re.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            confirm_button.setOnClickListener(new MyClickListener());
        }

        class MyClickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                String formerpassword = forpass.getText().toString();
                String newpassword = newpass.getText().toString();
                String confirmpassword = confirmpass.getText().toString();

                confirm();

                if(formerpasswordfail){
                    wrongpass.setText("Password is wrong");
                    return;
                }

                if(comfirmfail){
                    wrongconfirm.setText("Entered passwords differ! ");
                    return;
                }
            }
        }

        public  void confirm(){
        }

        public boolean getCheckFromServer(String username, String password) {
            OkHttpClient client = new OkHttpClient();
            String url = "http://10.7.209.104:8888/api/user";

            RequestBody requestBody = new FormBody.Builder()
                    .add("oldPassword", "111111")
                    .add("newPassword", "111111")
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWarnSweetDialog("服务器错误");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String res = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (res.equals("0")) {
                                showWarnSweetDialog("无此账号,请先注册");
                            } else if (res.equals("1")) {
                                showWarnSweetDialog("密码不正确");
                            } else {
                                showWarnSweetDialog(res);
                            }
                        }
                    });
                }
            });
            return true;
        }

        private void showWarnSweetDialog(String info) {
            SweetAlertDialog pDialog = new SweetAlertDialog(ChangePass.this, SweetAlertDialog.WARNING_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText(info);
            pDialog.setCancelable(true);
            pDialog.show();
        }
}

