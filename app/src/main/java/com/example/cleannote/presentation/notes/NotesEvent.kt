package com.example.cleannote.presentation.notes

import com.example.cleannote.domain.model.Note
import com.example.cleannote.domain.util.NoteOrder

sealed class NotesEvent{
    data class Order(val noteOrder: NoteOrder) : NotesEvent()
    data class Delete(val note : Note) : NotesEvent()
    object RestoreNote : NotesEvent()
    object ToggleOrderSection : NotesEvent()
}
