package com.genius.snapscribe

import app.cash.turbine.test
import com.genius.snapscribe.data.repository.FakeSessionRepository
import com.genius.snapscribe.domain.model.BillingCycle
import com.genius.snapscribe.domain.model.Session
import com.genius.snapscribe.domain.model.UserProfile
import com.genius.snapscribe.ui.screens.pro.ProCtaState
import com.genius.snapscribe.ui.screens.pro.ProViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testUser = UserProfile(
        name = "Budi Santoso",
        handle = "@budisantoso",
        email = "budi@email.com",
        phone = "+62 812-3456-7890",
        memberSince = "Nov 2024",
    )

    @Test
    fun guestSession_showsSignInRequiredCta() = runTest {
        val sessionRepo = FakeSessionRepository(initialSession = Session.Guest, delayMs = 0L)
        val vm = ProViewModel(sessionRepo)

        vm.uiState.test {
            val state = awaitItem()
            assertEquals(ProCtaState.SIGN_IN_REQUIRED, state.ctaState)
            assertTrue(state.showTrialTimeline)
        }
    }

    @Test
    fun freeAccount_startsTrialAndCancelsToSubscribeDirect() = runTest {
        val sessionRepo = FakeSessionRepository(
            initialSession = Session.FreeAccount(testUser, hasUsedTrial = false),
            delayMs = 0L,
        )
        val vm = ProViewModel(sessionRepo)

        vm.uiState.test {
            val initial = awaitItem()
            assertEquals(ProCtaState.START_FREE_TRIAL, initial.ctaState)
            assertTrue(initial.showTrialTimeline)

            vm.selectCycle(BillingCycle.MONTHLY)
            assertEquals(BillingCycle.MONTHLY, awaitItem().selectedCycle)

            vm.startTrial()
            val proState = expectMostRecentItem()
            assertEquals(ProCtaState.ALREADY_PRO, proState.ctaState)
            assertFalse(proState.showTrialTimeline)
            assertTrue((proState.session as Session.Pro).inTrial)

            vm.cancelSubscription()
            val afterCancel = expectMostRecentItem()
            assertEquals(ProCtaState.SUBSCRIBE_DIRECT, afterCancel.ctaState)
            assertFalse(afterCancel.showTrialTimeline)
        }
    }
}
