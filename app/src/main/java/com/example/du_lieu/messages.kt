package com.example.du_lieu
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class messages : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageList: MutableList<Message>
    private lateinit var adapter: MessageAdapter
    private lateinit var database: DatabaseReference
    private lateinit var btnAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        recyclerView = findViewById(R.id.recyclerMsg)
        btnAdd = findViewById(R.id.btnAddMessage)
        recyclerView.layoutManager = LinearLayoutManager(this)

        database = FirebaseDatabase.getInstance().getReference("messages")
        messageList = mutableListOf()

        btnAdd.setOnClickListener {
            showDialog(null)
        }

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (data in snapshot.children) {
                    val item = data.getValue(Message::class.java)
                    item?.id = data.key
                    item?.let { messageList.add(it) }
                }
                adapter = MessageAdapter(messageList, { showDialog(it) }, { deleteMessage(it) })
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun showDialog(item: Message?) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_message, null)
        val edtUser = view.findViewById<EditText>(R.id.edtUsername)
        val edtMsg = view.findViewById<EditText>(R.id.edtMessage)

        if (item != null) {
            edtUser.setText(item.username)
            edtMsg.setText(item.message)
        }

        AlertDialog.Builder(this)
            .setTitle(if (item == null) "Thêm Tin Nhắn" else "Sửa Tin Nhắn")
            .setView(view)
            .setPositiveButton("Lưu") { _, _ ->
                val msg = Message(
                    id = item?.id ?: database.push().key,
                    username = edtUser.text.toString(),
                    message = edtMsg.text.toString()
                )
                database.child(msg.id!!).setValue(msg)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun deleteMessage(item: Message) {
        database.child(item.id!!).removeValue()
    }
}
