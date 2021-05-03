package com.jss.githubtopstars.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.jss.githubtopstars.R
import com.jss.githubtopstars.databinding.ReposListActivityBinding
import com.jss.githubtopstars.framework.di.ApplicationModule
import com.jss.githubtopstars.framework.di.DaggerApplicationComponent
import com.jss.githubtopstars.framework.vm.RepoListViewModel
import com.jss.githubtopstars.utils.IdlingResourcesHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
class RepoListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewMode: RepoListViewModel

    private val binding by lazy {
        ReposListActivityBinding.inflate(layoutInflater)
    }
    private val reposListAdapter by lazy {
        RepoPagingDataAdapter()
    }

    private var fetchApiJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.repos_list_activity)
        setContentView(binding.root)
        buildDagger()
        initAdapter()
        getRepos()
        binding.retryButton.setOnClickListener { reposListAdapter.retry() }
    }

    private fun buildDagger() {
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(application))
                .build()
                .inject(this)
    }

    private fun initAdapter() {
        binding.repoListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reposListAdapter.withLoadStateHeaderAndFooter(
                    RepoLoadStateAdapter { reposListAdapter.retry() },
                    RepoLoadStateAdapter { reposListAdapter.retry() }
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
                IdlingResourcesHelper.decrementCounting()
            }
        }
    }

    private fun getRepos() {
        fetchApiJob?.let {
            it.cancel()
            IdlingResourcesHelper.decrementCounting()
        }

        IdlingResourcesHelper.incrementCounting()
        fetchApiJob = lifecycleScope.launch {
            viewMode.getReposList().collectLatest { pagingDataSource ->
                reposListAdapter.submitData(pagingDataSource)
            }
        }

        fetchApiJob!!.invokeOnCompletion {
            IdlingResourcesHelper.decrementCounting()
        }
    }
}
