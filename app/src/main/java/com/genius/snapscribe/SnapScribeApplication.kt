package com.genius.snapscribe

import android.app.Application
import com.genius.snapscribe.di.AppContainer

class SnapScribeApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}
