package com.masterplus.animals.features.animal.presentation


import AnimalViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.animals.core.presentation.components.ImageCategoryRow
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
                .padding(paddings),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Animal Page")

            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    ImageCategoryRow(
                        title = "Yaşam Alanları",
                        items = state.habitats,
                        onClickItem = { item ->

                        }
                    )
                }

                item {
                    ImageCategoryRow(
                        title = "Sınıflar",
                        items = state.classes,
                        showMore = true,
                        onClickMore = {

                        },
                        onClickItem = { item ->

                        }
                    )
                }

                item {
                    ImageCategoryRow(
                        title = "Takımlar",
                        items = state.orders,
                        showMore = true,
                        onClickMore = {

                        },
                        onClickItem = { item ->

                        }
                    )
                }

                item {
                    ImageCategoryRow(
                        title = "Familyalar",
                        items = state.families,
                        showMore = true,
                        onClickMore = {

                        },
                        onClickItem = { item ->

                        }
                    )
                }
            }
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