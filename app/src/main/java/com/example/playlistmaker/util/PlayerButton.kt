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
import kotlin.math.min
class PlayerButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var imageBitmap: Bitmap?
    private var pauseButtonImage: Bitmap?
    private var playButtonImage: Bitmap?
    var onTouchListener: (() -> Unit)? = null
    private var imageRect = RectF(0f, 0f, 0f, 0f)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN-> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                onTouchListener?.invoke()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun changeButtonStatus(isPlaying: Boolean) {
        imageBitmap = if (isPlaying) playButtonImage else pauseButtonImage
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
                pauseButtonImage =
                    getDrawable(R.styleable.CustomButtonView_pausedButtonsResId)?.toBitmap()
                playButtonImage =
                    getDrawable(R.styleable.CustomButtonView_playButtonsResId)?.toBitmap()
                imageBitmap = pauseButtonImage
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
