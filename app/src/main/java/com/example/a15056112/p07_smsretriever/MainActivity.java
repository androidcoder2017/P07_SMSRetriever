package com.example.a15056112.p07_smsretriever;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment f1 = new FragmentActivity1();
        ft.replace(R.id.frame1, f1);

        Fragment f2 = new FragmentActivity2();
        ft.replace(R.id.frame2, f2);

        ft.commit();
    }
}
