package com.xinhui.mobfinalproject.ui.screens.tabContainer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.databinding.FragmentTabContainerBinding
import com.xinhui.mobfinalproject.ui.adapter.tabAdapter
import com.xinhui.mobfinalproject.ui.screens.home.HomeFragment
import com.xinhui.mobfinalproject.ui.screens.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabContainerFragment : Fragment() {

    private lateinit var binding: FragmentTabContainerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            vpContainer.adapter = tabAdapter(
                this@TabContainerFragment,
                listOf(HomeFragment(), ProfileFragment())
            )

            vpContainer.isUserInputEnabled = false

            TabLayoutMediator(tlTabs, vpContainer) {tab, position ->
                val customTab = layoutInflater.inflate(R.layout.custom_tab_layout, null) as LinearLayout

                val tabText = customTab.findViewById<TextView>(R.id.tabText)
                val tabIcon = customTab.findViewById<ImageView>(R.id.tabIcon)

                when (position) {
                    0 -> {
                        tabIcon.setImageResource(R.drawable.ic_home)
                        tabText.text = ContextCompat.getString(requireContext(), R.string.home)
                    }
                    else -> {
                        tabIcon.setImageResource(R.drawable.ic_person)
                        tabText.text = ContextCompat.getString(requireContext(), R.string.profile)
                    }
                }
                tab.customView = customTab
            }.attach()
        }
    }
}