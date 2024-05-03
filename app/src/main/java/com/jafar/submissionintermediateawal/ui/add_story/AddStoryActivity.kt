package com.jafar.submissionintermediateawal.ui.add_story

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jafar.submissionintermediateawal.databinding.ActivityAddStoryBinding
import com.jafar.submissionintermediateawal.ui.ViewModelFactory
import com.jafar.submissionintermediateawal.ui.home.HomeActivity
import com.jafar.submissionintermediateawal.utils.image.getImageUri
import com.jafar.submissionintermediateawal.utils.image.reduceFileImage
import com.jafar.submissionintermediateawal.utils.image.uriToFile
import com.jafar.submissionintermediateawal.utils.result.Result
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null

    private val addStoryViewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    // LAUNCHER GALLERY
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            // Tampilkan gambar
            currentImageUri = uri
            showImage()
            binding.btnUpload.isEnabled = true
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    // LAUNCHER CAMERA
    private val laucherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
            binding.btnUpload.isEnabled = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (currentImageUri == null) binding.btnUpload.isEnabled = false

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }

        binding.btnUpload.setOnClickListener {
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()
                val description = binding.etDescription.text.toString()

                val requestBody = description.toRequestBody("text/plain".toMediaType())
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

                val multipartBody = MultipartBody.Part.createFormData(
                    "photo",
                    imageFile.name,
                    requestImageFile
                )
                addStoryViewModel.finalPostStory(requestBody, multipartBody)
                addStoryViewModel.postStoryState.observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                showLoading(false)
                                showAlertDialog("Yeay story mu berhasil diupload", true)
                            }
                            is Result.Error -> {
                                showLoading(false)
                                showAlertDialog(result.error, false)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivResult.setImageURI(it)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        currentImageUri?.let {
            laucherCamera.launch(it)
        }
    }

    private fun showAlertDialog(message: String, isSuccess: Boolean) {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("Upload Story")
        alertBuilder.setMessage(message)
        alertBuilder.setPositiveButton("OK") { dialog, _ ->
            if (isSuccess) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            else dialog.dismiss()
        }

        val dialog = alertBuilder.create()
        dialog.show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}