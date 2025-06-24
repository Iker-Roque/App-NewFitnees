package com.example.newfitnes.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date


@Entity(tableName = "users")
@TypeConverters(Converters::class)

data class User (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name : String,
    val exercise : String,
    val intensity : String,
    val duration: Int,
    val repetitions: Int,
    val sets: Int,


    @ColumnInfo(name = "created_at") val createdAt: Date = Date()
)