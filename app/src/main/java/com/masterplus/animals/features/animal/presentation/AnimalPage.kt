package com.masterplus.animals.features.animal.presentation


import AnimalViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnimalPageRoot(
    viewModel: AnimalViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AnimalPage(
        state = state,
        onAction = viewModel::onAction
    )
}


@Composable
fun AnimalPage(
    state: AnimalState,
    onAction: (AnimalAction) -> Unit
) {
    Scaffold { paddings ->
        Column(
            modifier = Modifier
                .padding(paddings)
        ) {
            Text(text = "Animal Page")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AnimalPagePreview() {
    AnimalPage(
        state = AnimalState(),
        onAction = {}
    )
}