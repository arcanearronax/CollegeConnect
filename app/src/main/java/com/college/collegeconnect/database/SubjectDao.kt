package com.college.collegeconnect.database
import androidx.lifecycle.LiveData
import androidx.room.*
@Dao
interface SubjectDao {
    @Insert
    suspend fun add(subject: SubjectDetails)

    @Update
    suspend fun update(subject: SubjectDetails)

    @Query("DELETE FROM SubjectDetails WHERE id=:uid")
    suspend fun delete(uid:Int)

    @Query("SELECT * FROM SubjectDetails")
    fun getAttendance(): LiveData<List<SubjectDetails>>

    @Query("SELECT SUM(attended) FROM SubjectDetails")
    fun getAttended():LiveData<Int>

    @Query("SELECT SUM(missed) FROM SubjectDetails")
    fun getMissed():LiveData<Int>

    @Query("SELECT subjectName FROM SubjectDetails")
    fun getSubjects():LiveData<List<String>>

    // This is used to record a punch for a subject.
    @Insert
    suspend fun addHistory(historyEntry: AttendanceHistory)

    // This is used to pull punch data for subjects
    @Query("SELECT * FROM AttendanceHistory ORDER BY subjectId, id DESC")
    fun getHistory(): LiveData<List<AttendanceHistory>>

    @Query("DELETE FROM AttendanceHistory WHERE id = :uid")
    suspend fun deleteHistory(uid:Int)

}