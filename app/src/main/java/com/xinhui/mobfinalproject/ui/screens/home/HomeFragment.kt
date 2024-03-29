package com.xinhui.mobfinalproject.ui.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.core.utils.Category
import com.xinhui.mobfinalproject.core.utils.ShowDialog
import com.xinhui.mobfinalproject.databinding.FragmentHomeBinding
import com.xinhui.mobfinalproject.ui.adapter.FoodItemAdapter
import com.xinhui.mobfinalproject.ui.adapter.HorizontalCategoryAdapter
import com.xinhui.mobfinalproject.ui.screens.base.BaseFragment
import com.xinhui.mobfinalproject.ui.screens.home.viewModel.HomeViewModelImpl
import com.xinhui.mobfinalproject.ui.screens.profile.viewModel.ProfileViewModelImpl
import com.xinhui.mobfinalproject.ui.screens.tabContainer.TabContainerFragmentDirections
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
        profileVM.getCurrUser()
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        setupHorizontalAdapter()
        setupFoodAdapter()
        binding.run {
            ivImage.setOnClickListener {
                val action = TabContainerFragmentDirections.actionTabContainerToAddFood()
                navController.navigate(action)
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.products.collect {
                foodItemAdapter.showItems(it)
                binding.tvNoData.isVisible = it.isEmpty()
            }
        }

        lifecycleScope.launch {
            profileVM.user.collect {
                binding.tvName.text = getString(R.string.name_of_user, it.name)
            }
        }

        lifecycleScope.launch {
            profileVM.loggedOut.collect {
                viewModel.stopJob()
            }
        }
    }

    private fun setupHorizontalAdapter() {
        horizontalAdapter = HorizontalCategoryAdapter(viewModel.selectedCat.value.categoryName) {
            if (it == Category.all) viewModel.getProducts()
            else viewModel.getProducts(it.categoryName)
        }

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHorizontalCategories.adapter = horizontalAdapter
        binding.rvHorizontalCategories.layoutManager = layoutManager

    }

    private fun setupFoodAdapter() {
        foodItemAdapter = FoodItemAdapter(emptyList())

        foodItemAdapter.listener = object : FoodItemAdapter.Listener {
            override fun onDelete(id: String) {
                ShowDialog.showDltConfirmationDialog(requireContext(),layoutInflater){
                    viewModel.deleteProduct(id, requireContext())
                }
            }
            override fun onEdit(id: String) {
                navController.navigate(
                    TabContainerFragmentDirections.actionTabContainerToUpdFood(id)
                )
            }
        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvHome.adapter = foodItemAdapter
        binding.rvHome.layoutManager = layoutManager

    }
}