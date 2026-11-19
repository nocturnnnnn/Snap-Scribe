package com.genius.snapscribe.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.genius.snapscribe.data.local.dao.DocumentDao
import com.genius.snapscribe.data.local.entity.DocumentEntity
import com.genius.snapscribe.mlkit.TextRecognitionManager
import kotlinx.coroutines.flow.Flow

interface DocumentRepository {
    val documents: Flow<List<DocumentEntity>>
    fun searchDocuments(query: String): Flow<List<DocumentEntity>>
    suspend fun insertDocument(title: String, content: String): Long
    suspend fun deleteDocument(document: DocumentEntity)
    suspend fun recognizeAndSaveFromBitmap(title: String, bitmap: Bitmap): Result<DocumentEntity>
    suspend fun recognizeAndSaveFromUri(context: Context, title: String, uri: Uri): Result<DocumentEntity>
}

class DefaultDocumentRepository(
    private val documentDao: DocumentDao,
    private val textRecognitionManager: TextRecognitionManager = TextRecognitionManager()
) : DocumentRepository {

    override val documents: Flow<List<DocumentEntity>> = documentDao.getAllDocuments()

    override fun searchDocuments(query: String): Flow<List<DocumentEntity>> {
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            return documentDao.getAllDocuments()
        }
        // SQLite FTS4 prefix query format: append '*' for prefix search
        val sanitized = trimmed.replace("\"", "\"\"")
        val ftsQuery = if (sanitized.endsWith("*")) sanitized else "$sanitized*"
        return documentDao.searchDocuments(ftsQuery)
    }

    override suspend fun insertDocument(title: String, content: String): Long {
        return documentDao.insertDocument(
            DocumentEntity(
                title = title,
                content = content
            )
        )
    }

    override suspend fun deleteDocument(document: DocumentEntity) {
        documentDao.deleteDocument(document)
    }

    override suspend fun recognizeAndSaveFromBitmap(title: String, bitmap: Bitmap): Result<DocumentEntity> {
        val result = textRecognitionManager.recognizeTextFromBitmap(bitmap)
        return result.mapCatching { visionText ->
            val entity = DocumentEntity(
                title = title,
                content = visionText.text
            )
            val id = documentDao.insertDocument(entity)
            entity.copy(id = id)
        }
    }

    override suspend fun recognizeAndSaveFromUri(
        context: Context,
        title: String,
        uri: Uri
    ): Result<DocumentEntity> {
        val result = textRecognitionManager.recognizeTextFromUri(context, uri)
        return result.mapCatching { visionText ->
            val entity = DocumentEntity(
                title = title,
                content = visionText.text
            )
            val id = documentDao.insertDocument(entity)
            entity.copy(id = id)
        }
    }
}
