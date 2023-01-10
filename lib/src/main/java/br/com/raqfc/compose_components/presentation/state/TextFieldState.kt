package br.com.raqfc.compose_components.presentation.state

import androidx.annotation.StringRes
import androidx.compose.ui.focus.FocusRequester
import br.com.raqfc.compose_components.R

data class TextFieldState(
    var text: String = "",
    @StringRes var hint: Int,
    var isFormRequired: Boolean = false,
    var forceValidate: Boolean = false,
    var validate: Boolean = false,
    @StringRes var validationErrorMessage: Int = R.string.default_validation_error_message,
    @StringRes var emptyMessage: Int = R.string.default_empty_error_message,
    var focusRequester: FocusRequester = FocusRequester(),
    var validator: (String) -> Boolean = { true }
)