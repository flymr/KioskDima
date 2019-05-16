package com.flymrgmail.kioskdima;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ScreenOfReceiver screenOfReceiver;
    private int backBtnPress = 0;
    private PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesHelper = new PreferencesHelper(this);
        //setup webview
        WebView theWebPage = new WebView(this);
        theWebPage.getSettings().setJavaScriptEnabled(true);
        theWebPage.getSettings().setPluginState(WebSettings.PluginState.ON);
        setContentView(theWebPage);
        theWebPage.loadUrl(preferencesHelper.getUrl());
        theWebPage.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //screen of broadcast receiver
        screenOfReceiver = new ScreenOfReceiver();
        registerReceiver(screenOfReceiver, screenOfReceiver.getFilter());


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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(event.getKeyCode() ) {
            case KeyEvent.KEYCODE_MENU:
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                backBtnPress = 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("HANDLER", "RUN");
                        backBtnPress = 0;
                    }
                }, 3000);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
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
    public void onBackPressed() {
        if (1 <= backBtnPress)
            backBtnPress += 1;
        if (backBtnPress == 5){
            if (!preferencesHelper.getPass().equals("")){
                showPasswordDialog();
            } else {
                openSettingActivity();
            }

        }
    }

    private void openSettingActivity(){
        startActivity(new Intent(this, SettingsActivity.class));
        finish();
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showPasswordDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setTitle("enter a password");

        alert.setView(edittext);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(edittext.getText().toString().equals(preferencesHelper.getPass())){
                    openSettingActivity();
                } else {
                    showToast("Invalid Password");
                }
            }
        });

        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });
        alert.setCancelable(false);
        alert.show();
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

    //    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
//            Log.i("", "Dispath event power");
//            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//            sendBroadcast(closeDialog);
//            return true;
//        }
//
//        return super.dispatchKeyEvent(event);
//    }
}
