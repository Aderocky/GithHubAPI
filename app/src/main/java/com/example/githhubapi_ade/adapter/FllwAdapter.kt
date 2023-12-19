package com.example.githhubapi_ade.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githhubapi_ade.data.response.FollowResponseItem
import com.example.githhubapi_ade.databinding.ListUserGithubBinding

class FllwAdapter : ListAdapter<FollowResponseItem, FllwAdapter.MyViewHolder>(DIFF_CALLBACK) {
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
        fun bind(user: FollowResponseItem) {
            binding.namaHero.text = user.login
            binding.tvId.text = user.id.toString()

            Glide.with(binding.root.context)
                .load(user.avatarUrl)
                .into(binding.gambar)

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowResponseItem>() {
            override fun areItemsTheSame(
                oldItem: FollowResponseItem,
                newItem: FollowResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: FollowResponseItem,
                newItem: FollowResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}


