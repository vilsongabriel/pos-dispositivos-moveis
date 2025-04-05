package dev.onofre.usandosqlite

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.onofre.usandosqlite.database.DatabaseHandler
import dev.onofre.usandosqlite.databinding.ActivityMainBinding
import dev.onofre.usandosqlite.entity.Registration

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)
        setButtonListeners()
        db = DatabaseHandler(this)
    }

    private fun setButtonListeners() {
        binding.btCreate.setOnClickListener {
            handleCreateClick()
        }
        binding.btUpdate.setOnClickListener {
            handleUpdateClick()
        }
        binding.btDelete.setOnClickListener {
            handleDeleteClick()
        }
        binding.btSearch.setOnClickListener {
            handleSearchClick()
        }
        binding.btList.setOnClickListener {
            handleListClick()
        }
    }

    private fun clearFields() {
        binding.etCode.setText("")
        binding.etName.setText("")
        binding.etPhone.setText("")
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun handleCreateClick() {
        val name = binding.etName.text.toString()
        val phone = binding.etPhone.text.toString()
        if (name.isEmpty() || phone.isEmpty()) {
            return showToast(getString(R.string.error_name_phone))
        }
        db.insert(Registration(0, name, phone))
        clearFields()
        showToast(getString(R.string.message_insert_success))
    }

    private fun handleUpdateClick() {
        val code = binding.etCode.text.toString()
        val name = binding.etName.text.toString()
        val phone = binding.etPhone.text.toString()
        if (code.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            return showToast(getString(R.string.error_empty_fields))
        }
        val updated = db.update(Registration(code.toInt(), name, phone))
        val message: String = if (updated > 0)
            getString(R.string.message_update_success)
        else
            getString(R.string.message_not_found)
        showToast(message)
    }

    private fun handleDeleteClick() {
        val code = binding.etCode.text.toString()
        if (code.isEmpty()) {
            return showToast(getString(R.string.error_code))
        }
        val deleted = db.delete(code.toInt())
        clearFields()
        val message: String = if (deleted > 0)
            getString(R.string.message_delete_success)
        else
            getString(R.string.message_not_found)
        showToast(message)
    }

    private fun handleSearchClick() {
        val code = binding.etCode.text.toString()
        if (code.isEmpty()) {
            return showToast(getString(R.string.error_code))
        }
        val registration = db.find(code.toInt())
        if (registration != null) {
            binding.etName.setText(registration.name)
            binding.etPhone.setText(registration.phone)
        } else {
            clearFields()
            showToast(getString(R.string.message_not_found))
        }
    }

    private fun handleListClick() {
        val resultIntent = Intent(this, ListActivity::class.java)
        startActivity(resultIntent)
    }
}