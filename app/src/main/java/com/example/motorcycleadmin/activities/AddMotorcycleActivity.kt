package com.example.motorcycleadmin.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.motorcycleadmin.databinding.ActivityAddMotorcycleBinding
import com.example.motorcycleadmin.models.Motorcycle

class AddMotorcycleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMotorcycleBinding
    private var selectedImageUri: Uri? = null
    private var editMotorcycle: Motorcycle? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            try {
                contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } catch (_: SecurityException) {
            }
            binding.ivMotorcyclePreview.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMotorcycleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editMotorcycle = intent.getParcelableExtra("editMotorcycle")

        if (editMotorcycle != null) {
            binding.tvFormTitle.text = "Редактирование мотоцикла"
            binding.btnAddMotorcycle.text = "Сохранить изменения"
            binding.etMotorcycleName.setText(editMotorcycle!!.name)
            binding.etMotorcycleDescription.setText(editMotorcycle!!.description)

            editMotorcycle!!.imageUri?.let {
                selectedImageUri = Uri.parse(it)
                binding.ivMotorcyclePreview.setImageURI(selectedImageUri)
            }
        } else {
            binding.ivMotorcyclePreview.setImageDrawable(null)
        }

        binding.btnPickImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnAddMotorcycle.setOnClickListener {
            val name = binding.etMotorcycleName.text.toString().trim()
            val desc = binding.etMotorcycleDescription.text.toString().trim()

            if (name.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val motorcycle = Motorcycle(
                name = name,
                description = desc,
                imageUri = selectedImageUri?.toString()
            )

            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra("motorcycle", motorcycle)
            })
            finish()
        }
    }
}
