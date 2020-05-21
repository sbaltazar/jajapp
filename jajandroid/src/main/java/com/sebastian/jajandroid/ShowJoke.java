package com.sebastian.jajandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ShowJoke extends AppCompatActivity {

    // Extra for receiving the joke from another activity
    public static final String EXTRA_JOKE = "EXTRA_JOKE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_joke);

        TextView tvJoke = findViewById(R.id.tv_joke);

        // If the joke was no found, send a message to the user
        if (getIntent() != null && getIntent().hasExtra(EXTRA_JOKE)) {
            tvJoke.setText(getIntent().getStringExtra(EXTRA_JOKE));
        } else {
            Toast.makeText(this, R.string.error_joke_not_found, Toast.LENGTH_LONG).show();
        }

    }
}
