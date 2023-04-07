package com.example.github.detail.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.adapter.UserAdapter
import com.example.github.data.model.GithubResponse
import com.example.github.databinding.FragmentFollowBinding
import com.example.github.detail.DetailViewModel
import com.example.github.utils.AllResult

class FollowFragment : Fragment() {

    private var binding: FragmentFollowBinding? = null
    private val adapter = UserAdapter{

    }
    private val viewModel by activityViewModels<DetailViewModel>()
    var type = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvFollow?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowFragment.adapter
        }

        when(type) {
            FOLLOWERS -> {
                viewModel.resultFollowersUser.observe(viewLifecycleOwner, this::manageFollow)
            }
            FOLLOWING -> {
                viewModel.resultFollowingUser.observe(viewLifecycleOwner, this::manageFollow)
            }
        }

        viewModel.resultFollowingUser.observe(viewLifecycleOwner, this::manageFollow)
    }

    private fun manageFollow(state: AllResult) {
        when (state) {
            is AllResult.Success<*> -> {
                adapter.setData(state.data as MutableList<GithubResponse.ItemsItem>)
            }
            is AllResult.Error -> {
                Toast.makeText(requireActivity(), state.exception.message.toString(), Toast.LENGTH_SHORT).show()
            }
            is AllResult.Loading -> {
                binding?.progressBar?.isVisible = state.isLoading
            }
        }
    }

    companion object {
        const val FOLLOWERS = 100
        const val FOLLOWING = 101
        fun newInstance(type: Int) = FollowFragment()
            .apply {
                this.type = type
            }
    }
}