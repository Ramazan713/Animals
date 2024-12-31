package com.masterplus.animals.features.species_list.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer


@Composable
fun SpeciesCardShimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shimmer()
    ) {
        // Left Side: Image Placeholder
        Box(
            modifier = Modifier
                .weight(1f)
                .size(200.dp)
                .background(Color.Gray.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Right Side: Title, Sub Title ve Description
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.3f)
                .padding(vertical = 4.dp)
        ) {
            // Title
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth(0.6f) // Title
                    .background(Color.Gray.copy(alpha = 0.5f), shape = RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth(0.4f) // Subtitle
                    .background(Color.Gray.copy(alpha = 0.5f), shape = RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(148.dp)
                    .background(Color.Gray.copy(alpha = 0.3f), shape = RoundedCornerShape(4.dp))
            )
        }
    }
}
