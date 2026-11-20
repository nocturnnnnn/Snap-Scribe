package com.genius.snapscribe.data.repository

import com.genius.snapscribe.domain.model.BillingCycle
import com.genius.snapscribe.domain.model.Session
import com.genius.snapscribe.domain.model.UserProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSessionRepository(
    initialSession: Session = Session.Guest,
    private val delayMs: Long = 600L,
) : SessionRepository {

    private val _session = MutableStateFlow(initialSession)
    override val session: StateFlow<Session> = _session.asStateFlow()

    override suspend fun signIn(email: String, password: String) {
        if (delayMs > 0L) delay(delayMs)
        val cleanEmail = email.trim().ifEmpty { "budi.santoso@email.com" }
        val localPart = cleanEmail.substringBefore('@')
            .replace('.', ' ')
            .split(' ')
            .filter { it.isNotBlank() }
            .joinToString(" ") { part ->
                part.replaceFirstChar { it.uppercaseChar() }
            }
            .ifBlank { "Budi Santoso" }

        // Spec: signing in keeps user on Free Account without auto-starting Pro trial.
        _session.value = Session.FreeAccount(
            user = UserProfile(
                name = localPart,
                handle = "@${cleanEmail.substringBefore('@').lowercase()}",
                email = cleanEmail,
                phone = "+62 812-3456-7890",
                memberSince = "Nov 2024",
            ),
            hasUsedTrial = false,
        )
    }

    override suspend fun signInWithGoogle() {
        if (delayMs > 0L) delay(delayMs)
        _session.value = Session.FreeAccount(
            user = UserProfile(
                name = "Budi Santoso",
                handle = "@budisantoso",
                email = "budi.santoso@gmail.com",
                phone = "+62 812-3456-7890",
                memberSince = "Nov 2024",
            ),
            hasUsedTrial = false,
        )
    }

    override suspend fun register(name: String, email: String, password: String) {
        if (delayMs > 0L) delay(delayMs)
        val cleanName = name.trim().ifBlank { "Pengguna Baru" }
        val cleanEmail = email.trim()
        // Spec: creating a free account NEVER auto-activates a Pro trial.
        _session.value = Session.FreeAccount(
            user = UserProfile(
                name = cleanName,
                handle = "@${cleanName.lowercase().replace(Regex("[^a-z0-9]"), "")}",
                email = cleanEmail,
                phone = "+62 812-0000-0000",
                memberSince = "Nov 2026",
            ),
            hasUsedTrial = false,
        )
    }

    override suspend fun continueOffline() {
        _session.value = Session.Guest
    }

    override suspend fun signOut() {
        _session.value = Session.Guest
    }

    override suspend fun startTrial(cycle: BillingCycle) {
        if (delayMs > 0L) delay(delayMs)
        val user = extractUser() ?: return
        _session.value = Session.Pro(
            user = user,
            cycle = cycle,
            renewsAt = "27 Nov 2026",
            inTrial = true,
        )
    }

    override suspend fun subscribe(cycle: BillingCycle) {
        if (delayMs > 0L) delay(delayMs)
        val user = extractUser() ?: return
        _session.value = Session.Pro(
            user = user,
            cycle = cycle,
            renewsAt = if (cycle == BillingCycle.YEARLY) "20 Nov 2027" else "20 Des 2026",
            inTrial = false,
        )
    }

    override suspend fun cancelSubscription() {
        if (delayMs > 0L) delay(delayMs)
        val current = _session.value
        if (current is Session.Pro) {
            _session.value = Session.FreeAccount(
                user = current.user,
                hasUsedTrial = true,
            )
        }
    }

    private fun extractUser(): UserProfile? = when (val current = _session.value) {
        Session.Guest -> null
        is Session.FreeAccount -> current.user
        is Session.Pro -> current.user
    }
}
