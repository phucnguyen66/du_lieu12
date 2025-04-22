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

class Users : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var userList: MutableList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        database = FirebaseDatabase.getInstance().getReference("Users")
        recyclerView = findViewById(R.id.recyclerViewUsers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        userList = mutableListOf()

        val btnAddUser = findViewById<Button>(R.id.btnAddUser)
        btnAddUser.setOnClickListener {
            showDialog(null)
        }

        loadUsers()
    }

    private fun loadUsers() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (item in snapshot.children) {
                    val user = item.getValue(User::class.java)
                    userList.add(user!!)
                }
                adapter = UserAdapter(userList,
                    onEdit = { showDialog(it) },
                    onDelete = { deleteUser(it) }
                )
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun showDialog(user: User?) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_user, null)
        val edtUsername = view.findViewById<EditText>(R.id.edtUsername)
        val edtPassword = view.findViewById<EditText>(R.id.edtPassword)

        user?.let {
            edtUsername.setText(it.username)
            edtPassword.setText(it.password)
        }

        AlertDialog.Builder(this)
            .setTitle(if (user == null) "Thêm Người Dùng" else "Sửa Người Dùng")
            .setView(view)
            .setPositiveButton("Lưu") { _, _ ->
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()

                val id = user?.username ?: database.push().key!!
                val newUser = User(username, password)
                database.child(id).setValue(newUser)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun deleteUser(user: User) {
        database.child(user.username).removeValue()
    }
}
