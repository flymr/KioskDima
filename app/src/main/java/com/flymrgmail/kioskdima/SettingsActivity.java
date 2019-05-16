package com.flymrgmail.kioskdima;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    private ScreenOfReceiver screenOfReceiver;
    private PreferencesHelper preferencesHelper;
    @BindView(R.id.et_url)
    EditText etUrl;
    @BindView(R.id.et_pass)
    EditText etPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        preferencesHelper = new PreferencesHelper(this);

        screenOfReceiver = new ScreenOfReceiver();
        registerReceiver(screenOfReceiver, screenOfReceiver.getFilter());

        etUrl.setText(preferencesHelper.getUrl());
        etPass.setText(preferencesHelper.getPass());
    }

    @OnClick(R.id.btn_done)
    public void onDoneClick(){
        preferencesHelper.saveUrl(etUrl.getText().toString());
        preferencesHelper.savePassword(etPass.getText().toString());
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @OnClick(R.id.fl_exit)
    public void onExitClick(){
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(event.getKeyCode() ) {
            case KeyEvent.KEYCODE_MENU:
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                return true;
            case KeyEvent.KEYCODE_BACK:
                return true;
            case KeyEvent.KEYCODE_POWER:
                Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                sendBroadcast(closeDialog);
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        //active apps button
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) {
            Intent closeDialog =
                    new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenOfReceiver);
    }
}
