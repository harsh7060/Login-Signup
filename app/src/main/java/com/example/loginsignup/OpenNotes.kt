package com.example.loginsignup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginsignup.databinding.ActivityOpenNotesBinding
import com.example.loginsignup.databinding.DialogEditNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OpenNotes : AppCompatActivity(), NoteAdapter.OnItemClickListener {
    private val binding by lazy {
        ActivityOpenNotesBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recyclerView = binding.recylerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val noteReference = databaseReference.child("Users").child(user.uid).child("Notes")
            noteReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val notelist = mutableListOf<AddNoteItem>()
                    for (noteSnapShot in snapshot.children) {
                        val note = noteSnapShot.getValue(AddNoteItem::class.java)
                        note?.let {
                            notelist.add(it)
                        }
                    }
                    notelist.reverse()
                    val adapter = NoteAdapter(notelist, this@OpenNotes)
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }


    }

    override fun onEditClick(noteId: String, title: String, description: String) {
        val dialogBinding = DialogEditNoteBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this).setView(dialogBinding.root)
            .setTitle("Edit Notes")
//            .setPositiveButton("Save") { dialog, _ ->
//                val newTitle = dialogBinding.etEditTitle.text.toString()
//                val newDescription = dialogBinding.etEditDes.text.toString()
//                editNoteDatabase(noteId, newTitle, newDescription)
//                dialog.dismiss()
//            }
//            .setNegativeButton("Cancel") { dialog, _ ->
//                dialog.dismiss()
//            }

            .create()

          //if u want to edit the existing text then use this
//        dialogBinding.etEditTitle.setText(title)
//        dialogBinding.etEditDes.setText(description)

        dialogBinding.btnSave.setOnClickListener{
            val newTitle = dialogBinding.etEditTitle.text.toString()
            val newDescription = dialogBinding.etEditDes.text.toString()
            editNoteDatabase(noteId, newTitle, newDescription)
            dialog.dismiss()
        }
        dialogBinding.btnCancel.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun editNoteDatabase(noteId: String, newTitle: String, newDescription: String) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val noteReference = databaseReference.child("Users")
                .child(user.uid)
                .child("Notes")
            val updateNote = AddNoteItem(newTitle, newDescription, noteId)
            noteReference.child(noteId).setValue(updateNote)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Note is Edited Successfully ✅", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "Note is Edited Unsuccessfully ❌", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    override fun onDeleteClick(noteId: String) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val noteReference = databaseReference.child("Users")
                .child(user.uid)
                .child("Notes")
            noteReference.child(noteId).removeValue()
        }
    }
}