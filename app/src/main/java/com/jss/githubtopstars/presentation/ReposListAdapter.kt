package com.jss.githubtopstars.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jss.core.data.Repository
import com.jss.githubtopstars.R
import com.jss.githubtopstars.R.string
import com.jss.githubtopstars.databinding.RepositoryItemBinding
import com.jss.githubtopstars.presentation.ReposListAdapter.RepoViewHolder
import java.util.ArrayList

class ReposListAdapter(private val repositories: ArrayList<Repository>) : RecyclerView.Adapter<RepoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder = RepoViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.repository_item, parent, false))

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(repositories[position])
    }

    override fun getItemCount(): Int = repositories.size

    fun updateRepos(newRepositories: List<Repository>) {
        repositories.clear()
        repositories.addAll(newRepositories)
        notifyDataSetChanged()
    }

    inner class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = RepositoryItemBinding.bind(view)
        private val context = view.context
        private val options = RequestOptions().centerCrop().error(R.drawable.ic_broken_image).placeholder(R.drawable.ic_image_place_holder)
        private val glide = Glide.with(context).setDefaultRequestOptions(options)

        fun bind(repository: Repository) {
            repository.name.also { binding.repositoryName.text = it }
            "${context.getString(string.forks)} ${repository.forks}".also { binding.repositoryForksCount.text = it }
            "${context.getString(string.starts)} ${repository.stars}".also { binding.repositoryStargazersCount.text = it }
            repository.owner?.name.toString().also { binding.repositoryOwnerName.text = it }
            glide.load(repository.owner?.photo).into(binding.repositoryOwnerPhoto)
        }
    }
}