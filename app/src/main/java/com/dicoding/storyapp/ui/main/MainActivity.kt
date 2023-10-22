package com.dicoding.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.ui.add.AddStoryActivity
import com.dicoding.storyapp.ui.detail.DetailActivity
import com.dicoding.storyapp.ui.welcome.WelcomeActivity
import com.dicoding.storyapp.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var factory: ViewModelFactory
    private lateinit var storyAdapter: ListStoryAdapter
    private var token = ""
    private val mainViewModel: MainViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupViewModel()
        setupAdapter()
        setupAction()
        setupUser()
    }

    private fun setupAction() {
        binding.buttonAdd.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun setupUser() {
        showLoading()
        mainViewModel.getSession().observe(this@MainActivity) {
            token = it.token
            if (!it.isLogin) {
                moveActivity()
            } else {
                setupData()
            }
        }
        showToast()
    }

    private fun setupData() {
        mainViewModel.getListStories.observe(this@MainActivity) { pagingData ->
            storyAdapter.submitData(lifecycle, pagingData)
        }
    }

    private fun setupAdapter() {
        storyAdapter = ListStoryAdapter { storyItem ->
            val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_DATA, storyItem)
            }
            startActivity(intent)
        }

        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun setupView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun showLoading() {
        mainViewModel.isLoading.observe(this@MainActivity) {
            binding.pbHome.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun showToast() {
        mainViewModel.toastText.observe(this@MainActivity) {
            it.getContentIfNotHandled()?.let { toastText ->
                val show = Toast.makeText(
                    this@MainActivity, toastText, Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    private fun moveActivity() {
        startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }

            R.id.home_logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.sign_out)
                    .setMessage(R.string.confirm_sign_out)
                    .setPositiveButton(R.string.yes) { _, _ ->
                        mainViewModel.logout()
                    }
                    .setNegativeButton(R.string.no) { _, _ ->
                        Toast.makeText(this, R.string.logout_canceled, Toast.LENGTH_SHORT).show()
                    }
                    .show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}





