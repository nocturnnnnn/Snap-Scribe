package com.genius.snapscribe.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.genius.snapscribe.domain.model.SnapCategory

@Stable
class SnapScribeAppState(
    val navController: NavHostController,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            val dest = currentDestination ?: return null
            return when {
                dest.hasRoute<SnapsTabRoute>() -> TopLevelDestination.SNAPS
                dest.hasRoute<FoldersTabRoute>() -> TopLevelDestination.FOLDERS
                dest.hasRoute<SearchTabRoute>() -> TopLevelDestination.SEARCH
                dest.hasRoute<SettingsTabRoute>() -> TopLevelDestination.SETTINGS
                else -> null
            }
        }

    val showBottomBar: Boolean
        @Composable get() = currentTopLevelDestination != null

    val showCaptureFab: Boolean
        @Composable get() = currentTopLevelDestination in setOf(
            TopLevelDestination.SNAPS,
            TopLevelDestination.FOLDERS,
            TopLevelDestination.SEARCH,
        )

    fun navigateToTab(
        destination: TopLevelDestination,
        searchQuery: String? = null,
    ) {
        val targetRoute = if (destination == TopLevelDestination.SEARCH && searchQuery != null) {
            SearchTabRoute(initialQuery = searchQuery)
        } else {
            destination.route
        }
        navController.navigate(targetRoute) {
            popUpTo<SnapsTabRoute> {
                saveState = true
            }
            launchSingleTop = true
            restoreState = searchQuery == null
        }
    }

    fun navigateToSnapDetail(snapId: String) {
        navController.navigate(SnapDetailRoute(snapId = snapId))
    }

    fun navigateToZoomViewer(snapId: String) {
        navController.navigate(ZoomViewerRoute(snapId = snapId))
    }

    fun navigateToFolderDetail(category: SnapCategory) {
        navController.navigate(FolderDetailRoute(category = category))
    }

    fun navigateToCamera() {
        navController.navigate(CameraCaptureRoute)
    }

    fun navigateToProfile() {
        navController.navigate(ProfileRoute)
    }

    fun navigateToPro(source: String? = null) {
        navController.navigate(ProRoute(source = source))
    }
}

@Composable
fun rememberSnapScribeAppState(
    navController: NavHostController = rememberNavController(),
): SnapScribeAppState = remember(navController) {
    SnapScribeAppState(navController = navController)
}
