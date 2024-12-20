package com.masterplus.animals.core.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.presentation.selections.CustomDropdownBarMenu

enum class TopBarType{
    DEFAULT, LARGE
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    topBarType: TopBarType = TopBarType.DEFAULT,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigateBack: (() -> Unit)? = null,
    actions: @Composable() (RowScope.() -> Unit) = {}
) {
    DefaultTopBarImpl<Nothing>(
        title = title,
        modifier = modifier,
        topBarType = topBarType,
        scrollBehavior = scrollBehavior,
        onNavigateBack = onNavigateBack,
        menuItems = listOf(),
        onMenuItemClick = null,
        actions = actions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopBar(
    title: String,
    modifier: Modifier = Modifier,
    topBarType: TopBarType = TopBarType.DEFAULT,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigateBack: (() -> Unit)? = null,
    actions: @Composable() (RowScope.() -> Unit) = {}
) {
    DefaultTopBarImpl(
        title = { Text(title) },
        modifier = modifier,
        topBarType = topBarType,
        scrollBehavior = scrollBehavior,
        onNavigateBack = onNavigateBack,
        menuItems = listOf(),
        onMenuItemClick = null,
        actions = actions
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : IMenuItemEnum> DefaultTopBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    topBarType: TopBarType = TopBarType.DEFAULT,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigateBack: (() -> Unit)? = null,
    menuItems: List<T>,
    onMenuItemClick: (T) -> Unit,
    actions: @Composable() (RowScope.() -> Unit) = {}
) {
    DefaultTopBarImpl(
        title = title,
        modifier = modifier,
        topBarType = topBarType,
        scrollBehavior = scrollBehavior,
        onNavigateBack = onNavigateBack,
        menuItems = menuItems,
        onMenuItemClick = onMenuItemClick,
        actions = actions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : IMenuItemEnum> DefaultTopBar(
    title: String,
    modifier: Modifier = Modifier,
    topBarType: TopBarType = TopBarType.DEFAULT,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigateBack: (() -> Unit)? = null,
    menuItems: List<T>,
    onMenuItemClick: (T) -> Unit,
    actions: @Composable() (RowScope.() -> Unit) = {}
) {
    DefaultTopBarImpl(
        title = { Text(title) },
        modifier = modifier,
        topBarType = topBarType,
        scrollBehavior = scrollBehavior,
        onNavigateBack = onNavigateBack,
        menuItems = menuItems,
        onMenuItemClick = onMenuItemClick,
        actions = actions
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : IMenuItemEnum> DefaultTopBarImpl(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    topBarType: TopBarType = TopBarType.DEFAULT,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigateBack: (() -> Unit)? = null,
    menuItems: List<T>,
    onMenuItemClick: ((T) -> Unit)?,
    actions: @Composable() (RowScope.() -> Unit) = {}
) {
    if(topBarType == TopBarType.DEFAULT){
        TopAppBar(
            title = title,
            modifier = modifier,
            scrollBehavior = scrollBehavior,
            navigationIcon = {
                if (onNavigateBack != null) {
                    NavigationBackIcon(onNavigateBack = onNavigateBack)
                }
            },
            actions = {
                actions()
                onMenuItemClick?.let { onMenuItemClick ->
                    CustomDropdownBarMenu(
                        items = menuItems,
                        onItemChange = onMenuItemClick
                    )
                }
            },
        )
    }else{
        LargeTopAppBar(
            title = title,
            modifier = modifier,
            scrollBehavior = scrollBehavior,
            navigationIcon = {
                if (onNavigateBack != null) {
                    NavigationBackIcon(onNavigateBack = onNavigateBack)
                }
            },
            actions = {
                actions()
                onMenuItemClick?.let { onMenuItemClick ->
                    CustomDropdownBarMenu(
                        items = menuItems,
                        onItemChange = onMenuItemClick
                    )
                }
            },
        )
    }
}
