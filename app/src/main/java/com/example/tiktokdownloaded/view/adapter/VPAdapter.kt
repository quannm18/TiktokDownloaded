package com.example.tiktokdownloaded.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tiktokdownloaded.view.fragment.HomeFragment
import com.example.tiktokdownloaded.view.fragment.MyFileFragment
import com.example.tiktokdownloaded.view.fragment.TrendingFragment

class VPAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4;
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> HomeFragment()
            1-> MyFileFragment()
            2-> TrendingFragment()
            3 -> MyFileFragment()
            else -> HomeFragment()
        }
    }
}