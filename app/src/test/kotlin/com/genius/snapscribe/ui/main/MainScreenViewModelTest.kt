package com.genius.snapscribe.ui.main

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.genius.snapscribe.data.DocumentRepository
import com.genius.snapscribe.data.local.entity.DocumentEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MainScreenViewModelTest {
    @Test
    fun uiState_initiallyLoading() = runTest {
        val viewModel = MainScreenViewModel(FakeDocumentRepository())
        assertEquals(MainScreenUiState.Loading, viewModel.uiState.first())
    }

    @Test
    fun searchQuery_updatesState() = runTest {
        val viewModel = MainScreenViewModel(FakeDocumentRepository())
        viewModel.onSearchQueryChanged("receipt")
        assertEquals("receipt", viewModel.searchQuery.value)
    }
}

private class FakeDocumentRepository : DocumentRepository {
    private val fakeDocs = MutableStateFlow<List<DocumentEntity>>(emptyList())
    override val documents: Flow<List<DocumentEntity>> = fakeDocs

    override fun searchDocuments(query: String): Flow<List<DocumentEntity>> {
        return flowOf(
            if (query.isBlank()) {
                fakeDocs.value
            } else {
                fakeDocs.value.filter { it.title.contains(query, ignoreCase = true) || it.content.contains(query, ignoreCase = true) }
            }
        )
    }

    override suspend fun insertDocument(title: String, content: String): Long {
        val newDoc = DocumentEntity(id = (fakeDocs.value.size + 1).toLong(), title = title, content = content)
        fakeDocs.value = fakeDocs.value + newDoc
        return newDoc.id
    }

    override suspend fun deleteDocument(document: DocumentEntity) {
        fakeDocs.value = fakeDocs.value.filter { it.id != document.id }
    }

    override suspend fun recognizeAndSaveFromBitmap(title: String, bitmap: Bitmap): Result<DocumentEntity> {
        val newDoc = DocumentEntity(id = (fakeDocs.value.size + 1).toLong(), title = title, content = "Recognized Text")
        fakeDocs.value = fakeDocs.value + newDoc
        return Result.success(newDoc)
    }

    override suspend fun recognizeAndSaveFromUri(context: Context, title: String, uri: Uri): Result<DocumentEntity> {
        val newDoc = DocumentEntity(id = (fakeDocs.value.size + 1).toLong(), title = title, content = "Recognized Text from URI")
        fakeDocs.value = fakeDocs.value + newDoc
        return Result.success(newDoc)
    }
}
