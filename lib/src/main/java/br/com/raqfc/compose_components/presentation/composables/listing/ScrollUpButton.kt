package br.com.raqfc.compose_components.presentation.composables.listing

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import br.com.raqfc.compose_components.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun ScrollUpButton(state: LazyListState) {
    val coroutineScope = rememberCoroutineScope()

    FloatingActionButton(
        onClick = {
            coroutineScope.launch {
                state.animateScrollToItem(0)
            }
        },
        modifier = Modifier
            .size(AppTheme.dimensions.scrollUpButtonSize)
            .padding(AppTheme.dimensions.padding4)
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            contentDescription = "Move up"
        )
    }
}
@Composable
fun ScrollUpButton(state: LazyGridState) {
    val coroutineScope = rememberCoroutineScope()

    FloatingActionButton(
        onClick = {
            coroutineScope.launch {
                state.animateScrollToItem(0)
            }
        },
        modifier = Modifier
            .size(AppTheme.dimensions.scrollUpButtonSize)
            .padding(AppTheme.dimensions.padding4)
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            contentDescription = "Move up"
        )
    }
}