/*
 * Copyright (c) 2021 onebone <me@onebone.me>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */
package br.com.raqfc.compose_components.composables.scaffold


import ActionItem
import ActionMenu
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.raqfc.compose_components.state.event.BaseUiEvent
import br.com.raqfc.compose_components.composables.dialog.CustomAlertDialog
import br.com.raqfc.compose_components.composables.scaffold.toolbar.CollapsingToolbar
import br.com.raqfc.compose_components.composables.scaffold.toolbar.CollapsingToolbarScope
import br.com.raqfc.compose_components.composables.scaffold.toolbar.CollapsingToolbarState
import br.com.raqfc.compose_components.composables.scaffold.toolbar.ScrollStrategy
import br.com.raqfc.compose_components.composables.scaffold.toolbar.ViewConfiguration
import br.com.raqfc.compose_components.composables.scaffold.toolbar.rememberCollapsingToolbarState
import br.com.raqfc.compose_components.composables.dialog.ProgressDialog
import kotlin.math.max

@Stable
class CollapsingToolbarScaffoldState(
    val toolbarState: CollapsingToolbarState,
    initialOffsetY: Int = 0
) {
    val offsetY: Int
        get() = offsetYState.value

    internal val offsetYState = mutableStateOf(initialOffsetY)
}

private class CollapsingToolbarScaffoldStateSaver : Saver<CollapsingToolbarScaffoldState, Bundle> {
    override fun restore(value: Bundle): CollapsingToolbarScaffoldState =
        CollapsingToolbarScaffoldState(
            CollapsingToolbarState(value.getInt("height", Int.MAX_VALUE)),
            value.getInt("offsetY", 0)
        )

    override fun SaverScope.save(value: CollapsingToolbarScaffoldState): Bundle =
        Bundle().apply {
            putInt("height", value.toolbarState.height)
            putInt("offsetY", value.offsetY)
        }
}

@Composable
fun rememberCollapsingToolbarScaffoldState(
    toolbarState: CollapsingToolbarState = rememberCollapsingToolbarState()
): CollapsingToolbarScaffoldState {
    return rememberSaveable(toolbarState, saver = CollapsingToolbarScaffoldStateSaver()) {
        CollapsingToolbarScaffoldState(toolbarState)
    }
}

interface CollapsingToolbarScaffoldScope {
    fun Modifier.align(alignment: Alignment): Modifier
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CollapsingToolbarScaffold(
	modifier: Modifier = Modifier,
	toolbarModifier: Modifier = Modifier,

	showUpButton: Boolean = true,
	upIcon: ImageVector = Icons.Default.ArrowBack,
	title: String? = null,
	@StringRes titleRes: Int? = null,
	actions: List<ActionItem> = listOf(),
	onUpClicked: () -> Unit = {},
	toolbar: (@Composable CollapsingToolbarScope.() -> Unit)? = null,
	bottomBar: (@Composable () -> Unit)? = null,
	snackbarHost: @Composable (SnackbarHostState) -> Unit = { },
	floatingActionButton: (@Composable () -> Unit)? = null,
	floatingActionButtonPosition: FabPosition = FabPosition.End,
	backgroundColor: Color = MaterialTheme.colors.background,
	contentColor: Color = contentColorFor(backgroundColor),

	uiEvents: BaseUiEvent? = null,

	state: CollapsingToolbarScaffoldState,
	scrollStrategy: ScrollStrategy = ScrollStrategy.ExitUntilCollapsed,
	enabled: Boolean = true,
	body: @Composable CollapsingToolbarScaffoldScope.() -> Unit
) {
    val flingBehavior = ScrollableDefaults.flingBehavior()
    val layoutDirection = LocalLayoutDirection.current

    val nestedScrollConnection = remember(scrollStrategy, state) {
        scrollStrategy.create(state.offsetYState, state.toolbarState, flingBehavior)
    }

    val toolbarState = state.toolbarState
    var mUiEvent by remember { mutableStateOf(uiEvents) }

    var toolbarWidth by remember { mutableStateOf(0) }

    Layout(
        content = {
            CollapsingToolbar(
                modifier = toolbarModifier
                    .defaultMinSize(minHeight = 48.0.dp)
                    .onGloballyPositioned {
                        toolbarWidth = it.size.width
                    },
                collapsingToolbarState = toolbarState,
            ) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colors.surface)
                        .fillMaxWidth()
                        .height(112.0.dp)// TopAppBarMediumTokens.ContainerHeight
                        .pin()
                )

                if (toolbar != null) {
                    toolbar()
                } else {
                    var actionsRowWidth by remember { mutableStateOf(0) }

                    val textSize = (18 + (30 - 18) * state.toolbarState.progress).sp
                    val titleBottomPadding = (16 * (1 - state.toolbarState.progress)).dp
                    var actionsFraction by remember { mutableStateOf(0.9f) }

                    var titleSize = IntSize(40, 40)
                    var titlePosition = Offset(0f, 0f)

                    var iconsSize = IntSize(40, 40)
                    var iconsPosition = Offset(0f, 0f)

                    fun resizeIcons() {
                        //if title is inline with actions row
                        val newActionsFraction =
                            if ((iconsPosition.y + iconsSize.height) > titlePosition.y) {
                                //consider title size when resizing row
                                1 - ((titlePosition.x + titleSize.width + 16.0.dp.value) / toolbarWidth)

                            } else {
                                //only consider backbutton width
                                1 - (148.0.dp.value / toolbarWidth)

                            }
                        if (newActionsFraction != actionsFraction) {
                            actionsFraction = newActionsFraction
                        }


                    }

                    Text(
                        text = title ?: if(titleRes != null) stringResource(id = titleRes) else "",
                        modifier = Modifier
                            .road(
                                ViewConfiguration(Alignment.CenterStart, 120.dp),
                                ViewConfiguration(Alignment.BottomStart, 16.dp)
                            )
                            .padding(
                                top = 16.dp,
                                end = 16.dp,
                                bottom = titleBottomPadding
                            )
                            .onGloballyPositioned {
                                titleSize = it.size
                                titlePosition = it.positionInParent()
                            },
                        color = MaterialTheme.colors.onPrimary,
                        fontSize = textSize
                    )

                    if (showUpButton)
                        IconButton(
                            onClick = onUpClicked,
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .pin(),
                        ) {
                            Icon(
                                imageVector = upIcon,
                                tint = MaterialTheme.colors.onPrimary,
                                contentDescription = "back button"
                            )
                        }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(actionsFraction)
                            .animateContentSize(
                                animationSpec = spring(stiffness = Spring.StiffnessLow)
                            )
                            .onGloballyPositioned {
                                var changed = false
                                if (it.size.width != actionsRowWidth)
                                    actionsRowWidth = it.size.width
                                if (iconsSize.height != it.size.height || iconsSize.width != it.size.width) {
                                    iconsSize = it.size
                                    changed = true
                                }
                                if (iconsPosition.y != it.positionInParent().y || iconsPosition.x != it.positionInParent().x) {
                                    iconsPosition = it.positionInParent()
                                    changed = true
                                }
                                if (changed)
                                    resizeIcons()
                            }
                            .pin(Alignment.TopEnd),
                        horizontalArrangement = Arrangement.End
                    ) {
                        ActionMenu(
                            items = actions,
                            viewWidth = actionsRowWidth.toFloat()
                        )
                    }
                }
            }

            Scaffold(
                modifier,
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                topBar = { },
                bottomBar = bottomBar ?: {},
                floatingActionButton = floatingActionButton ?: {},
                snackbarHost = snackbarHost,
                floatingActionButtonPosition = floatingActionButtonPosition,
            ) {
                Box(modifier = Modifier.padding(it)) {
                    CollapsingToolbarScaffoldScopeInstance.body()
                }

                when (mUiEvent) {
                    is BaseUiEvent.ShowDialog -> {}
                    is BaseUiEvent.ShowError -> {
                        (mUiEvent as BaseUiEvent.ShowError).let { e ->
                            CustomAlertDialog(
//                                content = e.error.errorMessage(),//todo
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
            }

        },
        modifier = modifier
            .then(
                if (enabled) {
                    Modifier.nestedScroll(nestedScrollConnection)
                } else {
                    Modifier
                }
            )
    ) { measurables, constraints ->
        check(measurables.size >= 2) {
            "the number of children should be at least 2: toolbar, (at least one) body"
        }

        val toolbarConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0
        )
        val toolbarPlaceable = measurables[0].measure(toolbarConstraints)
        val bodyConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
            maxHeight = when (scrollStrategy) {
                ScrollStrategy.ExitUntilCollapsed ->
                    ((constraints.maxHeight - toolbarState.minHeight) - (toolbarState.height - toolbarState.minHeight)).coerceAtLeast(
                        toolbarPlaceable.height
                    )

                ScrollStrategy.EnterAlways, ScrollStrategy.EnterAlwaysCollapsed ->
                    constraints.maxHeight
            }
        )


        val bodyMeasurables = measurables.subList(1, measurables.size)
        val childrenAlignments = bodyMeasurables.map {
            (it.parentData as? ScaffoldParentData)?.alignment
        }
        val bodyPlaceables = bodyMeasurables.map {
            it.measure(bodyConstraints)
        }

        val toolbarHeight = toolbarPlaceable.height

        val width = max(
            toolbarPlaceable.width,
            bodyPlaceables.maxOfOrNull { it.width } ?: 0
        ).coerceIn(constraints.minWidth, constraints.maxWidth)
        val height = max(
            toolbarHeight,
            bodyPlaceables.maxOfOrNull { it.height } ?: 0
        ).coerceIn(constraints.minHeight, constraints.maxHeight)

        layout(width, height) {
            bodyPlaceables.forEachIndexed { index, placeable ->
                val alignment = childrenAlignments[index]

                if (alignment == null) {
                    placeable.placeRelative(0, toolbarHeight + state.offsetY)
                } else {
                    val offset = alignment.align(
                        size = IntSize(placeable.width, placeable.height),
                        space = IntSize(width, height),
                        layoutDirection = layoutDirection
                    )
                    placeable.place(offset)
                }
            }
            toolbarPlaceable.placeRelative(0, state.offsetY)
        }
    }
}

internal object CollapsingToolbarScaffoldScopeInstance : CollapsingToolbarScaffoldScope {
    override fun Modifier.align(alignment: Alignment): Modifier =
        this.then(ScaffoldChildAlignmentModifier(alignment))
}

private class ScaffoldChildAlignmentModifier(
    private val alignment: Alignment
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any {
        return (parentData as? ScaffoldParentData) ?: ScaffoldParentData(
            alignment
        )
    }
}

private data class ScaffoldParentData(
    var alignment: Alignment? = null
)