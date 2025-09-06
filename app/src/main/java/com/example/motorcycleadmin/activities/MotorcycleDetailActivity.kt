package com.example.motorcycleadmin.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.motorcycleadmin.databinding.ActivityMotorcycleDetailBinding
import com.example.motorcycleadmin.models.Motorcycle

class MotorcycleDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMotorcycleDetailBinding
    private var motorcycle: Motorcycle? = null
    private var position: Int = -1

    private val editLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val editedMotorcycle = result.data?.getParcelableExtra<Motorcycle>("motorcycle")
            if (editedMotorcycle != null) {
                val returnIntent = Intent().apply {
                    putExtra("editMotorcycle", editedMotorcycle)
                    putExtra("position", position)
                }
                setResult(Activity.RESULT_FIRST_USER, returnIntent)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMotorcycleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        motorcycle = intent.getParcelableExtra("motorcycle")
        position = intent.getIntExtra("position", -1)

        motorcycle?.let { motorcycle ->
            binding.tvName.text = motorcycle.name
            binding.tvDescription.text = motorcycle.description

            motorcycle.imageUri?.let {
                val uri = Uri.parse(it)
                try {
                    binding.imageView.setImageURI(uri)
                } catch (e: SecurityException) {
                    e.printStackTrace()
                    binding.imageView.setImageResource(android.R.drawable.ic_menu_report_image)
                }
            } ?: run {
                binding.imageView.setImageResource(android.R.drawable.ic_menu_report_image)
            }
        }

        binding.btnEdit.setOnClickListener {
            motorcycle?.let {
                val editIntent = Intent(this, AddMotorcycleActivity::class.java).apply {
                    putExtra("editMotorcycle", it)
                }
                editLauncher.launch(editIntent)
            }
        }

        binding.btnDelete.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("position", position)
                putExtra("motorcycleId", motorcycle?.id)
            }
            setResult(Activity.RESULT_FIRST_USER + 1, resultIntent)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
