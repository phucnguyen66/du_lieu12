package com.example.du_lieu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ket_qua : AppCompatActivity() {
    private lateinit var spinnerUser: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: KetQuaAdapter
    private lateinit var ketQuaList: MutableList<KetQua>
    private lateinit var database: DatabaseReference
    private var selectedUser: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ket_qua)

        spinnerUser = findViewById(R.id.spinnerUser)
        recyclerView = findViewById(R.id.recyclerKetQua)
        findViewById<Button>(R.id.btnAddKetQua).setOnClickListener { showDialog(null) }

        ketQuaList = mutableListOf()
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Khởi tạo database Firebase
        database = FirebaseDatabase.getInstance().getReference("ket_qua")

        // Lấy danh sách người dùng từ Firebase (lấy các key của ket_qua)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<String>()
                for (data in snapshot.children) {
                    val username = data.key // Lấy key của từng node (tên người dùng)
                    username?.let { users.add(it) }
                }

                // Cập nhật Spinner với danh sách người dùng
                val userAdapter = ArrayAdapter(this@ket_qua, android.R.layout.simple_spinner_dropdown_item, users)
                spinnerUser.adapter = userAdapter

                // Khi Spinner chọn một người dùng, load kết quả của người đó
                spinnerUser.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p: AdapterView<*>, v: View?, pos: Int, id: Long) {
                        selectedUser = users[pos]
                        loadData()
                    }

                    override fun onNothingSelected(p: AdapterView<*>) {}
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun loadData() {
        // Lấy kết quả bài làm của người dùng đã chọn từ Firebase
        database.child(selectedUser).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ketQuaList.clear()
                for (data in snapshot.children) {
                    val item = data.getValue(KetQua::class.java)
                    item?.id = data.key
                    item?.let { ketQuaList.add(it) }
                }
                adapter = KetQuaAdapter(ketQuaList, { showDialog(it) }, { deleteKetQua(it) })
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun showDialog(item: KetQua?) {
        val view = LayoutInflater.from(this).inflate(R.layout.sua_kq, null)
        val edtTieude = view.findViewById<EditText>(R.id.edtTieude)
        val edtChuDe = view.findViewById<EditText>(R.id.edtChuDe)
        val edtDung = view.findViewById<EditText>(R.id.edtDung)
        val edtSai = view.findViewById<EditText>(R.id.edtSai)
        val edtNgay = view.findViewById<EditText>(R.id.edtNgay)

        if (item != null) {
            edtTieude.setText(item.tieude)
            edtChuDe.setText(item.chuDe)
            edtDung.setText(item.dung.toString())
            edtSai.setText(item.sai.toString())
            edtNgay.setText(item.ngayThang)
        }

        AlertDialog.Builder(this)
            .setTitle(if (item == null) "Thêm Kết Quả" else "Chỉnh Sửa")
            .setView(view)
            .setPositiveButton("Lưu") { _, _ ->
                val newItem = KetQua(
                    id = item?.id ?: database.push().key,
                    tieude = edtTieude.text.toString(),
                    chuDe = edtChuDe.text.toString(),
                    dung = edtDung.text.toString().toIntOrNull() ?: 0,
                    sai = edtSai.text.toString().toIntOrNull() ?: 0,
                    ngayThang = edtNgay.text.toString()
                )
                database.child(selectedUser).child(newItem.id!!).setValue(newItem)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun deleteKetQua(item: KetQua) {
        database.child(selectedUser).child(item.id!!).removeValue()
    }
}
