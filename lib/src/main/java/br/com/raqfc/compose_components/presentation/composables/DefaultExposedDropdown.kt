package br.com.raqfc.compose_components.presentation.composables

import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultExposedDropdown(
    labelText: String = "Selecione",
    label: @Composable (() -> Unit)? = null,
    values: HashMap<String, String>,
    selectedValue: String? = null,
    onValueChanged: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var value = selectedValue ?: values.values.toList().getOrElse(0) { "" }
    var valueText by remember { mutableStateOf(values[value] ?: "") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        DefaultTextField(
            value = valueText,
            onValueChange = { },
            readOnly = true,
            label = {
                if (label != null) {
                    label()
                } else  Text(labelText)
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
//            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            values.forEach { entry ->
                DropdownMenuItem(
                    text = { Text(text = entry.value) },
                    onClick = {
                        valueText = entry.value
                        value = entry.key
                        onValueChanged(value)
                        expanded = false
                    }
                )
            }
        }
    }

}