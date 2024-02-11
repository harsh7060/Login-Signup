package com.example.loginsignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.loginsignup.databinding.ActivityAddNotesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddNotes : AppCompatActivity() {
    private val binding by lazy {
        ActivityAddNotesBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Initializing both the variables
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        binding.btnAddNote.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text.toString()

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Fill both the fields", Toast.LENGTH_SHORT).show()
            } else {
                val currentUser = auth.currentUser
                currentUser?.let { user ->
                    //Generate a unique key for the note
                    val noteKey = databaseReference.child("Users")
                        .child(user.uid)
                        .child("Notes")
                        .push().key

                    //Making instance of AddNoteItem Data Class
                    val noteItem = AddNoteItem(title, description,noteKey?:"")
                    if (noteKey != null) {
                        databaseReference.child("Users")
                            .child(user.uid)
                            .child("Notes")
                            .child(noteKey)
                            .setValue(noteItem)
                            .addOnCompleteListener {task ->
                                if(task.isSuccessful){
                                    Toast.makeText(this,"Note is Added Successfully ✅", Toast.LENGTH_SHORT).show()
                                    finish()
                                }else{
                                    Toast.makeText(this,"Note is Added Unsuccessfully ❌", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
            }
        }
    }
}