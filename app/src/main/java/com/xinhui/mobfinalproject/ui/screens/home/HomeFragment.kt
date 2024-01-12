package com.xinhui.mobfinalproject.ui.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.data.model.Category
import com.xinhui.mobfinalproject.databinding.FragmentHomeBinding
import com.xinhui.mobfinalproject.ui.adapter.FoodItemAdapter
import com.xinhui.mobfinalproject.ui.adapter.horizontalCategoryAdapter
import com.xinhui.mobfinalproject.ui.screens.base.BaseFragment
import com.xinhui.mobfinalproject.ui.screens.home.viewModel.HomeViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val viewModel: HomeViewModelImpl by viewModels()

    private lateinit var horizontalAdapter: horizontalCategoryAdapter
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
            viewModel.user.collect {
                binding.tvName.text = getString(R.string.name_of_user, it.name)
            }
        }
    }

    private fun setupHorizontalAdapter() {
        val category = Category.values()
        horizontalAdapter = horizontalCategoryAdapter(category) {
            viewModel.getProductsByCategory(it.categoryName)
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