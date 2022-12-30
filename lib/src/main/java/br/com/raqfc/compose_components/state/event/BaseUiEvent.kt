package br.com.raqfc.compose_components.state.event

import androidx.annotation.StringRes
import br.com.raqfc.compose_components.R

sealed class BaseUiEvent {
    data class ShowProgressIndicator(@StringRes val content: Int = R.string.default_loading): BaseUiEvent()
    data class ShowError(val error: Error, val dismissible: Boolean = true, val onClick: () -> Unit = {}, val onDismissed: () -> Unit = {}): BaseUiEvent()
    data class ShowDialog(val title: Int, val content: Int, val dismissible: Boolean = true, val onClick: () -> Unit = {}, val onDismissed: () -> Unit = {}): BaseUiEvent()
}