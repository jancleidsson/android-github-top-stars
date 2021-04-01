package com.jss.githubtopstars.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jss.githubtopstars.R
import com.jss.githubtopstars.databinding.RepositoryItemBinding
import com.jss.githubtopstars.core.data.Repo

class ReposPagingDataAdapter :
    PagingDataAdapter<Repo, ReposPagingDataAdapter.RepoViewHolder>(RepositoryDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder =
        RepoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.repository_item, parent, false)
        )

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = getItem(position)
        holder.bindTo(repo)
    }

    companion object {
        val RepositoryDiffCallback = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(
                oldItem: Repo,
                newItem: Repo
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Repo,
                newItem: Repo
            ): Boolean {
                return oldItem.name == newItem.name &&
                        oldItem.forks == newItem.forks &&
                        oldItem.stars == newItem.stars
                        && oldItem.owner.login == newItem.owner.login &&
                        oldItem.owner.photo == newItem.owner.photo
            }
        }
    }

    inner class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = RepositoryItemBinding.bind(view)
        private val context = view.context
        private val options = RequestOptions().centerCrop().error(R.drawable.ic_broken_image)
            .placeholder(R.drawable.ic_image_place_holder)
        private val glide = Glide.with(context).setDefaultRequestOptions(options)

        fun bindTo(repo: Repo?) {
            repo?.let { repository ->
                repository.name.also { binding.repositoryName.text = it }
                "${context.getString(R.string.forks)} ${repository.forks}".also {
                    binding.repositoryForksCount.text = it
                }
                "${context.getString(R.string.starts)} ${repository.stars}".also {
                    binding.repositoryStargazersCount.text = it
                }
                repository.owner.login.also { binding.repositoryOwnerName.text = it }
                glide.load(repository.owner.photo).into(binding.repositoryOwnerPhoto)
            }
        }
    }
}