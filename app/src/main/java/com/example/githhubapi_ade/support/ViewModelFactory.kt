package com.example.githhubapi_ade.support

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githhubapi_ade.local.ThemePreferences
import com.example.githhubapi_ade.viewModel.DetailViewModel
import com.example.githhubapi_ade.viewModel.FavoriteViewModel
import com.example.githhubapi_ade.viewModel.ThemeViewModel


@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val application: Application, private val pref: ThemePreferences) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(application) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(application) as T
        } else if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            return ThemeViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application, pref: ThemePreferences): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(application, pref).also { INSTANCE = it }
            }
        }
    }
}