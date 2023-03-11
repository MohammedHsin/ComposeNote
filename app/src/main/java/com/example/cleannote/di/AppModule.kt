package com.example.cleannote.di

import android.app.Application
import androidx.room.Room
import com.example.cleannote.data.data_source.NoteDao
import com.example.cleannote.data.data_source.NoteDatabase
import com.example.cleannote.data.repository.NoteRepositoryImpl
import com.example.cleannote.domain.repository.NoteRepository
import com.example.cleannote.domain.use_case.AddNote
import com.example.cleannote.domain.use_case.DeleteNote
import com.example.cleannote.domain.use_case.GetNoteById
import com.example.cleannote.domain.use_case.GetNotes
import com.example.cleannote.domain.use_case.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app : Application) : NoteDatabase{
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME,
        ).build()
    }


    @Provides
    @Singleton
    fun provideNoteRepository(db : NoteDatabase) : NoteRepository{
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repo : NoteRepository) : NoteUseCases{
        return NoteUseCases(
            getNotes = GetNotes(repo),
            deleteNote = DeleteNote(repo),
            addNote = AddNote(repo),
            getNoteById = GetNoteById(repo)
        )
    }
}