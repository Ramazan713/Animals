package com.masterplus.animals.core.shared_features.database

interface TransactionProvider {

    suspend fun <R> runAsTransaction(block: suspend () -> R): R

}