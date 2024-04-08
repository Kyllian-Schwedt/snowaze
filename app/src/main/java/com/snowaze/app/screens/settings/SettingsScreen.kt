package com.snowaze.app.screens.settings

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIconType
import com.guru.fontawesomecomposelib.FaIcons
import com.snowaze.app.compose.BasicField
import com.snowaze.app.compose.ExposedDropdownMenu
import com.snowaze.app.compose.lottie.LottieLoadingView
import com.snowaze.app.compose.progress.HorizontalDottedProgressBar
import com.snowaze.app.model.Difficulty

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    SettingsScreenContent(
        onFirstNameChange = viewModel::onFirstNameChange,
        onLastNameChange = viewModel::onLastNameChange,
        onPseudoChange = viewModel::onPseudoChange,
        onSkillChange = viewModel::onSkillChange,
        onProfileSubmit = viewModel::onProfileSubmit,
        uiState = uiState
    )

}

@Composable
fun SettingsScreenContent(
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPseudoChange: (String) -> Unit,
    onSkillChange: (String) -> Unit,
    onProfileSubmit: () -> Unit,
    uiState: SettingsUiState
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    ProfileItem(
                        uiState,
                        onFirstNameChange,
                        onLastNameChange,
                        onPseudoChange,
                        onSkillChange
                    )
                }

                item {
                    Divider(
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)) {
                        Button(
                            onClick = {
                                onProfileSubmit()
                            },
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .animateContentSize()
                                .height(50.dp)
                                .clip(CircleShape)
                        ) {
                            if (uiState.isLoading) {
                                HorizontalDottedProgressBar(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .height(60.dp)
                                )
                            } else {
                                if(uiState.isValid){
                                    FaIcon(faIcon = FaIcons.Check, tint = Color.White, size = 24.dp)
                                } else {
                                    Text(
                                        text = "Save Changes",
                                        modifier = Modifier.padding(horizontal = 32.dp),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileItem(
    uiState: SettingsUiState,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPseudoChange: (String) -> Unit,
    onSkillChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieLoadingView(
            context = LocalContext.current,
            file = "profile.json",
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        )
        Text(
            text = "Your skiing profile",
            style = androidx.compose.material.MaterialTheme.typography.h5.copy(fontWeight = FontWeight.ExtraBold),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = "You can update your profile information here",
            style = androidx.compose.material.MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        BasicField(
            value = uiState.firstName,
            onValueChange = onFirstNameChange,
            placeholder = "Bob",
            label = "First Name",
            hasError = uiState.hasErrorFirstName
        )

        BasicField(
            value = uiState.lastName,
            onValueChange = onLastNameChange,
            placeholder = "Gusto",
            label = "Last Name",
            hasError = uiState.hasErrorLastName
        )

        BasicField(
            value = uiState.pseudo,
            onValueChange = onPseudoChange,
            placeholder = "BobGusto",
            label = "Pseudo*",
            hasError = uiState.hasErrorPseudo
        )

        Divider(
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = "Your skiing skills",
            style = androidx.compose.material.MaterialTheme.typography.h5.copy(fontWeight = FontWeight.ExtraBold),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )

        ExposedDropdownMenu(
            options = listOf(
                Difficulty.BLACK.name,
                Difficulty.RED.name,
                Difficulty.BLUE.name,
                Difficulty.GREEN.name
            ), label = null, onValueChange = {
                onSkillChange(it)
            },
        )
    }
}
