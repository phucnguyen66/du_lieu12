package com.example.du_lieu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(
    private val list: List<Message>,
    private val onEdit: (Message) -> Unit,
    private val onDelete: (Message) -> Unit
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUsername = view.findViewById<TextView>(R.id.tvUsername)
        val tvMessage = view.findViewById<TextView>(R.id.tvMessage)
        val btnEdit = view.findViewById<Button>(R.id.btnEditMsg)
        val btnDelete = view.findViewById<Button>(R.id.btnDeleteMsg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = list[position]
        holder.tvUsername.text = item.username
        holder.tvMessage.text = item.message
        holder.btnEdit.setOnClickListener { onEdit(item) }
        holder.btnDelete.setOnClickListener { onDelete(item) }
    }

    override fun getItemCount() = list.size
}
