package br.com.raqfc.compose_components.composables

import android.util.Patterns
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.justworks.volan2.common.presentation.text_input_transformation.MaskVisualTransformation
import br.com.raqfc.compose_components.state.TextFieldState

@OptIn(ExperimentalMaterial3Api::class)
@Preview("DefaultTextField")
@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    forceValidate: Boolean = false,
    focusRequester: FocusRequester? = null,
    enabled: Boolean = true,
    mask: FieldMask? = null,
    emptyMessage: String = "Não pode ser vazio",
    validate: Boolean = false,
    validator: (String) -> Boolean = { true },
    validationErrorMessage: String = "Campo inválido",
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    isFormRequired: Boolean = false,
    labelText: String? = null,
    label: @Composable() (() -> Unit)? = null,
    placeholder: @Composable() (() -> Unit)? = null,
    leadingIcon: @Composable() (() -> Unit)? = null,
    trailingIcon: @Composable() (() -> Unit)? = null,
    isPassword: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape =
        MaterialTheme.shapes.small.copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize),
    state: TextFieldState? = null,
) {
    val mEmptyMessage = state?.emptyMessage?.let { stringResource(id = it) } ?: emptyMessage
    val mValidator = state?.validator ?: validator
    val mValidationErrorMessage = state?.validationErrorMessage?.let { stringResource(id = it) } ?: validationErrorMessage
    val mLabelText = state?.hint?.let { stringResource(id = it) } ?: labelText

    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val fieldValidator: (String) -> Unit = { text ->
        if (text.isEmpty() && (state?.isFormRequired == true || isFormRequired)) {
            isError = true
            errorMessage = mEmptyMessage
        } else if (state?.validate == true || validate) {
            isError = !mValidator(text)
            errorMessage = mValidationErrorMessage
        } else {
            isError = false
        }
    }

    if(state?.forceValidate == true || forceValidate) {
        fieldValidator(state?.text ?: value)
    }

    Column(modifier) {
        TextField(
            value = state?.text ?: value,
            modifier = (state?.focusRequester ?: focusRequester)?.let { Modifier.focusRequester(it) }
                ?: Modifier,
            onValueChange = {
                fieldValidator(it)
                onValueChange(it)
            },
//            modifier = modifier,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            label = if (mLabelText != null) {
                { Text(text = if (state?.isFormRequired == true || isFormRequired) "$mLabelText *" else mLabelText) }
            } else label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff,
                            if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                } else trailingIcon?.invoke()
            },
            isError = isError,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else mask?.transformation
                ?: visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
        )
        if (isError && errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 0.dp)
            )
        }
    }
}

sealed class FieldMask(val transformation: VisualTransformation) {
    object DateMask : FieldMask(MaskVisualTransformation("##/##/####"))
    object DateCompleteMask : FieldMask(MaskVisualTransformation("##/##/#### ##:##"))
    object CpfMask : FieldMask(MaskVisualTransformation("###.###.###-##"))
    object CnpjMask : FieldMask(MaskVisualTransformation("##.###.###/####-##"))
    object PhoneMask : FieldMask(MaskVisualTransformation("(##) #########"))
}

sealed class FieldValidator(val regex: Regex) {
    object EmailValidator : FieldValidator(Patterns.EMAIL_ADDRESS.toRegex())
    object PasswordValidator : FieldValidator(Regex(".{6,}"))
    object PhoneValidator : FieldValidator(Regex("^\\([1-9]{2}\\) [2-9][0-9]{3}[0-9]{4,5}\$"))
    object HourValidator : FieldValidator(Regex("^(2[0-3]|[0-1][0-9]):[0-5][0-9]\$"))
    object DateValidator :
        FieldValidator(Regex("^(0?[1-9]|[12][0-9]|3[0-1])[\\\\/](0?[1-9]|1[0-2])[\\\\/](19|20)?\\d{2}\$"))

    object DateAndHourValidator :
        FieldValidator(Regex("^(0?[1-9]|[12][0-9]|3[0-1])[\\\\/](0?[1-9]|1[0-2])[\\\\/](19|20)?\\d{2} (2[0-3]|[0-1][0-9]):[0-5][0-9]\$"))

    object CpfValidator : FieldValidator(Regex("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}\$"))
}