package com.example.cleannote.presentation.notes

import com.example.cleannote.domain.model.Note
import com.example.cleannote.domain.util.NoteOrder
import com.example.cleannote.domain.util.OrderType

data class NotesState(
    val notes : List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible : Boolean = false
)