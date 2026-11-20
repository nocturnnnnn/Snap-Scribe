package com.genius.snapscribe.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.genius.snapscribe.di.AppContainer
import com.genius.snapscribe.ui.components.SnapBottomBar
import com.genius.snapscribe.ui.components.SnapFab
import com.genius.snapscribe.ui.navigation.MainGraphRoute
import com.genius.snapscribe.ui.navigation.SnapScribeAppState
import com.genius.snapscribe.ui.navigation.SnapScribeNavHost
import com.genius.snapscribe.ui.navigation.WelcomeRoute
import com.genius.snapscribe.ui.navigation.rememberSnapScribeAppState
import kotlinx.coroutines.launch

@Composable
fun SnapScribeApp(
    container: AppContainer,
    initialOnboardingDone: Boolean,
    currentOnboardingDone: Boolean,
    modifier: Modifier = Modifier,
    appState: SnapScribeAppState = rememberSnapScribeAppState(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val startDestination = remember(initialOnboardingDone) {
        if (initialOnboardingDone) MainGraphRoute else WelcomeRoute
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (appState.showBottomBar) {
                SnapBottomBar(
                    currentDestination = appState.currentTopLevelDestination,
                    onSelectDestination = { destination ->
                        appState.navigateToTab(destination)
                    },
                )
            }
        },
        floatingActionButton = {
            if (appState.showCaptureFab) {
                SnapFab(onClick = appState::navigateToCamera)
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            SnapScribeNavHost(
                appState = appState,
                container = container,
                startDestination = startDestination,
                onboardingDone = currentOnboardingDone,
                onShowMessage = { message ->
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(message)
                    }
                },
            )
        }
    }
}
