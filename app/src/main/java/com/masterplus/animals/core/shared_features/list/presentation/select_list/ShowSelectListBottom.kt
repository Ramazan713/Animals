package com.masterplus.animals.core.shared_features.list.presentation.select_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.animals.R
import com.masterplus.animals.core.presentation.components.SharedHeader
import com.masterplus.animals.core.presentation.dialogs.ShowGetTextDialog
import com.masterplus.animals.core.presentation.dialogs.ShowQuestionDialog
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.shared_features.list.presentation.select_list.components.SelectListItem
import org.koin.androidx.compose.koinViewModel


@ExperimentalFoundationApi
@Composable
fun ShowSelectListBottom(
    speciesId: Int,
    listIdControl: Int? = null,
    onClosed: () -> Unit,
    title: String = stringResource(id = R.string.add_to_list),
    listViewModel: SelectListViewModel = koinViewModel()
){
    val state by listViewModel.state.collectAsStateWithLifecycle()

    ShowSelectListBottom(
        speciesId = speciesId,
        listIdControl = listIdControl,
        onClosed = onClosed,
        title = title,
        state = state,
        onEvent = listViewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ShowSelectListBottom(
    speciesId: Int,
    state: SelectListState,
    onClosed: () -> Unit,
    listIdControl: Int? = null,
    title: String = stringResource(id = R.string.add_to_list),
    onEvent: (SelectListAction) -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onClosed) {
        SelectListBottomContent(
            speciesId = speciesId,
            listIdControl = listIdControl,
            onClosed = onClosed,
            title = title,
            state = state,
            onEvent = onEvent
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalFoundationApi
@Composable
private fun SelectListBottomContent(
    speciesId: Int,
    state: SelectListState,
    onClosed: () -> Unit,
    listIdControl: Int? = null,
    title: String = stringResource(id = R.string.add_to_list),
    onEvent: (SelectListAction) -> Unit,
){
    var showTitleDialog by rememberSaveable{
        mutableStateOf(false)
    }

    LaunchedEffect(speciesId,listIdControl){
        onEvent(SelectListAction.LoadData(speciesId, listIdControl))
    }

    Column(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        SharedHeader(
            modifier = Modifier.padding(top = 2.dp, bottom = 4.dp),
            title = title,
            onIconClick = onClosed
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                OutlinedButton(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth(),
                    onClick = { showTitleDialog = true }
                ) {
                    Text(text = stringResource(R.string.add_list),)
                }
            }

            item {
                if(state.items.isEmpty()){
                    Text(
                        stringResource(R.string.list_empty_text),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 70.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            items(
                state.items,
                key = {item->item.listView.id?:0}
            ){item->
                SelectListItem(
                    selectableListView = item,
                    isSelected = state.listIdControl == item.listView.id,
                    onChecked = {
                        onEvent(SelectListAction.AddOrAskToList(speciesId, item))
                    },
                    modifier = Modifier
                        .padding(vertical = 1.dp)
                        .fillMaxWidth()
                )
            }

        }
    }

    if(showTitleDialog){
        ShowGetTextDialog(
            title = stringResource(R.string.enter_title),
            onClosed = {showTitleDialog = false},
            onApproved = {onEvent(SelectListAction.NewList(it))},
        )
    }

    state.dialogEvent?.let { dialogEvent ->
        when(dialogEvent){
            is SelectListDialogEvent.AskListDelete -> {
                ShowQuestionDialog(
                    title = stringResource(R.string.question_remove_item_from_list),
                    content = stringResource(R.string.affect_current_list),
                    onClosed = {onEvent(SelectListAction.ShowDialog(null))},
                    onApproved = {
                        onEvent(SelectListAction.AddToList(dialogEvent.animalId, dialogEvent.listView))
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun SelectListBottomContentPreview() {
    val items = SampleDatas.selectableListViewArr

    SelectListBottomContent(
        speciesId = 1,
        listIdControl = 2,
        onClosed = {},
        state = SelectListState(items = items, listIdControl = 2),
        onEvent = {},
    )
}

