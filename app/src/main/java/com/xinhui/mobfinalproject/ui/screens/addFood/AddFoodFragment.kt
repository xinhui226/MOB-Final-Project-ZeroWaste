package com.xinhui.mobfinalproject.ui.screens.addFood

import android.app.Activity
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
import com.bumptech.glide.Glide
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.core.utils.AlertDialog
import com.xinhui.mobfinalproject.core.utils.BitmapConverter
import com.xinhui.mobfinalproject.data.model.Category
import com.xinhui.mobfinalproject.data.model.Product
import com.xinhui.mobfinalproject.databinding.FragmentAddFoodBinding
import com.xinhui.mobfinalproject.ui.screens.addFood.viewModel.AddFoodViewModelImpl
import com.xinhui.mobfinalproject.ui.screens.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFoodFragment : BaseFragment<FragmentAddFoodBinding>() {

    override val viewModel: AddFoodViewModelImpl by viewModels()
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private var selectedCategory: Category? = null
    private var productImgUri: Uri? = null

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
        binding = FragmentAddFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        setupCatAdapter()
        binding.run {
            btnChoose.setOnClickListener {
                pickMedia.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
            btnPhoto.setOnClickListener {
                openCamera()
            }
            etDate.setOnClickListener {
                AlertDialog.showDatePicker(requireContext(), it as TextView)
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
                        viewModel.addProduct(product, productImgUri)
                    }
                }
            }
        }
    }

    private fun setupCatAdapter() {
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_items, Category.values().map { it.categoryName })
        binding.run {
            actvCategory.setAdapter(arrayAdapter)
            actvCategory.setOnItemClickListener{ _, _, position, _ ->
                selectedCategory = Category.values()[position]
            }
        }
    }
    private fun setUriLoadGlide(uri: Uri) {
        productImgUri = uri
        // TODO: photo taken is blur and small
        Glide.with(requireView())
            .load(productImgUri)
            .placeholder(R.drawable.ic_image)
            .into(binding.ivImage)
    }
    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null)
            takePictureLauncher.launch(takePictureIntent)
        else
            Toast.makeText(requireContext(), "No camera app found", Toast.LENGTH_LONG).show()
    }
//    TODO: has been deprecated
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        when(requestCode){
//            PERMISSION_CODE -> {
//                if (grantResults.isNotEmpty() && grantResults[0] ==
//                    PackageManager.PERMISSION_GRANTED){
//                    openCamera()
//                } else{
//                    Toast.makeText(requireContext(),"Permission Denied", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }
}