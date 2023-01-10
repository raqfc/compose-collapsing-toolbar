import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

// Essentially a wrapper around a lambda function to give it a name and icon
// akin to Android menu XML entries.
// As an item on the action bar, the action will be displayed with an IconButton
// with the given icon, if not null. Otherwise, the string from the name resource is used.
// In overflow menu, item will always be displayed as text.
data class ActionItem(
    @StringRes
    val nameRes: Int,
    val icon: ImageVector? = null,
    val overflowMode: OverflowMode = OverflowMode.IF_NECESSARY,
    val doAction: () -> Unit,
    val iconColor: Color? = null
) {
    // allow 'calling' the action like a function
    operator fun invoke() = doAction()
}

// Whether action items are allowed to overflow into a dropdown menu - or NOT SHOWN to hide
enum class OverflowMode {
    IF_NECESSARY, ALWAYS_OVERFLOW, NOT_SHOWN
}

// Note: should be used in a RowScope
@Composable
fun ActionMenu(
    items: List<ActionItem>,
    viewWidth: Float, // includes overflow menu icon; may be overridden by NEVER_OVERFLOW
    menuVisible: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    if (items.isEmpty()) {
        return
    }
    // decide how many action items to show as icons
    val (appbarActions, overflowActions) = remember(items, viewWidth) {
        separateIntoIconAndOverflow(items, viewWidth)
    }

    for (item in appbarActions) {
        key(item.hashCode()) {
            val name = stringResource(item.nameRes)
            if (item.icon != null) {
                IconButton(onClick = {
                    item.doAction()
                }) {
                    Icon(imageVector = item.icon, contentDescription = "", tint = item.iconColor ?: MaterialTheme.colorScheme.onPrimary)
                }
            } else {
                TextButton(onClick = item.doAction) {
                    Text(
                        text = name,
                        color = item.iconColor ?: MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }

    if (overflowActions.isNotEmpty()) {
        IconButton(onClick = { menuVisible.value = true }) {
            Icon(Icons.Default.MoreVert, "More actions")
        }
        DropdownMenu(
            expanded = menuVisible.value,
            onDismissRequest = { menuVisible.value = false },
        ) {
            for (item in overflowActions) {
                key(item.hashCode()) {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = item.nameRes)) },
                        leadingIcon = if (item.icon != null) {
                            {
                                Icon(
                                    modifier = Modifier.padding(end = 8.dp),
                                    imageVector = item.icon,
                                    contentDescription = stringResource(id = item.nameRes)
                                )
                            }
                        } else null,
                        onClick = {
                            menuVisible.value = false
                            item.doAction() })
                }
            }
        }
    }
}

private fun separateIntoIconAndOverflow(
    items: List<ActionItem>,
    viewWidth: Float
): Pair<List<ActionItem>, List<ActionItem>> {
    var (overflowCount, preferIconCount) = Pair(0, 0)
    Log.e("separateIntoIconAndOverflow.viewWidth: ",viewWidth.toString())
    val iconActions = ArrayList<ActionItem>()
    val overflowActions = ArrayList<ActionItem>()
    var usedWidth = 0f

    for (item in items) {
        when (item.overflowMode) {
            OverflowMode.IF_NECESSARY -> preferIconCount++
            OverflowMode.ALWAYS_OVERFLOW -> overflowCount++
            OverflowMode.NOT_SHOWN -> {}
        }
    }

    fun calculateButtonWidth(hasIcon: Boolean): Float {
        return if(hasIcon) 148.0.dp.value else 86.dp.value
    }

    fun availableWidth(): Float {
        return viewWidth - usedWidth
    }

    fun checkAndRemoveIcons() {
        if(usedWidth > viewWidth && iconActions.size > 0) {
            overflowActions.add(iconActions.removeLast())
        }
    }



    if(overflowCount > 0) {
        usedWidth += calculateButtonWidth(false)
    }

    for (item in items) {
        when (item.overflowMode) {
            OverflowMode.ALWAYS_OVERFLOW -> {
                overflowActions.add(item)
            }
            OverflowMode.IF_NECESSARY -> {
                if (calculateButtonWidth(item.icon != null) <= availableWidth()) {
                    usedWidth += calculateButtonWidth(item.icon != null)
                    iconActions.add(item)
                } else {
                    if(overflowCount == 0) {
                        overflowCount +=1
                        usedWidth += calculateButtonWidth(true)
                    }
                    overflowActions.add(item)
                }
                checkAndRemoveIcons()
            }
            OverflowMode.NOT_SHOWN -> {
                // skip
            }
        }
    }
    return Pair(iconActions, overflowActions)
}