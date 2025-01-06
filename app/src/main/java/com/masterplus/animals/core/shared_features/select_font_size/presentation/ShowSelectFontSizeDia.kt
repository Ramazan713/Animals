package com.masterplus.animals.core.shared_features.select_font_size.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.animals.core.presentation.components.SharedHeader
import com.masterplus.animals.core.presentation.dialogs.BaseDialog
import com.masterplus.animals.core.shared_features.select_font_size.domain.enums.FontSizeEnum
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSelectFontSizeDia(
    viewModel: SelectFontSizeViewModel = koinViewModel(),
    onClose: () -> Unit,
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    ShowSelectFontSizeDia(
        state = state,
        onAction = viewModel::onAction,
        onClose = onClose
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSelectFontSizeDia(
    state: SelectFontSizeState,
    onAction: (SelectFontSizeAction) -> Unit,
    onClose: () -> Unit,
) {
    BaseDialog(
        onClosed = onClose
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .padding(bottom = 8.dp)
            ,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SharedHeader(
                title = "",
                onIconClick = onClose
            )
            Text(
                text = "Hello World",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    lineHeight = 24.0.sp
                ).scaleFontSize(state.currentFontSizeEnum),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 30.dp)
                    .animateContentSize()
                ,
                textAlign = TextAlign.Center
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Slider(
                    modifier = Modifier
                        .weight(3f),
                    value = state.currentFontSizeEnum.keyValue.toFloat(),
                    onValueChange = { onAction(SelectFontSizeAction.ChangeSliderPos(it)) },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    steps = FontSizeEnum.entries.size - 2,
                    valueRange = 1f..(FontSizeEnum.entries.size).toFloat()
                )

                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = state.currentFontSizeEnum.description.asString(),
                    textAlign = TextAlign.Center
                )
            }


            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.End)
            ) {
                TextButton(
                    enabled = state.anyChanges,
                    onClick = { onAction(SelectFontSizeAction.RestoreChanges) }
                ) {
                    Text("Değişiklikleri geri al")
                }

                Button(
                    enabled = state.anyChanges,
                    onClick = { onAction(SelectFontSizeAction.ApplyChanges) }
                ) {
                    Text("Kaydet")
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ShowSelectFontSizeDiaPreview() {
    ShowSelectFontSizeDia(
        onClose = {},
        state = SelectFontSizeState(),
        onAction = {}
    )
}