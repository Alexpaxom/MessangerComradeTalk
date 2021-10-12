package com.alexpaxom.homework_2.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import androidx.core.view.marginTop
import com.alexpaxom.homework_2.R

class FlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {


    private var maxWidth = 0
    private var separatorSize = 10

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        
        var totalWidth = 0
        var totalHeight = 0

        if(childCount < 1) {
            setMeasuredDimension(totalWidth, totalHeight)
            return
        }

        // размещаем первый элемент на первой строке не важно хватает нам места или нет
        val child = getChildAt(0)
        measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, totalHeight)

        var rowWidth = child.measuredWidth
        var maxRowHeight = child.measuredHeight


        for(i in 1 until childCount) {
            var separate = separatorSize
            val child = getChildAt(i)

            measureChildWithMargins(child, widthMeasureSpec, rowWidth, heightMeasureSpec, totalHeight)

            if(rowWidth+child.measuredWidth+separate >= maxWidth) {
                totalWidth = maxOf(totalWidth, rowWidth)
                totalHeight += maxRowHeight+separate
                rowWidth = 0
                maxRowHeight = 0
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, totalHeight)
                separate = 0
            }

            rowWidth += child.measuredWidth + separate
            maxRowHeight = maxOf(maxRowHeight, child.measuredHeight)

        }

        totalHeight+=maxRowHeight+separatorSize

        val resultWidth = resolveSize(totalWidth, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if(childCount < 1) return

        var top = 0
        var left = 0

        val child = getChildAt(0)

        // размещаем первый элемент на первой строке не важно хватает нам места или нет
        var rowWidth = child.measuredWidth
        var maxRowHeight = child.measuredHeight

        child.layout(left, top,left + child.measuredWidth,top + child.measuredHeight)

        // размещаем остальные элементы
        for(i in 1 until childCount) {
            var separate = separatorSize
            val child = getChildAt(i)

            left+=child.measuredWidth + separate

            if(rowWidth+child.measuredWidth+separate >= maxWidth) {
                top += maxRowHeight + separate
                left = 0
                rowWidth = 0
                maxRowHeight = 0
                separate = 0 // не учитываем разделитель для первого элемента
            }

            child.layout(
                left,
                top,
                left + child.measuredWidth,
                top + child.measuredHeight
            )

            rowWidth += child.measuredWidth + separate
            maxRowHeight = maxOf(maxRowHeight, child.measuredHeight)

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