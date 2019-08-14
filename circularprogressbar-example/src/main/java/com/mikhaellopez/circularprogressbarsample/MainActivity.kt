package com.mikhaellopez.circularprogressbarsample

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.larswerkman.lobsterpicker.OnColorListener
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        circularProgressBar.setProgressWithAnimation(65f)
        seekBarProgress.onProgressChanged { progress, fromUser -> if(fromUser) circularProgressBar.progress = progress }
        seekBarStrokeWidth.onProgressChanged { value, _ -> circularProgressBar.progressBarWidth = value }
        seekBarBackgroundStrokeWidth.onProgressChanged { value, _ -> circularProgressBar.backgroundProgressBarWidth = value }
        shadeslider.onColorChanged {
            circularProgressBar.color = it
            circularProgressBar.backgroundProgressBarColor = adjustAlpha(it, 0.3f)
        }

        // INDETERMINATE MODE
        switchIndeterminateMode.setOnCheckedChangeListener { _, isChecked -> circularProgressBar.indeterminateMode = isChecked }
        circularProgressBar.onIndeterminateModeChangeListener = { switchIndeterminateMode.isChecked = it }
    }

    //region Extensions
    private fun SeekBar.onProgressChanged(onProgressChanged: (Float, Boolean) -> Unit) {
        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                onProgressChanged(progress.toFloat(), fromUser)
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
    //endregion

    /**
     * Transparent the given color by the factor
     * The more the factor closer to zero the more the color gets transparent
     *
     * @param color  The color to transparent
     * @param factor 1.0f to 0.0f
     * @return int - A transplanted color
     */
    private fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).roundToInt()
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

}