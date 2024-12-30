package com.masterplus.animals.core.data.extensions

import androidx.paging.LoadType
import com.google.firebase.firestore.Source
import com.masterplus.animals.core.domain.enums.RemoteLoadType
import com.masterplus.animals.core.domain.enums.RemoteSourceType

fun RemoteSourceType.toFirebaseSource(): Source{
    return when(this){
        RemoteSourceType.DEFAULT -> Source.DEFAULT
        RemoteSourceType.CACHE -> Source.CACHE
        RemoteSourceType.SERVER -> Source.SERVER
    }
}

fun LoadType.toRemoteLoadType(): RemoteLoadType{
    return when(this) {
        LoadType.REFRESH -> RemoteLoadType.REFRESH
        LoadType.PREPEND -> RemoteLoadType.PREPEND
        LoadType.APPEND -> RemoteLoadType.APPEND
    }
}