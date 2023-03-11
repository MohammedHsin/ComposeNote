package com.example.cleannote.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cleannote.ui.theme.BabyBlue
import com.example.cleannote.ui.theme.DarkGray
import com.example.cleannote.ui.theme.LightBlue
import com.example.cleannote.ui.theme.LightGreen
import com.example.cleannote.ui.theme.RedOrange
import com.example.cleannote.ui.theme.RedPink
import com.example.cleannote.ui.theme.Violet

@Entity
data class Note(
    val title : String,
    val content : String,
    val timestamp : Long,
    val color : Int,
    @PrimaryKey val id : Int? = null
){

    companion object{
        val noteColors = listOf(DarkGray , LightBlue , RedOrange , RedPink , BabyBlue , Violet , LightGreen)
    }
}

class InvalidNoteException(msg : String) : Exception(msg)