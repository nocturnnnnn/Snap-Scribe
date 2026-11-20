package com.genius.snapscribe.domain.model

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM,
}

enum class TextScale(val factor: Float) {
    STANDARD(1.0f),
    LARGE(1.15f),
    EXTRA_LARGE(1.30f),
}

enum class AppLanguage(val tag: String?) {
    SYSTEM(null),
    INDONESIAN("id"),
    ENGLISH("en"),
}

data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val highContrast: Boolean = false,
    val textScale: TextScale = TextScale.STANDARD,
    val indexedFolders: Set<SnapSource> = setOf(SnapSource.SCREENSHOTS, SnapSource.WHATSAPP),
    val onboardingDone: Boolean = false,
    val biometricLockEnabled: Boolean = false,
    val cacheSizeMb: Int = 142,
    val recentSearches: List<String> = DEFAULT_RECENT_SEARCHES,
) {
    companion object {
        val DEFAULT_RECENT_SEARCHES = listOf(
            "Wi-Fi Kafe Roast",
            "Superindo",
            "Token PLN Rumah",
            "Resi Shopee J&T",
            "Transfer BCA",
        )
    }
}
