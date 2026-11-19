package com.genius.snapscribe.ui.main

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genius.snapscribe.data.DocumentRepository
import com.genius.snapscribe.data.local.entity.DocumentEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModel(
    private val documentRepository: DocumentRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isRecognizing = MutableStateFlow(false)
    val isRecognizing: StateFlow<Boolean> = _isRecognizing.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val uiState: StateFlow<MainScreenUiState> = _searchQuery
        .flatMapLatest { query ->
            documentRepository.searchDocuments(query)
                .map<List<DocumentEntity>, MainScreenUiState> { MainScreenUiState.Success(it) }
                .catch { emit(MainScreenUiState.Error(it)) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainScreenUiState.Loading
        )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun dismissError() {
        _errorMessage.value = null
    }

    fun addDocument(title: String, content: String) {
        viewModelScope.launch {
            try {
                documentRepository.insertDocument(title, content)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to save: ${e.localizedMessage}"
            }
        }
    }

    fun deleteDocument(document: DocumentEntity) {
        viewModelScope.launch {
            try {
                documentRepository.deleteDocument(document)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete: ${e.localizedMessage}"
            }
        }
    }

    fun recognizeAndSaveImage(context: Context, title: String, uri: Uri) {
        viewModelScope.launch {
            _isRecognizing.value = true
            _errorMessage.value = null
            val result = documentRepository.recognizeAndSaveFromUri(context, title, uri)
            result.onFailure {
                _errorMessage.value = "Recognition failed: ${it.localizedMessage}"
            }
            _isRecognizing.value = false
        }
    }

    fun recognizeAndSaveBitmap(title: String, bitmap: Bitmap) {
        viewModelScope.launch {
            _isRecognizing.value = true
            _errorMessage.value = null
            val result = documentRepository.recognizeAndSaveFromBitmap(title, bitmap)
            result.onFailure {
                _errorMessage.value = "Recognition failed: ${it.localizedMessage}"
            }
            _isRecognizing.value = false
        }
    }
}

sealed interface MainScreenUiState {
    data object Loading : MainScreenUiState
    data class Error(val throwable: Throwable) : MainScreenUiState
    data class Success(val documents: List<DocumentEntity>) : MainScreenUiState
}
