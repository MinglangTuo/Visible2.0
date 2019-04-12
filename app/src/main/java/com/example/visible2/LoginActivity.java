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
                    startActivity(intent);
                }
            }
        });
    }

    public boolean getCheckFromServer (String username, String password) {
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
