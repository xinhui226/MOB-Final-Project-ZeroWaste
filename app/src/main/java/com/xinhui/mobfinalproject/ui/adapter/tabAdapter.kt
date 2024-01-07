package com.xinhui.mobfinalproject.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class tabAdapter(
    fragment: Fragment,
    private val tabs: List<Fragment>
): FragmentStateAdapter(fragment) {
    override fun getItemCount() = tabs.size

    override fun createFragment(position: Int): Fragment {
        return tabs[position]
    }

}