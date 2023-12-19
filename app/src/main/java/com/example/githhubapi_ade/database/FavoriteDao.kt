package com.example.githhubapi_ade.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getAllFav(): LiveData<List<Favorite>>

    @Query("SELECT * FROM users WHERE login = :username")
    fun getFavUser(username: String): LiveData<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: Favorite)

    @Query("DELETE FROM users WHERE login = :username")
    fun delete(username: String)
}