package com.genius.snapscribe.data.repository

import com.genius.snapscribe.domain.model.BillingCycle
import com.genius.snapscribe.domain.model.Session
import kotlinx.coroutines.flow.StateFlow

interface SessionRepository {
    val session: StateFlow<Session>
    suspend fun signIn(email: String, password: String)
    suspend fun signInWithGoogle()
    suspend fun register(name: String, email: String, password: String)
    suspend fun continueOffline()
    suspend fun signOut()
    suspend fun startTrial(cycle: BillingCycle)
    suspend fun subscribe(cycle: BillingCycle)
    suspend fun cancelSubscription()
}
