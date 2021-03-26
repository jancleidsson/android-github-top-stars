package com.jss.githubtopstars.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.jss.githubtopstars.R
import com.jss.githubtopstars.databinding.ActivityRepoListBinding
import com.jss.githubtopstars.framework.networking.State
import com.jss.githubtopstars.framework.vm.RepoListViewModel

class ReposListActivity : AppCompatActivity() {

    private lateinit var viewModel: RepoListViewModel
    private lateinit var binding: ActivityRepoListBinding
    private var reposListAdapter = ReposPagedListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_list)
        binding = ActivityRepoListBinding.inflate(layoutInflater)
        initAdapter()
        observeViewModel()
        setContentView(binding.root)
    }

    private fun initAdapter() {
        binding.repoListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reposListAdapter
        }
    }

    private fun observeViewModel() {
        viewModel = ViewModelProviders.of(this).get(RepoListViewModel::class.java)

        viewModel.getRepoList().observe(this, {
            reposListAdapter.submitList(it)
        })

        viewModel.getState().observe(this, { state ->
            binding.loading.visibility = if (state == State.LOADING) View.VISIBLE else View.GONE
            binding.repoListRecyclerView.visibility = if (state == State.DONE || state == State.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                reposListAdapter.setState(state ?: State.DONE)
            }
        })
    }
}
