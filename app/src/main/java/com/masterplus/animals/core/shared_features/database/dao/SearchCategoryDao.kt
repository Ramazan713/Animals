package com.masterplus.animals.core.shared_features.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.masterplus.animals.core.shared_features.database.entity.ClassEntity
import com.masterplus.animals.core.shared_features.database.entity.FamilyEntity
import com.masterplus.animals.core.shared_features.database.entity.HabitatCategoryEntity
import com.masterplus.animals.core.shared_features.database.entity.ListEntity
import com.masterplus.animals.core.shared_features.database.entity.OrderEntity

@Dao
interface SearchCategoryDao {

    @Query("""
        select * from classes where (scientific_name like :query or class_tr like :query)
        order by case when scientific_name like :queryOrder then 1 when class_tr like :queryOrder then 2 else 3 end
    """)
    fun searchPagingClassesTr(query: String, queryOrder: String): PagingSource<Int, ClassEntity>

    @Query("""
        select * from classes where id = :classId and (scientific_name like :query or class_tr like :query) 
        order by case when scientific_name like :queryOrder then 1 when class_tr like :queryOrder then 2 else 3 end
    """)
    fun searchPagingClassesTrWithId(query: String, queryOrder: String, classId: Int): PagingSource<Int, ClassEntity>


    @Query("""
        select * from classes where (scientific_name like :query or class_en like :query)
        order by case when scientific_name like :queryOrder then 1 when class_en like :queryOrder then 2 else 3 end
    """)
    fun searchPagingClassesEn(query: String, queryOrder: String): PagingSource<Int, ClassEntity>

    @Query("""
        select * from classes where id = :classId and (scientific_name like :query or class_en like :query)
        order by case when scientific_name like :queryOrder then 1 when class_en like :queryOrder then 2 else 3 end
    """)
    fun searchPagingClassesEnWithId(query: String, queryOrder: String, classId: Int): PagingSource<Int, ClassEntity>


    @Query("""
        select * from habitatcategories where habitat_category_tr like :query
        order by habitat_category_tr like :queryOrder
    """)
    fun searchPagingHabitatTr(query: String, queryOrder: String): PagingSource<Int, HabitatCategoryEntity>

    @Query("""
        select * from habitatcategories where id = :habitatId and (habitat_category_tr like :query)
        order by habitat_category_tr like :queryOrder
    """)
    fun searchPagingHabitatTrWithId(query: String, queryOrder: String, habitatId: Int): PagingSource<Int, HabitatCategoryEntity>

    @Query("""
        select * from habitatcategories where habitat_category_en like :query
        order by habitat_category_en like :queryOrder
    """)
    fun searchPagingHabitatEn(query: String, queryOrder: String): PagingSource<Int, HabitatCategoryEntity>

    @Query("""
        select * from habitatcategories where id = :habitatId and (habitat_category_en like :query)
        order by habitat_category_en like :queryOrder
    """)
    fun searchPagingHabitatEnWithId(query: String, queryOrder: String, habitatId: Int): PagingSource<Int, HabitatCategoryEntity>



    @Query("""
        select * from orders where (scientific_name like :query or order_tr like :query)
        order by case when scientific_name like :queryOrder then 1 when order_tr like :queryOrder then 2 else 3 end
    """)
    fun searchOrdersTr(query: String, queryOrder: String): PagingSource<Int, OrderEntity>

    @Query("""
        select * from orders where id = :orderId and (scientific_name like :query or order_tr like :query)
        order by case when scientific_name like :queryOrder then 1 when order_tr like :queryOrder then 2 else 3 end
    """)
    fun searchOrdersTrWithId(query: String, queryOrder: String, orderId: Int): PagingSource<Int, OrderEntity>


    @Query("""
        select * from orders where (scientific_name like :query or order_en like :query)
        order by case when scientific_name like :queryOrder then 1 when order_en like :queryOrder then 2 else 3 end
    """)
    fun searchOrdersEn(query: String, queryOrder: String): PagingSource<Int, OrderEntity>

    @Query("""
        select * from orders where id = :orderId and (scientific_name like :query or order_en like :query)
        order by case when scientific_name like :queryOrder then 1 when order_en like :queryOrder then 2 else 3 end
    """)
    fun searchOrdersEnWithId(query: String, queryOrder: String, orderId: Int): PagingSource<Int, OrderEntity>



    @Query("""
        select * from families where (scientific_name like :query or family_tr like :query)
        order by case when scientific_name like :queryOrder then 1 when family_tr like :queryOrder then 2 else 3 end
    """)
    fun searchFamiliesTr(query: String, queryOrder: String): PagingSource<Int, FamilyEntity>

    @Query("""
        select * from families where id = :familyId and (scientific_name like :query or family_tr like :query)
        order by case when scientific_name like :queryOrder then 1 when family_tr like :queryOrder then 2 else 3 end
    """)
    fun searchFamiliesTrWithId(query: String, queryOrder: String, familyId: Int): PagingSource<Int, FamilyEntity>

    @Query("""
        select * from families where (scientific_name like :query or family_en like :query)
        order by case when scientific_name like :queryOrder then 1 when family_en like :queryOrder then 2 else 3 end
    """)
    fun searchFamiliesEn(query: String, queryOrder: String): PagingSource<Int, FamilyEntity>

    @Query("""
        select * from families where id = :familyId and (scientific_name like :query or family_en like :query)
        order by case when scientific_name like :queryOrder then 1 when family_en like :queryOrder then 2 else 3 end
    """)
    fun searchFamiliesEnWithId(query: String, queryOrder: String, familyId: Int): PagingSource<Int, FamilyEntity>


    @Query("""
        select * from lists where name like :query
        order by name like :queryOrder
    """)
    fun searchLists(query: String, queryOrder: String): PagingSource<Int, ListEntity>

    @Query("""
        select * from lists where id = :listId and (name like :query)
        order by name like :queryOrder
    """)
    fun searchListsWithId(query: String, queryOrder: String, listId: Int): PagingSource<Int, ListEntity>

}