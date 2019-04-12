package com.example.visible2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.example.visible2.fragment.MeFragment;
import com.example.visible2.fragment.RankFragment;
import com.example.visible2.fragment.TaskFragment;

public class MainActivity extends AppCompatActivity  {

    private Fragment currentFragment = new Fragment();
    private Fragment taskFragment = new TaskFragment();
    private Fragment rankFragment = new RankFragment();
    private Fragment meFragment = new MeFragment();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        currentFragment = new TaskFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, currentFragment, null).commit();
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
