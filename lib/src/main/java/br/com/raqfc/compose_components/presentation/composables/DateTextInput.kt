package br.com.raqfc.compose_components.presentation.composables

import android.view.MotionEvent
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.raqfc.compose_components.UtilsView.Companion.getActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class DateTextInput private constructor() {
	companion object {

		private val simpleDateFormat by lazy {
			SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
		}

		private val dateCompleteFormat by lazy {
			SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
		}

		private fun dateTextInDate(dateText: String, isDateAndHour: Boolean): Date? {
			return when {
				dateText.isEmpty() -> {
					null
				}
				isDateAndHour -> {
					val sDate = dateText.split(" ")
					if (sDate.isEmpty() || !br.com.raqfc.compose_components.presentation.composables.DateTextInput.Companion.validateDateInText(
							sDate[0]
						)
					) {
						return null
					}

					if (br.com.raqfc.compose_components.presentation.composables.DateTextInput.Companion.validateDateAndHourInText(
							dateText
						)
					) {
						br.com.raqfc.compose_components.presentation.composables.DateTextInput.Companion.dateComplete(
							dateText
						)
					} else {
						br.com.raqfc.compose_components.presentation.composables.DateTextInput.Companion.dateCompleteWithDefaultHour(
							sDate[0]
						)
					}
				}
				!br.com.raqfc.compose_components.presentation.composables.DateTextInput.Companion.validateDateInText(
					dateText
				) -> {
					null
				}
				else -> {
					br.com.raqfc.compose_components.presentation.composables.DateTextInput.Companion.dateCompleteWithDefaultHour(
						dateText
					)
				}
			}
		}

		private fun validateDateInText(dateText: String): Boolean {
			return br.com.raqfc.compose_components.presentation.composables.FieldValidator.DateValidator.regex.matches(dateText)
		}

		private fun validateDateAndHourInText(dateText: String): Boolean {
			return br.com.raqfc.compose_components.presentation.composables.FieldValidator.DateAndHourValidator.regex.matches(dateText)
		}

		private fun dateCompleteWithDefaultHour(dateText: String): Date? {
			return br.com.raqfc.compose_components.presentation.composables.DateTextInput.Companion.dateCompleteFormat.parse(String.format("%s %s", dateText, "12:00"))
		}

		private fun dateComplete(dateText: String): Date? {
			return br.com.raqfc.compose_components.presentation.composables.DateTextInput.Companion.dateCompleteFormat.parse(dateText)
		}

		private fun openDialogDate(
			fragmentManager: androidx.fragment.app.FragmentManager?,
			blockPastDates: Boolean,
			isDateAndHour: Boolean,
			labelText: String,
			lastDate: Calendar,
			onDateChanged: (Calendar) -> Unit
		) {
			fragmentManager?.also { fm ->
				val picker = MaterialDatePicker.Builder.datePicker().apply {
					setTitleText(labelText)

					setSelection(lastDate.time.time)

					if (blockPastDates) {
						val dateValidator: CalendarConstraints.DateValidator =
							DateValidatorPointForward.from(
								GregorianCalendar().timeInMillis
							)
						val constraintsBuilder = CalendarConstraints.Builder()
						constraintsBuilder.setValidator(dateValidator);
						setCalendarConstraints(constraintsBuilder.build());
					}
				}.build()

				picker.addOnPositiveButtonClickListener { selectedDate ->
					val timeZone = TimeZone.getDefault()
					val offsetFromUTC = timeZone.getOffset(Date().time) * -1
					lastDate.time = Date(selectedDate + offsetFromUTC)

					if (isDateAndHour) {
						onDateChanged(lastDate)
						br.com.raqfc.compose_components.presentation.composables.DateTextInput.Companion.openDialogHour(
							fm,
							lastDate,
							onDateChanged
						)
					} else {
						onDateChanged(lastDate)
					}
				}

				picker.show(fm, picker.toString())
			}
		}

		private fun openDialogHour(
			fragmentManager: androidx.fragment.app.FragmentManager?,
			selectedDate: Calendar,
			onDateChanged: (Calendar) -> Unit
		) {
			fragmentManager?.also { fm ->
				val picker = MaterialTimePicker.Builder()
					.setTimeFormat(TimeFormat.CLOCK_12H)
					.setHour(selectedDate.get(Calendar.HOUR))
					.setMinute(selectedDate.get(Calendar.MINUTE))
					.build()

				picker.addOnPositiveButtonClickListener {
					selectedDate.set(Calendar.HOUR, picker.hour)
					selectedDate.set(Calendar.MINUTE, picker.minute)
					onDateChanged(selectedDate)
				}

				picker.show(fm, picker.toString())
			}
		}

		private fun dateInString(date: Calendar, isDateAndHour: Boolean): String {
			return if (isDateAndHour) {
				br.com.raqfc.compose_components.presentation.composables.DateTextInput.Companion.dateCompleteFormat.format(date.time)
			} else {
				br.com.raqfc.compose_components.presentation.composables.DateTextInput.Companion.simpleDateFormat.format(date.time)
			}
		}

		@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
		@Preview("DateTextInput")
		@Composable
		fun DateTextInput(
			modifier: Modifier = Modifier,
			onValueChange: (String) -> Unit = {},
			initWithCurrentDate: Boolean = false,
			date: Calendar? = null,
			onDateChanged: (Calendar) -> Unit = {},
			enabled: Boolean = true,
			emptyMessage: String = "Não pode ser vazio",
			isDateAndHour: Boolean = false,
			blockPastDates: Boolean = false,
			validationErrorMessage: String = "Data inválida",
			readOnly: Boolean = false,
			textStyle: TextStyle = LocalTextStyle.current,
			isFormRequired: Boolean = false,
			labelText: String? = null,
			label: @Composable (() -> Unit)? = null,
			placeholder: @Composable (() -> Unit)? = null,
			leadingIcon: @Composable (() -> Unit)? = null,
			keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
			keyboardActions: KeyboardActions = KeyboardActions(),
			singleLine: Boolean = false,
			maxLines: Int = Int.MAX_VALUE,
			interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
			shape: Shape =
				MaterialTheme.shapes.small.copy(
					bottomEnd = ZeroCornerSize,
					bottomStart = ZeroCornerSize
				),
		) {
			var textFieldValueState by remember { mutableStateOf("") }

			var isError by remember { mutableStateOf(false) }
			var errorMessage by remember { mutableStateOf("") }

			var lastDate: Calendar
			if (date != null && initWithCurrentDate) {
				lastDate = Calendar.getInstance().apply {
					time = Date(System.currentTimeMillis())
				}
			} else {
				lastDate = date ?: Calendar.getInstance()
			}


			val localOnDateChanged: (Calendar) -> Unit = {
				textFieldValueState =
					br.com.raqfc.compose_components.presentation.composables.DateTextInput.Companion.dateInString(
						it,
						isDateAndHour
					)
				lastDate = it
				onDateChanged(it)
			}

			val supportFragmentManager = LocalContext.current.getActivity()?.supportFragmentManager

			Column(modifier) {
				TextField(
					value = textFieldValueState,
					onValueChange = {
						if (it.isEmpty() && isFormRequired) {
							isError = true
							errorMessage = emptyMessage
						} else {
							isError =
								!(if (isDateAndHour) br.com.raqfc.compose_components.presentation.composables.DateTextInput.Companion.validateDateAndHourInText(
									it
								) else br.com.raqfc.compose_components.presentation.composables.DateTextInput.Companion.validateDateInText(
									it
								))
							errorMessage = validationErrorMessage
						}

						onValueChange(it)
					},
					modifier = Modifier
						.pointerInteropFilter {
							if (it.action == MotionEvent.ACTION_UP) {
								if (!readOnly)
									br.com.raqfc.compose_components.presentation.composables.DateTextInput.Companion.openDialogDate(
										supportFragmentManager,
										blockPastDates,
										isDateAndHour,
										labelText ?: "",
										lastDate,
										localOnDateChanged
									)
							}
							true
						}
						.focusable(false),
//                    modifier = Modifier.padding(4.dp),
					enabled = enabled,
					readOnly = readOnly,
					textStyle = textStyle,
					label = if (labelText != null) {
						{ Text(text = if (isFormRequired) "$labelText *" else labelText) }
					} else label,
					placeholder = placeholder,
					leadingIcon = leadingIcon,
					trailingIcon = {
						IconButton(onClick = {
							if (!readOnly)
								br.com.raqfc.compose_components.presentation.composables.DateTextInput.Companion.openDialogDate(
									supportFragmentManager,
									blockPastDates,
									isDateAndHour,
									labelText ?: "",
									lastDate,
									localOnDateChanged
								)
						}) {
							Icon(
								imageVector = Icons.Default.CalendarMonth,
								tint = MaterialTheme.colorScheme.primary,
								contentDescription = "calendar icon",
							)
						}
					},
					isError = isError,
//                    visualTransformation = if (isDateAndHour) FieldMask.DateCompleteMask.transformation else FieldMask.DateMask.transformation,
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
	}
}