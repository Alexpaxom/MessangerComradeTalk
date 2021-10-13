package com.alexpaxom.homework_2.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.*
import com.alexpaxom.homework_2.R

inline fun View.getWidthWithMargins(): Int = measuredWidth + marginLeft + marginRight
inline fun View.getHeightWithMargins(): Int = measuredHeight + marginTop + marginBottom

inline fun View.layoutByNeighbors(left: View?, top: View?, addToLeft:Int = 0, addToTop: Int = 0) {
    val left = addToLeft + (left?.right ?: 0) + (left?.marginRight ?: 0) + marginLeft
    val top = addToTop + (top?.bottom ?: 0) + (top?.marginBottom ?: 0) + marginTop

    layout(
        left,
        top,
        left + measuredWidth,
        top + measuredHeight,
    )
}

class MassageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private val messageBackgroundRect = RectF()

    private val messageBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
    }

    init {
        inflate(context, R.layout.message_template_layout, this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val avatarImageView = getChildAt(0)
        val nameTextView = getChildAt(1)
        val messageTextView = getChildAt(2)
        val reactionsListLayout = getChildAt(3)

        var totalWidth = 0
        var totalHeight = 0

        // Получаем параметры аватара пользователя
        measureChildWithMargins(
            avatarImageView,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            0
        )

        // Получаем параметры текствью с именем пользователя
        measureChildWithMargins(
            nameTextView,
            widthMeasureSpec,
            avatarImageView.getWidthWithMargins(),
            heightMeasureSpec,
            0
        )

        // Получаем параметры текствью с сообщением
        measureChildWithMargins(
            messageTextView,
            widthMeasureSpec,
            avatarImageView.getWidthWithMargins(),
            heightMeasureSpec,
            0
        )

        // Получаем параметры слоя с реакциями и их колчеством
        measureChildWithMargins(
            reactionsListLayout,
            widthMeasureSpec,
            avatarImageView.getWidthWithMargins(),
            heightMeasureSpec,
            0
        )

        if (avatarImageView.isVisible)
            totalWidth = avatarImageView.getWidthWithMargins()

        totalWidth += maxOf(
            nameTextView.getWidthWithMargins(),
            messageTextView.getWidthWithMargins(),
            reactionsListLayout.getWidthWithMargins(),
        )

        totalHeight = maxOf(
            avatarImageView.getHeightWithMargins(),

            nameTextView.getHeightWithMargins()
                    + messageTextView.getHeightWithMargins()
                    + reactionsListLayout.getHeightWithMargins()
        )


        val resultWidth = resolveSize(totalWidth + paddingRight + paddingLeft, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight + paddingTop + paddingBottom, heightMeasureSpec)
        setMeasuredDimension(resultWidth, resultHeight)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val avatarImageView = getChildAt(0)
        val nameTextView = getChildAt(1)
        val messageTextView = getChildAt(2)
        val reactionsListLayout = getChildAt(3)

        val avatar = if(avatarImageView.isVisible) avatarImageView else null
        val leftPaddingWhereNotImage = if(avatarImageView.isVisible) 0 else paddingLeft

        // располагаем аватар
        avatarImageView.layoutByNeighbors(
            left = null,
            top = null,
            addToLeft = paddingLeft,
            addToTop = paddingTop
        )

        // располагаем имя пользователя
        nameTextView.layoutByNeighbors(
            left = avatar,
            top = null,
            addToLeft = leftPaddingWhereNotImage,
            addToTop = paddingTop
        )

        // располагаем сообщение пользователя
        messageTextView.layoutByNeighbors(
            left = avatar,
            top = nameTextView,
            addToLeft = leftPaddingWhereNotImage
        )

        // располагаем список рекций пользователя
        reactionsListLayout.layoutByNeighbors(
            left = avatar,
            top = messageTextView,
            addToLeft = leftPaddingWhereNotImage
        )

        messageBackgroundRect.left = minOf(nameTextView.left, messageTextView.left).toFloat()
        messageBackgroundRect.top = nameTextView.top.toFloat()
        messageBackgroundRect.right = maxOf(messageTextView.right, nameTextView.right).toFloat()
        messageBackgroundRect.bottom = messageTextView.bottom.toFloat()
    }

    override fun dispatchDraw(canvas: Canvas?) {

        canvas?.let {
            canvas.drawRoundRect(messageBackgroundRect, 50f, 50f, messageBackgroundPaint);
        }

        super.dispatchDraw(canvas)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }

}