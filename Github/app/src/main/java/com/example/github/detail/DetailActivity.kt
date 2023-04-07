package com.example.github.detail

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.github.MainActivity
import com.example.github.R
import com.example.github.adapter.DetailAdapter
import com.example.github.data.model.DetailUserResponse
import com.example.github.data.model.GithubResponse
import com.example.github.data.model.local.FavoriteRepo
import com.example.github.data.model.local.SettingPreferences
import com.example.github.databinding.ActivityDetailBinding
import com.example.github.detail.follow.FollowFragment
import com.example.github.utils.AllResult
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>{
        DetailViewModel.Factory(FavoriteRepo(this))
    }
    private val settingPreferences by lazy {
        SettingPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val item = intent.getParcelableExtra<GithubResponse.ItemsItem>("item")
        val username = item?.login?: ""

        viewModel.resultDetailUser.observe(this) {
            when (it) {
                is AllResult.Success<*> -> {
                    val data = it.data as DetailUserResponse
                    binding.apply {
                        tvName.text = data.name
                        tvUsername.text = data.login
                        tvFollowers.text = data.followers.toString()
                        tvFollowing.text = data.following.toString()
                        Glide.with(this@DetailActivity)
                            .load(data.avatarUrl)
                            .into(imgAvatarProfile)
                    }
                }
                is AllResult.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is AllResult.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }

        viewModel.getDetailUsers(username)

        binding.btnFavorite.setOnClickListener {
            viewModel.setFavoriteUser(item)
        }

        viewModel.findFavorite(item?.id ?: 0) {
            binding.btnFavorite.changeIconColor(R.color.red)
        }

        viewModel.resultSuccessFavorite.observe(this) {
            binding.btnFavorite.changeIconColor(R.color.red)
        }

        viewModel.resultDeleteFavorite.observe(this) {
            binding.btnFavorite.changeIconColor(R.color.white)
        }

        val fragments = mutableListOf<Fragment>(
            FollowFragment.newInstance(FollowFragment.FOLLOWERS),
            FollowFragment.newInstance(FollowFragment.FOLLOWING)
        )
        val titles = mutableListOf(
            getString(R.string.followers),
            getString(R.string.following)
        )
        val adapter = DetailAdapter(this, fragments)
        binding.viewPager.adapter = adapter

        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if ( tab?.position == 0) {
                    viewModel.getFollowers(username)
                } else {
                    viewModel.getFollowing(username)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        TabLayoutMediator(binding.tab, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun FloatingActionButton.changeIconColor(@ColorRes color: Int) {
        imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
    }
}

