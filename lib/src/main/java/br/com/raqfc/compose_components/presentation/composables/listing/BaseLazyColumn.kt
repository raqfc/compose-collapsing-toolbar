package br.com.raqfc.compose_components.presentation.composables.listing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.raqfc.compose_components.composables.animations.Transitions
import br.com.raqfc.compose_components.theme.AppTheme

@Composable
fun BaseLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = AppTheme.dimensions.padding3,
        vertical = AppTheme.dimensions.padding2
    ),
    showScrollUpButton: Boolean = true,
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(AppTheme.dimensions.padding3),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit
) {
    Box(modifier = modifier) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
            content = content,
        )

        if (showScrollUpButton) {
            val showButton by remember {
                derivedStateOf { state.firstVisibleItemIndex > 1 }
            }

            AnimatedVisibility(
                visible = showButton,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset((1).dp)
                    .padding(AppTheme.dimensions.padding4),
                enter = Transitions.slideInFromBottom() + fadeIn(),
                exit = Transitions.slideOutToBottom() + fadeOut(),
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset((-1).dp)
                ) {
                    ScrollUpButton(state)
                }
            }
        }
    }

}