package com.jss.githubtopstars.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.jss.githubtopstars.R
import com.jss.githubtopstars.databinding.ActivityRepoListBinding
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
        ActivityRepoListBinding.inflate(layoutInflater)
    }
    private val reposListAdapter by lazy {
        ReposPagingDataAdapter()
    }

    private var fetchApiJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_list)
        initAdapter()
        getRepos()
        setContentView(binding.root)
    }

    private fun initAdapter() {
        binding.repoListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reposListAdapter
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
