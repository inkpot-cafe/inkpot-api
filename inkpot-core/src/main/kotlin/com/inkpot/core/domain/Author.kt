package com.inkpot.core.domain

import java.util.*

interface Author {

    fun getUuid(): UUID
    fun setName(name: String)
    fun getName(): String

}
