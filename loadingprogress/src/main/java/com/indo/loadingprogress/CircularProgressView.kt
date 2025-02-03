package com.indo.loadingprogress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.annotation.ColorInt

class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progressColor: Int = Color.BLUE
    private var strokeWidth: Float = 8f
    private var rotationDuration: Long = 1000
    private var isIndeterminate: Boolean = true
    private var progress: Float = 0f

    private val paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private val rectF = RectF()
    private var rotateAnimation: RotateAnimation? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CircularProgressView,
            0, 0
        ).apply {
            try {
                progressColor = getColor(R.styleable.CircularProgressView_progressColor, Color.BLUE)
                strokeWidth = getDimension(R.styleable.CircularProgressView_strokeWidth, 8f)
                rotationDuration = getInteger(R.styleable.CircularProgressView_rotationDuration, 1000).toLong()
                isIndeterminate = getBoolean(R.styleable.CircularProgressView_isIndeterminate, true)
            } finally {
                recycle()
            }
        }

        setupPaint()
        if (isIndeterminate) {
            startRotationAnimation()
        }
    }

    private fun setupPaint() {
        paint.apply {
            color = progressColor
            strokeWidth = this@CircularProgressView.strokeWidth
            strokeCap = Paint.Cap.ROUND
        }
    }

    private fun startRotationAnimation() {
        rotateAnimation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = rotationDuration
            interpolator = LinearInterpolator()
            repeatCount = Animation.INFINITE
        }
        startAnimation(rotateAnimation)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = strokeWidth / 2
        rectF.set(padding, padding, w - padding, h - padding)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isIndeterminate) {
            canvas.drawArc(rectF, 0f, 270f, false, paint)
        } else {
            canvas.drawArc(rectF, -90f, progress * 360f, false, paint)
        }
    }

    fun setProgress(value: Float) {
        if (!isIndeterminate) {
            progress = value.coerceIn(0f, 1f)
            invalidate()
        }
    }

    fun setIndeterminate(indeterminate: Boolean) {
        if (this.isIndeterminate != indeterminate) {
            this.isIndeterminate = indeterminate
            if (indeterminate) {
                startRotationAnimation()
            } else {
                clearAnimation()
            }
            invalidate()
        }
    }

    fun setProgressColor(@ColorInt color: Int) {
        progressColor = color
        paint.color = color
        invalidate()
    }

    fun setStrokeWidth(width: Float) {
        strokeWidth = width
        paint.strokeWidth = width
        invalidate()
    }

    fun setRotationDuration(duration: Long) {
        rotationDuration = duration
        rotateAnimation?.duration = duration
    }

    fun restartAnimation() {
        if (isIndeterminate) {
            clearAnimation()
            startRotationAnimation()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearAnimation()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isIndeterminate) {
            startRotationAnimation()
        }
    }
}