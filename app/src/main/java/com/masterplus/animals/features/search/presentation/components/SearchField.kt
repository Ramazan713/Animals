package com.masterplus.animals.features.search.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.animals.R
import com.masterplus.animals.core.presentation.components.DefaultToolTip


@Composable
fun SearchField(
    query: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "text",
    showNavigateBack: Boolean = true,
    onBackPressed: (() -> Unit)? = null,
    onSearch: (() -> Unit)? = null,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var currentFocus by rememberSaveable {
        mutableStateOf(false)
    }

    val handleNavigateBack = remember {{
        onBackPressed?.invoke()
    }}

    BackHandler {
        handleNavigateBack()
    }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    Surface {
       BasicTextField(
           modifier = modifier
               .focusRequester(focusRequester)
               .onFocusEvent {
                   currentFocus = it.isFocused
               }
               .clip(MaterialTheme.shapes.large)
               .background(MaterialTheme.colorScheme.surfaceContainerHigh),
           value = query,
           onValueChange = onValueChange,
           textStyle = MaterialTheme.typography.bodyLarge.copy(
               color = MaterialTheme.colorScheme.onSurface
           ),
           keyboardOptions = KeyboardOptions(
               imeAction = ImeAction.Search
           ),
           keyboardActions = KeyboardActions(
               onSearch = {
                   focusRequester.freeFocus()
                   keyboardController?.hide()
                   onSearch?.invoke()
               }
           ),
           decorationBox = { innerTextField ->
               Row(
                   modifier = Modifier
                       .padding(vertical = 12.dp, horizontal = 4.dp),
                   verticalAlignment = Alignment.CenterVertically
               ) {
                   LeadingIcon(
                       showNavigateBack = showNavigateBack,
                       onSearchClick = { onSearch?.invoke() },
                       onNavigateBackClick = {
                           handleNavigateBack()
                       }
                   )
                   Box(
                       modifier = Modifier.weight(1f),
                       contentAlignment = Alignment.CenterStart
                   ) {
                       innerTextField()
                       if(query.isBlank()){
                           Text(
                               text = placeholder,
                               style = MaterialTheme.typography.bodyLarge.copy(
                                   color = MaterialTheme.colorScheme.onSurfaceVariant
                               )
                           )
                       }
                   }
                   SuffixClearIcon(
                       onClick = {
                           focusRequester.requestFocus()
                           onClear()
                       }
                   )
               }
           }
       )
    }
}



@Composable
private fun LeadingIcon(
    showNavigateBack: Boolean,
    onNavigateBackClick: () -> Unit,
    onSearchClick: () -> Unit,
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
        DefaultToolTip(tooltip = "Ara") {
            IconButton(onClick = onSearchClick) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Ara"
                )
            }
        }
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
            onValueChange = {},
            query = ""
        )
    }
}