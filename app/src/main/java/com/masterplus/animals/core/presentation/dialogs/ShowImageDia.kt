package com.masterplus.animals.core.presentation.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.masterplus.animals.R
import com.masterplus.animals.core.presentation.components.DefaultImage
import kotlinx.coroutines.launch


@Composable
fun ShowImageDia(
    imageDataList: List<Any>,
    onDismiss: () -> Unit,
    currentPageIndex: Int = 0
) {
    val pageSize = imageDataList.size
    val pagerState = rememberPagerState(
        initialPage = currentPageIndex,
        pageCount = { pageSize }
    )
    val scope = rememberCoroutineScope()

    val screenConfigurationHeight = LocalConfiguration.current.screenHeightDp.dp
    val pageMaxHeight = maxOf(350.dp, (screenConfigurationHeight / 100) * 70)

    Dialog(
        onDismissRequest = onDismiss,

        ) {
        Box(
            modifier = Modifier
                .height(pageMaxHeight)
                .fillMaxWidth()
            ,
            contentAlignment = Alignment.TopCenter
        ) {
            HorizontalPager(
                modifier = Modifier.matchParentSize(),
                state = pagerState
            ) { pageIndex ->
                DefaultImage(
                    imageData = imageDataList[pageIndex],
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .matchParentSize()
                )
            }

            IconButtonForImage(
                modifier = Modifier
                    .align(Alignment.TopEnd),
                imageVector = Icons.Default.Close,
                onClick = onDismiss
            )

            IconButtonForImage(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                enabled = pagerState.canScrollBackward,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }
            )

            IconButtonForImage(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                enabled = pagerState.canScrollForward,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            )

            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (0..<pageSize).forEach {
                    Dot(isSelected = pagerState.currentPage == it)
                }
            }

        }
    }
}


@Composable
private fun IconButtonForImage(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null
) {
    FilledTonalIconButton(
        modifier = modifier
            .padding(4.dp),
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = IconButtonDefaults.filledTonalIconButtonColors()
                .containerColor.copy(alpha = 0.4f)
        ),
        onClick = onClick,
        enabled = enabled
    ) {
        Icon(imageVector = imageVector, contentDescription = contentDescription)
    }
}

@Composable
private fun Dot(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 8.dp
) {
    val color = if(isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
    Spacer(
        modifier = modifier
            .clip(CircleShape)
            .background(color)
            .size(size)
    )
}


@Preview(showBackground = true)
@Composable
private fun SamplePreview() {
//    var showDialog by remember {
//        mutableStateOf(true)
//    }
//
//    TextButton(onClick = { showDialog = true }) {
//        Text(text = "Show Dialog")
//    }
//
//    if(showDialog){
//        Dialog(onDismissRequest = { showDialog = false }) {
//
//        }
//    }

    ShowImageDia(
        imageDataList = listOf(
            R.drawable.all_animals
        ),
        currentPageIndex = 0,
        onDismiss = {}
    )
}