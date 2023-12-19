package com.example.githhubapi_ade.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.githhubapi_ade.database.Favorite
import com.example.githhubapi_ade.database.FavoriteDao
import com.example.githhubapi_ade.database.FavoriteDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteDatabase.getDatabase(application)
        mFavDao = db.FavoriteDao()
    }

    fun getAllFav(): LiveData<List<Favorite>> = mFavDao.getAllFav()

    fun insert(note: Favorite) {
        executorService.execute { mFavDao.insert(note) }
    }

    fun delete(username: String) {
        executorService.execute { mFavDao.delete(username) }
    }

    fun selectUser(login: String): LiveData<Boolean> {
        return mFavDao.getFavUser(login).map { it.isNotEmpty() }
    }
}