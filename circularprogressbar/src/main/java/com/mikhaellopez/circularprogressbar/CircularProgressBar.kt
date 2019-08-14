package com.mikhaellopez.circularprogressbar

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

/**
 * Copyright (C) 2019 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 */
class CircularProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    companion object {
        private const val DEFAULT_MAX_VALUE = 100f
        private const val DEFAULT_START_ANGLE = 270f
        private const val DEFAULT_ANIMATION_DURATION = 1500L
    }

    // Properties
    private var progressAnimator: ValueAnimator? = null
    private var indeterminateModeHandler: Handler? = null

    // View
    private var rectF = RectF()
    private var backgroundPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    private var foregroundPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    //region Attributes
    var progress: Float = 0f
        set(value) {
            field = if (progress <= progressMax) value else progressMax
            onProgressChangeListener?.invoke(progress)
            invalidate()
        }

    var progressMax: Float = DEFAULT_MAX_VALUE
        set(value) {
            field = if (field >= 0) value else DEFAULT_MAX_VALUE
            invalidate()
        }

    var progressBarWidth: Float = resources.getDimension(R.dimen.default_stroke_width)
        set(value) {
            field = value.dpToPx()
            foregroundPaint.strokeWidth = field
            requestLayout()
            invalidate()
        }

    var backgroundProgressBarWidth: Float = resources.getDimension(R.dimen.default_background_stroke_width)
        set(value) {
            field = value.dpToPx()
            backgroundPaint.strokeWidth = field
            requestLayout()
            invalidate()
        }

    var color: Int = Color.BLACK
        set(value) {
            field = value
            foregroundPaint.color = field
            invalidate()
        }

    var backgroundProgressBarColor: Int = Color.GRAY
        set(value) {
            field = value
            backgroundPaint.color = field
            invalidate()
        }

    var roundBorder = false
        set(value) {
            field = value
            foregroundPaint.strokeCap = if (field) Paint.Cap.ROUND else Paint.Cap.BUTT
            invalidate()
        }

    var startAngle: Float = DEFAULT_START_ANGLE
        set(value) {
            var angle = value + DEFAULT_START_ANGLE
            while (angle > 360) {
                angle -= 360
            }
            field = if (angle < 0) 0f else if (angle > 360) 360f else angle
            invalidate()
        }

    var progressDirection: ProgressDirection = ProgressDirection.TO_RIGHT
        set(value) {
            field = value
            invalidate()
        }

    var indeterminateMode = false
        set(value) {
            field = value
            onIndeterminateModeChangeListener?.invoke(field)
            progressIndeterminateMode = 0f
            progressDirectionIndeterminateMode = ProgressDirection.TO_RIGHT
            startAngleIndeterminateMode = DEFAULT_START_ANGLE

            indeterminateModeHandler?.removeCallbacks(indeterminateModeRunnable)
            progressAnimator?.cancel()
            indeterminateModeHandler = Handler()

            if (field) {
                indeterminateModeHandler?.post(indeterminateModeRunnable)
            }
        }

    var onProgressChangeListener: ((Float) -> Unit)? = null

    var onIndeterminateModeChangeListener: ((Boolean) -> Unit)? = null
    //endregion

    //region Indeterminate Mode
    private var progressIndeterminateMode: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var progressDirectionIndeterminateMode: ProgressDirection = ProgressDirection.TO_RIGHT
        set(value) {
            field = value
            invalidate()
        }
    private var startAngleIndeterminateMode: Float = DEFAULT_START_ANGLE
        set(value) {
            field = value
            invalidate()
        }

    private val indeterminateModeRunnable = Runnable {
        if (indeterminateMode) {
            postIndeterminateModeHandler()
            // whatever you want to do below
            this@CircularProgressBar.progressDirectionIndeterminateMode = this@CircularProgressBar.progressDirectionIndeterminateMode.reverse()
            if (this@CircularProgressBar.progressDirectionIndeterminateMode.isToRight()) {
                setProgressWithAnimation(0f)
            } else {
                setProgressWithAnimation(progressMax)
            }
        }
    }

    private fun postIndeterminateModeHandler() {
        indeterminateModeHandler?.postDelayed(indeterminateModeRunnable, DEFAULT_ANIMATION_DURATION)
    }
    //endregion

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        // Load the styled attributes and set their properties
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar, 0, 0)

        // Value
        progress = attributes.getFloat(R.styleable.CircularProgressBar_cpb_progress, progress)
        progressMax = attributes.getFloat(R.styleable.CircularProgressBar_cpb_progress_max, progressMax)
        // Indeterminate Mode
        indeterminateMode = attributes.getBoolean(R.styleable.CircularProgressBar_cpb_indeterminate_mode, indeterminateMode)
        // StrokeWidth
        progressBarWidth = attributes.getDimension(R.styleable.CircularProgressBar_cpb_progressbar_width, progressBarWidth).pxToDp()
        backgroundProgressBarWidth = attributes.getDimension(R.styleable.CircularProgressBar_cpb_background_progressbar_width, backgroundProgressBarWidth).pxToDp()
        // Color
        color = attributes.getInt(R.styleable.CircularProgressBar_cpb_progressbar_color, color)
        backgroundProgressBarColor = attributes.getInt(R.styleable.CircularProgressBar_cpb_background_progressbar_color, backgroundProgressBarColor)
        // Round Border
        roundBorder = attributes.getBoolean(R.styleable.CircularProgressBar_cpb_round_border, roundBorder)
        // Progress Direction
        val progressDirectionIntValue = attributes.getInteger(R.styleable.CircularProgressBar_cpb_progress_direction, progressDirection.value)
        progressDirection = ProgressDirection.fromValue(progressDirectionIntValue)
        // Angle
        startAngle = attributes.getFloat(R.styleable.CircularProgressBar_cpb_start_angle, 0f)

        attributes.recycle()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        progressAnimator?.cancel()
        indeterminateModeHandler?.removeCallbacks(indeterminateModeRunnable)
    }

    //region Draw Method
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawOval(rectF, backgroundPaint)
        val realProgress = (if (indeterminateMode) progressIndeterminateMode else progress) * DEFAULT_MAX_VALUE / progressMax
        val angle = (if ((indeterminateMode && progressDirectionIndeterminateMode.isToRight())
                || (!indeterminateMode && progressDirection.isToRight())) 360 else -360) * realProgress / 100

        canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else startAngle, angle, false, foregroundPaint)
    }

    override fun setBackgroundColor(backgroundColor: Int) {
        backgroundProgressBarColor = backgroundColor
    }
    //endregion

    //region Measure Method
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val min = min(width, height)
        setMeasuredDimension(min, min)
        val highStroke = if (progressBarWidth > backgroundProgressBarWidth) progressBarWidth else backgroundProgressBarWidth
        rectF.set(0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2, min - highStroke / 2)
    }
    //endregion

    /**
     * Set the progress with animation.
     *
     * @param progress The progress it should animate to it.
     * @param duration The length of the animation, in milliseconds.
     */
    @JvmOverloads
    fun setProgressWithAnimation(progress: Float, duration: Long = DEFAULT_ANIMATION_DURATION) {
        progressAnimator?.cancel()
        progressAnimator = ValueAnimator.ofFloat(if (indeterminateMode) progressIndeterminateMode else this.progress, progress)
        progressAnimator?.duration = duration
        progressAnimator?.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            if (indeterminateMode) progressIndeterminateMode = value else this.progress = value
            if (indeterminateMode) {
                val updateAngle = value * 360 / 100
                startAngleIndeterminateMode = DEFAULT_START_ANGLE +
                        if (progressDirectionIndeterminateMode.isToRight()) updateAngle else -updateAngle
            }
        }
        progressAnimator?.start()
    }

    private fun Float.dpToPx(): Float =
            this * Resources.getSystem().displayMetrics.density

    private fun Float.pxToDp(): Float =
            this / Resources.getSystem().displayMetrics.density

    enum class ProgressDirection(val value: Int) {
        TO_RIGHT(1),
        TO_LEFT(2);

        companion object {
            fun fromValue(value: Int): ProgressDirection =
                    when (value) {
                        1 -> TO_RIGHT
                        2 -> TO_LEFT
                        else -> throw IllegalArgumentException("This value is not supported for ShadowGravity: $value")
                    }
        }

        fun reverse(): ProgressDirection = if (this.isToRight()) TO_LEFT else TO_RIGHT

        fun isToRight(): Boolean = this == TO_RIGHT
    }

}
