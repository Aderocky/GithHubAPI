package com.example.githhubapi_ade.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githhubapi_ade.database.Favorite
import com.example.githhubapi_ade.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val mFavRepository: FavoriteRepository = FavoriteRepository(application)
    fun getAllNotes(): LiveData<List<Favorite>> = mFavRepository.getAllFav()


}