package com.yodiet.data.db.dao

import androidx.room.*
import com.yodiet.data.db.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Query("SELECT * FROM users ORDER BY first_name ASC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Long): User?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun observeUserById(id: Long): Flow<User?>

    @Query("SELECT * FROM users LIMIT 1")
    fun getCurrentUserFlow(): Flow<User?>

}
