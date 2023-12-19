package com.example.githhubapi_ade.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githhubapi_ade.adapter.FavoriteAdapter
import com.example.githhubapi_ade.database.Favorite
import com.example.githhubapi_ade.databinding.ActivityFavoriteBinding
import com.example.githhubapi_ade.local.ThemePreferences
import com.example.githhubapi_ade.local.dataStore
import com.example.githhubapi_ade.support.ViewModelFactory
import com.example.githhubapi_ade.viewModel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteviewModel: FavoriteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val themePreferences = ThemePreferences.getInstance(application.dataStore)

        favoriteviewModel = obtainViewModel(this@FavoriteActivity, themePreferences)

        favoriteviewModel.getAllNotes().observe(this) { favList ->
            if (favList != null) {
                setReviewData(favList)
                binding.tvEmpty.visibility = if (favList.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        favoriteviewModel.isLoading.observe(this) { loading ->
            if (loading != null) showLoading(loading)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvHero.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHero.addItemDecoration(itemDecoration)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun obtainViewModel(
        activity: AppCompatActivity,
        themePreferences: ThemePreferences
    ): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, themePreferences)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun setReviewData(userData: List<Favorite>) {
        val adapter = FavoriteAdapter()
        adapter.submitList(userData)
        binding.rvHero.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbHero.visibility = View.VISIBLE
            binding.tvMohon.visibility = View.VISIBLE
        } else {
            binding.pbHero.visibility = View.GONE
            binding.tvMohon.visibility = View.GONE
        }
    }
}