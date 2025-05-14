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

class bai_toan_cap3 : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var baiHinhList: MutableList<BaiHinh>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bai_hinh)

        database = FirebaseDatabase.getInstance().getReference("bai_toan_cap3")
        recyclerView = findViewById(R.id.recyclerView)
        baiHinhList = mutableListOf()



        val btnAdd = findViewById<Button>(R.id.btnAdd)
        btnAdd.setOnClickListener {
            showDialogAddOrEdit(null)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Lấy dữ liệu từ Firebase và xử lý
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                baiHinhList.clear()
                for (data in snapshot.children) {
                    val item = data.getValue(BaiHinh::class.java)

                    // Chuyển key và các trường dữ liệu từ Firebase thành kiểu Int
                    item?.id = data.key?.toIntOrNull() ?: 0 // Chuyển key từ String sang Int
                    item?.dapAn = item?.dapAn?.toString()?.toIntOrNull() ?: 0 // Chuyển dapAn thành Int
                    item?.lop = item?.lop?.toString()?.toIntOrNull() ?: 0 // Chuyển lop thành Int

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
        val edtDeToan = dialogView.findViewById<EditText>(R.id.edtDeToan)
        val edtDapAn = dialogView.findViewById<EditText>(R.id.edtDapAn)
        val edtLop = dialogView.findViewById<EditText>(R.id.edtLop)

        if (baiHinh != null) {
            edtDeToan.setText(baiHinh.deToan)
            edtDapAn.setText(baiHinh.dapAn.toString())
            edtLop.setText(baiHinh.lop.toString())
        }

        AlertDialog.Builder(this)
            .setTitle(if (baiHinh == null) "Thêm mới" else "Chỉnh sửa")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val deToan = edtDeToan.text.toString()
                val dapAn = edtDapAn.text.toString().toIntOrNull() ?: 0 // Kiểm tra và chuyển sang Int
                val lop = edtLop.text.toString().toIntOrNull() ?: 0 // Kiểm tra và chuyển sang Int

                if (baiHinh == null) {
                    val id = database.push().key?.toIntOrNull() ?: 0 // Lấy key từ Firebase và chuyển thành Int
                    val newItem = BaiHinh(id, deToan, dapAn, lop)
                    database.child(id.toString()).setValue(newItem) // Lưu với id là Int
                } else {
                    val updated = BaiHinh(baiHinh.id, deToan, dapAn, lop)
                    database.child(baiHinh.id.toString()).setValue(updated) // Cập nhật theo id Int
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun deleteItem(item: BaiHinh) {
        database.child(item.id.toString()).removeValue() // Sử dụng id.toString() khi xóa
    }
}
