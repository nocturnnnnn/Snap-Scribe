package com.genius.snapscribe.util

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

fun createVoiceSearchIntent(prompt: String): Intent {
    val activeLocale = AppCompatDelegate.getApplicationLocales()[0] ?: Locale.getDefault()
    val languageTag = if (activeLocale.language == "en") "en-US" else "id-ID"
    return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
        )
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageTag)
        putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)
        putExtra(RecognizerIntent.EXTRA_PROMPT, prompt)
    }
}
