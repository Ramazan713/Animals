package com.masterplus.animals.core.presentation.utils

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ShapeUtils {

    fun getRowStyleShape(isRow: Boolean, cornerRadius: Dp): Shape{
        return getRowStyleShape(
            isRow = isRow,
            shape = RoundedCornerShape(cornerRadius)
        )
    }

    fun getRowStyleShape(isRow: Boolean, shape: CornerBasedShape): Shape{
        val zeroCorner = CornerSize(0.dp)
        return shape.copy(
            topEnd = if(isRow)zeroCorner else shape.topEnd,
            bottomStart = if(isRow) shape.bottomStart else zeroCorner,
            bottomEnd = zeroCorner
        )
    }

}