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
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //Log.d(TAG, new java.util.Date(remoteMessage.getSentTime()) +"");
            String message = new java.util.Date(remoteMessage.getSentTime()) + ": " + remoteMessage.getNotification().getBody();

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

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}
