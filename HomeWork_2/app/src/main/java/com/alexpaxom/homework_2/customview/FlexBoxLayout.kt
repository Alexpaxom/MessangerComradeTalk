package com.alexpaxom.homework_2.customview

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.alexpaxom.homework_2.R

class FlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    var itemBoundMinWidth = 75
        set(value) {
            field = value
            requestLayout()
        }

    //Расстояние между элементами по высоте и ширине, px
    var separatorSize = 15
        set(value) {
            field = value
            requestLayout()
        }

    private var maxWidth = 0

    init {
        with(context.obtainStyledAttributes(attrs, R.styleable.FlexBoxLayout)) {
            separatorSize = getDimensionPixelSize(R.styleable.FlexBoxLayout_separatorSizeHW, separatorSize)
            itemBoundMinWidth = getDimensionPixelSize(R.styleable.FlexBoxLayout_itemBoundMinWidth, itemBoundMinWidth)
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        
        var totalWidth = 0
        var totalHeight = 0

        if(childCount < 1) {
            setMeasuredDimension(totalWidth, totalHeight)
            return
        }

        // размещаем первый элемент на первой строке не важно хватает нам места или нет
        var child = getChildAt(0)
        measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, totalHeight)

        var rowWidth = maxOf(child.measuredWidth, itemBoundMinWidth)
        var maxRowElementHeight = child.measuredHeight

        var hasTwoRowsAndMore = false

        // размещаем остальные элементы
        for(i in 1 until childCount) {
            child = getChildAt(i)
            measureChildWithMargins(child, widthMeasureSpec, rowWidth, heightMeasureSpec, totalHeight)
            var elementWidth = maxOf(child.measuredWidth, itemBoundMinWidth)

            if(rowWidth+elementWidth+separatorSize < maxWidth) {
                rowWidth += elementWidth+separatorSize
                maxRowElementHeight = maxOf(maxRowElementHeight, child.measuredHeight)
            }
            else {
                totalWidth = maxOf(totalWidth, rowWidth)
                totalHeight += maxRowElementHeight + separatorSize

                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, totalHeight)
                elementWidth = maxOf(child.measuredWidth, itemBoundMinWidth)

                rowWidth = elementWidth
                maxRowElementHeight = child.measuredHeight
                hasTwoRowsAndMore = true
            }
        }

        totalHeight += maxRowElementHeight + if (hasTwoRowsAndMore) separatorSize else 0
        totalWidth = maxOf(totalWidth, rowWidth)

        val resultWidth = resolveSize(totalWidth, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if(childCount < 1) return

        var top = 0
        var left = 0
        var right = 0

        var child = getChildAt(0)

        // размещаем первый элемент на первой строке не важно хватает нам места или нет
        var maxRowElementHeight = child.measuredHeight

        child.layout(left, top,left + child.measuredWidth,top + child.measuredHeight)
        left = maxOf(child.measuredWidth, itemBoundMinWidth)

        // размещаем остальные элементы
        for(i in 1 until childCount) {
            child = getChildAt(i)
            var elementWidth = maxOf(child.measuredWidth, itemBoundMinWidth)


            if(left+elementWidth+separatorSize < maxWidth) {
                left += separatorSize
                maxRowElementHeight = maxOf(maxRowElementHeight, child.measuredHeight)
            }
            else {
                left = 0
                top += maxRowElementHeight + separatorSize
                maxRowElementHeight = child.measuredHeight
            }

            right = left+child.measuredWidth

            child.layout(
                left,
                top,
                right,
                top + child.measuredHeight
            )

            left = left+elementWidth
        }
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