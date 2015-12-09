package com.mikhaellopez.circleprogressbarsample;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import com.larswerkman.lobsterpicker.OnColorListener;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class MainActivity extends AppCompatActivity {

    private CircularProgressBar circularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressbar);
        circularProgressBar.setProgressWithAnimation(65);

        // PROGRESS
        ((SeekBar) findViewById(R.id.seekBarProgress)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                circularProgressBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // BORDER
        ((SeekBar) findViewById(R.id.seekBarStrokeWidth)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                circularProgressBar.setProgressBarWidth(progress * getResources().getDisplayMetrics().density);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // BACKGROUND BORDER
        ((SeekBar) findViewById(R.id.seekBarBackgroundStrokeWidth)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                circularProgressBar.setBackgroundProgressBarWidth(progress * getResources().getDisplayMetrics().density);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // COLOR
        ((LobsterShadeSlider) findViewById(R.id.shadeslider)).addOnColorListener(new OnColorListener() {
            @Override
            public void onColorChanged(@ColorInt int color) {
                circularProgressBar.setColor(color);
                circularProgressBar.setBackgroundColor(adjustAlpha(color, 0.3f));
            }

            @Override
            public void onColorSelected(@ColorInt int color) {
            }
        });
    }

    /**
     * Transparent the given color by the factor
     * The more the factor closer to zero the more the color gets transparent
     *
     * @param color  The color to transparent
     * @param factor 1.0f to 0.0f
     * @return int - A transplanted color
     */
    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.github:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/lopspower/CircularProgressBar")));
                return true;
            case R.id.beer:
                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.pay_me_a_beer))
                        .setMessage(getResources().getString(R.string.offer_me_a_beer))
                        .setPositiveButton(getResources().getString(android.R.string.ok).toUpperCase(), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.me/LopezMikhael")));
                            }
                        })
                        .setNegativeButton(getResources().getString(android.R.string.cancel).toUpperCase(), null)
                        .show();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
