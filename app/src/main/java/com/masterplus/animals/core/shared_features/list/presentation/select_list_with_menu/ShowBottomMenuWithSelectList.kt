package com.masterplus.animals.core.shared_features.list.presentation.select_list_with_menu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.presentation.dialogs.ShowQuestionDialog
import com.masterplus.animals.core.presentation.selections.ShowSelectBottomMenuItems
import com.masterplus.animals.core.shared_features.list.domain.enums.SelectListMenuEnum
import com.masterplus.animals.core.shared_features.list.presentation.select_list.ShowSelectListBottom
import org.koin.androidx.compose.koinViewModel


@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun <T: IMenuItemEnum> ShowBottomMenuWithSelectList(
    items: List<T>,
    animalId: Int,
    listIdControl: Int? = null,
    onClickItem: (T) -> Unit,
    onClose: () -> Unit,
    title: String? = null,
    selectListTitle: String = stringResource(id = R.string.add_to_list),
    listViewModel: SelectListMenuViewModel = koinViewModel()
){
    val state by listViewModel.state.collectAsStateWithLifecycle()

    ShowBottomMenuWithSelectList(
        items = items,
        animalId = animalId,
        listIdControl = listIdControl,
        onClickItem = onClickItem,
        title = title,
        selectListTitle = selectListTitle,
        onClose = onClose,
        state = state,
        onEvent = listViewModel::onEvent
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun <T: IMenuItemEnum> ShowBottomMenuWithSelectList(
    items: List<T>,
    animalId: Int,
    listIdControl: Int? = null,
    onClickItem: (T) -> Unit,
    onClose: () -> Unit,
    title: String? = null,
    selectListTitle: String = stringResource(id = R.string.add_to_list),
    state: SelectListMenuState,
    onEvent: (SelectListMenuAction) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onClose
    ) {
        SelectMenuWithListBottomContent(
            items = items,
            animalId = animalId,
            listIdControl = listIdControl,
            onClickItem = onClickItem,
            title = title,
            selectListTitle = selectListTitle,
            onClose = onClose,
            state = state,
            onEvent = onEvent
        )
    }
}


@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
private fun <T: IMenuItemEnum> SelectMenuWithListBottomContent(
    items: List<T>,
    animalId: Int,
    listIdControl: Int? = null,
    onClickItem: (T) -> Unit,
    onClose: () -> Unit,
    title: String? = null,
    selectListTitle: String = stringResource(id = R.string.add_to_list),
    state: SelectListMenuState,
    onEvent: (SelectListMenuAction) -> Unit,
){
    var showSelectListDia by rememberSaveable{
        mutableStateOf(false)
    }

    var newItems by remember {
        mutableStateOf(emptyList<IMenuItemEnum>())
    }

    LaunchedEffect(animalId,listIdControl){
        onEvent(SelectListMenuAction.LoadData(animalId, listIdControl))
    }

    LaunchedEffect(items,state.listMenuItems){
        newItems = mutableListOf<IMenuItemEnum>().apply {
            addAll(state.listMenuItems)
            addAll(items)
        }
    }

    ShowSelectBottomMenuItems(
        items = newItems,
        title = title,
        onClose = onClose,
        onClickItem = { menuItem ->
            when(menuItem){
                is SelectListMenuEnum -> {
                    when(menuItem){
                        SelectListMenuEnum.AddList -> {
                            showSelectListDia = true
                        }
                        SelectListMenuEnum.AddedList -> {
                            showSelectListDia = true
                        }
                        SelectListMenuEnum.AddFavorite -> {
                            onEvent(SelectListMenuAction.AddToFavorite(animalId))
                        }
                        SelectListMenuEnum.AddedFavorite -> {
                            onEvent(SelectListMenuAction.AddOrAskFavorite(animalId))
                        }
                    }
                }
                else -> {
                    (menuItem as? T)?.let(onClickItem)
                }
            }
        }
    )

    if(showSelectListDia){
        ShowSelectListBottom(
            animalId = animalId,
            listIdControl = listIdControl,
            title = selectListTitle,
            onClosed = { showSelectListDia = false }
        )
    }

    state.dialogEvent?.let { dialogEvent ->
        when(dialogEvent){
            is SelectListMenuDialogEvent.AskFavoriteDelete -> {
                ShowQuestionDialog(
                    title = stringResource(R.string.question_remove_list_from_favorite),
                    content = stringResource(R.string.affect_current_list),
                    onClosed = {onEvent(SelectListMenuAction.ShowDialog(null))},
                    onApproved = {
                        onEvent(SelectListMenuAction.AddToFavorite(dialogEvent.animalId))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun SelectMenuWithListBottomPreview() {
    SelectMenuWithListBottomContent(
        items = listOf(),
        animalId = 1,
        onClickItem = {},
        state = SelectListMenuState(),
        onEvent = {},
        onClose = {}
    )
}



