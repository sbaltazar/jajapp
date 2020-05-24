package com.udacity.gradle.builditbigger;

import android.support.test.InstrumentationRegistry;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.gradle.builditbigger.free.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

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
    public void getGCEResponse() throws InterruptedException {

        final CountDownLatch signal = new CountDownLatch(1);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                new MainActivity.EndpointsAsyncTask(activityRule.getActivity()) {

                    @Override
                    protected void onPostExecute(String joke) {
                        if (joke != null && !joke.isEmpty()) {
                            assertTrue(true);
                        } else {
                            fail("Error retrieving the joke. Check CGE Endpoint");
                        }
                        signal.countDown();
                    }
                }.execute();
            }
        });
        // Force Espresso to wait for asynctask to finish
        signal.await();
    }

}
