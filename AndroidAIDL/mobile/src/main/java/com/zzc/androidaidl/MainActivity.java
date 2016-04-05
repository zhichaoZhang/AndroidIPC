package com.zzc.androidaidl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 *
 * Created by zczhang on 16/4/2.
 */
public class MainActivity extends AppCompatActivity {
    private Button btnMessenger;
    private Button btnBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btnMessenger = (Button) findViewById(R.id.btn_messenger);
        btnMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, IPCMessengerActivity.class));
            }
        });

        btnBinder = (Button) findViewById(R.id.btn_binder);
        btnBinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, IPCBinderActivity.class));
            }
        });
    }
}

