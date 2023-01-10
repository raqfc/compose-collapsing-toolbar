package br.com.raqfc.compose_components.presentation.state

import androidx.annotation.StringRes

data class ButtonState(
    @StringRes var text: Int,
    val enabled: Boolean = false,
    val loading: Boolean = false,
)