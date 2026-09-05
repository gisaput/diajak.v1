package com.example

import android.app.Application
import com.example.data.database.AppDatabase
import com.example.data.DiajakRepository

class DiajakApplication : Application() {
  val database by lazy { AppDatabase.getDatabase(this) }

  override fun onCreate() {
    super.onCreate()
    DiajakRepository.initialize(database, this)
  }
}
