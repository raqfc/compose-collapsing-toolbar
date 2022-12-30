package br.com.raqfc.compose_components.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

object AppTheme {
//	val colors: VolanAppColors
//		@Composable
//		@ReadOnlyComposable
//		get() = LocalColors.current
	val dimensions: AppDimensions
		@Composable
		@ReadOnlyComposable
		get() = LocalDimensions.current
}