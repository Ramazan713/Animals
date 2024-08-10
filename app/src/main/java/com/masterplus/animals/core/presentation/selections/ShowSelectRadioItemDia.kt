package com.masterplus.animals.core.presentation.selections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.presentation.components.DefaultRadioRow
import com.masterplus.animals.core.shared_features.theme.domain.enums.ThemeEnum


@Composable
fun <T: IMenuItemEnum> ShowSelectRadioItemDia(
    items: List<T>,
    onClose: () -> Unit,
    title: String? = null,
    onApprove: ((T) -> Unit)? = null,
    selectedItem: (T)? = null,
    imageVector: ImageVector? = null
){
    val currentItem = rememberSaveable {
        mutableStateOf(selectedItem)
    }


    AlertDialog(
        onDismissRequest = { onClose() },
        title = {
            Text(
                title ?: return@AlertDialog,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items){item->
                    DefaultRadioRow(
                        title = item.title.asString(),
                        value = currentItem.value == item,
                        onValueChange = { currentItem.value = item},
                        defaultColor = Color.Transparent,
                        selectedRow = false,
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    currentItem.value?.let { onApprove?.invoke(it) }
                    onClose()
                }
            ) {
                Text(text = stringResource(R.string.approve))
            }
        },
        icon = {
            Icon(imageVector = imageVector ?: return@AlertDialog, contentDescription = null)
        }
    )


}

@Preview(showBackground = true)
@Composable
fun ShowSelectRadioItemAlertDialogPreview() {
    ShowSelectRadioItemDia(
        items = ThemeEnum.entries,
        title = "Title",
        onClose = {},
        onApprove = {},
        imageVector = Icons.Default.Palette
    )
}

