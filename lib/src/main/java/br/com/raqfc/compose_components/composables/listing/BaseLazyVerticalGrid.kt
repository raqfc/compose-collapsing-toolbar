package br.com.raqfc.compose_components.composables.listing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.justworks.volan2.common.presentation.composables.animations.Transitions
import br.com.raqfc.compose_components.theme.AppTheme


@Composable
fun BaseLazyVerticalGrid(
    columns: GridCells,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    showScrollUpButton: Boolean = true,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyGridScope.() -> Unit
) {
    Box(modifier = modifier) {

        LazyVerticalGrid(
            columns = columns,
            modifier = modifier,
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            horizontalArrangement = horizontalArrangement,
            verticalArrangement = verticalArrangement,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
            content = content
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