package com.example.pizzeria.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzeria.adapters.PizzaAdapter
import com.example.pizzeria.databinding.ActivityMainBinding
import com.example.pizzeria.db.DatabaseHelper
import com.example.pizzeria.models.Pizza

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PizzaAdapter
    private lateinit var databaseHelper: DatabaseHelper

    private val addPizzaLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val newPizza = result.data?.getParcelableExtra<Pizza>("pizza")
            newPizza?.let {
                val id = databaseHelper.insertPizza(it)
                val pizzaWithId = it.copy(id = id)
                adapter.addPizza(pizzaWithId)
            }
        }
    }

    private val detailLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data ?: return@registerForActivityResult
        val position = data.getIntExtra("position", -1)

        when (result.resultCode) {
            Activity.RESULT_FIRST_USER -> {
                val editedPizza = data.getParcelableExtra<Pizza>("editPizza")
                if (position >= 0 && editedPizza != null) {
                    databaseHelper.updatePizza(editedPizza, editedPizza.id)
                    adapter.updatePizza(editedPizza, position)
                }
            }
            Activity.RESULT_FIRST_USER + 1 -> {
                val pizzaId = data.getLongExtra("pizzaId", -1)
                if (position >= 0 && pizzaId != -1L) {
                    databaseHelper.deletePizza(pizzaId)
                    adapter.removePizza(position)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        databaseHelper = DatabaseHelper(this)
        val pizzaList = databaseHelper.getAllPizzas()

        adapter = PizzaAdapter(pizzaList.toMutableList()) { pizza, position ->
            val intent = Intent(this, PizzaDetailActivity::class.java).apply {
                putExtra("pizza", pizza)
                putExtra("position", position)
            }
            detailLauncher.launch(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddPizzaActivity::class.java)
            addPizzaLauncher.launch(intent)
        }
    }
}