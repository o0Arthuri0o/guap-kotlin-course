package com.example.motorcycleadmin.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.motorcycleadmin.adapters.MotorcycleAdapter
import com.example.motorcycleadmin.databinding.ActivityMainBinding
import com.example.motorcycleadmin.db.DatabaseHelper
import com.example.motorcycleadmin.models.Motorcycle

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MotorcycleAdapter
    private lateinit var databaseHelper: DatabaseHelper

    private val addMotorcycleLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val newMotorcycle = result.data?.getParcelableExtra<Motorcycle>("motorcycle")
            newMotorcycle?.let {
                val id = databaseHelper.insertMotorcycle(it)
                val motorcycleWithId = it.copy(id = id)
                adapter.addMotorcycle(motorcycleWithId)
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
                val editedMotorcycle = data.getParcelableExtra<Motorcycle>("editMotorcycle")
                if (position >= 0 && editedMotorcycle != null) {
                    databaseHelper.updateMotorcycle(editedMotorcycle, editedMotorcycle.id)
                    adapter.updateMotorcycle(editedMotorcycle, position)
                }
            }
            Activity.RESULT_FIRST_USER + 1 -> {
                val motorcycleId = data.getLongExtra("motorcycleId", -1)
                if (position >= 0 && motorcycleId != -1L) {
                    databaseHelper.deleteMotorcycle(motorcycleId)
                    adapter.removeMotorcycle(position)
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
        val motorcycleList = databaseHelper.getAllMotorcycles()

        adapter = MotorcycleAdapter(motorcycleList.toMutableList()) { motorcycle, position ->
            val intent = Intent(this, MotorcycleDetailActivity::class.java).apply {
                putExtra("motorcycle", motorcycle)
                putExtra("position", position)
            }
            detailLauncher.launch(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddMotorcycleActivity::class.java)
            addMotorcycleLauncher.launch(intent)
        }
    }
}
