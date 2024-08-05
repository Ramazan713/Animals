package com.masterplus.animals.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.animals.core.presentation.models.ImageWithTitleModel
import com.masterplus.animals.core.presentation.utils.SampleDatas





@Composable
fun ImageCategoryRow(
    title: String,
    items: List<ImageWithTitleModel>,
    modifier: Modifier = Modifier,
    showMore: Boolean = false,
    onClickMore: (() -> Unit)? = null,
    onClickItem: (ImageWithTitleModel) -> Unit,
    contentPaddings: PaddingValues = PaddingValues()
) {
    ImageCategoryRow(
        title = title,
        modifier = modifier,
        showMore = showMore,
        onClickMore = onClickMore,
        contentPaddings = contentPaddings
    ){ showMoreBtn ->
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = contentPaddings
        ) {
            items(
                items = items,
                key = { it.id ?: it.title }
            ){ item ->
                ImageWithTitle(
                    imageData = item.imageUrl,
                    title = item.title,
                    subTitle = item.subTitle,
                    contentDescription = item.contentDescription,
                    onClick = {
                        onClickItem(item)
                    }
                )
            }

            item {
                showMoreBtn()
            }
        }

    }
}

@Composable
fun ImageCategoryRow(
    title: String,
    modifier: Modifier = Modifier,
    showMore: Boolean = false,
    onClickMore: (() -> Unit)? = null,
    contentPaddings: PaddingValues = PaddingValues(),
    content: @Composable ( showMoreBtn: @Composable () -> Unit ) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(contentPaddings)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(4.dp)
            )
            if(showMore){
                Spacer(modifier = Modifier.width(12.dp))
                TextButton(
                    onClick = { onClickMore?.invoke() }
                ) {
                    Text(text = "daha fazlası")
                }
            }
        }

        content {
            if(showMore){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    TextButton(
                        onClick = { onClickMore?.invoke() }
                    ) {
                        Text(text = "Tümünü Göster")
                    }
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
private fun ImageCategoryRowPreview() {
    ImageCategoryRow(
        title = "Sınıflar",
        items = listOf(SampleDatas.imageWithTitleModel1, SampleDatas.imageWithTitleModel2),
        onClickItem = {},
        onClickMore = {}
    )
}