package com.example.visible2.myapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.visible2.R;


public class BottomDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private int layoutResID;

    /**
     * 要监听的控件id
     */
    private int[] listenedItems;

    private OnBottomMenuItemClickListener listener;

    public BottomDialog(FragmentActivity context, int layoutResID, int[] listenedItems) {
        super(context, R.style.dialog_custom);
        this.context = context;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.bottom_menu_animation); // 添加动画
        setContentView(layoutResID);
        WindowManager windowManager = ((Activity) context).getWindowManager(); // 宽度全屏
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth()*9/10; // 设置宽度
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true); // 点击Dialog外部消失
        for (int id : listenedItems) {
            findViewById(id).setOnClickListener(this);
        }
    }

    public interface OnBottomMenuItemClickListener {

        void onBottomMenuItemClick(BottomDialog dialog, View view);

    }

    public void setOnBottomMenuItemClickListener(OnBottomMenuItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        dismiss();
        listener.onBottomMenuItemClick(this, view);
    }

}
