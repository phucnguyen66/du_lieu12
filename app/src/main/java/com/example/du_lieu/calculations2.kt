package com.example.du_lieu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class calculations2 : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var baiHinhList: MutableList<BaiHinh>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bai_hinh)

        database = FirebaseDatabase.getInstance().getReference("calculations2")
        recyclerView = findViewById(R.id.recyclerView)
        baiHinhList = mutableListOf()

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        btnAdd.setOnClickListener {
            showDialogAddOrEdit(null)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                baiHinhList.clear()
                for (data in snapshot.children) {
                    val item = data.getValue(BaiHinh::class.java)
                    item?.id = data.key
                    item?.let { baiHinhList.add(it) }
                }
                recyclerView.adapter = BaiHinhAdapter(baiHinhList,
                    onEdit = { showDialogAddOrEdit(it) },
                    onDelete = { deleteItem(it) }
                )
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun showDialogAddOrEdit(baiHinh: BaiHinh?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit, null)
        val edtCauHoi = dialogView.findViewById<EditText>(R.id.edtCauHoi)
        val edtKetQua = dialogView.findViewById<EditText>(R.id.edtKetQua)

        if (baiHinh != null) {
            edtCauHoi.setText(baiHinh.cau_hoi)
            edtKetQua.setText(baiHinh.ket_qua.toString())
        }

        AlertDialog.Builder(this)
            .setTitle(if (baiHinh == null) "Thêm mới" else "Chỉnh sửa")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val cauHoi = edtCauHoi.text.toString()
                val ketQua = edtKetQua.text.toString().toIntOrNull() ?: 0

                if (baiHinh == null) {
                    val id = database.push().key!!
                    val newItem = BaiHinh(id, cauHoi,  ketQua)
                    database.child(id).setValue(newItem)
                } else {
                    val updated = BaiHinh(baiHinh.id, cauHoi,  ketQua)
                    database.child(baiHinh.id!!).setValue(updated)
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun deleteItem(item: BaiHinh) {
        database.child(item.id!!).removeValue()
    }
}
