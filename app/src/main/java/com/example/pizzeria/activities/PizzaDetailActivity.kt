package com.example.pizzeria.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.pizzeria.databinding.ActivityPizzaDetailBinding
import com.example.pizzeria.models.Pizza

class PizzaDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPizzaDetailBinding
    private var pizza: Pizza? = null
    private var position: Int = -1

    private val editLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val editedPizza = result.data?.getParcelableExtra<Pizza>("pizza")
            if (editedPizza != null) {
                val returnIntent = Intent().apply {
                    putExtra("editPizza", editedPizza)
                    putExtra("position", position)
                }
                setResult(Activity.RESULT_FIRST_USER, returnIntent)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPizzaDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pizza = intent.getParcelableExtra("pizza")
        position = intent.getIntExtra("position", -1)

        pizza?.let { pizza ->
            binding.tvName.text = pizza.name
            binding.tvDescription.text = pizza.description

            pizza.imageUri?.let {
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
            pizza?.let {
                val editIntent = Intent(this, AddPizzaActivity::class.java).apply {
                    putExtra("editPizza", it)
                }
                editLauncher.launch(editIntent)
            }
        }

        binding.btnDelete.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("position", position)
                putExtra("pizzaId", pizza?.id)
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