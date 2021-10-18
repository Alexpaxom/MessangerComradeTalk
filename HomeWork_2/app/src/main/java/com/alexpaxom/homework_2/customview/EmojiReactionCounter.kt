package com.alexpaxom.homework_2.customview

import android.content.Context
import android.graphics.*
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.helpers.NumAbbreviationFormatter
import kotlinx.android.parcel.Parcelize

class EmojiReactionCounter  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    // Строка с символом отображаемого юникода
    var displayEmoji:String = DEFAULT_EMOJI
        set(value) {
            field = value
            requestLayout()
        }

    // Количество рекцицй
    var countReaction: Int = DEFAULT_COUNT_REACTIONS
        set(value) {
            field = value
            stringPresentationCount = NumAbbreviationFormatter.convertNumToAbbreviation(value)
            requestLayout()
        }

    private var stringPresentationCount = DEFAULT_STRING_PRESENTATION_COUNT

    // Минимальные расстояние между смойлом и количеством
    // Если места для текста больше его ширины он будет отцентрован в свободном пространстве
    var separatorWith = DEFAULT_SEPARATOR_WIDTH
        set(value) {
            field = value
            requestLayout()
        }


    private val emojiBounds = Rect()
    private val emojiCoordinate = PointF()

    private val countReactionTextBounds = Rect()
    private val countTextCoordinate = PointF()
    private val emojiPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val countReactionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }

    private val emojiFontMetrics = Paint.FontMetrics()
    private val countReactionFontMetrics = Paint.FontMetrics()

    init {
        with(context.obtainStyledAttributes(attrs, R.styleable.EmojiReactionCounter)) {
            displayEmoji = getString(R.styleable.EmojiReactionCounter_emojiUnicode) ?: DEFAULT_EMOJI
            countReaction = getInt(R.styleable.EmojiReactionCounter_countReactions, DEFAULT_COUNT_REACTIONS)

            separatorWith = getDimensionPixelSize(
                R.styleable.EmojiReactionCounter_separatorSize,
                DEFAULT_SEPARATOR_WIDTH
            )

            emojiPaint.textSize = getDimensionPixelSize(
                R.styleable.EmojiReactionCounter_emojiTextSize,
                DEFAULT_EMOJI_TEXT_SIZE
            ).toFloat()

            countReactionPaint.textSize = getDimensionPixelSize(
                R.styleable.EmojiReactionCounter_android_textSize,
                DEFAULT_COUNT_REACTION_TEXT_SIZE
            ).toFloat()

            countReactionPaint.color = getColor(
                R.styleable.EmojiReactionCounter_android_textColor,
                DEFAULT_COUNT_REACTION_TEXT_COLOR
            )


            recycle()
        }

        setOnClickListener{} // Устанавливаем дефолтный слушатель
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        emojiPaint.getTextBounds(displayEmoji, 0, displayEmoji.length, emojiBounds)

        countReactionPaint.getTextBounds(
            stringPresentationCount,
            0, stringPresentationCount.length,
            countReactionTextBounds
        )

        var totalWidth = emojiBounds.width() + countReactionTextBounds.width() + separatorWith + paddingRight + paddingLeft
        var totalHeight = maxOf(emojiBounds.height(), countReactionTextBounds.height()) + paddingTop + paddingBottom

        totalWidth = maxOf(totalWidth, minimumWidth)
        totalHeight = maxOf(totalHeight, minimumHeight)

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


        countTextCoordinate.x = (w-separatorWith)/2f + (w-separatorWith)/4f
        countTextCoordinate.y = h/2f + countReactionTextBounds.height()/2f
    }

    override fun onDraw(canvas: Canvas) {
        // Отдельно рисуем смайлик и количество реакций
        canvas.drawText(displayEmoji, emojiCoordinate.x, emojiCoordinate.y, emojiPaint)
        canvas.drawText (
            stringPresentationCount,
            countTextCoordinate.x,
            countTextCoordinate.y,
            countReactionPaint
        )
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + SUPPORTED_DRAWABLE_STATE.size)
        if (isSelected) {
            mergeDrawableStates(drawableState, SUPPORTED_DRAWABLE_STATE)
        }
        return drawableState
    }

    override fun onSaveInstanceState(): Parcelable {
        return State(isSelected, displayEmoji, countReaction, super.onSaveInstanceState())
    }

    override fun onRestoreInstanceState(state: Parcelable?) {

        with(state as State) {
            isSelected = isSelectedState
            displayEmoji = displayEmojiState
            countReaction = countReactionState
            super.onRestoreInstanceState(superState)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        val onClick = OnClickListener { v ->
            countReaction += if(isSelected) -1 else 1
            countReaction = if(countReaction >= 0) countReaction else 0
            isSelected = !isSelected

            l?.onClick(v)
        }

        super.setOnClickListener(onClick)
    }

    companion object {
        private const val DEFAULT_EMOJI = "\uD83D\uDE36"
        private const val DEFAULT_COUNT_REACTIONS = 1
        private const val DEFAULT_STRING_PRESENTATION_COUNT = "1"
        private const val DEFAULT_SEPARATOR_WIDTH = 10
        private const val DEFAULT_EMOJI_TEXT_SIZE = 45
        private const val DEFAULT_COUNT_REACTION_TEXT_SIZE = 40
        private const val DEFAULT_COUNT_REACTION_TEXT_COLOR = Color.WHITE
        private val SUPPORTED_DRAWABLE_STATE = intArrayOf(android.R.attr.state_selected)
    }

    @Parcelize
    data class State (val isSelectedState:Boolean,
                       val displayEmojiState:String,
                       val countReactionState:Int,
                        val superState:Parcelable? ): Parcelable

}