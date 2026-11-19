package com.genius.snapscribe.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.genius.snapscribe.data.local.entity.DocumentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: DocumentEntity): Long

    @Update
    suspend fun updateDocument(document: DocumentEntity)

    @Delete
    suspend fun deleteDocument(document: DocumentEntity)

    @Query("SELECT * FROM documents WHERE rowid = :id")
    suspend fun getDocumentById(id: Long): DocumentEntity?

    @Query("SELECT * FROM documents ORDER BY rowid DESC")
    fun getAllDocuments(): Flow<List<DocumentEntity>>

    /**
     * Executes Full-Text Search (FTS4) matching against indexed columns.
     * Use SQLite MATCH syntax (e.g. "keyword*" for prefix search).
     */
    @Query("SELECT * FROM documents WHERE documents MATCH :ftsQuery ORDER BY rowid DESC")
    fun searchDocuments(ftsQuery: String): Flow<List<DocumentEntity>>
}
