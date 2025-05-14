package com.example.du_lieu

import com.google.firebase.database.DataSnapshot

data class BaiHinh(
    var id: Int? = null,        // id vẫn là Int? để lưu trữ giá trị số nguyên
    var deToan: String = "",
    var dapAn: Int = 0,
    var lop: Int = 0
) {
    // Tạo một phương thức để xử lý việc chuyển kiểu dữ liệu của các trường
    fun fromDataSnapshot(snapshot: DataSnapshot) {
        val deToan = snapshot.child("deToan").getValue(String::class.java) ?: ""
        val dapAn = snapshot.child("dapAn").getValue(String::class.java)?.toIntOrNull() ?: 0
        val lop = snapshot.child("lop").getValue(String::class.java)?.toIntOrNull() ?: 0
        this.deToan = deToan
        this.dapAn = dapAn
        this.lop = lop
    }
}