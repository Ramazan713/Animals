package com.masterplus.animals.features.settings.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun SwitchItem(
    title: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
    contentPaddingValues: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
){
    Row(
        modifier = modifier
            .padding(
                horizontal = 1.dp, vertical = 4.dp
            )
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .toggleable(value = value, onValueChange = onValueChange, role = Role.Switch)
            .padding(contentPaddingValues)
        ,
        verticalAlignment = Alignment.CenterVertically
    ){

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 5.dp)
                .padding(end = 8.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W500
                )
            )
            if(subTitle != null){
                Spacer(Modifier.padding(top = 2.dp))
                Text(
                    subTitle,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

        }

        Switch(checked = value, onCheckedChange = null)
    }
}