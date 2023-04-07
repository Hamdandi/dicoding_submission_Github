package com.example.github

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.adapter.UserAdapter
import com.example.github.data.model.GithubResponse
import com.example.github.data.model.local.SettingPreferences
import com.example.github.databinding.ActivityMainBinding
import com.example.github.detail.DetailActivity
import com.example.github.favorite.FavoriteActivity
import com.example.github.setting.SettingActivity
import com.example.github.utils.AllResult

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy {
        UserAdapter { user ->
            Intent(this, DetailActivity::class.java).apply {
                putExtra("item", user)
                startActivity(this)
            }
        }
    }

    private val viewModel by viewModels<MainViewModel> {
        MainViewModel.Factory(SettingPreferences(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getTheme().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = adapter

        binding.searchUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getSearchUser(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?) = false
        })

        viewModel.resultUser.observe(this) {
           when (it) {
               is AllResult.Success<*> -> {
                   adapter.setData(it.data as MutableList<GithubResponse.ItemsItem>)
               }
               is AllResult.Error -> {
                   Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
               }
               is AllResult.Loading -> {
                   binding.progressBar.isVisible = it.isLoading
               }
           }
        }

        viewModel.getUsers()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                Intent(this, FavoriteActivity::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.setting -> {
                Intent(this, SettingActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    fun startAnotherActivity() {
//        val intent = Intent(this, SettingActivity::class.java)
//        startActivityForResult(intent, 1001)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            val theme = data?.getBooleanExtra("theme", false) ?: false
            // Lakukan sesuatu dengan data yang diterima
        }
    }
}