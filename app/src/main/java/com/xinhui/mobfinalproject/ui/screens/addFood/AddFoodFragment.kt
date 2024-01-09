package com.xinhui.mobfinalproject.ui.screens.addFood

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.data.model.Category
import com.xinhui.mobfinalproject.databinding.FragmentAddFoodBinding
import com.xinhui.mobfinalproject.ui.screens.addFood.viewModel.AddFoodViewModelImpl
import com.xinhui.mobfinalproject.ui.screens.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AddFoodFragment : BaseFragment<FragmentAddFoodBinding>() {
    override val viewModel: AddFoodViewModelImpl by viewModels()

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private val PERMISSION_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Glide.with(requireView())
                    .load(uri)
                    .placeholder(R.drawable.ic_image)
                    .into(binding.ivImage)
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
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

        binding.run {
            val cat = resources.getStringArray(R.array.category)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_items, cat)
            actvCategory.setAdapter(arrayAdapter)

            var selectedCategory: Category? = null

            actvCategory.setOnItemClickListener{ _, _, position, _ ->
                val selectedCategoryString = arrayAdapter.getItem(position).toString()
                selectedCategory = Category.values().find { it.categoryName == selectedCategoryString }
            }

            btnChoose.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                Log.d("debugging", "choose image here")
                Glide.with(requireView())
                    .load(it)
                    .placeholder(R.drawable.ic_image)
                    .into(binding.ivImage)
            }

            btnPhoto.setOnClickListener {
                openCamera()
                Log.d("debugging", "open camera here")
            }

            etDate.setOnClickListener {
                showDatePicker()
            }

            btnSave.setOnClickListener {
                val product = etProductName.text.toString()
                val storagePlace = etLocation.text.toString()
                val quantity = etQuantity.text.toString().toIntOrNull() ?: 0
                val unit = etUnits.text.toString()
                val expDate = etDate.text.toString()
                val image: Uri? = ivImage.tag as? Uri
                if (
                    product.isNotEmpty() &&
                    storagePlace.isNotEmpty() && quantity != 0 &&
                    unit.isNotEmpty() && expDate.isNotEmpty() && image != null)
                {
                    viewModel.addProduct(
                        product, storagePlace, quantity, unit, expDate, image, selectedCategory!!
                    )
                } else {
                    Snackbar.make(requireView(), "Please fill up all field", Snackbar.LENGTH_LONG).show()
                    Log.d("debugging", image.toString())
                }
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                binding.etDate.setText(String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear))
            },
            year, month, dayOfMonth
        )
        datePickerDialog.show()
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            // Now handle the result as needed
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uploadImageToFirebaseStorage(imageBitmap)
        } else {
            // If the user canceled the camera operation or an error occurred
            Log.d("debugging", "Camera operation canceled or failed")
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
            takePictureLauncher.launch(takePictureIntent)
        } else {
            Toast.makeText(requireContext(), "No camera app found", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera()
                } else{
                    //permission from popup was denied
                    Toast.makeText(requireContext(),"Permission Denied", Toast.LENGTH_LONG).show()

                }
            }
        }
    }

    private fun uploadImageToFirebaseStorage(imageBitmap: Bitmap) {
        // Create a unique filename (e.g., using timestamp)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val filename = "PHOTO_$timeStamp.jpg"

        // Reference to the Firebase Storage location where you want to store the image
        val storageReference = FirebaseStorage.getInstance().reference.child("images/$filename")

        // Convert the Bitmap to a ByteArray (JPEG format with quality 100)
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageData = baos.toByteArray()

        // Upload the ByteArray to Firebase Storage
        storageReference.putBytes(imageData)
            .addOnSuccessListener {
                // Image uploaded successfully
                Log.d("debugging", "Image uploaded to Firebase Storage")

                // TODO: Handle the result as needed (e.g., update UI, store image URL)
                // val downloadUrl = it.metadata?.reference?.downloadUrl.toString()
            }
            .addOnFailureListener {
                // Handle any errors that may occur during the upload
                Log.e("debugging", "Error uploading image to Firebase Storage: ${it.message}")
            }
    }


}