package com.genius.snapscribe.domain.model

enum class BillingCycle {
    MONTHLY,
    YEARLY,
}

data class UserProfile(
    val name: String,
    val handle: String,
    val email: String,
    val phone: String,
    val memberSince: String,
    val emailVerified: Boolean = true,
) {
    val initials: String
        get() = name.trim()
            .split(Regex("\\s+"))
            .filter { it.isNotEmpty() }
            .take(2)
            .joinToString("") { it.first().uppercaseChar().toString() }
            .ifEmpty { "S" }
}

sealed interface Session {
    data object Guest : Session

    data class FreeAccount(
        val user: UserProfile,
        val hasUsedTrial: Boolean = false,
    ) : Session

    data class Pro(
        val user: UserProfile,
        val cycle: BillingCycle,
        val renewsAt: String,
        val inTrial: Boolean,
    ) : Session
}
