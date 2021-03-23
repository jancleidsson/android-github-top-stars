package com.jss.githubtopstars.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.jss.githubtopstars.R
import com.jss.githubtopstars.databinding.ActivityRepoListBinding
import com.jss.githubtopstars.framework.vm.RepoListViewModel

class ReposListActivity : AppCompatActivity() {

    private lateinit var viewModel: RepoListViewModel
    private lateinit var binding: ActivityRepoListBinding
    private val reposListAdapter = ReposListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_list)

        binding = ActivityRepoListBinding.inflate(layoutInflater)
        binding.repoListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reposListAdapter
        }

        setContentView(binding.root)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel = ViewModelProviders.of(this).get(RepoListViewModel::class.java)
        viewModel.getRepositories()
        viewModel.reposList.observe(this, { repositories ->
            repositories?.let {
                binding.repoListRecyclerView.visibility = View.VISIBLE
                reposListAdapter.updateRepos(repositories)
            }
        })

        viewModel.loading.observe(this, { loading ->
            binding.loading.visibility = if (loading) View.VISIBLE else View.GONE
            binding.repoListRecyclerView.visibility =  if (loading) View.GONE else View.VISIBLE
        })
    }
}