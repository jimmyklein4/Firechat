package edu.temple.tuf21842.firechat;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class MessageService extends FirebaseMessagingService {
    private final String TAG = "MessagingService";

    public MessageService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {
            String message = new java.util.Date(remoteMessage.getSentTime()) + ": " + remoteMessage.getNotification().getBody();

            //Path of log file. Should be /sdcard/Documents. Sometimes doesn't exist
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File logFile = new File(path, "log.txt");

            if(!logFile.exists()){
                try{
                    logFile.createNewFile();
                } catch(Exception e){
                    Log.d(TAG, e.toString() + "logfile exists");
                }
            }
            try{
                //Write message to log file
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                buf.append(message);
                buf.newLine();
                buf.close();
            }catch(Exception e){
                Log.d(TAG, e.toString());
            }

            Intent messageIntent = new Intent("android.intent.action.MAIN").putExtra("message", message);
            this.sendBroadcast(messageIntent);

        }

    }
}
