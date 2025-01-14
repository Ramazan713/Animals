package com.masterplus.animals.features.search.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.animals.R
import com.masterplus.animals.core.presentation.components.DefaultToolTip
import com.masterplus.animals.features.search.domain.enums.SearchType


@Composable
fun SearchField(
    queryState: TextFieldState,
    onClear: () -> Unit,
    onSearch: () -> Unit,
    searchType: SearchType,
    modifier: Modifier = Modifier,
    placeholder: String = "text",
    onBackPressed: (() -> Unit)? = null,
    showNavigateBack: Boolean = onBackPressed != null,
    isSearching: Boolean = false
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val handleNavigateBack = remember {{
        onBackPressed?.invoke()
    }}

    BackHandler(onBackPressed != null) {
        handleNavigateBack()
    }

    LaunchedEffect(key1 = Unit) {
        if(queryState.text.isEmpty()){
            focusRequester.requestFocus()
        }
    }


    Surface {

       BasicTextField(
           modifier = modifier
               .focusRequester(focusRequester)
               .clip(MaterialTheme.shapes.large)
               .background(MaterialTheme.colorScheme.surfaceContainerHigh),
           state = queryState,
           textStyle = MaterialTheme.typography.bodyLarge.copy(
               color = MaterialTheme.colorScheme.onSurface
           ),
           keyboardOptions = KeyboardOptions(
               imeAction = ImeAction.Search
           ),
           onKeyboardAction = {
               it()
               focusManager.clearFocus(true)
               keyboardController?.hide()
               onSearch.invoke()
           },
           decorator = { innerTextField ->
               Row(
                   modifier = Modifier
                       .padding(vertical = 12.dp, horizontal = 4.dp),
                   verticalAlignment = Alignment.CenterVertically
               ) {
                   LeadingIcon(
                       showNavigateBack = showNavigateBack,
                       onNavigateBackClick = {
                           handleNavigateBack()
                       }
                   )
                   Box(
                       modifier = Modifier.weight(1f),
                       contentAlignment = Alignment.CenterStart
                   ) {
                       innerTextField()
                       if(queryState.text.isBlank()){
                           Text(
                               text = placeholder,
                               style = MaterialTheme.typography.bodyLarge.copy(
                                   color = MaterialTheme.colorScheme.onSurfaceVariant
                               )
                           )
                       }
                   }
                   AnimatedVisibility(searchType.isServer) {
                       SearchTextIconButton(
                           onClick = onSearch,
                           isSearching = isSearching
                       )
                   }
                   AnimatedVisibility(searchType.isLocal) {
                       SuffixClearIcon(
                           onClick = {
                               focusRequester.requestFocus()
                               onClear()
                           }
                       )
                   }
               }
           }
       )
    }
}

@Composable
private fun SearchTextIconButton(
    onClick: () -> Unit,
    isSearching: Boolean
) {
    AnimatedContent(
        targetState = isSearching,
        label = "searching"
    ) { searching->
        if(searching){
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(end = 8.dp)
            )
        }else{
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable(
                        enabled = !isSearching
                    ) { onClick() }
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Ara"
                )
                Text("Search")
            }
        }
    }

}


@Composable
private fun LeadingIcon(
    showNavigateBack: Boolean,
    onNavigateBackClick: () -> Unit,
) {
    if(showNavigateBack){
        DefaultToolTip(tooltip = stringResource(id = R.string.back)) {
            IconButton(onClick = onNavigateBackClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back)
                )
            }
        }
    }else{
        Icon(
            Icons.Default.Search,
            modifier = Modifier.padding(12.dp),
            contentDescription = "Ara"
        )
    }
}

@Composable
private fun SuffixClearIcon(
    onClick: () -> Unit
) {
    DefaultToolTip(tooltip = stringResource(id = R.string.clear_v)) {
        IconButton(onClick = onClick) {
            Icon(
                Icons.Default.Clear,
                contentDescription = stringResource(id = R.string.clear_v),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchFieldPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        SearchField(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            onBackPressed = {},

            onSearch = {},
            onClear = {},

            queryState = TextFieldState(),
            searchType = SearchType.Server,
            isSearching = true
        )
    }
}