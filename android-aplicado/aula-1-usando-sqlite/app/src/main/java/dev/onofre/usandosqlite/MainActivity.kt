package dev.onofre.usandosqlite

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import dev.onofre.usandosqlite.database.DatabaseHandler
import dev.onofre.usandosqlite.databinding.ActivityMainBinding
import dev.onofre.usandosqlite.entity.Cadastro

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
        binding.btIncluir.setOnClickListener {
            handleIncluirClick()
        }
        binding.btAlterar.setOnClickListener {
            handleAlterarClick()
        }
        binding.btExcluir.setOnClickListener {
            handleExcluirClick()
        }
        binding.btPesquisar.setOnClickListener {
            handlePesquisarClick()
        }
        binding.btListar.setOnClickListener {
            handleListarClick()
        }
    }

    private fun clearFields() {
        binding.etCod.setText("")
        binding.etNome.setText("")
        binding.etTelefone.setText("")
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun handleIncluirClick() {
        val nome = binding.etNome.text.toString()
        val telefone = binding.etTelefone.text.toString()
        if (nome == "" || telefone == "") {
            return showToast(getString(R.string.error_name_phone))
        }
        db.insert(Cadastro (
            0,
            nome,
            telefone,
        ))
        clearFields()
        showToast(getString(R.string.message_insert_success))
    }

    private fun handleAlterarClick() {
        val codigo = binding.etCod.text.toString()
        val nome = binding.etNome.text.toString()
        val telefone = binding.etTelefone.text.toString()
        if (codigo == "" || nome == "" || telefone == "") return showToast(getString(R.string.error_empty_fields))
        val updated = db.update(Cadastro (
            codigo.toInt(),
            nome,
            telefone,
        ))
        val message: String = if (updated > 0) getString(R.string.message_update_success) else getString(
            R.string.message_not_found
        )
        showToast(message)
    }

    private fun handleExcluirClick() {
        val codigo = binding.etCod.text.toString()
        if (codigo == "") return showToast(getString(R.string.error_code))
        val deleted = db.delete(codigo.toInt())
        clearFields()
        val message: String = if (deleted > 0) getString(R.string.message_delete_success) else getString(
            R.string.message_not_found
        )
        showToast(message)
    }

    private fun handlePesquisarClick() {
        val codigo = binding.etCod.text.toString()
        if (codigo == "") {
            return showToast(getString(R.string.error_code))
        }
        val cadastro = db.find(codigo.toInt())
        if (cadastro !== null) {
            binding.etNome.setText(cadastro.nome)
            binding.etTelefone.setText(cadastro.telefone)
        } else {
            clearFields()
            showToast(getString(R.string.message_not_found))
        }
    }

    private fun handleListarClick() {
        val cadastros = db.list()
        val output = StringBuilder()
        for (cadastro in cadastros) {
            output.append("${cadastro.id} - ${cadastro.nome} - ${cadastro.telefone}\n")
        }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage(output)
            .setTitle(getString(R.string.modal_title_entries))
            .setNegativeButton(getString(R.string.modal_button_close)) { dialog, which -> dialog.dismiss() }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}