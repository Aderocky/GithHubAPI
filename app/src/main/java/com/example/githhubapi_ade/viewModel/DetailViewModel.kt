package com.example.githhubapi_ade.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githhubapi_ade.data.response.DetailUserResponse
import com.example.githhubapi_ade.data.retrofit.ApiConfig
import com.example.githhubapi_ade.database.Favorite
import com.example.githhubapi_ade.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val mFavriteRepository: FavoriteRepository = FavoriteRepository(application)

    private val _loading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _loading

    private val _responseDetail = MutableLiveData<DetailUserResponse?>()
    val responseDetail: MutableLiveData<DetailUserResponse?> = _responseDetail

    private val _errorText = MutableLiveData<Event<String>>()
    val errorText: MutableLiveData<Event<String>> = _errorText


    fun insert(favorite: Favorite) {
        mFavriteRepository.insert(favorite)
    }

    fun delete(username: String) {
        mFavriteRepository.delete(username)
    }

    fun selectUser(login: String): LiveData<Boolean> {
        return mFavriteRepository.selectUser(login)
    }

    fun getUser(usernamaDetail: String) {
        _loading.value = true
        val client = ApiConfig.getApiService().getDetailUser(usernamaDetail)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _loading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _responseDetail.value = responseBody
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _loading.value = false
                _errorText.value = Event("Gagal mendapatkan detail user")
            }
        })
    }
}