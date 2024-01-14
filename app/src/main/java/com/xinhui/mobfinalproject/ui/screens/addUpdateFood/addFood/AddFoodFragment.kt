package com.xinhui.mobfinalproject.ui.screens.addUpdateFood.addFood

import android.content.Context
import android.net.Uri
import androidx.fragment.app.viewModels
import com.xinhui.mobfinalproject.data.model.Product
import com.xinhui.mobfinalproject.ui.screens.addUpdateFood.addFood.viewModel.AddFoodViewModelImpl
import com.xinhui.mobfinalproject.ui.screens.addUpdateFood.baseAddUpdate.BaseAddUpdateFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFoodFragment : BaseAddUpdateFragment() {

    override val viewModel: AddFoodViewModelImpl by viewModels()

    override fun submit(product: Product, uri: Uri?, context: Context) {
        viewModel.addProduct(product, uri, context)
    }
}