package com.example.du_lieu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BaiHinhAdapter(
    private val list: List<BaiHinh>,
    private val onEdit: (BaiHinh) -> Unit,
    private val onDelete: (BaiHinh) -> Unit
) : RecyclerView.Adapter<BaiHinhAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCauHoi = itemView.findViewById<TextView>(R.id.tvCauHoi)
        val tvKetQua = itemView.findViewById<TextView>(R.id.tvKetQua)
        val btnEdit = itemView.findViewById<Button>(R.id.btnEdit)
        val btnDelete = itemView.findViewById<Button>(R.id.btnDelete)

        fun bind(item: BaiHinh) {
            tvCauHoi.text = item.cau_hoi
            tvKetQua.text = "Đáp án: ${item.ket_qua}"
            btnEdit.setOnClickListener { onEdit(item) }
            btnDelete.setOnClickListener { onDelete(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_baihinh, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])
    override fun getItemCount(): Int = list.size
}
