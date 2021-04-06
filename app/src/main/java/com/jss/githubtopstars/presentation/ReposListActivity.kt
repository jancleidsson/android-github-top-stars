package com.jss.githubtopstars.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.jss.githubtopstars.R
import com.jss.githubtopstars.databinding.ReposListActivityBinding
import com.jss.githubtopstars.framework.vm.RepoListViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class ReposListActivity : AppCompatActivity() {

    private val viewMode by lazy {
        ViewModelProviders.of(this).get(RepoListViewModel::class.java)
    }
    private val binding by lazy {
        ReposListActivityBinding.inflate(layoutInflater)
    }
    private val reposListAdapter by lazy {
        ReposPagingDataAdapter()
    }

    private var fetchApiJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.repos_list_activity)
        setContentView(binding.root)

        initAdapter()
        getRepos()
        binding.retryButton.setOnClickListener { reposListAdapter.retry() }
    }

    private fun initAdapter() {
        binding.repoListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reposListAdapter.withLoadStateHeaderAndFooter(
                    ReposLoadStateAdapter { reposListAdapter.retry() },
                    ReposLoadStateAdapter { reposListAdapter.retry() }
            )
        }

        reposListAdapter.addLoadStateListener { loadState ->
            binding.repoListRecyclerView.isVisible = loadState.mediator?.refresh is LoadState.NotLoading
            binding.loading.isVisible = loadState.mediator?.refresh is LoadState.Loading
            binding.retryButton.isVisible = loadState.mediator?.refresh is LoadState.Error
            val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(this, getString(R.string.fetch_data_error), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getRepos() {
        fetchApiJob?.cancel()
        fetchApiJob = lifecycleScope.launch {
            viewMode.getReposList().collectLatest { pagingDataSource ->
                reposListAdapter.submitData(pagingDataSource)
            }
        }
    }
}
