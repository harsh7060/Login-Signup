package com.example.loginsignup

data class AddNoteItem(val title: String, val description: String, val noteId: String) {
    constructor() : this("", "","")
}
