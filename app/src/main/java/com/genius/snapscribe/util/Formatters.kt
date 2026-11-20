package com.genius.snapscribe.util

import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val indonesianLocale = Locale.forLanguageTag("id-ID")

fun formatRupiah(amount: Long): String {
    val nf = NumberFormat.getIntegerInstance(indonesianLocale)
    return "Rp ${nf.format(amount)}"
}

fun formatRupiahCompact(amount: Long): String {
    return if (amount >= 1_000_000L) {
        val millions = amount / 1_000_000.0
        String.format(indonesianLocale, "Rp %.2f jt", millions)
    } else {
        formatRupiah(amount)
    }
}

sealed interface DateGroupKey {
    data object Today : DateGroupKey
    data object Yesterday : DateGroupKey
    data class DateLabel(val formatted: String) : DateGroupKey
}

fun Instant.toDateGroupKey(
    zoneId: ZoneId = ZoneId.systemDefault(),
    today: LocalDate = LocalDate.now(zoneId),
    locale: Locale = Locale.getDefault(),
): DateGroupKey {
    val date = this.atZone(zoneId).toLocalDate()
    return when (date) {
        today -> DateGroupKey.Today
        today.minusDays(1) -> DateGroupKey.Yesterday
        else -> {
            val fmt = DateTimeFormatter.ofPattern("dd MMM yyyy", locale)
            DateGroupKey.DateLabel(date.format(fmt))
        }
    }
}

fun Instant.formatRelativeTimestamp(
    todayLabel: String,
    yesterdayLabel: String,
    zoneId: ZoneId = ZoneId.systemDefault(),
    today: LocalDate = LocalDate.now(zoneId),
    locale: Locale = Locale.getDefault(),
): String {
    val zdt = this.atZone(zoneId)
    val timeText = zdt.format(DateTimeFormatter.ofPattern("HH:mm", locale))
    return when (zdt.toLocalDate()) {
        today -> "$todayLabel, $timeText"
        today.minusDays(1) -> "$yesterdayLabel, $timeText"
        else -> zdt.format(DateTimeFormatter.ofPattern("dd MMM, HH:mm", locale))
    }
}
