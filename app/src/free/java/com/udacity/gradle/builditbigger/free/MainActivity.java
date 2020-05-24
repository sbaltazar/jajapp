package com.udacity.gradle.builditbigger.free;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.sebastian.jajandroid.ShowJoke;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void tellJoke(View view) {

        new EndpointsAsyncTask(this).execute();

        // showJoke(this, JajaTeller.tellJajavaJoke());
        //Toast.makeText(this, JajaTeller.tellJajavaJoke(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Mehtod for showing a joke by starting a new activity
     *
     * @param joke Joke provided
     */
    public static void showJoke(Context context, String joke) {

        Intent intent = new Intent(context, ShowJoke.class);
        intent.putExtra(ShowJoke.EXTRA_JOKE, joke);

        context.startActivity(intent);
    }

    public static class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {
        private MyApi myApiService = null;
        private WeakReference<Context> wrContext; // To avoid leaking context

        public EndpointsAsyncTask(Context context) {
            wrContext = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            if (wrContext != null) {
                FragmentManager fragmentManager = ((AppCompatActivity) wrContext.get()).getSupportFragmentManager();
                MainActivityFragment fragment = (MainActivityFragment) fragmentManager.findFragmentById(R.id.fragment);
                fragment.showProgressBar();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            if (myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        //.setRootUrl("http://10.0.2.2:8080/_ah/api/") // This is when using emulator
                        .setRootUrl("http://192.168.31.173:8080/_ah/api/") // This is when using physical android device
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver

                myApiService = builder.build();
            }

            try {
                // Geting the joke from the API call
                return myApiService.sayJoke().execute().getData();
            } catch (IOException e) {
                Log.e("MainActivity", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String joke) {
            if (wrContext != null) {
                // Hiding the progress bar
                FragmentManager fragmentManager = ((AppCompatActivity) wrContext.get()).getSupportFragmentManager();
                MainActivityFragment fragment = (MainActivityFragment) fragmentManager.findFragmentById(R.id.fragment);
                fragment.hideProgressBar();

                if (joke == null || joke.isEmpty()) {
                    Toast.makeText(wrContext.get(), R.string.error_retrieving_joke, Toast.LENGTH_SHORT).show();
                } else {
                    // Start a new activity from the Android library
                    showJoke(wrContext.get(), joke);
                }
            }
        }
    }


}
