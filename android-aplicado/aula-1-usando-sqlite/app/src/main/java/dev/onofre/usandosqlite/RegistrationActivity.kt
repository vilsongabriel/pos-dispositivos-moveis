package dev.onofre.usandosqlite

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import dev.onofre.usandosqlite.database.DatabaseHandler
import dev.onofre.usandosqlite.databinding.ActivityRegistrationBinding
import dev.onofre.usandosqlite.entity.Registration

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var db: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.main)
        initData()
        setupListeners()
        db = DatabaseHandler(this)
    }

    private fun initData () {
        val code = intent.getIntExtra("code", 0)
        if (code == 0) {
            binding.btDelete.visibility = View.GONE
            binding.btSearch.visibility = View.GONE
        } else {
            binding.etCode.setText(code.toString())
            binding.etName.setText(intent.getStringExtra("name"))
            binding.etPhone.setText(intent.getStringExtra("phone"))
        }
    }

    private fun setupListeners() {
        binding.btSave.setOnClickListener {
            handleSaveClick()
        }
        binding.btDelete.setOnClickListener {
            handleDeleteClick()
        }
        binding.btSearch.setOnClickListener {
            handleSearchClick()
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

    private fun goToList() {
        val resultIntent = Intent(this, ListActivity::class.java)
        startActivity(resultIntent)
    }

    private fun handleSaveClick() {
        val code = binding.etCode.text.toString()
        val name = binding.etName.text.toString()
        val phone = binding.etPhone.text.toString()
        if (name.isEmpty() || phone.isEmpty()) {
            return showToast(getString(R.string.error_name_phone))
        }
        var message: String
        if (code == "") {
            db.insert(Registration(0, name, phone))
            message = getString(R.string.message_insert_success)
        } else {
            db.update(Registration(code.toInt(), name, phone))
            message = getString(R.string.message_update_success)
        }
        showToast(message)
        goToList()
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
        goToList()
    }

    private fun handleSearchClick() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.modal_search_title))
        val input = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        builder.apply {
            setCancelable(false)
            setView(input)
            setNegativeButton (getString(R.string.modal_search_close), null )
            setPositiveButton (getString(R.string.modal_search_confirm), { _, _ ->
                val code = input.text.toString()
                if (code.isEmpty()) {
                    showToast(getString(R.string.error_code))
                    return@setPositiveButton
                }
                val registration = db.find(code.toInt())
                if (registration != null) {
                    binding.etCode.setText(code)
                    binding.etName.setText(registration.name)
                    binding.etPhone.setText(registration.phone)
                } else {
                    clearFields()
                    showToast(getString(R.string.message_not_found))
                }
            })
            show()
        }
    }
}