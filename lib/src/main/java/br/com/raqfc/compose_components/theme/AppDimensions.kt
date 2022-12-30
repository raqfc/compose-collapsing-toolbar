package br.com.raqfc.compose_components.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AppDimensions(
	val padding0: Dp = 0.dp,
	val padding1: Dp = 2.dp,
	val padding2: Dp = 4.dp,
	val padding3: Dp = 6.dp,
	val padding4: Dp = 8.dp,
	val padding5: Dp = 12.dp,
	val padding6: Dp = 16.dp,
	val padding7: Dp = 20.dp,
	val padding8: Dp = 24.dp,
	val padding9: Dp = 30.dp,
	val padding10: Dp = 32.dp,

	val actionSize: Dp = 32.dp,

	val formContentHorizontalPadding: Dp = padding4,
	val listTopPadding: Dp = padding5,

	val cardMinHeight: Dp = 60.dp,
	val defaultCircleAvatarSize: Dp = 60.dp,

	val scrollUpButtonSize: Dp = 60.dp,

	val elevation0: Dp = 0.0.dp,
	val elevation1: Dp = 1.0.dp,
	val elevation2: Dp = 3.0.dp,
	val elevation3: Dp = 6.0.dp,
	val elevation4: Dp = 8.0.dp,
	val elevation5: Dp = 12.0.dp,
//    val elevation1: Dp = 4.dp,
//    val elevation2: Dp = 6.dp,
//    val elevation3: Dp = 8.dp,
)

internal val LocalDimensions = staticCompositionLocalOf { AppDimensions() }

@Composable
fun toDp(value: Int): Dp {
	return with(LocalDensity.current) { value.toDp() }
}
@Composable
fun toDp(value: Float): Dp {
	return with(LocalDensity.current) { value.toDp() }
}
@Composable
fun toDp(value: Double): Dp {
	return with(LocalDensity.current) { value.toFloat().toDp() }
}