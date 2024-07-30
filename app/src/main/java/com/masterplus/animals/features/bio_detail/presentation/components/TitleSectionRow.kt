package com.masterplus.animals.features.bio_detail.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.features.bio_detail.presentation.models.TitleSectionModel

@Composable
fun TitleSectionRow(
    titleSectionModel: TitleSectionModel,
    modifier: Modifier = Modifier,
    spaceBy: Dp = 12.dp
) {
    Column(
        modifier = modifier
    ) {
        titleSectionModel.sectionTitle?.let {
            Text(
                modifier = Modifier,
                text = it,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(spaceBy / 2))
        }
        titleSectionModel.titleContents.forEach { titleContent ->
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = titleContent.title,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = titleContent.content,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(spaceBy))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TitleSectionRowPreview() {
    TitleSectionRow(
        titleSectionModel = SampleDatas.titleSectionModel
    )
}