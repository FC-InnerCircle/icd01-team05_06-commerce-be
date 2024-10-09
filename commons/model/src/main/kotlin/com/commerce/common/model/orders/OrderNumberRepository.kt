package com.commerce.common.model.orders

import java.time.LocalDate

interface OrderNumberRepository {

    fun countIncrementAndGet(date: LocalDate): Long
}