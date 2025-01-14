package com.masterplus.animals.core.shared_features.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.animals.core.shared_features.database.entity_helper.ClassWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.FamilyWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.HabitatWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.OrderWithImageEmbedded

@Dao
interface SearchCategoryDao {

    @Transaction
    @Query("""
        select * from classes where (scientific_name like :query or class_tr like :query) group by id
        order by case when scientific_name like :queryOrder then 1 when class_tr like :queryOrder then 2 else 3 end
    """)
    fun searchPagingClassesTr(query: String, queryOrder: String): PagingSource<Int, ClassWithImageEmbedded>

    @Transaction
    @Query("""
        select * from classes where (scientific_name like :query or class_en like :query) group by id
        order by case when scientific_name like :queryOrder then 1 when class_en like :queryOrder then 2 else 3 end
    """)
    fun searchPagingClassesEn(query: String, queryOrder: String): PagingSource<Int, ClassWithImageEmbedded>


    @Transaction
    @Query("""
        select * from orders where (scientific_name like :query or order_tr like :query) group by id
        order by case when scientific_name like :queryOrder then 1 when order_tr like :queryOrder then 2 else 3 end
    """)
    fun searchOrdersTr(query: String, queryOrder: String): PagingSource<Int, OrderWithImageEmbedded>

    @Transaction
    @Query(
        """
        select * from orders where class_id = :classId and (scientific_name like :query or order_tr like :query)
        group by id
        order by case when scientific_name like :queryOrder then 1 when order_tr like :queryOrder then 2 else 3 end
    """
    )
    fun searchOrdersTrWithClassId(query: String, queryOrder: String, classId: Int): PagingSource<Int, OrderWithImageEmbedded>

    @Transaction
    @Query("""
        select * from orders where (scientific_name like :query or order_en like :query) group by id
        order by case when scientific_name like :queryOrder then 1 when order_en like :queryOrder then 2 else 3 end
    """)
    fun searchOrdersEn(query: String, queryOrder: String): PagingSource<Int, OrderWithImageEmbedded>

    @Transaction
    @Query(
        """
        select * from orders where class_id = :classId and (scientific_name like :query or order_en like :query) group by id
        order by case when scientific_name like :queryOrder then 1 when order_en like :queryOrder then 2 else 3 end
    """
    )
    fun searchOrdersEnWithClassId(query: String, queryOrder: String, classId: Int): PagingSource<Int, OrderWithImageEmbedded>


    @Transaction
    @Query("""
        select * from families where (scientific_name like :query or family_tr like :query) group by id
        order by case when scientific_name like :queryOrder then 1 when family_tr like :queryOrder then 2 else 3 end
    """)
    fun searchFamiliesTr(query: String, queryOrder: String): PagingSource<Int, FamilyWithImageEmbedded>

    @Transaction
    @Query(
        """
        select * from families where order_id = :orderId and (scientific_name like :query or family_tr like :query)
        group by id
        order by case when scientific_name like :queryOrder then 1 when family_tr like :queryOrder then 2 else 3 end
    """
    )
    fun searchFamiliesTrWithOrderId(query: String, queryOrder: String, orderId: Int): PagingSource<Int, FamilyWithImageEmbedded>

    @Transaction
    @Query("""
        select * from families where (scientific_name like :query or family_en like :query) group by id
        order by case when scientific_name like :queryOrder then 1 when family_en like :queryOrder then 2 else 3 end
    """)
    fun searchFamiliesEn(query: String, queryOrder: String): PagingSource<Int, FamilyWithImageEmbedded>

    @Transaction
    @Query(
        """
        select * from families where order_id = :orderId and (scientific_name like :query or family_en like :query) group by id
        order by case when scientific_name like :queryOrder then 1 when family_en like :queryOrder then 2 else 3 end
    """
    )
    fun searchFamiliesEnWithOrderId(query: String, queryOrder: String, orderId: Int): PagingSource<Int, FamilyWithImageEmbedded>


    @Transaction
    @Query("""
        select HC.* from habitatcategories HC, habitatkingdoms HK 
        where HC.id = HK.habitat_id and HK.kingdom_id = :kingdomId and HC.habitat_category_tr like :query group by HC.id
        order by case when HC.habitat_category_tr like :queryOrder then 1 else 2 end
    """)
    fun searchHabitatsTr(query: String, kingdomId: Int, queryOrder: String): PagingSource<Int, HabitatWithImageEmbedded>

    @Transaction
    @Query("""
        select HC.* from habitatcategories HC, habitatkingdoms HK 
        where HC.id = HK.habitat_id and HK.kingdom_id = :kingdomId and HC.habitat_category_en like :query group by HC.id
        order by case when HC.habitat_category_tr like :queryOrder then 1 else 2 end
    """)
    fun searchHabitatsEn(query: String, kingdomId: Int, queryOrder: String): PagingSource<Int, HabitatWithImageEmbedded>
}