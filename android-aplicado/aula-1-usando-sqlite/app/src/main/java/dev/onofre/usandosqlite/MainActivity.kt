package dev.onofre.usandosqlite

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import dev.onofre.usandosqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)
        setButtonListeners()
        setupDatabase()
    }

    private fun setupDatabase() {
        db = SQLiteDatabase.openOrCreateDatabase(
            this.getDatabasePath("dbfile.sqlite"),
            null
        )
        var createTableQuery = "CREATE TABLE IF NOT EXISTS cadastro(_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, telefone TEXT)"
        db.execSQL(createTableQuery)
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

    private fun handleIncluirClick() {
        val nome = binding.etNome.text.toString()
        val telefone = binding.etTelefone.text.toString()
        if (nome == "" || telefone == "") {
            Toast.makeText(this, "Preencha nome e telefone", Toast.LENGTH_SHORT).show()
            return
        }
        val register = ContentValues()
        register.put("nome", binding.etNome.text.toString())
        register.put("telefone", binding.etTelefone.text.toString())
        db.insert("cadastro", null, register)
        binding.etNome.setText("")
        binding.etTelefone.setText("")
        Toast.makeText(this, "Registro incluido!", Toast.LENGTH_LONG).show()
    }

    private fun handleAlterarClick() {
        val codigo = binding.etCod.text.toString()
        val nome = binding.etNome.text.toString()
        val telefone = binding.etTelefone.text.toString()
        if (codigo == "" || nome == "" || telefone == "") {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }
        val register = ContentValues()
        register.put("nome", binding.etNome.text.toString())
        register.put("telefone", binding.etTelefone.text.toString())
        val updated = db.update("cadastro", register, "_id=${codigo}", null)
        val message: String = if (updated > 0) "Registro alterado!" else "Registro não encontrado!"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun handleExcluirClick() {
        val codigo = binding.etCod.text.toString()
        if (codigo == "") {
            Toast.makeText(this, "Insira um código!", Toast.LENGTH_SHORT).show()
            return
        }
        val deleted = db.delete("cadastro", "_id=${codigo}", null)
        val message: String = if (deleted > 0) "Registro deletado" else "Registro não encontrado!"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun handlePesquisarClick() {
        val codigo = binding.etCod.text.toString()
        if (codigo == "") {
            Toast.makeText(this, "Insira um código!", Toast.LENGTH_SHORT).show()
            return
        }
        val register = db.query("cadastro", null, "_id=${codigo}", null, null, null, null)
        if (register.moveToNext()) {
            binding.etNome.setText(register.getString(NOME))
            binding.etTelefone.setText(register.getString(TELEFONE))
        } else {
            binding.etNome.setText("")
            binding.etTelefone.setText("")
            Toast.makeText(this, "Registro não encontrado", Toast.LENGTH_LONG).show()
        }
        register.close()
    }

    private fun handleListarClick() {
        val register = db.query("cadastro", null, null, null, null, null, null)
        val output = StringBuilder()
        while (register.moveToNext()) {
            output.append("${register.getString(NOME)} - ${register.getString(TELEFONE)}\n")
        }
        register.close()

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage(output)
            .setTitle("Registros")
            .setNegativeButton("Fechar") { dialog, which -> dialog.dismiss() }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    companion object {
        private const val ID = 0
        private const val NOME = 1
        private const val TELEFONE = 2
    }

}