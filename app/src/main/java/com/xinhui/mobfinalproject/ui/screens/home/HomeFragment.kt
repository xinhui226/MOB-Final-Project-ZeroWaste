package com.xinhui.mobfinalproject.ui.screens.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.data.model.Category
import com.xinhui.mobfinalproject.databinding.FragmentHomeBinding
import com.xinhui.mobfinalproject.ui.adapter.FoodItemAdapter
import com.xinhui.mobfinalproject.ui.adapter.HorizontalCategoryAdapter
import com.xinhui.mobfinalproject.ui.screens.base.BaseFragment
import com.xinhui.mobfinalproject.ui.screens.home.viewModel.HomeViewModelImpl
import com.xinhui.mobfinalproject.ui.screens.profile.viewModel.ProfileViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val viewModel: HomeViewModelImpl by viewModels()
    val profileVM : ProfileViewModelImpl by activityViewModels()

    private lateinit var horizontalAdapter: HorizontalCategoryAdapter
    private lateinit var foodItemAdapter: FoodItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        setupHorizontalAdapter()
        setupFoodAdapter()
        binding.run {
            ivImage.setOnClickListener {
                //TODO: add the action to Add Product
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.products.collect {
                foodItemAdapter.showItems(it)
            }
        }

        lifecycleScope.launch {
            profileVM.user.collect {
                Log.d("debugging", "setupViewModelObserver: ${it.name}")
                binding.tvName.text = getString(R.string.name_of_user, it.name)
            }
        }
    }

    private fun setupHorizontalAdapter() {
        horizontalAdapter = HorizontalCategoryAdapter {
            if (it == Category.all) viewModel.getProducts()
            else viewModel.getProducts(it.categoryName)
        }

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHorizontalCategories.adapter = horizontalAdapter
        binding.rvHorizontalCategories.layoutManager = layoutManager

    }

    private fun setupFoodAdapter() {
        foodItemAdapter = FoodItemAdapter(emptyList())

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvHome.adapter = foodItemAdapter
        binding.rvHome.layoutManager = layoutManager

    }
}