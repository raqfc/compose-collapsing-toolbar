package br.com.justworks.volan2.common.presentation.composables.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.com.justworks.volan2.R

@Composable
fun CustomAlertDialog(
    title: Int = R.string.default_error_title,
    content: Int = R.string.default_error_content,
    confirmButton: Int = R.string.ok,
    onClick: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(text = stringResource(id = title))
        },
        text = {
            Text(text = stringResource(id = content))
        },
        confirmButton = {
            Button(
                onClick = onClick) {
                Text(text = stringResource(id = confirmButton))
            }
        },
//        dismissButton = {
//            Button(
//
//                onClick = {
//                    openDialog.value = false
//                }) {
//                Text("This is the dismiss Button")
//            }
//        }
    )
}