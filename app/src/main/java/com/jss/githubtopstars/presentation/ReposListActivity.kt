package com.jss.githubtopstars.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.jss.githubtopstars.R
import com.jss.githubtopstars.databinding.ActivityRepoListBinding
import com.jss.githubtopstars.framework.networking.State
import com.jss.githubtopstars.framework.vm.RepoListViewModel

class ReposListActivity : AppCompatActivity() {

    private val viewMode by lazy {
        ViewModelProviders.of(this).get(RepoListViewModel::class.java)
    }
    private val binding by lazy {
        ActivityRepoListBinding.inflate(layoutInflater)
    }
    private val reposListAdapter by lazy {
        ReposPagedListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_list)
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
        viewMode.getRepoList().observe(this, {
            reposListAdapter.submitList( it )
        })

        viewMode.getState().observe(this, { state ->
            binding.loading.visibility = if (state == State.LOADING) View.VISIBLE else View.GONE
            binding.repoListRecyclerView.visibility = if (state == State.DONE || state == State.ERROR) View.VISIBLE else View.GONE
            if (state == State.ERROR) {
                Toast.makeText(this, getString(R.string.fetch_data_error), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
