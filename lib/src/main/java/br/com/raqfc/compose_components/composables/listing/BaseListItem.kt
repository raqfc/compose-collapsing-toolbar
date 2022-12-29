package br.com.raqfc.compose_components.composables.listing

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun BaseListItem(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onClick: () -> Unit = {},
    content: @Composable RowScope.() -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        onClick = onClick,
        colors = cardColors(
            //todo selection color
            containerColor = if (checked) Color.Black.copy(alpha = 0.4f) else MaterialTheme.colors.surfaceColorAtElevation(
                AppTheme.dimensions.elevation1
            )
        )
    ) {
        Row(
            Modifier
                .defaultMinSize(
                    minHeight = AppTheme.dimensions.cardMinHeight
                )
                .padding(
                    horizontal = AppTheme.dimensions.padding4,
                    vertical = AppTheme.dimensions.padding3
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}