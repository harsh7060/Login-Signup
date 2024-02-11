package com.example.loginsignup

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.loginsignup.databinding.ActivityOpenNotesBinding
import com.example.loginsignup.databinding.NoteItemBinding

class NoteAdapter(private val notes : List<AddNoteItem>, private val itemClickListener: OnItemClickListener): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
interface OnItemClickListener{
    fun onEditClick(noteId: String, title:String, description:String)
    fun onDeleteClick(noteId: String)
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
        holder.binding.btnEdit.setOnClickListener{
            itemClickListener.onEditClick(note.noteId,note.title,note.description)
        }
        holder.binding.btnDelete.setOnClickListener{
            itemClickListener.onDeleteClick(note.noteId)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NoteViewHolder (val binding: NoteItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(note: AddNoteItem) {
            binding.textviewTitle.text = note.title
            binding.textviewDescription.text = note.description
        }

    }
}