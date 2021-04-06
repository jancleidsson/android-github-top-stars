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
import com.jss.githubtopstars.core.data.Repo
import com.jss.githubtopstars.databinding.RepoListItemBinding
import com.jss.githubtopstars.utils.Constants

class ReposPagingDataAdapter :
        PagingDataAdapter<Repo, ReposPagingDataAdapter.RepoViewHolder>(RepositoryDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder =
            RepoViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.repo_list_item, parent, false)
            )

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        getItem(position)?.let { holder.bindTo(it) }
    }

    companion object {
        val RepositoryDiffCallback = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(
                    oldItem: Repo,
                    newItem: Repo,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                    oldItem: Repo,
                    newItem: Repo,
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
        private val binding = RepoListItemBinding.bind(view)
        private val context = view.context
        private val options = RequestOptions().centerCrop().error(R.drawable.ic_broken_image)
                .placeholder(R.drawable.ic_image_place_holder)
        private val glide = Glide.with(context).setDefaultRequestOptions(options)

        fun bindTo(repo: Repo) {
            binding.repositoryName.text = repo.name
            binding.repositoryForksCount.text = context.getString(R.string.forks).plus(Constants.EMPTY_SPACE).plus(repo.forks)
            binding.repositoryStargazersCount.text = context.getString(R.string.starts).plus(Constants.EMPTY_SPACE).plus(repo.stars)
            binding.repositoryOwnerName.text = repo.owner.login
            glide.load(repo.owner.photo).into(binding.repositoryOwnerPhoto)
        }
    }
}