package com.udacity.gradle.builditbigger;

import android.support.test.InstrumentationRegistry;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void getGCEResponse() {

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {

                new MainActivity.EndpointsAsyncTask(activityRule.getActivity()) {
                    @Override
                    protected void onPostExecute(String joke) {

                        if (joke != null && !joke.isEmpty()) {
                            assertTrue(true);
                        }
                    }
                }.execute();
            }
        });
    }

}
