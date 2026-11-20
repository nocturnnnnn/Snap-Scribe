package com.genius.snapscribe

import androidx.activity.ComponentActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.genius.snapscribe.data.local.SettingsRepository
import com.genius.snapscribe.data.repository.FakeSessionRepository
import com.genius.snapscribe.data.repository.FakeSnapRepository
import com.genius.snapscribe.data.sample.SampleSnaps
import com.genius.snapscribe.di.AppContainer
import com.genius.snapscribe.domain.model.AppSettings
import com.genius.snapscribe.domain.model.Session
import com.genius.snapscribe.domain.model.SnapSource
import com.genius.snapscribe.domain.model.TextScale
import com.genius.snapscribe.domain.model.ThemeMode
import com.genius.snapscribe.ui.SnapScribeApp
import com.genius.snapscribe.ui.theme.SnapScribeTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant

@RunWith(AndroidJUnit4::class)
class SnapScribeNavigationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private fun buildContainer(onboardingDone: Boolean): AppContainer {
        val settingsRepo = TestSettingsRepository(AppSettings(onboardingDone = onboardingDone))
        return AppContainer(
            context = composeRule.activity.applicationContext,
            snapRepository = FakeSnapRepository(SampleSnaps.buildDefaultSnaps(Instant.parse("2026-11-20T09:00:00Z"))),
            settingsRepository = settingsRepo,
            sessionRepository = FakeSessionRepository(initialSession = Session.Guest, delayMs = 0L),
        )
    }

    private fun setAppContent(container: AppContainer, initialOnboarding: Boolean) {
        composeRule.setContent {
            val settings by container.settingsRepository.settings.collectAsState(
                initial = AppSettings(onboardingDone = initialOnboarding),
            )
            SnapScribeTheme(
                themeMode = settings.themeMode,
                highContrast = settings.highContrast,
                textScale = settings.textScale,
            ) {
                SnapScribeApp(
                    container = container,
                    initialOnboardingDone = initialOnboarding,
                    currentOnboardingDone = settings.onboardingDone,
                )
            }
        }
    }

    @Test
    fun onboardingSkipFlow_reachesHomeScreen() {
        val container = buildContainer(onboardingDone = false)
        setAppContent(container, initialOnboarding = false)

        val skipText = composeRule.activity.getString(R.string.welcome_skip)
        val allowText = composeRule.activity.getString(R.string.setup_cta)
        val homeTitle = composeRule.activity.getString(R.string.home_title)

        composeRule.onNodeWithText(skipText).performScrollTo().performClick()
        composeRule.onNodeWithText(allowText).performClick()
        composeRule.onNodeWithText(homeTitle).assertIsDisplayed()
    }

    @Test
    fun tabSwitching_preservesFilter_andNavigatesToDetailAndPro() {
        val container = buildContainer(onboardingDone = true)
        setAppContent(container, initialOnboarding = true)

        val homeTitle = composeRule.activity.getString(R.string.home_title)
        val codesLabel = composeRule.activity.getString(R.string.category_codes_links)
        val foldersTab = composeRule.activity.getString(R.string.nav_folders)
        val snapsTab = composeRule.activity.getString(R.string.nav_snaps)
        val copyPassLabel = composeRule.activity.getString(R.string.action_copy_password)
        val copyAmountLabel = composeRule.activity.getString(R.string.action_copy_amount)
        val copiedLabel = composeRule.activity.getString(R.string.action_copied)
        val allExtractedLabel = composeRule.activity.getString(R.string.detail_all_extracted)
        val backLabel = composeRule.activity.getString(R.string.action_back)
        val addFolderLabel = composeRule.activity.getString(R.string.folders_add_button)
        val proCompareTitle = composeRule.activity.getString(R.string.pro_compare_title)

        composeRule.onNodeWithText(homeTitle).assertIsDisplayed()

        // Quick copy flips to Copied
        composeRule.onAllNodesWithText(copyAmountLabel).onFirst().performClick()
        composeRule.onAllNodesWithText(copiedLabel).onFirst().assertIsDisplayed()

        // Filter to Codes & Links
        composeRule.onAllNodesWithText(codesLabel).onFirst().performClick()
        composeRule.onAllNodesWithText(copyPassLabel).onFirst().assertIsDisplayed()

        // Switch to Folders tab and back to Snaps tab — filter stays on Codes & Links
        composeRule.onNodeWithText(foldersTab).performClick()
        composeRule.onNodeWithText(addFolderLabel).assertIsDisplayed()
        composeRule.onNodeWithText(snapsTab).performClick()
        composeRule.onAllNodesWithText(copyPassLabel).onFirst().assertIsDisplayed()

        // Open Snap Detail and return via explicit Back button
        composeRule.onNodeWithText("SSID: Roast & Brew Guest").performClick()
        composeRule.onNodeWithText(allExtractedLabel).performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText(backLabel).performClick()
        composeRule.onNodeWithText(homeTitle).assertIsDisplayed()

        // Open Folders and tap + Folder -> opens Pro screen for Guest/Free
        composeRule.onNodeWithText(foldersTab).performClick()
        composeRule.onNodeWithText(addFolderLabel).performClick()
        composeRule.onNodeWithText(proCompareTitle).performScrollTo().assertIsDisplayed()
    }
}

private class TestSettingsRepository(initial: AppSettings) : SettingsRepository {
    private val flow = MutableStateFlow(initial)
    override val settings: Flow<AppSettings> = flow.asStateFlow()

    override suspend fun setThemeMode(mode: ThemeMode) { flow.update { it.copy(themeMode = mode) } }
    override suspend fun setHighContrast(enabled: Boolean) { flow.update { it.copy(highContrast = enabled) } }
    override suspend fun setTextScale(scale: TextScale) { flow.update { it.copy(textScale = scale) } }
    override suspend fun toggleIndexedFolder(source: SnapSource, enabled: Boolean) {
        flow.update { current ->
            val next = current.indexedFolders.toMutableSet()
            if (enabled) next.add(source) else next.remove(source)
            current.copy(indexedFolders = next)
        }
    }
    override suspend fun setOnboardingDone(done: Boolean) { flow.update { it.copy(onboardingDone = done) } }
    override suspend fun setBiometricLock(enabled: Boolean) { flow.update { it.copy(biometricLockEnabled = enabled) } }
    override suspend fun clearCache() { flow.update { it.copy(cacheSizeMb = 0) } }
    override suspend fun addRecentSearch(query: String) {}
    override suspend fun clearRecentSearches() { flow.update { it.copy(recentSearches = emptyList()) } }
}
