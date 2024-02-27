package com.example.playlistmaker.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R
import java.lang.StrictMath.min

class PlayerButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = R.style.defaultStyle,
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var imageBitmap: Bitmap?
    private var imageBitmapPaused: Bitmap?
    private var imageBitmapPlay: Bitmap?
    var onTouchListener: (() -> Unit)? = null
    private var imageRect = RectF(0f, 0f, 0f, 0f)
    private var isPlayed = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                onTouchListener?.invoke()
                changeButtonStatus()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun changeButtonStatus() {
        isPlayed = !isPlayed
        imageBitmap = if (isPlayed) imageBitmapPlay else imageBitmapPaused
        invalidate()

    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                imageBitmapPaused =
                    getDrawable(R.styleable.CustomButtonView_pausedButtonsResId)?.toBitmap()
                imageBitmapPlay =
                    getDrawable(R.styleable.CustomButtonView_playButtonsResId)?.toBitmap()
                imageBitmap = if (isPlayed) imageBitmapPlay else imageBitmapPaused
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        if (imageBitmap != null) {
            canvas.drawBitmap(imageBitmap!!, null, imageRect, null)
        }
    }
}
