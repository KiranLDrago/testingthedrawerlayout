package com.example.josep.testingthedrawerlayout;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;

import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BroadcastReceiver broadcastReceiver;
    private final String GPS_serviceName = "com.example.josep.testingthedrawerlayout.GPS_Service";            // To search if service is already running
    private final String Shake_serviceName = "com.example.josep.testingthedrawerlayout.ShakeMonitor_Service";
    private TextView latitude;
    private TextView longitude;
    private TextView speed;
    private String Fix;

    @Override
    protected void onResume() {

        super.onResume();

        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    Fix = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).get("Fix")).toString();    // Data is sent as a string seperated by commas
                    String[] GPS_data = Fix.split(",");
                    latitude.setText( "Latitude: " + GPS_data[0]);
                    longitude.setText( "Longitude: " + GPS_data[1]);
                    speed.setText(GPS_data[2]);

                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));

    }
        @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitude= (TextView) findViewById(R.id.textView2);
        longitude= (TextView) findViewById(R.id.textView3);
        speed = (TextView)findViewById(R.id.textView4);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(isServiceRunning(GPS_serviceName))
        {

        }
        else {
            Intent i = new Intent(getApplicationContext(), GPS_Service.class);
            startService(i);
        }
        if(isServiceRunning(Shake_serviceName)){

        }
        else{
            Intent j = new Intent(getApplicationContext(), ShakeMonitor_Service.class);
            startService(j);

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent backtocontacts=new Intent(MainActivity.this,Contact.class);
            startActivity(backtocontacts);
        } else if (id == R.id.nav_gallery) {
            Intent i=new Intent(MainActivity.this,Alert_SMS.class);
            startActivity(i);

        } else if (id == R.id.nav_slideshow) {

        }
// else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isServiceRunning(String name){
        ActivityManager manager= (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
         if(name.equals(service.service.getClassName())){
             return true;

         }

        }
        return false;
    }

    @Override
    protected void onDestroy() {


        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }

        super.onDestroy();
    }



}
