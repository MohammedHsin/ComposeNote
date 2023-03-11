package com.example.cleannote.presentation.note_edit

data class NoteTextFieldState(
    val text : String = "",
    val hint : String = "",
    val isHintVisible : Boolean = true
)