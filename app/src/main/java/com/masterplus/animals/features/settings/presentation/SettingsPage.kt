package com.masterplus.animals.features.settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SettingsPage() {
    Scaffold { paddings ->
        Column(
            modifier = Modifier
                .padding(paddings)
        ) {
            Text(text = "Settings Page")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SettingsPagePreview() {
    SettingsPage()
}