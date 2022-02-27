package com.example.custominbox;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.inbox.CTInboxMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements CTInboxListener {
    Button DefaultInbox,AllMessage,FirstMessage,UnreadMessageCount,ReadMessage,DeleteMessage;
    TextView Text1;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
//        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);
        CleverTapAPI cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(this);
        CleverTapAPI.setDebugLevel(3);

        if (cleverTapDefaultInstance != null) {
            //Set the Notification Inbox Listener
            cleverTapDefaultInstance.setCTNotificationInboxListener(this);
            //Initialize the inbox and wait for callbacks on overridden methods
            cleverTapDefaultInstance.initializeInbox();
        }
        CleverTapAPI.createNotificationChannel(getApplicationContext(),"abtest","abtest","test ab", NotificationManager.IMPORTANCE_MAX,true);
//        HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
//        profileUpdate.put("Name", "Abeezer Golwala");    // String
//        profileUpdate.put("Identity", 123);      // String or number
//        profileUpdate.put("Email", "abeezer@android.com"); // Email address of the user
//        profileUpdate.put("Phone", "+918800000000");   // Phone (with the country code, starting with +)
//        profileUpdate.put("Gender", "M");             // Can be either M or F
//        profileUpdate.put("DOB", new Date());         // Date of Birth. Set the Date object to the appropriate value first
//        // optional fields. controls whether the user will be sent email, push etc.
//        profileUpdate.put("MSG-email", true);        // Disable email notifications
//        profileUpdate.put("MSG-push", true);          // Enable push notifications
//        profileUpdate.put("MSG-sms", true);          // Disable SMS notifications
//        profileUpdate.put("MSG-whatsapp", true);      // Enable WhatsApp notifications
//        clevertapDefaultInstance.onUserLogin(profileUpdate);
        Text1 = findViewById(R.id.text1);
        findViewById(R.id.dfin).setOnClickListener(view -> {
            clevertapDefaultInstance.showAppInbox();
        });
        // Sending event to dashboard for receiving a message
        findViewById(R.id.getmsg).setOnClickListener(view -> {
            clevertapDefaultInstance.pushEvent("Abeezergetmsg");});
        //Displaying the first messge
        findViewById(R.id.sm).setOnClickListener(v->{
            ArrayList<CTInboxMessage> test =  clevertapDefaultInstance.getAllInboxMessages();
            String Fmsg= test.get(0).getMessageId();
            Text1.setText("Title :"+
                    clevertapDefaultInstance.getInboxMessageForId(Fmsg).getInboxMessageContents().get(0).getTitle()
                    +"\nMessage " + test.get(0).getInboxMessageContents().get(0).getMessage()+"\n");
        });
        //Displaying Count of all messages
        findViewById(R.id.cnt).setOnClickListener(v->{
            Text1.setText("All Message Count is "+clevertapDefaultInstance.getInboxMessageCount());});
        //Displaying Count of unread message
        findViewById(R.id.unct).setOnClickListener(v->{
            Text1.setText("Unread Message Count is "+clevertapDefaultInstance.getInboxMessageUnreadCount());});
        //Displaying all messages
        findViewById(R.id.btci).setOnClickListener(v->{
            ArrayList<CTInboxMessage> test =  clevertapDefaultInstance.getAllInboxMessages();
            String allmsg= "";
            for(int i = 0;i<clevertapDefaultInstance.getInboxMessageCount();i++) {
                allmsg += "Inbox Message "+ (i+1) +"\nTitle "+ test.get(i).getInboxMessageContents().get(0).getTitle()
                        +"\nMessage " + test.get(i).getInboxMessageContents().get(0).getMessage()+"\n";
            }
            Text1.setText(allmsg);
        });
        //deleting first message
        findViewById(R.id.dlm).setOnClickListener(v->{
            ArrayList<CTInboxMessage> test =  clevertapDefaultInstance.getAllInboxMessages();
            String Fmsg= test.get(0).getMessageId();
            clevertapDefaultInstance.deleteInboxMessage(Fmsg);
            Text1.setText("msg id: "+Fmsg+" mark as deleted");
        });
        //Marking first un read message as read
        findViewById(R.id.msgread).setOnClickListener(v->{
            ArrayList<CTInboxMessage> test =  clevertapDefaultInstance.getUnreadInboxMessages();
            String Fmsg= test.get(0).getMessageId();
            clevertapDefaultInstance.markReadInboxMessage(Fmsg);
            Text1.setText("msg id: "+Fmsg+" mark as read");
        });
        //Displaying all unread messages
        findViewById(R.id.unreadall).setOnClickListener(v->{
            ArrayList<CTInboxMessage> test =  clevertapDefaultInstance.getUnreadInboxMessages();
            String unreadmsg= "";
            for(int i = 0;i<clevertapDefaultInstance.getInboxMessageUnreadCount();i++) {
                unreadmsg += "Inbox Message "+ i +"\nTitle "+test.get(i).getInboxMessageContents().get(0).getTitle()
                        +"\nMessage " + test.get(i).getInboxMessageContents().get(0).getMessage()+"\n";
            }
            Text1.setText(unreadmsg);
        });
    }
    @Override
    public void inboxDidInitialize() { }
    @Override
    public void inboxMessagesDidUpdate() { }
}