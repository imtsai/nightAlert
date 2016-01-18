package com.example.testing.nightalert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class setMessage extends AppCompatActivity {
    private static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_message);
    }
    public void gaveMessage(View view) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        EditText message = (EditText) findViewById(R.id.optMessage);
        editor.putString("message", message.getText().toString());
        editor.commit();
        Intent intent = new Intent(this, startTimer.class);
        startActivity(intent);
    }

    public void noMessage(View view) {
        Intent intent = new Intent(this, startTimer.class);
        startActivity(intent);
    }

}
