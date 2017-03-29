package edu.temple.tuf21842.firechat;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MainActivity extends Activity {

    private IntentFilter intentFilter;
    private BroadcastReceiver messageReceiver;
    private LinearLayout messageBody;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        messageBody = (LinearLayout) findViewById(R.id.message_body);

        //Read messages from log file if they're there.
        //Log file writing is handled in MessageService
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File logFile = new File(path, "log.txt");
        if(logFile.exists()){
            try {
                FileReader file = new FileReader(logFile.getAbsoluteFile());
                BufferedReader reader = new BufferedReader(file);
                String line;
                while((line = reader.readLine())!=null){
                    TextView message = new TextView(getApplicationContext());
                    message.setText(line);
                    messageBody.addView(message);
                }
                reader.close();

            } catch(Exception e){
                Log.d(TAG, e.toString());
            }
        }
        intentFilter = new IntentFilter("android.intent.action.MAIN");
    }

    @Override
    protected void onResume(){
        super.onResume();
        messageBody = (LinearLayout) findViewById(R.id.message_body);
        messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Intent from MessageService for receiving a message 
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
