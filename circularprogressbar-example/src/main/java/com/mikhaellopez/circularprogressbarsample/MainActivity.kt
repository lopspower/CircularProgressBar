package com.mikhaellopez.circularprogressbarsample

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.larswerkman.lobsterpicker.OnColorListener
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val circularProgressBar = findViewById<CircularProgressBar>(R.id.circularProgressBar)

        // Set Init progress with animation
        circularProgressBar.setProgressWithAnimation(65f, 1000) // =1s

        // Update circularProgressBar
        findViewById<SeekBar>(R.id.seekBarProgress).onProgressChanged {
            circularProgressBar.progress = it
        }
        findViewById<SeekBar>(R.id.seekBarStartAngle).onProgressChanged {
            circularProgressBar.startAngle = it
        }
        findViewById<SeekBar>(R.id.seekBarStrokeWidth).onProgressChanged {
            circularProgressBar.progressBarWidth = it
        }
        findViewById<SeekBar>(R.id.seekBarBackgroundStrokeWidth).onProgressChanged {
            circularProgressBar.backgroundProgressBarWidth = it
        }
        findViewById<LobsterShadeSlider>(R.id.shadeSlider).onColorChanged {
            circularProgressBar.progressBarColor = it
            circularProgressBar.backgroundProgressBarColor = adjustAlpha(it, 0.3f)
        }
        findViewById<SwitchCompat>(R.id.switchRoundBorder).onCheckedChange {
            circularProgressBar.roundBorder = it
        }
        findViewById<SwitchCompat>(R.id.switchProgressDirection).onCheckedChange {
            circularProgressBar.progressDirection =
                if (it) CircularProgressBar.ProgressDirection.TO_RIGHT
                else CircularProgressBar.ProgressDirection.TO_LEFT
        }

        // Indeterminate Mode
        val switchIndeterminateMode = findViewById<SwitchCompat>(R.id.switchIndeterminateMode)
        switchIndeterminateMode.onCheckedChange { circularProgressBar.indeterminateMode = it }
        circularProgressBar.onIndeterminateModeChangeListener =
            { switchIndeterminateMode.isChecked = it }
    }

    //region Extensions
    private fun SeekBar.onProgressChanged(onProgressChanged: (Float) -> Unit) {
        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                onProgressChanged(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Nothing
            }
        })
    }

    private fun LobsterShadeSlider.onColorChanged(onColorChanged: (Int) -> Unit) {
        addOnColorListener(object : OnColorListener {
            override fun onColorChanged(color: Int) {
                onColorChanged(color)
            }

            override fun onColorSelected(color: Int) {
                // Nothing
            }
        })
    }

    private fun SwitchCompat.onCheckedChange(onCheckedChange: (Boolean) -> Unit) {
        setOnCheckedChangeListener { _, isChecked -> onCheckedChange(isChecked) }
    }
    //endregion

    /**
     * Transparent the given progressBarColor by the factor
     * The more the factor closer to zero the more the progressBarColor gets transparent
     *
     * @param color  The progressBarColor to transparent
     * @param factor 1.0f to 0.0f
     * @return int - A transplanted progressBarColor
     */
    private fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).roundToInt()
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

}