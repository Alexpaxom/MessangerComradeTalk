package com.alexpaxom.homework_2.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class EmojiReactionCounter  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    var displayEmoji = "\uD83D\uDE36"
        set(value) {
            field = value
            requestLayout()
        }

    var countReaction = 10
        set(value) {
            field = value
            requestLayout()
        }

    var spaceSeparator = 20
        set(value) {
            field = value
            requestLayout()
        }


    private val emojiBounds = Rect()
    private val emojiCoordinate = PointF()

    private val countReactionTextBounds = Rect()
    private val countTextCoordinate = PointF()

    private val emojiPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 50f
    }

    private val countReactionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        textSize = 45f
    }

    private val emojiFontMetrics = Paint.FontMetrics()
    private val countReactionFontMetrics = Paint.FontMetrics()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        emojiPaint.getTextBounds(displayEmoji, 0, displayEmoji.length, emojiBounds)

        countReactionPaint.getTextBounds(
            countReaction.toString(),
            0, countReaction.toString().length,
            countReactionTextBounds
        )

        val totalWidth = emojiBounds.width() + countReactionTextBounds.width() + spaceSeparator + paddingRight + paddingLeft
        val totalHeight = maxOf(emojiBounds.height(), countReactionTextBounds.height()) + paddingTop + paddingBottom

        val resultViewWidth = resolveSize(totalWidth, widthMeasureSpec)
        val resultViewHeight = resolveSize(totalHeight, heightMeasureSpec)

        setMeasuredDimension(resultViewWidth, resultViewHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        emojiPaint.getFontMetrics(emojiFontMetrics)
        countReactionPaint.getFontMetrics(countReactionFontMetrics)

        // Приходится отнимать emojiBounds.left потому при отрисовке(canvas.drawText) весь текст
        // смещается вправо при этом обрезается - на сколько я понял это связано
        // с особенностями шрифта и расчета границ. Решение проблемы и описание нашел задесь:
        // https://stackoverflow.com/questions/5714600/gettextbounds-in-android/14766372#14766372
        emojiCoordinate.x = -emojiBounds.left + paddingLeft.toFloat()

        // здесь мы просто центруем по высоте поэтому emojiBounds.top в этом случае не нужно отнимать
        emojiCoordinate.y = h / 2f + emojiBounds.height()/2f - emojiFontMetrics.descent

        // Приходится отнимать emojiBounds.left потому при отрисовке(canvas.drawText) весь текст
        // смещается вправо при этом обрезается - на сколько я понял это связано
        // с особенностями шрифта и расчета границ. Решение проблемы и описание нашел задесь:
        // https://stackoverflow.com/questions/5714600/gettextbounds-in-android/14766372#14766372
        countTextCoordinate.x = -countReactionTextBounds.left + paddingLeft + emojiBounds.width() + spaceSeparator.toFloat()
        // здесь мы просто центруем по высоте поэтому emojiBounds.top в этом случае не нужно отнимать
        countTextCoordinate.y = h/2f + countReactionTextBounds.height()/2f
    }

    override fun onDraw(canvas: Canvas) {
        // Отдельно рисуем смайлик и количество реакций
        canvas.drawText(displayEmoji, emojiCoordinate.x, emojiCoordinate.y, emojiPaint)
        canvas.drawText(countReaction.toString(), countTextCoordinate.x, countTextCoordinate.y, countReactionPaint)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + SUPPORTED_DRAWABLE_STATE.size)
        if (isSelected) {
            mergeDrawableStates(drawableState, SUPPORTED_DRAWABLE_STATE)
        }
        return drawableState
    }


    companion object {
        private val SUPPORTED_DRAWABLE_STATE = intArrayOf(android.R.attr.state_selected)
    }

}