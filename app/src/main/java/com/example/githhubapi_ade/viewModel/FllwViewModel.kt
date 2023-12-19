package com.example.githhubapi_ade.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githhubapi_ade.data.response.FollowResponseItem
import com.example.githhubapi_ade.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FllwViewModel : ViewModel() {
    private val _listFollow = MutableLiveData<List<FollowResponseItem>?>()
    val listFollow: LiveData<List<FollowResponseItem>?> = _listFollow

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorText = MutableLiveData<Event<String>>()
    val errorText: MutableLiveData<Event<String>> = _errorText

    private val _empty = MutableLiveData<Boolean>()
    val empty: LiveData<Boolean> = _empty

    private val _temp = MutableLiveData<String>()
    val temp: LiveData<String> = _temp


    fun showFllw(username: String, isfllw: Boolean) {
        _loading.value = true
        val client = if (isfllw) {
            ApiConfig.getApiService().getFollowers(username)
        } else {
            ApiConfig.getApiService().getFollowing(username)
        }
        client.enqueue(object : Callback<List<FollowResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowResponseItem>?>,
                response: Response<List<FollowResponseItem>?>
            ) {
                _loading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _listFollow.value = responseBody
                    if (responseBody.isEmpty()) {
                        _empty.value = true
                        if (isfllw) {
                            _temp.value = "Tidak ada pengikut."
                        } else {
                            _temp.value = "Tidak ada yang diikuti."
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<FollowResponseItem>?>, t: Throwable) {
                _loading.value = false
                _errorText.value = Event("Gagal mendapatkan detail user")
            }
        })
    }
}