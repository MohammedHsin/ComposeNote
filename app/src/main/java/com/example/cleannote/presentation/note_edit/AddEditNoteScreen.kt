package com.example.cleannote.presentation.note_edit

import android.widget.Space
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cleannote.domain.model.Note
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScree(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel()
){

    val titleState = viewModel.titleState.value
    val contentState = viewModel.contentState.value
    val colorState = viewModel.colorState.value


    val snackBarHostSate = remember{ SnackbarHostState() }


    val noteBackgroundAnimatable = remember{
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.colorState.value)
        )
    }

    val scope = rememberCoroutineScope()


    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest {
            when(it){
                is AddEditNoteViewModel.UiEvent.ShowSnackBar ->{
                    snackBarHostSate.showSnackbar(
                        message = it.message
                    )
                }

                is AddEditNoteViewModel.UiEvent.SaveNote ->{
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostSate) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(AddEditNoteEvent.SaveNote)
            } , containerColor = MaterialTheme.colorScheme.primary) {

                Icon(imageVector = Icons.Default.Save, contentDescription = "Save Note")
            }

            Column(modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(16.dp)) {

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp) ,
                horizontalArrangement = Arrangement.SpaceBetween) {

                    Note.noteColors.forEach{color ->
                        val colorInt = color.toArgb()
                        
                        Box(modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                color = if (viewModel.colorState.value == colorInt) {
                                    Color.Black
                                } else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                                viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                            }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                TransparentHintTextField(text = titleState.text,
                    hint = titleState.hint,
                    onValueChange = {
                                    viewModel.onEvent(AddEditNoteEvent.EnterTitle(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                    } , isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium)


                Spacer(Modifier.height(16.dp))
                TransparentHintTextField(text = contentState.text,
                    hint = contentState.hint,
                    onValueChange = {
                        viewModel.onEvent(AddEditNoteEvent.EnterContent(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                    } , isHintVisible = contentState.isHintVisible,
                    textStyle = MaterialTheme.typography.bodySmall, modifier = Modifier.fillMaxSize())

            }
        }
    ) {

        val ignore = it
    }
    
}