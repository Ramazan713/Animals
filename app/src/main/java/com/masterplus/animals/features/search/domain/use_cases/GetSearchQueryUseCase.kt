package com.masterplus.animals.features.search.domain.use_cases

class GetSearchQueryUseCase {

    operator fun invoke(query: String): QueryResult{
        val rawQuery = query.trim()
        val queryInLike = rawQuery.let { "%$it%" }.split(" ").joinToString("%")
        val queryForOrder = rawQuery.let { "$it%" }.split(" ").joinToString("%")
        return QueryResult(
            rawQuery = rawQuery,
            queryInLike = queryInLike,
            queryForOrder = queryForOrder
        )
    }

    companion object {
        data class QueryResult(
            val rawQuery: String,
            val queryInLike: String,
            val queryForOrder: String
        )
    }
}