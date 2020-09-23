package com.college.collegeconnect.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubjectDetails(
    val subjectName: String,
    val attended: Int,
    val missed: Int,
    val mostRecentChoice: Int
) {
    @PrimaryKey( autoGenerate = true )
    var id:Int = 0
}