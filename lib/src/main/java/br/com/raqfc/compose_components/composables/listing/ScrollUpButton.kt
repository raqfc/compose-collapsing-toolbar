package br.com.justworks.volan2.common.presentation.composables.listing

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import br.com.justworks.volan2.ui.theme.AppTheme
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