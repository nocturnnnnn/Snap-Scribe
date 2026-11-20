package com.genius.snapscribe.ui.navigation

import com.genius.snapscribe.domain.model.SnapCategory
import kotlinx.serialization.Serializable

@Serializable data object WelcomeRoute
@Serializable data object AuthRoute
@Serializable data object RegisterRoute
@Serializable data object IndexSetupRoute
@Serializable data object MainGraphRoute
@Serializable data object SnapsTabRoute
@Serializable data object FoldersTabRoute
@Serializable data class SearchTabRoute(val initialQuery: String? = null)
@Serializable data object SettingsTabRoute
@Serializable data class SnapDetailRoute(val snapId: String)
@Serializable data class ZoomViewerRoute(val snapId: String)
@Serializable data class FolderDetailRoute(val category: SnapCategory)
@Serializable data object CameraCaptureRoute
@Serializable data object ProfileRoute
@Serializable data class ProRoute(val source: String? = null)
