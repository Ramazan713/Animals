package com.masterplus.animals.core.data.repo

import com.masterplus.animals.core.domain.repo.StringProvider
import org.junit.jupiter.api.Assertions.*

class StringProviderFake: StringProvider{
    var returnedValue: String = "mockValue"

    override fun getString(resId: Int, vararg args: Any): String {
        return returnedValue
    }
}