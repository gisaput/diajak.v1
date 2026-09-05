package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
  @Query("SELECT * FROM activities")
  fun getAllActivities(): Flow<List<ActivityEntity>>

  @Query("SELECT COUNT(*) FROM activities")
  suspend fun getActivitiesCount(): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(activities: List<ActivityEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(activity: ActivityEntity)

  @Query("UPDATE activities SET currentPeserta = :currentPeserta WHERE id = :id")
  suspend fun updatePeserta(id: String, currentPeserta: Int)
}

typealias AcaraDao = ActivityDao

@Dao
interface BookingDao {
  @Query("SELECT * FROM bookings")
  fun getAllBookings(): Flow<List<BookingEntity>>

  @Query("SELECT COUNT(*) FROM bookings")
  suspend fun getBookingsCount(): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(bookings: List<BookingEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(booking: BookingEntity)
}

@Dao
interface UserProfileDao {
  @Query("SELECT * FROM user_profiles WHERE email = :email LIMIT 1")
  suspend fun getUserProfile(email: String): UserProfileEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUserProfile(userProfile: UserProfileEntity)
}

@Dao
interface FavoriteDao {
  @Query("SELECT activityId FROM favorites WHERE email = :email")
  suspend fun getFavoriteActivityIds(email: String): List<String>

  suspend fun getFavoriteAcaraIds(email: String): List<String> = getFavoriteActivityIds(email)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFavorite(favorite: FavoriteEntity)

  @Query("DELETE FROM favorites WHERE email = :email AND activityId = :activityId")
  suspend fun deleteFavorite(email: String, activityId: String)
}


