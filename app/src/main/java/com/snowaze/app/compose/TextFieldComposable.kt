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
    @StringRes text: Int,
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(text)) }
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