package com.example.githhubapi_ade.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githhubapi_ade.R
import com.example.githhubapi_ade.adapter.UserAdapter
import com.example.githhubapi_ade.data.response.ItemsItem
import com.example.githhubapi_ade.databinding.ActivityMainBinding
import com.example.githhubapi_ade.local.ThemePreferences
import com.example.githhubapi_ade.local.dataStore
import com.example.githhubapi_ade.support.ViewModelFactory
import com.example.githhubapi_ade.viewModel.MainViewModel
import com.example.githhubapi_ade.viewModel.ThemeViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.listUser.observe(this) { listUser ->
            if (listUser != null) setReviewData(listUser)
        }

        viewModel.errorText.observe(this) {
            it.getContentIfNotHandled()?.let { theMsg ->
                Toast.makeText(this@MainActivity, theMsg, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.loading.observe(this) { loading ->
            if (loading != null) showLoading(loading)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    val searchText = searchView.text.toString().trim()
                    if (searchText.isEmpty()) {
                        viewModel.UserGithub()
                    } else {
                        viewModel.findUser(searchText)
                    }
                    false
                }
        }

        supportActionBar?.hide()
        val layoutManager = LinearLayoutManager(this)
        binding.rvHero.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHero.addItemDecoration(itemDecoration)


        val menuTheme = binding.searchBar.menu.findItem(R.id.action_theme)
        val menuFavorite = binding.searchBar.menu.findItem(R.id.action_favorite)
        var isDarkMode = false

        val pref = ThemePreferences.getInstance(application.dataStore)
        val themeViewModel = ViewModelProvider(this, ViewModelFactory(application, pref))[ThemeViewModel::class.java]
        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            isDarkMode = isDarkModeActive
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                menuTheme.setIcon(R.drawable.baseline_brightness_low_24)
                menuFavorite.setIcon(R.drawable.baseline_favorite_white__24)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                menuTheme.setIcon(R.drawable.baseline_bedtime_24)
                menuFavorite.setIcon(R.drawable.baseline_favorite_24)
            }
        }

        binding.searchBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_theme -> {
                    themeViewModel.saveThemeSetting(!isDarkMode)
                    true
                }

                R.id.action_favorite -> {
                    val intent = Intent(this, FavoriteActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

    }

    private fun setReviewData(userData: List<ItemsItem>) {
        val adapter = UserAdapter()
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