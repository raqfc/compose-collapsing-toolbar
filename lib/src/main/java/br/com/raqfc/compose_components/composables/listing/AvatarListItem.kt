package br.com.justworks.volan2.common.presentation.composables.listing

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.raqfc.compose_components.composables.listing.BaseListItem

@Composable
fun AvatarListItem(
    isAssetImage: Boolean,
    modifier: Modifier = Modifier,
    srcUrl: String? = null,
    @DrawableRes imgRes: Int? = null,
    checkable: Boolean = false,
    checked: Boolean = false,
    onClick: () -> Unit = {},
    content: @Composable RowScope.() -> Unit = {}
) {
    BaseListItem(
        checked = checkable && checked,
        modifier = modifier,
        onClick = onClick
    ) {

        if(isAssetImage)
            CircleImageView(
                checked = checkable && checked,
                imageRes = imgRes
            )
        else
            CircleImageView(
                checked = checkable && checked,
                srcUrl = srcUrl
            )
        content()
    }
}