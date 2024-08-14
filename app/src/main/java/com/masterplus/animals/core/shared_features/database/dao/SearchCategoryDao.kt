package com.masterplus.animals.core.shared_features.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.masterplus.animals.core.shared_features.database.entity.ClassEntity
import com.masterplus.animals.core.shared_features.database.entity.FamilyEntity
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
        select * from classes where (scientific_name like :query or class_en like :query)
        order by case when scientific_name like :queryOrder then 1 when class_en like :queryOrder then 2 else 3 end
    """)
    fun searchPagingClassesEn(query: String, queryOrder: String): PagingSource<Int, ClassEntity>



    @Query("""
        select * from orders where (scientific_name like :query or order_tr like :query)
        order by case when scientific_name like :queryOrder then 1 when order_tr like :queryOrder then 2 else 3 end
    """)
    fun searchOrdersTr(query: String, queryOrder: String): PagingSource<Int, OrderEntity>

    @Query(
        """
        select * from orders where class_id = :classId and (scientific_name like :query or order_tr like :query)
        order by case when scientific_name like :queryOrder then 1 when order_tr like :queryOrder then 2 else 3 end
    """
    )
    fun searchOrdersTrWithClassId(query: String, queryOrder: String, classId: Int): PagingSource<Int, OrderEntity>


    @Query("""
        select * from orders where (scientific_name like :query or order_en like :query)
        order by case when scientific_name like :queryOrder then 1 when order_en like :queryOrder then 2 else 3 end
    """)
    fun searchOrdersEn(query: String, queryOrder: String): PagingSource<Int, OrderEntity>

    @Query(
        """
        select * from orders where class_id = :classId and (scientific_name like :query or order_en like :query)
        order by case when scientific_name like :queryOrder then 1 when order_en like :queryOrder then 2 else 3 end
    """
    )
    fun searchOrdersEnWithClassId(query: String, queryOrder: String, classId: Int): PagingSource<Int, OrderEntity>



    @Query("""
        select * from families where (scientific_name like :query or family_tr like :query)
        order by case when scientific_name like :queryOrder then 1 when family_tr like :queryOrder then 2 else 3 end
    """)
    fun searchFamiliesTr(query: String, queryOrder: String): PagingSource<Int, FamilyEntity>

    @Query(
        """
        select * from families where order_id = :orderId and (scientific_name like :query or family_tr like :query)
        order by case when scientific_name like :queryOrder then 1 when family_tr like :queryOrder then 2 else 3 end
    """
    )
    fun searchFamiliesTrWithOrderId(query: String, queryOrder: String, orderId: Int): PagingSource<Int, FamilyEntity>

    @Query("""
        select * from families where (scientific_name like :query or family_en like :query)
        order by case when scientific_name like :queryOrder then 1 when family_en like :queryOrder then 2 else 3 end
    """)
    fun searchFamiliesEn(query: String, queryOrder: String): PagingSource<Int, FamilyEntity>

    @Query(
        """
        select * from families where order_id = :orderId and (scientific_name like :query or family_en like :query)
        order by case when scientific_name like :queryOrder then 1 when family_en like :queryOrder then 2 else 3 end
    """
    )
    fun searchFamiliesEnWithOrderId(query: String, queryOrder: String, orderId: Int): PagingSource<Int, FamilyEntity>


}