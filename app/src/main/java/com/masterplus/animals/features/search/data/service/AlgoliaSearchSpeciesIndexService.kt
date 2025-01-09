package com.masterplus.animals.features.search.data.service

import com.algolia.client.api.SearchClient
import com.algolia.client.model.search.FacetFilters
import com.algolia.client.model.search.SearchForFacetValuesResponse
import com.algolia.client.model.search.SearchForHits
import com.algolia.client.model.search.SearchMethodParams
import com.algolia.client.model.search.SearchParams
import com.algolia.client.model.search.SearchParamsObject
import com.algolia.client.model.search.SearchResponse
import com.algolia.client.model.search.SearchResult
import com.masterplus.animals.BuildConfig
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.safeCall
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.features.search.domain.service.SearchSpeciesIndexService

class AlgoliaSearchSpeciesIndexService: SearchSpeciesIndexService {

    private val client = SearchClient(
        BuildConfig.ALGOLIA_APP_ID,
        BuildConfig.ALGOLIA_API_KEY
    )

    override suspend fun searchAll(
        query: String,
        pageSize: Int,
        languageEnum: LanguageEnum
    ): DefaultResult<List<SearchSpeciesIndexService.Companion.SearchAllResult>> {
        val name = if(languageEnum.isTr) "name_tr" else "name_en"
        return safeCall {
            val response = client.search(
                searchMethodParams = SearchMethodParams(
                    requests = listOf(
                        SearchForHits(
                            indexName = SPECIES_INDEX_NAME,
                            hitsPerPage = pageSize,
                            query = query,
                            restrictSearchableAttributes = listOf("scientific_name",name)
                        ),
                        SearchForHits(
                            indexName = CLASSES_INDEX_NAME,
                            hitsPerPage = pageSize,
                            query = query,
                            restrictSearchableAttributes = getCategorySearchableAttributes(
                                categoryType = CategoryType.Class,
                                languageEnum = languageEnum
                            )
                        ),
                        SearchForHits(
                            indexName = ORDERS_INDEX_NAME,
                            hitsPerPage = pageSize,
                            query = query,
                            restrictSearchableAttributes = getCategorySearchableAttributes(
                                categoryType = CategoryType.Order,
                                languageEnum = languageEnum
                            )
                        ),
                        SearchForHits(
                            indexName = FAMILIES_INDEX_NAME,
                            hitsPerPage = pageSize,
                            query = query,
                            restrictSearchableAttributes = getCategorySearchableAttributes(
                                categoryType = CategoryType.Family,
                                languageEnum = languageEnum
                            )
                        )
                    )
                )
            )
            val dataMap = mapOf<String,MutableList<String>>(
                SPECIES_INDEX_NAME to mutableListOf(),
                CLASSES_INDEX_NAME to mutableListOf(),
                ORDERS_INDEX_NAME to mutableListOf(),
                FAMILIES_INDEX_NAME to mutableListOf()
            )
            response.results.forEach { searchResult ->
                when(searchResult){
                    is SearchForFacetValuesResponse -> Unit
                    is SearchResponse -> dataMap[searchResult.index]?.addAll(searchResult.hits.map { it.objectID })
                    is SearchResult.SearchForFacetValuesResponseValue -> Unit
                    is SearchResult.SearchResponseValue -> dataMap[searchResult.value.index]?.addAll(searchResult.value.hits.map { it.objectID })
                }
            }
            listOf(
                SearchSpeciesIndexService.Companion.SearchAllResult(
                    ids = dataMap[SPECIES_INDEX_NAME].orEmpty(),
                    type = SearchSpeciesIndexService.Companion.SearchAllResultType.Species
                ),
                SearchSpeciesIndexService.Companion.SearchAllResult(
                    ids = dataMap[CLASSES_INDEX_NAME].orEmpty(),
                    type = SearchSpeciesIndexService.Companion.SearchAllResultType.Classes
                ),
                SearchSpeciesIndexService.Companion.SearchAllResult(
                    ids = dataMap[ORDERS_INDEX_NAME].orEmpty(),
                    type = SearchSpeciesIndexService.Companion.SearchAllResultType.Orders
                ),
                SearchSpeciesIndexService.Companion.SearchAllResult(
                    ids = dataMap[FAMILIES_INDEX_NAME].orEmpty(),
                    type = SearchSpeciesIndexService.Companion.SearchAllResultType.Families
                )
            )
        }
    }

    override suspend fun searchSpecies(
        query: String,
        categoryItemId: Int?,
        pageSize: Int,
        categoryType: CategoryType,
        kingdomType: KingdomType,
        languageEnum: LanguageEnum
    ): DefaultResult<List<String>>{
        val name = if(languageEnum.isTr) "name_tr" else "name_en"
        val facetFilters = mutableListOf(
            FacetFilters.of("kingdom_id:${kingdomType.kingdomId}")
        )

        if(categoryItemId != null){
            facetFilters.add(FacetFilters.Companion.of("${getDbKeyId(categoryType)}:${categoryItemId}"))
        }
        return safeCall {
            val response = client.searchSingleIndex(
                indexName = SPECIES_INDEX_NAME,
                searchParams = SearchParams.of(SearchParamsObject(
                    query = query,
                    hitsPerPage = pageSize,
                    page = 0,
                    facetFilters = FacetFilters.of(facetFilters),
                    restrictSearchableAttributes = listOf("scientific_name",name)
                ))
            )
            response.hits.map { it.objectID }
        }
    }

    override suspend fun searchCategories(
        query: String,
        parentItemId: Int?,
        pageSize: Int,
        categoryType: CategoryType,
        kingdomType: KingdomType,
        languageEnum: LanguageEnum
    ): DefaultResult<List<String>> {
        return safeCall {
            val response = client.searchSingleIndex(
                indexName = getIndexNameFromCategoryType(categoryType),
                searchParams = SearchParams.of(SearchParamsObject(
                    query = query,
                    hitsPerPage = pageSize,
                    page = 0,
                    facetFilters = getFiltersFromCategoryType(
                        categoryType = categoryType,
                        categoryItemId = parentItemId,
                        kingdomType = kingdomType
                    ),
                    restrictSearchableAttributes = getCategorySearchableAttributes(
                        categoryType = categoryType,
                        languageEnum = languageEnum
                    )
                ))
            )
            response.hits.map { it.objectID }
        }
    }

    private fun getFiltersFromCategoryType(
        categoryType: CategoryType,
        categoryItemId: Int?,
        kingdomType: KingdomType,
    ): FacetFilters{
        val filters = mutableListOf<FacetFilters>(
            FacetFilters.Companion.of("kingdom_id:${kingdomType.kingdomId}")
        )
        if(categoryItemId != null){
            when(categoryType){
                CategoryType.Habitat -> Unit
                CategoryType.Class -> filters.add(FacetFilters.Companion.of("phylum_id:$categoryItemId"))
                CategoryType.Order -> filters.add(FacetFilters.Companion.of("class_id:$categoryItemId"))
                CategoryType.Family -> filters.add(FacetFilters.Companion.of("order_id:$categoryItemId"))
                CategoryType.List -> Unit
            }
        }
        return FacetFilters.of(filters)
    }

    private fun getCategorySearchableAttributes(
        categoryType: CategoryType,
        languageEnum: LanguageEnum
    ): List<String>{
        val searchableAttributes = mutableListOf("scientific_name")
        val isEn = languageEnum.isEn
        when(categoryType){
            CategoryType.Habitat -> Unit
            CategoryType.Class -> searchableAttributes.add(if(isEn) "class_en" else "class_tr")
            CategoryType.Order -> searchableAttributes.add(if(isEn) "order_en" else "order_tr")
            CategoryType.Family -> searchableAttributes.add(if(isEn) "family_en" else "family_tr")
            CategoryType.List -> Unit
        }
        return searchableAttributes
    }

    private fun getIndexNameFromCategoryType(categoryType: CategoryType): String{
        return when(categoryType){
            CategoryType.Habitat -> HABITATS_INDEX_NAME
            CategoryType.Class -> CLASSES_INDEX_NAME
            CategoryType.Order -> ORDERS_INDEX_NAME
            CategoryType.Family -> FAMILIES_INDEX_NAME
            CategoryType.List -> ""
        }
    }

    private fun getDbKeyId(categoryType: CategoryType): String{
        return when(categoryType){
            CategoryType.Habitat -> "habitats"
            CategoryType.Class -> "class_id"
            CategoryType.Order -> "order_id"
            CategoryType.Family -> "family_id"
            CategoryType.List -> ""
        }
    }


    companion object{
        private const val SPECIES_INDEX_NAME = "species-default-index"
        private const val HABITATS_INDEX_NAME = "habitats-default-index"
        private const val CLASSES_INDEX_NAME = "classes-default-index"
        private const val ORDERS_INDEX_NAME = "orders-default-index"
        private const val FAMILIES_INDEX_NAME = "families-default-index"
    }

}