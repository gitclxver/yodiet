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

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE email = :email AND first_name = :firstName LIMIT 1")
    suspend fun validateUser(email: String, firstName: String): User?


    @Query("SELECT * FROM users WHERE email = :emailOrUsername OR user_name = :emailOrUsername LIMIT 1")
    suspend fun getUserByEmailOrUsername(emailOrUsername: String): User?

    @Query("SELECT * FROM users WHERE user_name = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun validateUserCredentials(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE is_current = 1 LIMIT 1")
    suspend fun getCurrentUser(): User?

    @Query("UPDATE users SET is_current = 0")
    suspend fun clearCurrentUser()

    @Query("UPDATE users SET is_current = 1 WHERE id = :userId")
    suspend fun setCurrentUser(userId: Long)
}
