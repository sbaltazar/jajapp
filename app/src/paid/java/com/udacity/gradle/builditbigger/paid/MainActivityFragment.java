package com.udacity.gradle.builditbigger.paid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.udacity.gradle.builditbigger.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    private ProgressBar spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        spinner = root.findViewById(R.id.pb_fetchingJoke);

        return root;
    }

    public void showProgressBar() {
        spinner.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        spinner.setVisibility(View.INVISIBLE);
    }
}
