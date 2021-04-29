package com.jss.githubtopstars.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jss.githubtopstars.R
import com.jss.githubtopstars.databinding.ReposListStateFooterItemBinding
import com.jss.githubtopstars.utils.Constants

class RepoLoadStateAdapter(
        private val retry: () -> Unit,
) : LoadStateAdapter<RepoLoadStateAdapter.ReposLoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ReposLoadStateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repos_list_state_footer_item, parent, false)
        return ReposLoadStateViewHolder(view, retry)
    }

    override fun onBindViewHolder(holder: ReposLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class ReposLoadStateViewHolder(view: View, retry: () -> Unit) : RecyclerView.ViewHolder(view) {
        private val binding = ReposListStateFooterItemBinding.bind(view)
        private val context = view.context

        init {
            binding.footerItemRetryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            binding.footerItemProgress.isVisible = loadState is LoadState.Loading
            binding.footerItemRetryButton.isVisible = loadState is LoadState.Error
            binding.footerItemLoadStateText.isVisible = loadState is LoadState.Error
            if (loadState is LoadState.Error) {
                binding.footerItemLoadStateText.text = context.getString(R.string.fetch_data_error)
                        .plus(Constants.EMPTY_SPACE).plus(loadState.error.message)
            }
        }
    }
}