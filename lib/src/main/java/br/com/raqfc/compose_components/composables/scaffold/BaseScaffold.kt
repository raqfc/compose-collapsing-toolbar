
package br.com.raqfc.compose_components.composables.scaffold

import ActionItem
import ActionMenu
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.raqfc.compose_components.state.event.BaseUiEvent
import br.com.raqfc.compose_components.composables.dialog.CustomAlertDialog
import br.com.raqfc.compose_components.R
import br.com.raqfc.compose_components.composables.dialog.ProgressDialog
import br.com.raqfc.compose_components.composables.scaffold.toolbar.CollapsingToolbarScopeInstance.road
import br.com.raqfc.compose_components.composables.scaffold.toolbar.ViewConfiguration

@Composable
fun BaseScaffold(
	modifier: Modifier = Modifier,
	showBackButton: Boolean = true,
	@StringRes titleRes: Int,
	actions: List<ActionItem> = listOf(),
	onUpClicked: () -> Unit = {},
	showDefaultAppBar: Boolean = true,
	topBar: (@Composable () -> Unit) = {},
	bottomBar: @Composable () -> Unit = {},
	snackbarHost: @Composable (SnackbarHostState) -> Unit = { },
	floatingActionButton: @Composable () -> Unit = {},
	floatingActionButtonPosition: FabPosition = FabPosition.End,
	backgroundColor: Color = MaterialTheme.colors.background,
	contentColor: Color = contentColorFor(backgroundColor),

	uiEvents: BaseUiEvent?,

	content: @Composable (PaddingValues) -> Unit
) {
    var mUiEvent by remember { mutableStateOf(uiEvents) }
    //todo descobrir erro interno da variavel

    Scaffold(
        modifier = modifier,
        topBar = {
            if (!showDefaultAppBar) topBar()
            else {
                var actionsRowWidth by remember { mutableStateOf(0) }
                TopAppBar(
                    modifier = Modifier
                        .onGloballyPositioned {
                            actionsRowWidth = it.size.width
                        },
                    navigationIcon = {
                        if (showBackButton)
                            IconButton(
                                onClick = onUpClicked,
                                modifier = Modifier
                                    .padding(12.dp),
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_back),
                                    contentDescription = "back button"
                                )
                            }
                    },
                    title = {
                        Text(
                            text = stringResource(id = titleRes),
                            modifier = Modifier
                                .road(
                                    ViewConfiguration(Alignment.CenterStart, 120.dp),
                                    ViewConfiguration(Alignment.BottomStart, 16.dp)
                                )
                                .padding(
                                    top = 16.dp,
                                    end = 16.dp,
                                    bottom = 16.dp
                                ),
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    },
                    actions = {
                        ActionMenu(
                            items = actions,
                            viewWidth = actionsRowWidth.toFloat()
                        )
                    }
                )

            }
        },
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
    ) {

        when (mUiEvent) {
            is BaseUiEvent.ShowDialog -> {}
            is BaseUiEvent.ShowError -> {
                (mUiEvent as BaseUiEvent.ShowError).let { e ->
                    CustomAlertDialog(
//                        content = e.error.errorMessage(),
                        onClick = {
                            e.onClick()
                            mUiEvent = null

                        },
                        onDismiss = {
                            if (e.dismissible) {
                                e.onDismissed()
                                mUiEvent = null
                            }
                        }
                    )
                }
            }
            is BaseUiEvent.ShowProgressIndicator -> {
                ProgressDialog()
            }
            null -> {}
        }

        content(it)
    }
}