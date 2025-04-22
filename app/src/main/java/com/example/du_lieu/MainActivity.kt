package com.example.du_lieu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.du_lieu.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)


        binding.btnBaiHinh.setOnClickListener {
            startActivity(Intent(this, Bai_hinh::class.java))
        }

        binding.btnBaiHinhCap2.setOnClickListener {
            startActivity(Intent(this, Bai_hinh_cap2::class.java))
        }

        binding.btnBaiHinhCap3.setOnClickListener {
            startActivity(Intent(this, Bai_hinh_cap3::class.java))
        }

        binding.btnCalculations.setOnClickListener {
            startActivity(Intent(this, Calculations::class.java))
        }

        binding.btnUsers.setOnClickListener {
            startActivity(Intent(this, Users::class.java))
        }

        binding.btnBaiToan.setOnClickListener {
            startActivity(Intent(this, bai_toan::class.java))
        }

        binding.btnBaiToanCap2.setOnClickListener {
            startActivity(Intent(this, bai_toan_cap2::class.java))
        }

        binding.btnBaiToanCap3.setOnClickListener {
            startActivity(Intent(this, bai_toan_cap3::class.java))
        }

        binding.btnCalculations2.setOnClickListener {
            startActivity(Intent(this, calculations2::class.java))
        }

        binding.btnKetQua.setOnClickListener {
            startActivity(Intent(this, ket_qua::class.java))
        }

        binding.btnMessages.setOnClickListener {
            startActivity(Intent(this, messages::class.java))
        }
    }
}
