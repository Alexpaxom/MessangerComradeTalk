package com.alexpaxom.homework_2.customview

import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop

fun View.getWidthWithMargins(): Int = measuredWidth + marginLeft + marginRight

fun View.getHeightWithMargins(): Int = measuredHeight + marginTop + marginBottom

fun View.layoutByLeftTopNeighbors(
    left: View?,
    top: View?,
    addOffsetLeft: Int = 0,
    addOffsetTop: Int = 0
) {
    val leftCoord = addOffsetLeft + (left?.right ?: 0) + (left?.marginRight ?: 0) + marginLeft
    val topCoord = addOffsetTop + (top?.bottom ?: 0) + (top?.marginBottom ?: 0) + marginTop

    layout(
        leftCoord,
        topCoord,
        leftCoord + measuredWidth,
        topCoord + measuredHeight,
    )
}

fun View.layoutByRightTopNeighbors(
    right: View?, top: View?,
    addOffsetRight: Int = 0, addOffsetTop: Int = 0,
    maxWidth: Int = 0
) {

    val rightCoord =
        maxWidth - addOffsetRight - (right?.left ?: 0) - (right?.marginLeft ?: 0) - marginRight
    val topCoord = addOffsetTop + (top?.bottom ?: 0) + (top?.marginBottom ?: 0) + marginTop

    layout(
        rightCoord - measuredWidth,
        topCoord,
        rightCoord,
        topCoord + measuredHeight,
    )
}