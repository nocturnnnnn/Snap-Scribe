package com.genius.snapscribe.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.genius.snapscribe.di.AppContainer
import com.genius.snapscribe.di.viewModelFactory
import com.genius.snapscribe.ui.screens.auth.AuthRoute
import com.genius.snapscribe.ui.screens.auth.AuthViewModel
import com.genius.snapscribe.ui.screens.auth.RegisterRoute
import com.genius.snapscribe.ui.screens.camera.CameraCaptureRoute
import com.genius.snapscribe.ui.screens.detail.SnapDetailRoute
import com.genius.snapscribe.ui.screens.detail.SnapDetailViewModel
import com.genius.snapscribe.ui.screens.detail.ZoomViewerRoute
import com.genius.snapscribe.ui.screens.folders.FolderDetailRoute
import com.genius.snapscribe.ui.screens.folders.FolderDetailViewModel
import com.genius.snapscribe.ui.screens.folders.FoldersRoute
import com.genius.snapscribe.ui.screens.folders.FoldersViewModel
import com.genius.snapscribe.ui.screens.home.HomeRoute
import com.genius.snapscribe.ui.screens.home.HomeViewModel
import com.genius.snapscribe.ui.screens.onboarding.IndexSetupRoute
import com.genius.snapscribe.ui.screens.pro.ProRoute
import com.genius.snapscribe.ui.screens.pro.ProViewModel
import com.genius.snapscribe.ui.screens.profile.ProfileRoute
import com.genius.snapscribe.ui.screens.profile.ProfileViewModel
import com.genius.snapscribe.ui.screens.search.SearchRoute
import com.genius.snapscribe.ui.screens.search.SearchViewModel
import com.genius.snapscribe.ui.screens.settings.SettingsRoute
import com.genius.snapscribe.ui.screens.settings.SettingsViewModel
import com.genius.snapscribe.ui.screens.welcome.WelcomeScreen
import kotlinx.coroutines.launch

@Composable
fun SnapScribeNavHost(
    appState: SnapScribeAppState,
    container: AppContainer,
    startDestination: Any,
    onboardingDone: Boolean,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        modifier = modifier,
    ) {
        composable<WelcomeRoute> {
            WelcomeScreen(
                onStartClick = { navController.navigate(AuthRoute) },
                onSkipClick = {
                    scope.launch {
                        container.sessionRepository.continueOffline()
                        navController.navigate(IndexSetupRoute)
                    }
                },
            )
        }

        composable<AuthRoute> {
            val authViewModel: AuthViewModel = viewModel(
                factory = viewModelFactory { AuthViewModel(container.sessionRepository) },
            )
            AuthRoute(
                viewModel = authViewModel,
                onAuthenticated = {
                    if (onboardingDone) {
                        navController.popBackStack()
                    } else {
                        navController.navigate(IndexSetupRoute)
                    }
                },
                onNavigateToRegister = { navController.navigate(RegisterRoute) },
            )
        }

        composable<RegisterRoute> {
            val authViewModel: AuthViewModel = viewModel(
                factory = viewModelFactory { AuthViewModel(container.sessionRepository) },
            )
            RegisterRoute(
                viewModel = authViewModel,
                onBack = { navController.popBackStack() },
                onRegistered = {
                    if (onboardingDone) {
                        navController.popBackStack(AuthRoute, inclusive = true)
                    } else {
                        navController.navigate(IndexSetupRoute)
                    }
                },
            )
        }

        composable<IndexSetupRoute> {
            IndexSetupRoute(
                settingsRepository = container.settingsRepository,
                onBack = { navController.popBackStack() },
                onCompleteSetup = {
                    navController.navigate(MainGraphRoute) {
                        popUpTo<WelcomeRoute> { inclusive = true }
                    }
                },
            )
        }

        navigation<MainGraphRoute>(
            startDestination = SnapsTabRoute,
        ) {
            composable<SnapsTabRoute> {
                val homeViewModel: HomeViewModel = viewModel(
                    factory = viewModelFactory {
                        HomeViewModel(
                            snapRepository = container.snapRepository,
                            sessionRepository = container.sessionRepository,
                        )
                    },
                )
                HomeRoute(
                    viewModel = homeViewModel,
                    onSnapClick = appState::navigateToSnapDetail,
                    onSearchClick = { query ->
                        appState.navigateToTab(TopLevelDestination.SEARCH, searchQuery = query)
                    },
                    onProfileClick = appState::navigateToProfile,
                    onAutoOrganizeClick = {
                        appState.navigateToTab(TopLevelDestination.SETTINGS)
                    },
                    onShowMessage = onShowMessage,
                )
            }

            composable<FoldersTabRoute> {
                val foldersViewModel: FoldersViewModel = viewModel(
                    factory = viewModelFactory {
                        FoldersViewModel(
                            snapRepository = container.snapRepository,
                            sessionRepository = container.sessionRepository,
                        )
                    },
                )
                FoldersRoute(
                    viewModel = foldersViewModel,
                    onFolderClick = appState::navigateToFolderDetail,
                    onProfileClick = appState::navigateToProfile,
                    onProClick = appState::navigateToPro,
                    onShowMessage = onShowMessage,
                )
            }

            composable<SearchTabRoute> { entry ->
                val route = entry.toRoute<SearchTabRoute>()
                val searchViewModel: SearchViewModel = viewModel(
                    factory = viewModelFactory {
                        SearchViewModel(
                            snapRepository = container.snapRepository,
                            settingsRepository = container.settingsRepository,
                            sessionRepository = container.sessionRepository,
                            initialQuery = route.initialQuery,
                        )
                    },
                )
                SearchRoute(
                    viewModel = searchViewModel,
                    initialQuery = route.initialQuery,
                    onSnapClick = appState::navigateToSnapDetail,
                    onProfileClick = appState::navigateToProfile,
                    onShowMessage = onShowMessage,
                )
            }

            composable<SettingsTabRoute> {
                val settingsViewModel: SettingsViewModel = viewModel(
                    factory = viewModelFactory {
                        SettingsViewModel(
                            settingsRepository = container.settingsRepository,
                            sessionRepository = container.sessionRepository,
                        )
                    },
                )
                SettingsRoute(
                    viewModel = settingsViewModel,
                    onProfileClick = appState::navigateToProfile,
                    onProClick = appState::navigateToPro,
                    onShowMessage = onShowMessage,
                )
            }
        }

        composable<SnapDetailRoute> { entry ->
            val route = entry.toRoute<SnapDetailRoute>()
            val detailViewModel: SnapDetailViewModel = viewModel(
                factory = viewModelFactory {
                    SnapDetailViewModel(
                        snapId = route.snapId,
                        snapRepository = container.snapRepository,
                    )
                },
            )
            SnapDetailRoute(
                viewModel = detailViewModel,
                onBack = { navController.popBackStack() },
                onZoomClick = appState::navigateToZoomViewer,
                onShowMessage = onShowMessage,
            )
        }

        composable<ZoomViewerRoute> { entry ->
            val route = entry.toRoute<ZoomViewerRoute>()
            val detailViewModel: SnapDetailViewModel = viewModel(
                factory = viewModelFactory {
                    SnapDetailViewModel(
                        snapId = route.snapId,
                        snapRepository = container.snapRepository,
                    )
                },
            )
            ZoomViewerRoute(
                viewModel = detailViewModel,
                onBack = { navController.popBackStack() },
            )
        }

        composable<FolderDetailRoute> { entry ->
            val route = entry.toRoute<FolderDetailRoute>()
            val folderDetailViewModel: FolderDetailViewModel = viewModel(
                factory = viewModelFactory {
                    FolderDetailViewModel(
                        category = route.category,
                        snapRepository = container.snapRepository,
                        sessionRepository = container.sessionRepository,
                    )
                },
            )
            FolderDetailRoute(
                viewModel = folderDetailViewModel,
                onBack = { navController.popBackStack() },
                onSnapClick = appState::navigateToSnapDetail,
                onCameraClick = appState::navigateToCamera,
                onProClick = appState::navigateToPro,
                onShowMessage = onShowMessage,
            )
        }

        composable<CameraCaptureRoute> {
            CameraCaptureRoute(
                snapRepository = container.snapRepository,
                sessionRepository = container.sessionRepository,
                onBack = { navController.popBackStack() },
                onSnapCaptured = { newSnapId ->
                    navController.navigate(SnapDetailRoute(snapId = newSnapId)) {
                        popUpTo<CameraCaptureRoute> { inclusive = true }
                    }
                },
                onProClick = appState::navigateToPro,
                onShowMessage = onShowMessage,
            )
        }

        composable<ProfileRoute> {
            val profileViewModel: ProfileViewModel = viewModel(
                factory = viewModelFactory {
                    ProfileViewModel(
                        sessionRepository = container.sessionRepository,
                        settingsRepository = container.settingsRepository,
                        snapRepository = container.snapRepository,
                    )
                },
            )
            ProfileRoute(
                viewModel = profileViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToAuth = { navController.navigate(AuthRoute) },
                onNavigateToPro = appState::navigateToPro,
                onSignedOut = {
                    navController.navigate(WelcomeRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onShowMessage = onShowMessage,
            )
        }

        composable<ProRoute> {
            val proViewModel: ProViewModel = viewModel(
                factory = viewModelFactory {
                    ProViewModel(sessionRepository = container.sessionRepository)
                },
            )
            ProRoute(
                viewModel = proViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToAuth = { navController.navigate(AuthRoute) },
                onShowMessage = onShowMessage,
            )
        }
    }
}
