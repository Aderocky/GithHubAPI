package com.example.githhubapi_ade.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githhubapi_ade.data.response.GithubResponse
import com.example.githhubapi_ade.data.response.ItemsItem
import com.example.githhubapi_ade.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listUser = MutableLiveData<List<ItemsItem>?>()
    val listUser: LiveData<List<ItemsItem>?> = _listUser

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorText = MutableLiveData<Event<String>>()
    val errorText: MutableLiveData<Event<String>> = _errorText

    init {
        UserGithub()
    }

    fun UserGithub() {
        _loading.value = true
        val client = ApiConfig.getApiService().getUsers()
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _loading.value = false
                if (response.isSuccessful) {
                    val userData = response.body()
                    if (userData != null) {
                        _listUser.value = userData
                    }
                } else {
                    _errorText.value = Event("Gagal mendapatkan data-data user")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _loading.value = false
                _errorText.value = Event("Gagal mendapatkan data-data user")
            }
        })
    }

    fun findUser(username: String) {
        _loading.value = true
        val apiService = ApiConfig.getApiService().getSearchUser(username)
        apiService.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>, response: Response<GithubResponse>
            ) {
                _loading.value = false
                val githubResponse = response.body()

                if (response.isSuccessful && githubResponse != null) {
                    if (githubResponse.items.isNotEmpty()) {
                        _listUser.value = githubResponse.items
                    } else {
                        _listUser.value = githubResponse.items
                        _errorText.value = Event("User tidak ada")
                    }
                } else {
                    _errorText.value = Event("User tidak ada")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _loading.value = false
                _errorText.value = Event("Gagal mendapatkan data-data User yang dicari")
            }
        })
    }
}