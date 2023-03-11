package com.example.cleannote.domain.use_case

import com.example.cleannote.domain.model.Note
import com.example.cleannote.domain.repository.NoteRepository

class DeleteNote(
    private val repo : NoteRepository
) {
    suspend operator fun invoke(note : Note){
        repo.deleteNote(note)
    }
}