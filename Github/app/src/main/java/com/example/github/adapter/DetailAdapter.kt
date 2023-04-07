package com.example.github.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailAdapter(fragActivity : FragmentActivity, private val fragment: MutableList<Fragment>):
FragmentStateAdapter(fragActivity) {

    override fun getItemCount(): Int = fragment.size

    override fun createFragment(position: Int): Fragment = fragment[position]

}
