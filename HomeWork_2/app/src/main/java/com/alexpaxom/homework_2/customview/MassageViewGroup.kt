package com.alexpaxom.homework_2.customview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
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

inline fun View.layoutByLeftTopNeighbors(left: View?, top: View?, addOffsetLeft:Int = 0, addOffsetTop: Int = 0) {
    val leftCoord = addOffsetLeft + (left?.right ?: 0) + (left?.marginRight ?: 0) + marginLeft
    val topCoord = addOffsetTop + (top?.bottom ?: 0) + (top?.marginBottom ?: 0) + marginTop

    layout(
        leftCoord,
        topCoord,
        leftCoord + measuredWidth,
        topCoord + measuredHeight,
    )
}

inline fun View.layoutByRightTopNeighbors(
    right: View?, top: View?,
    addOffsetRight:Int = 0, addOffsetTop: Int = 0,
    maxWidth: Int = 0) {

    val rightCoord = maxWidth - addOffsetRight - (right?.left ?: 0) - (right?.marginLeft ?: 0) - marginRight
    val topCoord = addOffsetTop + (top?.bottom ?: 0) + (top?.marginBottom ?: 0) + marginTop

    layout(
        rightCoord-measuredWidth,
        topCoord,
        rightCoord,
        topCoord + measuredHeight,
    )
}


class MassageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

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

    var align: String

    // макет фона сообщения
    private var messageBackgroundDrawable: Drawable? = null
    // Дополнитлеьные паддиги вокруг сообщения, созданные фоном
    private val messageBackgroundPaddings = Rect()
    // Размеры фона сообщения
    private val messageBackgroundRect = Rect()

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
            messageBackgroundDrawable = getDrawable(
                R.styleable.MassageViewGroup_massageTextBackground
            )

            align = getString(R.styleable.MassageViewGroup_horizontalAlign) ?: DEFAULT_ALIGN

            messageTextView.isVisible = getBoolean(
                R.styleable.MassageViewGroup_showMessageText,
                DEFAULT_SHOW_MESSAGE_FLAG
            )

            avatarImageView.isVisible = getBoolean(
                R.styleable.MassageViewGroup_showAvatar,
                DEFAULT_SHOW_AVATAR_FLAG
            )

            reactionsListLayout.isVisible = getBoolean(
                R.styleable.MassageViewGroup_showReactionsList,
                DEFAULT_SHOW_REACTIONS_LIST_FLAG

            )

            nameTextView.isVisible = getBoolean(
                R.styleable.MassageViewGroup_showUserName,
                DEFAULT_SHOW_USER_NAME_FLAG
            )
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

        messageBackgroundDrawable?.getPadding(messageBackgroundPaddings)

        // Получаем параметры аватара пользователя
        if(avatarImageView.isVisible)
        measureChildWithMargins(
            avatarImageView,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            0
        )

        // Получаем параметры текствью с именем пользователя
        if(nameTextView.isVisible)
        measureChildWithMargins(
            nameTextView,
            widthMeasureSpec,
            avatarImageView.getWidthWithMargins()
                    +messageBackgroundPaddings.left
                    +messageBackgroundPaddings.right,
            heightMeasureSpec,
            messageBackgroundPaddings.top
        )

        // Получаем параметры текствью с сообщением
        if(messageTextView.isVisible)
        measureChildWithMargins(
            messageTextView,
            widthMeasureSpec,
            avatarImageView.getWidthWithMargins()
                    +messageBackgroundPaddings.left
                    +messageBackgroundPaddings.right,
            heightMeasureSpec,
            nameTextView.top
        )

        // Получаем параметры слоя с реакциями и их колчеством
        if(reactionsListLayout.isVisible)
        measureChildWithMargins(
            reactionsListLayout,
            widthMeasureSpec,
            avatarImageView.getWidthWithMargins()+messageBackgroundPaddings.left,
            heightMeasureSpec,
            messageTextView.top+messageBackgroundPaddings.top
        )

        if (avatarImageView.isVisible)
            totalWidth = avatarImageView.getWidthWithMargins()

        totalWidth += maxOf(
            nameTextView.getWidthWithMargins(),
            messageTextView.getWidthWithMargins(),
            reactionsListLayout.getWidthWithMargins(),
        )

        totalWidth += messageBackgroundPaddings.right + messageBackgroundPaddings.left

        totalHeight = maxOf(
            avatarImageView.getHeightWithMargins(),

            nameTextView.getHeightWithMargins()
                    + messageTextView.getHeightWithMargins()
                    + reactionsListLayout.getHeightWithMargins()
                    + messageBackgroundPaddings.top
                    + messageBackgroundPaddings.bottom
        )


        val resultWidth = resolveSize(totalWidth + paddingRight + paddingLeft, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight + paddingTop + paddingBottom, heightMeasureSpec)
        setMeasuredDimension(resultWidth, resultHeight)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if(align == "right")
            alignLayoutRight()
        else
            alignLayoutLeft()

        calcMessageBackgroundBounds()
    }

    private fun alignLayoutRight() {

        // располагаем имя пользователя
        nameTextView.layoutByRightTopNeighbors(
            right = null,
            top = null,
            addOffsetRight = paddingRight+messageBackgroundPaddings.right,
            addOffsetTop = paddingTop+messageBackgroundPaddings.top
        )

        // располагаем сообщение пользователя
        messageTextView.layoutByRightTopNeighbors(
            right = null,
            top = nameTextView,
            addOffsetRight = paddingRight+messageBackgroundPaddings.right,
            maxWidth = measuredWidth
        )

        // располагаем аватар
        avatarImageView.layoutByRightTopNeighbors(
            right = messageTextView,
            top = null,
            addOffsetRight = messageBackgroundPaddings.left,
            addOffsetTop = paddingTop
        )

        // располагаем список рекций пользователя
        reactionsListLayout.layoutByRightTopNeighbors(
            right = null,
            top = messageTextView,
            addOffsetTop = messageBackgroundPaddings.bottom,
            addOffsetRight = paddingRight+messageBackgroundPaddings.right,
            maxWidth = measuredWidth
        )

        messageBackgroundRect.left = messageTextView.left
        messageBackgroundRect.top = nameTextView.top
        messageBackgroundRect.right = maxOf(messageTextView.right, nameTextView.right)
        messageBackgroundRect.bottom = messageTextView.bottom

    }

    private fun alignLayoutLeft() {
        val avatar = if(avatarImageView.isVisible) avatarImageView else null
        val leftPaddingWhereNotImage = if(avatarImageView.isVisible) 0 else paddingLeft

        // располагаем аватар
        avatarImageView.layoutByLeftTopNeighbors(
            left = null,
            top = null,
            addOffsetLeft = paddingLeft,
            addOffsetTop = paddingTop
        )

        // располагаем имя пользователя
        nameTextView.layoutByLeftTopNeighbors(
            left = avatar,
            top = null,
            addOffsetLeft = leftPaddingWhereNotImage+messageBackgroundPaddings.left,
            addOffsetTop = paddingTop+messageBackgroundPaddings.top
        )

        // располагаем сообщение пользователя
        messageTextView.layoutByLeftTopNeighbors(
            left = avatar,
            top = nameTextView,
            addOffsetLeft = leftPaddingWhereNotImage+messageBackgroundPaddings.left,
        )

        // располагаем список рекций пользователя
        reactionsListLayout.layoutByLeftTopNeighbors(
            left = avatar,
            top = messageTextView,
            addOffsetTop = messageBackgroundPaddings.top,
            addOffsetLeft = leftPaddingWhereNotImage,
        )

        messageBackgroundRect.left = minOf(nameTextView.left, messageTextView.left)
        messageBackgroundRect.top = nameTextView.top
        messageBackgroundRect.right = maxOf(messageTextView.right, nameTextView.right)
        messageBackgroundRect.bottom = messageTextView.bottom
    }

    private fun calcMessageBackgroundBounds() {
        // Расчитываем размеры фона сообщения в зависимости от размеров имени, сообщения и их видимости
        messageBackgroundRect.left -= messageBackgroundPaddings.left
        messageBackgroundRect.right += messageBackgroundPaddings.right
        messageBackgroundRect.top -= messageBackgroundPaddings.top
        messageBackgroundRect.bottom += messageBackgroundPaddings.bottom
    }


    override fun dispatchDraw(canvas: Canvas?) {

        canvas?.let {
            messageBackgroundDrawable?.bounds = messageBackgroundRect
            messageBackgroundDrawable?.draw(canvas)
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
        private const val DEFAULT_SHOW_MESSAGE_FLAG = true
        private const val DEFAULT_SHOW_AVATAR_FLAG = true
        private const val DEFAULT_SHOW_USER_NAME_FLAG = true
        private const val DEFAULT_SHOW_REACTIONS_LIST_FLAG = true
        private const val DEFAULT_ALIGN = "left"
    }

}