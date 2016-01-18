package com.example.testing.nightalert;

import java.util.concurrent.TimeUnit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;



public class startTimer extends AppCompatActivity implements LocationListener {
    private LocationManager locationManager;
    boolean done = false;
    boolean unsent = true;
    private TextView text1;
    private static final String FORMAT = "%02d:%02d:%02d";
    int seconds, minutes;
    private static final String PREFS_NAME = "MyPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_timer);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1f, this);
        text1 = (TextView) findViewById(R.id.timer);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        int eta = Integer.parseInt(settings.getString("travelTime", "0"));
        if (eta != 0) {
            int time = eta * 60000;
            new CountDownTimer(time, 1000) { // adjust the milli seconds here
                public void onTick(long millisUntilFinished) {
                    if (done || !unsent) {
                        return;
                    }
                    text1.setText("" + String.format(FORMAT,
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }

                public void onFinish() {
                    if (unsent && !done) {
                        unsent = false;
                        text1.setText("Alert Sent!");
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        String phoneNo = settings.getString("chosenContact", "4085688056");
                        String message = settings.getString("message", "");
                        String start = settings.getString("startLocation", "");
                        String end = settings.getString("destination", "");
                        message += "\nintended to travel from " + start + " to " + end + "\n";
                        String geoloc = settings.getString("geoloc", "");
                        message += geoloc;
                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phoneNo, null, message, null, null);
                            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        return;
                    }
                }
            }.start();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = "New Latitude: " + location.getLatitude()
                + "New Longitude: " + location.getLongitude() + "\n";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String geoloc = settings.getString("geoloc", "");
        if (geoloc.matches("")) {
            geoloc = msg;
        } else {
            geoloc += msg;
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("geoloc", geoloc);
            editor.commit();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ", Toast.LENGTH_SHORT).show();
    }

    public void cancelTimer(View view) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        int truePin = Integer.parseInt(settings.getString("pin", ""));
        EditText fakePin = (EditText) findViewById(R.id.fakePin);
        String checkPin = fakePin.getText().toString();
        if (checkPin.matches("" + truePin)) {
            done = true;
            Intent intent = new Intent(this, endScreen.class);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "incorrect pin ", Toast.LENGTH_SHORT).show();
        }
    }

    public void callCops(View view) {
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
//        phoneIntent.setData(Uri.parse("tel:911"));
        phoneIntent.setData(Uri.parse("tel:408-568-8056"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(phoneIntent);

    }
}
