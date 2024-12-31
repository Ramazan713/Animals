package com.masterplus.animals.features.category_list.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer


@Composable
fun CategoryItemShipper() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .shimmer()
            .clip(MaterialTheme.shapes.medium)
    ) {
        // Arka Plan Görseli
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.5f)) // image Placeholder
        )

        // Number Placeholder
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(20.dp)
                .background(Color.Gray.copy(alpha = 0.5f), shape = RoundedCornerShape(4.dp))
        )

        // Sol Alttaki Title ve Subtitle
        Column(
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomStart)
                .padding(8.dp)
        ) {
            // Title
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(120.dp)
                    .background(Color.Gray.copy(alpha = 0.7f), shape = RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Subtitle
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .width(80.dp)
                    .background(Color.Gray.copy(alpha = 0.7f), shape = RoundedCornerShape(4.dp))
            )
        }
    }
}
