package br.com.justworks.volan2.common.presentation

import androidx.annotation.StringRes
import br.com.justworks.volan2.R
import br.com.justworks.volan2.common.domain.validation.Failure

sealed class BaseUiEvent {
    data class ShowProgressIndicator(@StringRes val content: Int = R.string.default_loading): BaseUiEvent()
    data class ShowError(val error: Failure, val dismissible: Boolean = true, val onClick: () -> Unit = {}, val onDismissed: () -> Unit = {}): BaseUiEvent()
    data class ShowDialog(val title: Int, val content: Int, val dismissible: Boolean = true, val onClick: () -> Unit = {}, val onDismissed: () -> Unit = {}): BaseUiEvent()
}