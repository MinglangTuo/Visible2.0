package com.example.visible2.utils;

import android.os.CountDownTimer;
import android.util.Log;

public class MyCountDownTimer extends CountDownTimer {

    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onFinish() {
    }//结束之后方法

    @Override
    public void onTick(long millisUntilFinished) {//做的时候
        Log.i("MainActivity", millisUntilFinished + "");
        Log.d("timespend", getTime(millisUntilFinished));
    }

    public static String getTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

}
