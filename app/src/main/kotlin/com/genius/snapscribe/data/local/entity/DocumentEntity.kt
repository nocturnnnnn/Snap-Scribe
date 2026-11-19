package com.genius.snapscribe.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.FtsOptions
import androidx.room.PrimaryKey

/**
 * Full-Text Search (FTS4) entity for indexed documents.
 * In SQLite FTS4 virtual tables, the primary key must be an integer column named "rowid".
 */
@Fts4(tokenizer = FtsOptions.TOKENIZER_UNICODE61)
@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val id: Long = 0,
    val title: String,
    val content: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
