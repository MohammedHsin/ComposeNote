package com.example.cleannote.domain.use_case

import com.example.cleannote.domain.repository.NoteRepository
import com.example.cleannote.domain.model.Note

class GetNoteById(
private val repo : NoteRepository
) {
    suspend operator fun invoke(id : Int) : Note?{
       return repo.getNoteById(id)
    }
}