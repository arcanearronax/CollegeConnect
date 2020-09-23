package com.college.collegeconnect.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

data class AttendanceHistory(
        val attended: Int,
        val missed: Int,
        val mostRecentChoice: Int
) {
    @PrimaryKey( autoGenerate = true )
    var id:Int = 0

    @ForeignKey( entity=SubjectDetails )
    val subjectId: Int = 0
}