package com.example.cleannote.domain.util

sealed class OrderType{
    object Ascending : OrderType()
    object Descending : OrderType()
}
