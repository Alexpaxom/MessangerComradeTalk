package com.alexpaxom.homework_2.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.*
import com.alexpaxom.homework_2.R
import com.bumptech.glide.Glide

inline fun View.getWidthWithMargins(): Int = measuredWidth + marginLeft + marginRight
inline fun View.getHeightWithMargins(): Int = measuredHeight + marginTop + marginBottom

inline fun View.layoutByNeighbors(left: View?, top: View?, addToLeft:Int = 0, addToTop: Int = 0) {
    val leftCoord = addToLeft + (left?.right ?: 0) + (left?.marginRight ?: 0) + marginLeft
    val topCoord = addToTop + (top?.bottom ?: 0) + (top?.marginBottom ?: 0) + marginTop

    layout(
        leftCoord,
        topCoord,
        leftCoord + measuredWidth,
        topCoord + measuredHeight,
    )
}

class MassageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    var messageRadius = DEFAULT_MESSAGE_CORNER_RADIUS
    set(value) {
        field = value
        requestLayout()
    }

    var userName: CharSequence
        get() = nameTextView.text
        set(value) {
            nameTextView.text = value
        }

    var messageText: CharSequence
        get() = messageTextView.text
        set(value) {
            messageTextView.text = value
        }


    private val messageBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val messageBackgroundRect = RectF()
    private val avatarImageView: ImageView
    private val nameTextView: TextView
    private val messageTextView: TextView
    private val reactionsListLayout: FlexBoxLayout
    private var onReactionClickListener: (EmojiReactionCounter.()->Unit)? = null

    init {
        inflate(context, R.layout.message_template_layout, this)

        avatarImageView = findViewById(R.id.massage_user_avatar_img)
        nameTextView = findViewById(R.id.massage_name_user)
        messageTextView = findViewById(R.id.massage_text)
        reactionsListLayout = findViewById(R.id.massage_reactions_list)

        with(context.obtainStyledAttributes(attrs, R.styleable.MassageViewGroup)) {
            messageBackgroundPaint.color = getColor(
                R.styleable.MassageViewGroup_massageTextBackground,
                Color.GRAY
            )

            messageRadius = getDimensionPixelSize(
                R.styleable.MassageViewGroup_massageTextRadius,
                DEFAULT_MESSAGE_CORNER_RADIUS.toInt()
            ).toFloat()
        }
    }

    fun setAvatarByUrl(url:String) {
        Glide
            .with(this)
            .load(url)
            .circleCrop()
            .into(avatarImageView)
    }


    fun addReaction(codeEmoji: String = "\uD83D\uDE36", count:Int = 1, selected: Boolean = false) {
        val emoji = View.inflate(context, R.layout.emoji_view, null) as EmojiReactionCounter

        //removeReaction(codeEmoji) // удаляем идентичную рекцию если она уже была

        emoji.displayEmoji = codeEmoji
        emoji.countReaction = count
        emoji.isSelected = selected
        reactionsListLayout.addView(emoji)



        emoji.setOnClickListener {
            onReactionClickListener?.invoke(it as EmojiReactionCounter)
        }
    }

    fun findReactionId(codeEmoji: String): View? {

        for(i in 0 until reactionsListLayout.childCount) {
            val emoji = reactionsListLayout.getChildAt(i) as EmojiReactionCounter
            if(emoji.displayEmoji == codeEmoji)
                return emoji
        }

        return null
    }

    fun removeReaction(codeEmoji: String) {
        reactionsListLayout.removeView( findReactionId(codeEmoji) )
    }

    fun removeReaction(view: View) {
        reactionsListLayout.removeView( view )
    }

    fun removeAllReactions() {
        reactionsListLayout.removeAllViews()
    }

    fun setOnReactionClickListener(onClickListener:(EmojiReactionCounter.()->Unit)?  = null) {
        onReactionClickListener = onClickListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
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
            canvas.drawRoundRect(messageBackgroundRect, messageRadius, messageRadius, messageBackgroundPaint);
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

    companion object {
        private const val DEFAULT_MESSAGE_CORNER_RADIUS = 50f
    }

}