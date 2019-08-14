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
    private var rightToLeft = true
    private var startAngle = DEFAULT_START_ANGLE
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

    var indeterminateMode = false
        set(value) {
            if (!value && field) {
                progress = 0f
            }

            field = value
            onIndeterminateModeChangeListener?.invoke(field)
            rightToLeft = true
            startAngle = DEFAULT_START_ANGLE

            indeterminateModeHandler?.removeCallbacks(indeterminateModeRunnable)
            progressAnimator?.cancel()
            indeterminateModeHandler = Handler()

            if (field) {
                indeterminateModeHandler?.post(indeterminateModeRunnable)
            }
        }

    var roundBorder = false
        set(value) {
            field = value
            foregroundPaint.strokeCap = if(field) Paint.Cap.ROUND else Paint.Cap.BUTT
        }

    var onProgressChangeListener: ((Float) -> Unit)? = null

    var onIndeterminateModeChangeListener: ((Boolean) -> Unit)? = null
    //endregion

    //region Indeterminate Mode
    private val indeterminateModeRunnable = Runnable {
        if (indeterminateMode) {
            postIndeterminateModeHandler()
            // whatever you want to do below
            this@CircularProgressBar.rightToLeft = !this@CircularProgressBar.rightToLeft
            if (this@CircularProgressBar.rightToLeft) {
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
        val realProgress = progress * DEFAULT_MAX_VALUE / progressMax
        val angle = (if (rightToLeft) 360 else -360) * realProgress / 100
        canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint)
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
        progressAnimator = ValueAnimator.ofFloat(this.progress, progress)
        progressAnimator?.duration = duration
        progressAnimator?.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            this.progress = value
            if (indeterminateMode) {
                val updateAngle = value * 360 / 100
                startAngle = DEFAULT_START_ANGLE + if (rightToLeft) updateAngle else -updateAngle
            }
        }
        progressAnimator?.start()
    }

    private fun Float.dpToPx(): Float =
            this * Resources.getSystem().displayMetrics.density

    private fun Float.pxToDp(): Float =
            this / Resources.getSystem().displayMetrics.density

}
