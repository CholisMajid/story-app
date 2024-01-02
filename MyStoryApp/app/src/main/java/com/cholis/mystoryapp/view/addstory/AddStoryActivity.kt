package com.cholis.mystoryapp.view.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.cholis.mystoryapp.R
import com.cholis.mystoryapp.Result
import com.cholis.mystoryapp.databinding.ActivityAddStoryBinding
import com.cholis.mystoryapp.view.FactoryVM
import com.cholis.mystoryapp.view.liststory.ListStoryActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddStoryActivity : AppCompatActivity() {
    private var getFile: File? = null
    private var binding: ActivityAddStoryBinding? = null
    private val createStoryViewModel: AddStoryVM by viewModels {
        FactoryVM(this)
    }

    private var currentImageUri : Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, getString(R.string.succes_permission), Toast.LENGTH_LONG)
                    .show()
            } else {
                Toast.makeText(this, getString(R.string.error_permission), Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        binding?.btnCamera?.setOnClickListener {
            startCamera()
        }

        binding?.btnGallery?.setOnClickListener {
            startGallery()
        }

        binding?.btnAdd?.setOnClickListener {
            uploadImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding?.ivPreviewImage?.setImageURI(it)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun startCamera() {
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val myCameraDir = File(storageDir, "MyCamera")
        if (!myCameraDir.exists()) {
            myCameraDir.mkdirs()
        }

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFile = File(myCameraDir, "temp.jpg") // Simpan file gambar di dalam "MyCamera" directory
        getFile = imageFile // Simpan file gambar ke getFile
        currentImageUri = FileProvider.getUriForFile(
            this,
            "com.cholis.mystoryapp.fileprovider",
            imageFile
        )

        launcherIntentCamera.launch(currentImageUri)
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImg: Uri = result.data?.data as Uri
                selectedImg.let { uri ->
                    val myFile = uriToFile(uri, this)
                    getFile = myFile

                    // Menggunakan Glide untuk menampilkan gambar dengan mengatasi orientasinya
                    binding?.ivPreviewImage?.let {
                        Glide.with(this)
                            .load(uri)
                            .into(it)
                    }
                }
            }
        }

    private fun uploadImage() {
        val descriptionText = binding?.edDescription?.text

        if (getFile != null && !descriptionText.isNullOrEmpty()) {
            showLoading(true)

            val file = reduceFileImage(getFile as File)
            val description = descriptionText.toString().toRequestBody("text/plain".toMediaType())
            val requestImage = file.asRequestBody("image/*".toMediaType())
            val imageMultiPart: MultipartBody.Part =
                MultipartBody.Part.createFormData("photo", file.name, requestImage)

            createStoryViewModel.addStory(imageMultiPart, description)
                .observe(this) { it ->
                    when (it) {
                        is Result.Success -> {
                            showLoading(false)
                            Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, ListStoryActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        } else {
            showLoading(false)
            if (getFile == null) {
                Toast.makeText(this, "Gambar tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Deskripsi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding?.progressBarCreateStory?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}


