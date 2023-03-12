package com.example.cleannote.domain.use_case

import com.example.cleannote.domain.model.InvalidNoteException
import com.example.cleannote.domain.model.Note
import com.example.cleannote.domain.repository.NoteRepository

class AddNote(
    private val repo : NoteRepository
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note : Note){
        if (note.title.isBlank()){
            throw InvalidNoteException("The title of the note cannot be empty")
        }

        if(note.content.isBlank()){
            throw InvalidNoteException("The content of the note cannot be empty")
        }

        repo.insertNote(note)
    }
}