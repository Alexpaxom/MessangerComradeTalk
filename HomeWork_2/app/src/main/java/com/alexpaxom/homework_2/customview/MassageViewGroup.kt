package com.alexpaxom.homework_2.customview

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.alexpaxom.homework_2.R

class MassageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

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

        measureChildWithMargins(avatarImageView, widthMeasureSpec, 0, heightMeasureSpec, 0)

        // Получаем параметры с аватара пользователя
        val avatarsMarginParams = avatarImageView.layoutParams as MarginLayoutParams
        val marginLeft = avatarsMarginParams.leftMargin
        val marginRight = avatarsMarginParams.rightMargin


        // Получаем параметры текствью с именем пользователя
        measureChildWithMargins(
            nameTextView,
            widthMeasureSpec,
            avatarImageView.measuredWidth,
            heightMeasureSpec,
            0
        )

        val nameMarginParams = nameTextView.layoutParams as MarginLayoutParams
        val nameMarginLeft = nameMarginParams.leftMargin
        val nameMarginRight = nameMarginParams.rightMargin


        // Получаем параметры текствью с сообщением
        measureChildWithMargins(
            messageTextView,
            widthMeasureSpec,
            avatarImageView.measuredWidth,
            heightMeasureSpec,
            0
        )

        val messageMarginParams = messageTextView.layoutParams as MarginLayoutParams
        val messageMarginLeft = messageMarginParams.leftMargin
        val messageMarginRight = messageMarginParams.rightMargin

        // Получаем параметры слоя с реакциями и их колчеством
        measureChildWithMargins(
            reactionsListLayout,
            widthMeasureSpec,
            avatarImageView.measuredWidth,
            heightMeasureSpec,
            0
        )

        val reactionsListMarginParams = reactionsListLayout.layoutParams as MarginLayoutParams
        val reactionsListMarginLeft = reactionsListMarginParams.leftMargin
        val reactionsListMarginRight = reactionsListMarginParams.rightMargin


        totalWidth += avatarImageView.measuredWidth + marginLeft + marginRight
        totalHeight = maxOf(totalHeight, avatarImageView.measuredHeight)

        totalWidth += maxOf(
            nameTextView.measuredWidth + nameMarginLeft + nameMarginRight,
            messageTextView.measuredWidth + messageMarginLeft + messageMarginRight,
            reactionsListLayout.measuredWidth + reactionsListMarginLeft + reactionsListMarginRight,
        )

        totalHeight = maxOf(totalHeight,
            nameTextView.measuredHeight
                    + messageTextView.measuredHeight +
                    reactionsListLayout.measuredHeight)



        val resultWidth = resolveSize(totalWidth + paddingRight + paddingLeft, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight + paddingTop + paddingBottom, heightMeasureSpec)
        setMeasuredDimension(resultWidth, resultHeight)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val imageView = getChildAt(0)
        val nameTextView = getChildAt(1)
        val messageTextView = getChildAt(2)
        val reactionsListLayout = getChildAt(3)

        imageView.layout(
            paddingLeft,
            paddingTop,
            paddingLeft + imageView.measuredWidth,
            paddingTop + imageView.measuredHeight
        )

        val marginRight = (imageView.layoutParams as MarginLayoutParams).rightMargin

        nameTextView.layout(
            imageView.right + marginRight,
            paddingTop,
            imageView.right + marginRight + nameTextView.measuredWidth,
            paddingTop + nameTextView.measuredHeight
        )

        messageTextView.layout(
            imageView.right + marginRight,
            nameTextView.bottom,
            imageView.right + marginRight + messageTextView.measuredWidth,
            nameTextView.bottom + messageTextView.measuredHeight
        )

        reactionsListLayout.layout(
            imageView.right + marginRight,
            messageTextView.bottom,
            imageView.right + marginRight + messageTextView.measuredWidth,
            messageTextView.bottom + messageTextView.measuredHeight
        )
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