package com.jss.githubtopstars.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jss.core.data.Repository
import com.jss.githubtopstars.R
import com.jss.githubtopstars.databinding.RepositoryItemBinding
import com.jss.githubtopstars.framework.db.RepositoryEntity
import com.jss.githubtopstars.framework.networking.State

class ReposPagedListAdapter :
    PagedListAdapter<RepositoryEntity, ReposPagedListAdapter.RepoViewHolder>(RepositoryDiffCallback) {

    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder =
        RepoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.repository_item, parent, false)
        )

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repositoryEntity: RepositoryEntity? = getItem(position)
        holder.bindTo(repositoryEntity)
    }

    companion object {
        val RepositoryDiffCallback = object : DiffUtil.ItemCallback<RepositoryEntity>() {
            override fun areItemsTheSame(
                oldItem: RepositoryEntity,
                newItem: RepositoryEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: RepositoryEntity,
                newItem: RepositoryEntity
            ): Boolean {
                return oldItem.name == newItem.name &&
                        oldItem.forks == newItem.forks &&
                        oldItem.stars == newItem.stars
                        && oldItem.owner?.login == newItem.owner?.login &&
                        oldItem.owner?.photo == newItem.owner?.photo
            }
        }
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    inner class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = RepositoryItemBinding.bind(view)
        private val context = view.context
        private val options = RequestOptions().centerCrop().error(R.drawable.ic_broken_image)
            .placeholder(R.drawable.ic_image_place_holder)
        private val glide = Glide.with(context).setDefaultRequestOptions(options)

        fun bindTo(repositoryEntity: RepositoryEntity?) {
            repositoryEntity?.let { repo ->
                repo.name.also { binding.repositoryName.text = it }
                "${context.getString(R.string.forks)} ${repo.forks}".also { binding.repositoryForksCount.text = it }
                "${context.getString(R.string.starts)} ${repo.stars}".also { binding.repositoryStargazersCount.text = it }
                repo.owner?.login.toString().also { binding.repositoryOwnerName.text = it }
                glide.load(repo.owner?.photo).into(binding.repositoryOwnerPhoto)
            }
        }
    }
}