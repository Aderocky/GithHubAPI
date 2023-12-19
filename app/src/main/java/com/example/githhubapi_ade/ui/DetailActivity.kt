package com.example.githhubapi_ade.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githhubapi_ade.R
import com.example.githhubapi_ade.adapter.SectionPagerAdapter
import com.example.githhubapi_ade.data.response.DetailUserResponse
import com.example.githhubapi_ade.database.Favorite
import com.example.githhubapi_ade.databinding.ActivityDetailBinding
import com.example.githhubapi_ade.local.ThemePreferences
import com.example.githhubapi_ade.local.dataStore
import com.example.githhubapi_ade.support.ViewModelFactory
import com.example.githhubapi_ade.viewModel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailviewModel: DetailViewModel

    private var isLoading = false

    private lateinit var theUsername: String
    private lateinit var theAvatar: String

    private var fav: Favorite? = Favorite()
    private var isFav = false

    companion object {
        const val SEND_LOGIN = "login"
        const val LOADING_STATE = "loading_state"
        private val TAB_TITLES = arrayOf(
            "Followers",
            "Following",
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val themePreferences = ThemePreferences.getInstance(application.dataStore)

        detailviewModel = obtainViewModel(this@DetailActivity, themePreferences)

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        sectionsPagerAdapter.username = intent.getStringExtra(SEND_LOGIN).toString()

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = TAB_TITLES[position]
        }.attach()
        supportActionBar?.elevation = 0f

        if (savedInstanceState != null) {
            theUsername = savedInstanceState.getString("username", "")
            isLoading = savedInstanceState.getBoolean(LOADING_STATE, false)
        } else {
            theUsername = intent.getStringExtra(SEND_LOGIN).toString()
        }

        if (savedInstanceState == null || isLoading) {
            detailviewModel.getUser(theUsername)
        }

        detailviewModel.responseDetail.observe(this) { detailResponse ->
            if (detailResponse != null) {
                theAvatar = detailResponse.avatarUrl
                setUserData(detailResponse)
            }
        }
        detailviewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
        detailviewModel.errorText.observe(this) { event ->
            event.getContentIfNotHandled()?.let { errorMessage ->
                Toast.makeText(this@DetailActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
        detailviewModel.selectUser(theUsername).observe(this) { isFavorite ->
            isFav = if (isFavorite) {
                binding.fabAdd.setImageDrawable(getDrawable(R.drawable.baseline_favorite_24))
                true
            } else {
                binding.fabAdd.setImageDrawable(getDrawable(R.drawable.baseline_favorite_border_24))
                false
            }

        }

        binding.fabAdd.setOnClickListener {
            if (isFav) {
                detailviewModel.delete(theUsername)
                isFav = false
            } else {
                fav?.username = theUsername
                fav?.avatarUrl = theAvatar
                detailviewModel.insert(fav as Favorite)
                isFav = true
            }

        }
        binding.btnShare.setOnClickListener {

            val shareMessage =
                "Hallo ayo saling mutualan akun Github , berikut username Github saya $theUsername"

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)

            startActivity(Intent.createChooser(shareIntent, "Bagikan melalui"))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("username", theUsername)
        outState.putBoolean(LOADING_STATE, isLoading)
    }

    private fun setUserData(theResponse: DetailUserResponse) {
        Glide.with(this@DetailActivity)
            .load(theResponse.avatarUrl)
            .into(binding.ivAvatar)
        binding.tvLogin.text = theResponse.login
        binding.tvName.text = theResponse.name?.toString() ?: ""
        binding.followerAcc.text = theResponse.followers.toString()
        binding.followingAcc.text = theResponse.following.toString()
    }

    private fun obtainViewModel(
        activity: AppCompatActivity,
        themePreferences: ThemePreferences
    ): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, themePreferences)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbHero.visibility = View.VISIBLE
            binding.tvMohon.visibility = View.VISIBLE
            binding.ivAvatar.visibility = View.INVISIBLE
            binding.tvLogin.visibility = View.INVISIBLE
            binding.tvName.visibility = View.INVISIBLE
            binding.consFF.visibility = View.INVISIBLE
        } else {
            binding.pbHero.visibility = View.GONE
            binding.tvMohon.visibility = View.GONE
            binding.ivAvatar.visibility = View.VISIBLE
            binding.tvLogin.visibility = View.VISIBLE
            binding.tvName.visibility = View.VISIBLE
            binding.consFF.visibility = View.VISIBLE
        }
    }
}