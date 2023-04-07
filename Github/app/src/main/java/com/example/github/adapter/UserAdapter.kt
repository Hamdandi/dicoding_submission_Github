package com.example.github.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.github.data.model.GithubResponse
import com.example.github.databinding.ItemUserBinding

class UserAdapter(
    private val data: MutableList<GithubResponse.ItemsItem> = mutableListOf(),
    private val listener: (GithubResponse.ItemsItem) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    fun setData(data: MutableList<GithubResponse.ItemsItem>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class UserViewHolder(private val v: ItemUserBinding) : RecyclerView.ViewHolder(v.root) {
        fun bind(user: GithubResponse.ItemsItem) {
            v.apply {
                tvUsername.text = user.login
                Glide.with(imgAvatar.context)
                    .load(user.avatar_url)
                    .into(imgAvatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }
}

