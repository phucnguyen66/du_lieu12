package com.example.du_lieu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KetQuaAdapter(
    private val list: List<KetQua>,
    private val onEdit: (KetQua) -> Unit,
    private val onDelete: (KetQua) -> Unit
) : RecyclerView.Adapter<KetQuaAdapter.KetQuaViewHolder>() {

    inner class KetQuaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTieude = view.findViewById<TextView>(R.id.tvTieude)
        val tvChuDe = view.findViewById<TextView>(R.id.tvChuDe)
        val tvDung = view.findViewById<TextView>(R.id.tvDung)
        val tvSai = view.findViewById<TextView>(R.id.tvSai)
        val tvNgayThang = view.findViewById<TextView>(R.id.tvNgayThang)
        val btnEdit = view.findViewById<Button>(R.id.btnEdit)
        val btnDelete = view.findViewById<Button>(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KetQuaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ket_qua, parent, false)
        return KetQuaViewHolder(view)
    }

    override fun onBindViewHolder(holder: KetQuaViewHolder, position: Int) {
        val item = list[position]
        holder.tvTieude.text = "Tiêu đề: ${item.tieude}"
        holder.tvChuDe.text = "Chủ đề: ${item.chuDe}"
        holder.tvDung.text = "Đúng: ${item.dung}"
        holder.tvSai.text = "Sai: ${item.sai}"
        holder.tvNgayThang.text = "Ngày: ${item.ngayThang}"
        holder.btnEdit.setOnClickListener { onEdit(item) }
        holder.btnDelete.setOnClickListener { onDelete(item) }
    }

    override fun getItemCount() = list.size
}
