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

class Calculations : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var baiHinhList: MutableList<BaiHinh>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bai_hinh)

        database = FirebaseDatabase.getInstance().getReference("Calculations")
        recyclerView = findViewById(R.id.recyclerView)
        baiHinhList = mutableListOf()

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        btnAdd.setOnClickListener {
            showDialogAddOrEdit(null)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Lắng nghe sự thay đổi dữ liệu trong Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                baiHinhList.clear()
                for (data in snapshot.children) {
                    val item = data.getValue(BaiHinh::class.java)
                    item?.id = data.key
                    item?.let { baiHinhList.add(it) }
                }
                // Cập nhật adapter với danh sách các bài hình
                recyclerView.adapter = BaiHinhAdapter(baiHinhList,
                    onEdit = { showDialogAddOrEdit(it) },
                    onDelete = { deleteItem(it) }
                )
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // Hiển thị dialog để thêm hoặc chỉnh sửa bài hình
    private fun showDialogAddOrEdit(baiHinh: BaiHinh?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit, null)
        val edtExpression = dialogView.findViewById<EditText>(R.id.edtCauHoi)
        val edtResult = dialogView.findViewById<EditText>(R.id.edtKetQua)

        // Nếu bài hình đã có, điền thông tin vào các EditText
        if (baiHinh != null) {
            edtExpression.setText(baiHinh.cau_hoi)
            edtResult.setText(baiHinh.ket_qua.toString().toInt())
        }

        AlertDialog.Builder(this)
            .setTitle(if (baiHinh == null) "Thêm mới" else "Chỉnh sửa")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val expression = edtExpression.text.toString()
                val result = edtResult.text.toString().toInt()

                // Nếu không có bài hình (tạo mới), thêm vào Firebase
                if (baiHinh == null) {
                    val id = database.push().key!!
                    val newItem = BaiHinh(id, expression, result)
                    database.child(id).setValue(newItem)
                } else {
                    // Nếu có bài hình, chỉnh sửa và lưu lại
                    val updated = BaiHinh(baiHinh.id, expression, result)
                    database.child(baiHinh.id!!).setValue(updated)
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    // Xóa bài hình từ Firebase
    private fun deleteItem(item: BaiHinh) {
        database.child(item.id!!).removeValue()
    }
}
