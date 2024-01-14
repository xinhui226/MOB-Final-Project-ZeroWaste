package com.xinhui.mobfinalproject.ui.screens.addUpdateFood.baseAddUpdate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.core.utils.BitmapConverter
import com.xinhui.mobfinalproject.core.utils.Category
import com.xinhui.mobfinalproject.core.utils.ShowDialog
import com.xinhui.mobfinalproject.data.model.Product
import com.xinhui.mobfinalproject.databinding.FragmentBaseAddUpdateBinding
import com.xinhui.mobfinalproject.ui.screens.addUpdateFood.baseAddUpdate.viewModel.BaseAddUpdateViewModelImpl
import com.xinhui.mobfinalproject.ui.screens.base.BaseFragment
import kotlinx.coroutines.launch

abstract class BaseAddUpdateFragment : BaseFragment<FragmentBaseAddUpdateBinding>() {

    override val viewModel: BaseAddUpdateViewModelImpl by viewModels()
    protected lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    protected lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    protected var selectedCategory: Category? = null
    protected var productImgUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { setUriLoadGlide(it) }
        }
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                setUriLoadGlide(BitmapConverter.bitmapToFileToUri(imageBitmap, requireContext()))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseAddUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        setupCatAdapter()
        binding.run {
            ivBack.setOnClickListener {
                navController.popBackStack()
            }
            btnChoose.setOnClickListener {
                pickMedia.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
            btnPhoto.setOnClickListener {
                openCamera()
            }
            etDate.setOnClickListener {
                ShowDialog.showDatePicker(requireContext(), it as TextView)
            }
            btnSave.setOnClickListener {
                selectedCategory.let {cat ->
                    if (cat == null) showSnackbar("Please select the food category")
                    else {
                        val product = Product(
                            quantity = etQuantity.text.toString().toIntOrNull() ?: 0,
                            productName = etProductName.text.toString(),
                            storagePlace = etLocation.text.toString(),
                            unit = etUnits.text.toString(),
                            expiryDate = etDate.text.toString(),
                            category = cat
                        )
                        submit(product, productImgUri)
                    }
                }
            }
        }
    }


    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.success.collect {
                navController.popBackStack()
            }
        }
    }

    protected fun setUriLoadGlide(uri: Uri) {
        productImgUri = uri
        Glide.with(requireView())
            .load(productImgUri)
            .placeholder(R.drawable.ic_image)
            .into(binding.ivImage)
    }
    protected fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null)
            takePictureLauncher.launch(takePictureIntent)
        else
            Toast.makeText(requireContext(), "No camera app found", Toast.LENGTH_LONG).show()
    }

    protected fun setupCatAdapter() {
        val categories =  Category.values().filter { it != Category.all }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_items, categories.map { it.categoryName })
        binding.run {
            actvCategory.setAdapter(arrayAdapter)
            actvCategory.setOnItemClickListener{ _, _, position, _ ->
                selectedCategory = categories[position]
            }
        }
    }

    abstract fun submit(product: Product, uri: Uri?, context: Context = requireContext())

}