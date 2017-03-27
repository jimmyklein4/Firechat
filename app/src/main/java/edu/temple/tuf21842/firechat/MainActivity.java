package edu.temple.tuf21842.firechat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends Activity {

    private IntentFilter intentFilter;
    private BroadcastReceiver messageReceiver;
    private File logFile;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logFile = new File("sdcard/log.txt");
        if(!logFile.exists()){
            try{
                logFile.createNewFile();
            } catch(Exception e){
                Log.d(TAG, e.toString());
            }
        }
        intentFilter = new IntentFilter("android.intent.action.MAIN");
    }

    @Override
    protected void onResume(){
        super.onResume();
        final LinearLayout messageBody = (LinearLayout) findViewById(R.id.message_body);
        messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String receivedMessage = intent.getStringExtra("message");
                TextView message = new TextView(getApplicationContext());
                message.setText(receivedMessage);
                messageBody.addView(message);
            }
        };
        this.registerReceiver(messageReceiver, intentFilter);
    }

    @Override
    protected void onPause(){
        super.onPause();
        this.unregisterReceiver(this.messageReceiver);
    }

}
