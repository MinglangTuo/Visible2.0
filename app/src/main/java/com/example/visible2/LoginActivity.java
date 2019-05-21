package com.example.visible2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.visible2.json_obj.tasks;
import com.example.visible2.json_obj.user_info;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
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
    private Button registerButton;   //注册界面
    private Button register2Button;   //
    private Button Back2;      //
    private Button forgetPasswordButton;  //
    private Button forgetButton;
    private Button ensure;  //

    private TextInputEditText userName;
    private String userNameString;
    private TextInputEditText passWord;
    private String passWordString;

    private TextInputEditText r_userNameEdit;
    private String r_userNameEditString;
    private TextInputEditText r_passWordEdit;
    private String r_passWordEditString;

    private ConstraintLayout RegisterPage;
    private ConstraintLayout forgetPasswordPage;   //

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final String host = "http://kyrie.top:8888/";
    private final String host_register = host + "api/user";
    private final String host_login = "http://kyrie.top:8888/api/user/login";
    private final String host_getTasks = "http://kyrie.top:8888/api/task/";
    private final String host_getTasks_test = "http://kyrie.top:8888/api/task/5ccd5e5593011e5364ba23f4";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Gson gson = new Gson();
            try{
            user_info current_user = gson.fromJson(val, user_info.class);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("nickname", current_user.get_user_info().nickname);
            intent.putExtra("credits", current_user.get_user_info().credits);
            intent.putExtra("id", current_user.get_user_info()._id);
            startActivity(intent);}
            catch (Exception e){
                showWarnSweetDialog("Log in fail!");
            }
        }
    };


    private Handler handler_register = new Handler(){     //
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.d("registerback", val);
            showSuccessSweetDialog("Success!");
        }
    };


    public Runnable login = new Runnable() {
        @Override
        public void run() {
            String nickname = userName.getText().toString();
            String password = passWord.getText().toString();
            String response_string = "";
            JSONObject login = new JSONObject();
            try {
                login.put("nickname", nickname);
                login.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            OkHttpClient okHttpClient = new OkHttpClient();
            String json = login.toString();
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(host_login)
                    .post(requestBody)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    response_string = response.body().string();
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", response_string);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public Runnable getRegister = new Runnable(){  //

        @Override
        public void run() { String response_string = "";
            JSONObject register = new JSONObject();
            String username = r_userNameEdit.getText().toString();
            String password = r_passWordEdit.getText().toString();
            try {
                register.put("nickname", username);
                register.put("password", password);
                register.put("email", username + "@xjtlu.edu.cn");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            OkHttpClient okHttpClient = new OkHttpClient();
            String json = register.toString();
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(host_register)
                    .post(requestBody)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    response_string = response.body().string();
                    Log.d("asad",response_string);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", response_string);
                    msg.setData(data);
                    handler_register.sendMessage(msg);

                }
            } catch (IOException e) {
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

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
                forgetPasswordPage.setVisibility(GONE);  //

            }
        }.start();
    }

    private void initViews() {
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Simplehandwritting-Regular.ttf");
        bookIconImageView = findViewById(R.id.bookIconImageView);
        bookITextView = findViewById(R.id.bookITextView);
        bookITextView.setTypeface(typeFace);

        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        rootView = findViewById(R.id.login_activity);
        afterAnimationView = findViewById(R.id.afterAnimationView);
        logButton = findViewById(R.id.loginButton);


        registerButton = findViewById(R.id.register_button);  //



        userName = findViewById(R.id.emailEditText);
        passWord = findViewById(R.id.passwordEditText);

        r_userNameEdit = findViewById(R.id.r_username);
        r_passWordEdit = findViewById(R.id.r_password);

        forgetPasswordPage = findViewById(R.id.forgetPassword_page); //
        forgetPasswordButton = findViewById(R.id.forget_button);  //
        forgetButton = findViewById(R.id.r_back_forgetPassWord);  //
        ensure = findViewById(R.id.r_ensure);  //

        RegisterPage = findViewById(R.id.register_page);
        register2Button = findViewById(R.id.r_register);    //
        Back2 = findViewById(R.id.r_back);   //





        forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //
                afterAnimationView.setVisibility(GONE);
                RegisterPage.setVisibility(GONE);
                forgetPasswordPage.setVisibility(VISIBLE);

            }
        } );

        forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {       //忘记密码之后后端传输数据给邮箱
                //new Thread(getRegister).start();
            }
        });


        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {               //
                forgetPasswordPage.setVisibility(GONE);
                afterAnimationView.setVisibility(VISIBLE);
                RegisterPage.setVisibility(GONE);
            }
        });



        register2Button.setOnClickListener(new View.OnClickListener() {  //
            @Override
            public void onClick(View view) {
                new Thread(getRegister).start();                //上传数据
            }
        });

        Back2.setOnClickListener(new View.OnClickListener(){    //

            @Override
            public void onClick(View view) {                  //
                forgetPasswordPage.setVisibility(GONE);
                afterAnimationView.setVisibility(VISIBLE);
                RegisterPage.setVisibility(GONE);
            }
        });





        registerButton.setOnClickListener(new View.OnClickListener() {//
            @Override
            public void onClick(View view) {           //
                afterAnimationView.setVisibility(GONE);
                RegisterPage.setVisibility(VISIBLE);


            }
        });   //registerButton



        logButton.setOnClickListener(new View.OnClickListener(){ //
            public void onClick(View v) {
                new Thread(login).start();
            }
        });
    }



    private void showSuccessSweetDialog(String info)
    {
        SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(info);
        pDialog.setCancelable(true);
        pDialog.show();
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
