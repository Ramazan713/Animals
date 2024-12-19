package com.masterplus.animals.core.shared_features.database

import androidx.room.withTransaction

class TransactionProviderImpl(
    private val db: AppDatabase
): TransactionProvider {
    override suspend fun <R> runAsTransaction(block: suspend () -> R): R {
        return db.withTransaction(block)
    }
}