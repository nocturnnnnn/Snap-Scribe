package com.genius.snapscribe.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.genius.snapscribe.data.local.DataStoreSettingsRepository
import com.genius.snapscribe.data.local.SettingsRepository
import com.genius.snapscribe.data.repository.FakeSessionRepository
import com.genius.snapscribe.data.repository.FakeSnapRepository
import com.genius.snapscribe.data.repository.SessionRepository
import com.genius.snapscribe.data.repository.SnapRepository

class AppContainer(
    context: Context,
    val snapRepository: SnapRepository = FakeSnapRepository(),
    val settingsRepository: SettingsRepository = DataStoreSettingsRepository(context.applicationContext),
    val sessionRepository: SessionRepository = FakeSessionRepository(),
)

inline fun <reified VM : ViewModel> viewModelFactory(
    crossinline creator: () -> VM,
): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = creator() as T
}
