package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
  entities = [ActivityEntity::class, BookingEntity::class, UserProfileEntity::class, FavoriteEntity::class],
  version = 20,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun activityDao(): ActivityDao
  fun acaraDao(): ActivityDao = activityDao()
  abstract fun bookingDao(): BookingDao
  abstract fun userProfileDao(): UserProfileDao
  abstract fun favoriteDao(): FavoriteDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "diajak_database"
        )
        .fallbackToDestructiveMigration(true)
        .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
