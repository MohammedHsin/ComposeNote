package com.example.cleannote.presentation.note_edit

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleannote.domain.model.InvalidNoteException
import com.example.cleannote.domain.model.Note
import com.example.cleannote.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _titleState = mutableStateOf(NoteTextFieldState(
        hint = "Enter title .."
    ))
    val titleState : State<NoteTextFieldState> = _titleState

    private val _contentState = mutableStateOf(NoteTextFieldState(
        hint = "Enter some content .."
    ))
    val contentState : State<NoteTextFieldState> = _contentState

    private val _colorState = mutableStateOf(Note.noteColors.random().toArgb())
    val colorState :State<Int> = _colorState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId : Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let {
            if(it != -1){
                viewModelScope.launch {
                    noteUseCases.getNoteById(it)?.also {
                        currentNoteId = it.id
                        _titleState.value = _titleState.value.copy(
                            text = it.title,
                            isHintVisible = false
                        )

                        _contentState.value = _contentState.value.copy(
                            text = it.content,
                            isHintVisible = false
                        )

                        _colorState.value = it.color
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent){
        when(event){

            is AddEditNoteEvent.EnterTitle -> {
                _titleState.value = titleState.value.copy(text = event.value)
            }

            is AddEditNoteEvent.EnterContent -> {
                _contentState.value = contentState.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                _titleState.value = titleState.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            titleState.value.text.isBlank()
                )
            }

             is AddEditNoteEvent.ChangeContentFocus -> {
                _contentState.value = contentState.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            contentState.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.ChangeColor -> {
                _colorState.value = event.color
            }

            is AddEditNoteEvent.SaveNote ->{
                viewModelScope.launch {
                    try {
                    noteUseCases.addNote(Note(
                        title = titleState.value.text,
                        content = contentState.value.text,
                        timestamp = System.currentTimeMillis(),
                        color = colorState.value,
                        id = currentNoteId

                    ))
                        _eventFlow.emit(UiEvent.SaveNote)
                    }catch (e : InvalidNoteException){
                        _eventFlow.emit(UiEvent.ShowSnackBar(
                            message = e.message ?: "Couldn't save note"
                        ))

                    }

                }
            }
        }
    }

    sealed class UiEvent{
        data class ShowSnackBar(val message : String) : UiEvent()
        object SaveNote : UiEvent()
    }

}