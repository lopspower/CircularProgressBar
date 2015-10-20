package com.mikhaellopez.circleprogressbarsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class MainActivity extends AppCompatActivity {

    private CircularProgressBar secondCircularProgressBar;
    private CircularProgressBar thirdCircularProgressBar;
    private CircularProgressBar fourthCircularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        // First init
        initCircularProgressBar();

        findViewById(R.id.buttonReload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseCircularProgressBar();
                initCircularProgressBar();
            }
        });
    }

    private void initCircularProgressBar() {
        // FIRST
        // Basic behavior

        // SECOND
        secondCircularProgressBar = (CircularProgressBar)findViewById(R.id.second_circular_progressbar);
        secondCircularProgressBar.setProgressWithAnimation(65);

        // THIRD
        thirdCircularProgressBar = (CircularProgressBar)findViewById(R.id.third_circular_progressbar);
        thirdCircularProgressBar.setProgressWithAnimation(85, 2500);

        // FOURTH
        fourthCircularProgressBar = (CircularProgressBar)findViewById(R.id.fourth_circular_progressbar);
        fourthCircularProgressBar.setProgressWithAnimation(30, 1000);
    }

    private void releaseCircularProgressBar() {
        secondCircularProgressBar.setProgress(0);
        thirdCircularProgressBar.setProgress(0);
        fourthCircularProgressBar.setProgress(0);
    }

}
