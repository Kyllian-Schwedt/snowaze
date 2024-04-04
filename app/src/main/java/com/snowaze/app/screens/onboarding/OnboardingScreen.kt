package com.snowaze.app.screens.onboarding

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.snowaze.app.common.utils.pager.Pager
import com.snowaze.app.common.utils.pager.PagerState
import com.snowaze.app.compose.BasicField
import com.snowaze.app.compose.ExposedDropdownMenu
import com.snowaze.app.compose.lottie.LottieLoadingView
import com.snowaze.app.compose.progress.HorizontalDottedProgressBar
import com.snowaze.app.model.Difficulty

@Composable
fun OnboardingScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    viewModel.openAndPopUp = openAndPopUp

    OnBoardingScreenContent(
        onSkip = { openAndPopUp("login", "signup") },
        pagerState = uiState.pagerState,
        onboardingList = viewModel.onboardingList,
        onFirstNameChange = { viewModel.onFirstNameChange(it) },
        onLastNameChange = { viewModel.onLastNameChange(it) },
        onPseudoChange = { viewModel.onPseudoChange(it) },
        onSkillChange = { viewModel.onSkillChange(it) },
        canAccessNextPage = { viewModel.canAccessNextPage(it) },
        uiState = uiState
    )
}

@Composable
fun OnBoardingScreenContent(
    onSkip: () -> Unit,
    pagerState: PagerState,
    onboardingList: List<OnboardingViewModel.Onboard>,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPseudoChange: (String) -> Unit,
    onSkillChange: (String) -> Unit,
    canAccessNextPage: (Int) -> Boolean = { true },
    uiState: OnboardingUiState
)
{
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Pager(
                state = pagerState,
                orientation = Orientation.Horizontal,
                modifier = Modifier.fillMaxSize().padding(bottom = 100.dp),
                canProceed = canAccessNextPage
            ) {
                OnboardingPagerItem(onboardingList[commingPage], uiState, onFirstNameChange, onLastNameChange, onPseudoChange, onSkillChange)
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp)
            ) {
                onboardingList.forEachIndexed { index, _ ->
                    OnboardingPagerSlide(
                        selected = index == pagerState.currentPage,
                        MaterialTheme.colorScheme.primary,
                        Icons.Filled.Album
                    )
                }
            }

            Button(
                onClick = {
                        onboardingList[pagerState.currentPage].btnfunction()
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.BottomCenter)
                    .animateContentSize()
                    .padding(bottom = 32.dp)
                    .height(50.dp)
                    .clip(CircleShape)
            ) {
                if (uiState.isLoading) {
                    HorizontalDottedProgressBar(modifier = Modifier.padding(horizontal = 16.dp).height(60.dp))
                } else {
                    Text(
                        text = if (pagerState.currentPage == onboardingList.size - 1) "Let's Begin" else "Next",
                        modifier = Modifier.padding(horizontal = 32.dp),
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun OnboardingPagerItem(item: OnboardingViewModel.Onboard, uiState: OnboardingUiState, onFirstNameChange: (String) -> Unit, onLastNameChange: (String) -> Unit, onPseudoChange: (String) -> Unit, onSkillChange: (String) -> Unit){
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieLoadingView(
            context = LocalContext.current,
            file = item.lottieFile,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        )
        Text(
            text = item.title,
            style = androidx.compose.material.MaterialTheme.typography.h5.copy(fontWeight = FontWeight.ExtraBold),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = item.description,
            style = androidx.compose.material.MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        if(item.page == OnboardingViewModel.OnboardingStep.PROFILE){
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
        } else if(item.page == OnboardingViewModel.OnboardingStep.SKILLS) {
            ExposedDropdownMenu(options = listOf(Difficulty.BLACK.name, Difficulty.RED.name, Difficulty.BLUE.name, Difficulty.GREEN.name), label = null, onValueChange = {  })
        }
    }
}



@Composable
fun OnboardingPagerSlide(selected: Boolean, color: Color, icon: ImageVector) {
    Icon(
        imageVector = icon,
        modifier = Modifier.padding(4.dp),
        contentDescription = null,
        tint = if (selected) color else Color.Gray
    )
}