package com.example.githhubapi_ade.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githhubapi_ade.database.Favorite
import com.example.githhubapi_ade.databinding.ListUserGithubBinding
import com.example.githhubapi_ade.ui.DetailActivity

class FavoriteAdapter : ListAdapter<Favorite, FavoriteAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ListUserGithubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }


    class MyViewHolder(private val binding: ListUserGithubBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(users: Favorite) {
            binding.namaHero.text = users.username
            binding.tvId.text = users.id.toString()

            Glide.with(binding.root.context)
                .load(users.avatarUrl)
                .into(binding.gambar)

            itemView.setOnClickListener {
                val userName = users.username
                val toDetail = Intent(binding.root.context, DetailActivity::class.java)
                toDetail.putExtra(DetailActivity.SEND_LOGIN, userName)
                binding.root.context.startActivity(toDetail)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Favorite>() {
            override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem == newItem
            }
        }
    }
}