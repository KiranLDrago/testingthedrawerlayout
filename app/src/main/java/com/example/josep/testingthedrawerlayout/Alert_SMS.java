package com.example.josep.testingthedrawerlayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class Alert_SMS extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver1;
    private String Fix;
    private String latitude;
    private String longitude;
    private TinyDB tinyDB;
    private String speed;
    String test;
    private ArrayList<String> arrayList,numbersOnly;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert__sms);
        TextView textView = findViewById(R.id.textView3);
       final SmsManager smsManager = SmsManager.getDefault();
        tinyDB=new TinyDB(getApplicationContext());

        arrayList= tinyDB.getListString("NumbersOnly");

        if (broadcastReceiver1 == null) {
            broadcastReceiver1 = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    Fix = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).get("Fix")).toString();    // Data is sent as a string seperated by commas
                    String[] GPS_data = Fix.split(",");
                    latitude = GPS_data[0];
                    longitude = GPS_data[1];
                    speed= GPS_data[2];

                }
            };
        }
        registerReceiver(broadcastReceiver1, new IntentFilter("location_update"));


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                for(int i=0;i<tinyDB.getListString("NumbersOnly").size();i++) {
                    smsManager.sendTextMessage(arrayList.get(i), null, "Help! I've met with an accident at http://maps.google.com/?q="+latitude+"," + longitude, null, null);
                }
                System.exit(1);

            }
        }, 10000);


        Button dismiss = (Button) findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(1);
            }
        });


    }


}
