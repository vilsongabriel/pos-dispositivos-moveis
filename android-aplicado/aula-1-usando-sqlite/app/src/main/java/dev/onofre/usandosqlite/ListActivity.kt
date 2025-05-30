package dev.onofre.usandosqlite

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.onofre.usandosqlite.adapter.RegistrationAdapter
import dev.onofre.usandosqlite.database.DatabaseHandler
import dev.onofre.usandosqlite.databinding.ActivityListBinding

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private lateinit var db: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()

        db = DatabaseHandler(this)
        val registrations = db.list()

        val adapter = RegistrationAdapter(this, registrations)
        binding.lvRegistrations.adapter = adapter
    }

    private fun setupListeners () {
        binding.fabAdd.setOnClickListener {
            handleAddClick()
        }
    }

    private fun handleAddClick () {
        startActivity(Intent(this, RegistrationActivity::class.java))
    }
}