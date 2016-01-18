package com.example.testing.nightalert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class initSettings extends AppCompatActivity {
    private static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_settings);
    }

    public void promptMessage(View view) {
        Map<String, EditText> validate = new HashMap<String, EditText>();
        validate.put("startLocation", (EditText) findViewById(R.id.startLocation));
        validate.put("destination", (EditText) findViewById(R.id.destination));
        validate.put("travelTime", (EditText) findViewById(R.id.travelTime));
        validate.put("chosenContact", (EditText) findViewById(R.id.chosenContact));
        validate.put("pin", (EditText) findViewById(R.id.pin));

        // check each editText has text before storing into shared preferneces
        String warning = "You did not enter a ";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        for (String key : validate.keySet()) {
            EditText val = validate.get(key);
            String text = val.getText().toString();
            if (text.matches("")) {
                Toast.makeText(this, warning + key, Toast.LENGTH_SHORT).show();
                return;
            } else {
                editor.putString(key, text);
                editor.putString(key+"Saved",text);
                editor.commit();
            }
        }

        //switch to message activity
        Intent intent = new Intent(this, setMessage.class);
        startActivity(intent);
    }

    public void useSaved(View view) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String startLocationSaved = settings.getString("startLocationSaved", "");
        String destinationSaved = settings.getString("destinationSaved", "");
        String travelTimeSaved = settings.getString("travelTimeSaved", "");
        String chosenContactSaved = settings.getString("chosenContactSaved", "");
        String pinSaved = settings.getString("pinSaved", "");

        Map<String, String> validate = new HashMap<String, String>();
        validate.put("startLocationSaved", startLocationSaved);
        validate.put("destinationSaved", destinationSaved);
        validate.put("travelTimeSaved", travelTimeSaved);
        validate.put("chosenContactSaved",  chosenContactSaved);
        validate.put("pinSaved", pinSaved);

        for (String key : validate.keySet()) {
            String val = validate.get(key);
            if (val.matches("")) {
                Toast.makeText(getApplicationContext(), "no saved settings, please reenter", Toast.LENGTH_LONG).show();
                return;
            }
        }
        EditText temp = (EditText) findViewById(R.id.startLocation);
        temp.setText(validate.get("startLocation"), EditText.BufferType.EDITABLE);
        temp = (EditText) findViewById(R.id.destination);
        temp.setText(validate.get("destination"), EditText.BufferType.EDITABLE);
        temp =(EditText) findViewById(R.id.travelTime);
        temp.setText(validate.get("travelTime"), EditText.BufferType.EDITABLE);
        temp =(EditText) findViewById(R.id.chosenContact);
        temp.setText(validate.get("chosenContact"), EditText.BufferType.EDITABLE);
        temp =(EditText) findViewById(R.id.pin);
        temp.setText(validate.get("pin"), EditText.BufferType.EDITABLE);
        SharedPreferences.Editor editor = settings.edit();
        for (String key : validate.keySet()) {
            String val = validate.get(key);
            String newKey = key.replace("Saved","");
            editor.putString(key, val);
            editor.putString(newKey, val);
            editor.commit();
        }
        Intent intent = new Intent(this, setMessage.class);
        startActivity(intent);
    }
}
