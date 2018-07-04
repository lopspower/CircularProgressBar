package com.mikhaellopez.circularprogressbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * Copyright (C) 2018 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 */
public class CircularProgressBar extends View {

    private static final float DEFAULT_MAX_VALUE = 100;
    private static final float DEFAULT_START_ANGLE = 270;
    private static final int DEFAULT_ANIMATION_DURATION = 1500;

    // Properties
    private float progress = 0;
    private float progressMax = DEFAULT_MAX_VALUE;
    private float strokeWidth = getResources().getDimension(R.dimen.default_stroke_width);
    private float backgroundStrokeWidth = getResources().getDimension(R.dimen.default_background_stroke_width);
    private int color = Color.BLACK;
    private int backgroundColor = Color.GRAY;
    private boolean rightToLeft = true;
    private boolean indeterminateMode = false;
    private float startAngle = DEFAULT_START_ANGLE;
    private ProgressChangeListener progressChangeListener;
    private IndeterminateModeChangeListener indeterminateModeChangeListener;
    private ValueAnimator progressAnimator;
    private Handler indeterminateModeHandler;

    // View
    private RectF rectF;
    private Paint backgroundPaint;
    private Paint foregroundPaint;

    //region Constructor & Init Method
    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        rectF = new RectF();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularProgressBar, 0, 0);
        //Reading values from the XML layout
        try {
            // Value
            progress = typedArray.getFloat(R.styleable.CircularProgressBar_cpb_progress, progress);
            progressMax = typedArray.getFloat(R.styleable.CircularProgressBar_cpb_progress_max, progressMax);
            // Indeterminate Mode
            indeterminateMode = typedArray.getBoolean(R.styleable.CircularProgressBar_cpb_indeterminate_mode, indeterminateMode);
            // StrokeWidth
            strokeWidth = typedArray.getDimension(R.styleable.CircularProgressBar_cpb_progressbar_width, strokeWidth);
            backgroundStrokeWidth = typedArray.getDimension(R.styleable.CircularProgressBar_cpb_background_progressbar_width, backgroundStrokeWidth);
            // Color
            color = typedArray.getInt(R.styleable.CircularProgressBar_cpb_progressbar_color, color);
            backgroundColor = typedArray.getInt(R.styleable.CircularProgressBar_cpb_background_progressbar_color, backgroundColor);
        } finally {
            typedArray.recycle();
        }

        // Init Background
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(backgroundStrokeWidth);

        // Init Foreground
        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setColor(color);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(strokeWidth);
    }
    //endregion

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (indeterminateMode) enableIndeterminateMode(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (progressAnimator != null) progressAnimator.cancel();
        if (indeterminateModeHandler != null) indeterminateModeHandler.removeCallbacks(indeterminateModeRunnable);
    }

    //region Draw Method
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(rectF, backgroundPaint);
        float realProgress = progress * DEFAULT_MAX_VALUE / progressMax;
        float angle = (rightToLeft ? 360 : -360) * realProgress / 100;
        canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint);
    }

    private void reDraw() {
        requestLayout();//Because it should recalculate its bounds
        invalidate();
    }
    //endregion

    //region Mesure Method
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        float highStroke = strokeWidth > backgroundStrokeWidth ? strokeWidth : backgroundStrokeWidth;
        rectF.set(0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2, min - highStroke / 2);
    }
    //endregion

    //region Method Get/Set
    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        setProgress(progress, false);
    }

    private void setProgress(float progress, boolean fromAnimation) {
        if (!fromAnimation && progressAnimator != null) {
            progressAnimator.cancel();
            if (indeterminateMode) enableIndeterminateMode(false);
        }
        this.progress = progress <= progressMax ? progress : progressMax;
        if (progressChangeListener != null) progressChangeListener.onProgressChanged(progress);
        invalidate();
    }

    public float getProgressMax() {
        return progressMax;
    }

    public void setProgressMax(float progressMax) {
        this.progressMax = progressMax >= 0 ? progressMax : DEFAULT_MAX_VALUE;
        reDraw();
    }

    public float getProgressBarWidth() {
        return strokeWidth;
    }

    public void setProgressBarWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        foregroundPaint.setStrokeWidth(strokeWidth);
        reDraw();
    }

    public float getBackgroundProgressBarWidth() {
        return backgroundStrokeWidth;
    }

    public void setBackgroundProgressBarWidth(float backgroundStrokeWidth) {
        this.backgroundStrokeWidth = backgroundStrokeWidth;
        backgroundPaint.setStrokeWidth(backgroundStrokeWidth);
        reDraw();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        foregroundPaint.setColor(color);
        reDraw();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        backgroundPaint.setColor(backgroundColor);
        reDraw();
    }
    //endregion

    //region Progress Animation

    /**
     * Set the progress with an animation with 1500 by default duration.
     *
     * @param progress The progress it should animate to it.
     */
    public void setProgressWithAnimation(float progress) {
        setProgressWithAnimation(progress, DEFAULT_ANIMATION_DURATION);
    }

    /**
     * Set the progress with an animation.
     *
     * @param progress The progress it should animate to it.
     * @param duration The length of the animation, in milliseconds.
     */
    public void setProgressWithAnimation(float progress, int duration) {
        if (progressAnimator != null) {
            progressAnimator.cancel();
        }
        progressAnimator = ValueAnimator.ofFloat(this.progress, progress);
        progressAnimator.setDuration(duration);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (Float) animation.getAnimatedValue();
                setProgress(progress, true);
                if (indeterminateMode) {
                    float updateAngle = progress * 360 / 100;
                    startAngle = DEFAULT_START_ANGLE + (rightToLeft ? updateAngle : -updateAngle);
                }
            }
        });
        progressAnimator.start();
    }
    //endregion

    //region Indeterminate Mode
    private Runnable indeterminateModeRunnable = new Runnable() {
        @Override
        public void run() {
            if (indeterminateMode) {
                indeterminateModeHandler.postDelayed(indeterminateModeRunnable, DEFAULT_ANIMATION_DURATION);
                // whatever you want to do below
                CircularProgressBar.this.rightToLeft = !CircularProgressBar.this.rightToLeft;
                if (CircularProgressBar.this.rightToLeft) {
                    setProgressWithAnimation(0);
                } else {
                    setProgressWithAnimation(progressMax);
                }
            }
        }
    };

    public void enableIndeterminateMode(boolean enable) {
        indeterminateMode = enable;
        if (indeterminateModeChangeListener != null) indeterminateModeChangeListener.onModeChange(indeterminateMode);
        rightToLeft = true;
        startAngle = DEFAULT_START_ANGLE;

        if (indeterminateModeHandler != null) indeterminateModeHandler.removeCallbacks(indeterminateModeRunnable);
        if (progressAnimator != null) progressAnimator.cancel();
        indeterminateModeHandler = new Handler();

        if (indeterminateMode) {
            indeterminateModeHandler.post(indeterminateModeRunnable);
        } else {
            setProgress(0, true);
        }
    }
    //endregion

    //region Listener
    public void setOnProgressChangedListener(ProgressChangeListener listener) {
        progressChangeListener = listener;
    }

    public void setOnIndeterminateModeChangeListener(IndeterminateModeChangeListener listener) {
        indeterminateModeChangeListener = listener;
    }

    public interface ProgressChangeListener {
        void onProgressChanged(float progress);
    }

    public interface IndeterminateModeChangeListener {
        void onModeChange(boolean isEnable);
    }
    //endregion

}
