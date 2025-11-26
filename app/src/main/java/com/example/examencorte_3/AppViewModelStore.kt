package com.example.examencorte_3

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

object AppViewModelStore : ViewModelStoreOwner {
    private val store = ViewModelStore()

    override val viewModelStore: ViewModelStore
        get() = store

    val provider: ViewModelProvider by lazy {
        ViewModelProvider(this)
    }
}
