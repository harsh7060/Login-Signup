package com.example.loginsignup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class NoteItem(newTitle: String, newDescription: String, noteId: String) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_item)

        val btnEdit = findViewById<Button>(R.id.btn_edit)
        val btnDelete = findViewById<Button>(R.id.btn_delete)

        btnEdit.setOnClickListener {
            Toast.makeText(this,"edit",Toast.LENGTH_SHORT).show()
        }
        btnDelete.setOnClickListener {
            Toast.makeText(this,"delete",Toast.LENGTH_SHORT).show()
        }
    }
}