package com.college.collegeconnect.database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
@Entity
data class AttendanceHistory(
        val subjectId: Int,
        val punch: Int
) {
    @PrimaryKey( autoGenerate = true )
    var id:Int = 0
    //@ForeignKey( entity=SubjectDetails )
    //val subjectId: Int = 0
}