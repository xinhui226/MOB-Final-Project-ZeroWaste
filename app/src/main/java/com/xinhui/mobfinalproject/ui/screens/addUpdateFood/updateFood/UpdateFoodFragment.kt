package com.xinhui.mobfinalproject.ui.screens.addUpdateFood.updateFood

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.core.utils.ShowDialog
import com.xinhui.mobfinalproject.data.model.Product
import com.xinhui.mobfinalproject.ui.screens.addUpdateFood.baseAddUpdate.BaseAddUpdateFragment
import com.xinhui.mobfinalproject.ui.screens.addUpdateFood.updateFood.viewModel.UpdateFoodViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UpdateFoodFragment : BaseAddUpdateFragment() {

    override val viewModel: UpdateFoodViewModelImpl by viewModels()

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        binding.run {
            tvTitle.text = getString(R.string.edit_item)
            etDate.setOnClickListener {
                ShowDialog.showDatePicker(requireContext(), it as TextView, viewModel.product.value.expiryDate)
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.product.collect {
                binding.run {
                    etProductName.setText(it.productName)
                    selectedCategory = it.category
                    actvCategory.setText(it.category.categoryName)
                    setupCatAdapter()
                    etLocation.setText(it.storagePlace)
                    etQuantity.setText(it.quantity.toString())
                    etUnits.setText(it.unit)
                    etDate.text = it.expiryDate
                    it.productUrl?.let { url ->
                        Glide.with(requireView())
                            .load(url)
                            .placeholder(R.drawable.ic_image)
                            .into(binding.ivImage)
                    }
                }
            }
        }
    }

    override fun submit(product: Product, uri: Uri?, context: Context) {
        viewModel.updateFood(product, uri, context)
    }

}