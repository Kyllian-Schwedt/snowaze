package com.snowaze.app.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.snowaze.app.ui.theme.TextFieldDefaultsMaterial

@Composable
fun BasicField(
    value: String, onValueChange: (String) -> Unit, hasError: Boolean = false, placeholder: String, label: String
) {
    val fieldInteractionState = remember { MutableInteractionSource() }
    OutlinedTextField(
        value = value,
        maxLines = 1,
        isError = hasError,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        colors = TextFieldDefaultsMaterial.outlinedTextFieldColors(),
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        onValueChange = onValueChange,
        interactionSource = fieldInteractionState,
    )
}

@Composable
fun EmailField(value: String, onNewValue: (String) -> Unit, hasError: Boolean = false) {
    val emailInteractionState = remember { MutableInteractionSource() }
    OutlinedTextField(
        value = value,
        leadingIcon = {
            FaIcon(
                faIcon = FaIcons.Envelope,
                tint = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
            )
        },
        maxLines = 1,
        isError = hasError,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        colors = TextFieldDefaultsMaterial.outlinedTextFieldColors(),
        label = { Text(text = "Email address") },
        placeholder = { Text(text = "bob@gymgusto.fr") },
        onValueChange = onNewValue,
        interactionSource = emailInteractionState,
    )
}

@Composable
fun PasswordField(value: String, onNewValue: (String) -> Unit, hasError: Boolean = false) {
    PasswordField(value, "Password", onNewValue, hasError)
}

@Composable
fun RepeatPasswordField(
    value: String,
    onNewValue: (String) -> Unit,
    hasError: Boolean = false
) {
    PasswordField(value, "Repeat password", onNewValue, hasError)
}

@Composable
private fun PasswordField(
    value: String,
    placeholder: String,
    onNewValue: (String) -> Unit,
    hasError: Boolean = false
) {
    var passwordVisualTransformation by remember {
        mutableStateOf<VisualTransformation>(
            PasswordVisualTransformation()
        )
    }
    val passwordInteractionState = remember { MutableInteractionSource() }

    OutlinedTextField(
        value = value,
        leadingIcon = {
            FaIcon(
                faIcon = FaIcons.Key,
                tint = androidx.compose.material3.LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
            )
        },
        trailingIcon = {
            FaIcon(
                faIcon = (if (passwordVisualTransformation != VisualTransformation.None) FaIcons.EyeSlash else FaIcons.Eye),
                tint = androidx.compose.material3.LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
                modifier = Modifier.clickable(onClick = {
                    passwordVisualTransformation =
                        if (passwordVisualTransformation != VisualTransformation.None) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        }
                })
            )
        },
        colors = TextFieldDefaultsMaterial.outlinedTextFieldColors(),
        maxLines = 1,
        isError = hasError,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        label = { Text(text = "Password") },
        placeholder = { Text(text = placeholder) },
        onValueChange = onNewValue,
        interactionSource = passwordInteractionState,
        visualTransformation = passwordVisualTransformation,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExposedDropdownMenu(options: List<String>, label: String?, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    // We want to react on tap/press on TextField to show menu
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            readOnly = true,
            value = selectedOptionText,
            onValueChange =onValueChange,
            label = { if(label != null) Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    content = { Text(selectionOption) },
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    },
                )
            }
        }
    }
}