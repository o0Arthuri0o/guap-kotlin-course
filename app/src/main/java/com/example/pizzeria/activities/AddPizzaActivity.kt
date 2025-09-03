package com.example.pizzeria.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.pizzeria.databinding.ActivityAddPizzaBinding
import com.example.pizzeria.models.Pizza

class AddPizzaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPizzaBinding
    private var selectedImageUri: Uri? = null
    private var editPizza: Pizza? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            try {
                contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } catch (_: SecurityException) {
            }
            binding.ivPizzaPreview.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPizzaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editPizza = intent.getParcelableExtra("editPizza")

        if (editPizza != null) {
            binding.tvFormTitle.text = "Редактирование пиццы"
            binding.btnAddPizza.text = "Сохранить изменения"
            binding.etPizzaName.setText(editPizza!!.name)
            binding.etPizzaDescription.setText(editPizza!!.description)

            editPizza!!.imageUri?.let {
                selectedImageUri = Uri.parse(it)
                binding.ivPizzaPreview.setImageURI(selectedImageUri)
            }
        } else {
            binding.ivPizzaPreview.setImageDrawable(null)
        }

        binding.btnPickImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnAddPizza.setOnClickListener {
            val name = binding.etPizzaName.text.toString().trim()
            val desc = binding.etPizzaDescription.text.toString().trim()

            if (name.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val pizza = Pizza(
                name = name,
                description = desc,
                imageUri = selectedImageUri?.toString()
            )

            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra("pizza", pizza)
            })
            finish()
        }
    }
}