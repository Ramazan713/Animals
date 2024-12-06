package com.masterplus.animals.core.shared_features.add_species_to_list.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.domain.models.SpeciesListDetail
import com.masterplus.animals.core.presentation.dialogs.ShowQuestionDialog
import com.masterplus.animals.core.shared_features.list.presentation.select_list_with_menu.ShowBottomMenuWithSelectList


@Composable
fun AddSpeciesToListHandler(
    state: AddSpeciesToListState,
    onAction: (AddSpeciesToListAction) -> Unit,
    listIdControl: Int?
){
    AddSpeciesToListHandler(
        state = state,
        onAction = onAction,
        listIdControl = listIdControl,
        bottomMenuItems = listOf(),
        onBottomMenuItemClick = { _, _, _ ->}
    )
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun <T: IMenuItemEnum> AddSpeciesToListHandler(
    state: AddSpeciesToListState,
    onAction: (AddSpeciesToListAction) -> Unit,
    bottomMenuItems: List<T>,
    onBottomMenuItemClick: (T, SpeciesListDetail, Int) -> Unit,
    listIdControl: Int?
) {

    LaunchedEffect(listIdControl) {
        onAction(AddSpeciesToListAction.SetListIdControl(listIdControl))
    }

    state.dialogEvent?.let { dialogEvent ->
        val close = remember {{
            onAction(AddSpeciesToListAction.ShowDialog(null))
        }}
        when(dialogEvent){
            is AddSpeciesToListDialogEvent.ShowItemBottomMenu -> {
                ShowBottomMenuWithSelectList(
                    items = bottomMenuItems,
                    title = stringResource(id = R.string.n_for_number_word,dialogEvent.posIndex + 1, dialogEvent.item.name),
                    animalId = dialogEvent.item.id ?: 0,
                    onClose = close,
                    listIdControl = state.listIdControl,
                    onClickItem = { menuItem ->
                        onBottomMenuItemClick.invoke(menuItem, dialogEvent.item, dialogEvent.posIndex)
                    }
                )
            }
            is AddSpeciesToListDialogEvent.AskFavoriteDelete -> {
                ShowQuestionDialog(
                    title = stringResource(R.string.question_remove_list_from_favorite),
                    content = stringResource(R.string.affect_current_list),
                    onClosed = close,
                    onApproved = {
                        onAction(AddSpeciesToListAction.AddToFavorite(dialogEvent.speciesId))
                    }
                )
            }

        }
    }
}