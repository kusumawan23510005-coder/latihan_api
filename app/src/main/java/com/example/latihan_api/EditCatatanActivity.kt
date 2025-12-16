package com.example.latihan_api

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.latihan_api.databinding.ActivityEditCatatanBinding
import com.example.latihan_api.entities.Catatan
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class EditCatatanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditCatatanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEditCatatanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupEvents()
    }

    override fun onStart(){
        super.onStart()
        loadData()
    }


    fun setupEvents(){
        binding.tombolSimpan.setOnClickListener{
            val id = intent.getIntExtra("id_catatan",0)
            val judul = binding.inputJudul.text.toString()
            val isi = binding.inputIsi.text.toString()

            if (isi.isEmpty() || judul.isEmpty()) {
                displayMessage("Judul dan isi catatan harus diisi")
                return@setOnClickListener
            }

        lifecycleScope.launch {
            val catatan = Catatan(id, judul, isi, user_id = 1)
            val data = RetrofitClient.catatanRepository.editcatatan(id, catatan)

            if (data.isSuccessful){
              displayMessage("Catatan berhasil diubah ")
                switchPage(MainActivity::class.java)
            }else{
                displayMessage("Error: ${data.message()}")

            }
        }
        }
    }

    fun loadData(){
        val id = intent.getIntExtra("id_catatan",0)

        if (id == 0 ) {

            //tampilkan pesain error
        displayMessage("error: id catatan tidak terkirim")
        switchPage(MainActivity::class.java)
            return
    }
        lifecycleScope.launch {
            val data = RetrofitClient.catatanRepository.getCatatan(id)
            if (data.isSuccessful){
                val catatan = data.body()
                binding.inputJudul.setText(catatan?.judul)
                binding.inputIsi.setText(catatan?.isi)
            }else{
                displayMessage("Eror: ${data.message()}")
                switchPage(MainActivity::class.java)

            }            }
        }

    fun displayMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun switchPage(destination: Class<MainActivity>){
        val intent = Intent(this, destination)
        startActivity(intent)
        finish()
    }
}