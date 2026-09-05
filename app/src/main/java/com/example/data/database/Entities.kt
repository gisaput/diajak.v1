package com.example.data.database

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.R
import com.example.model.ActivityModel
import com.example.model.BookingModel

private fun getResourceName(context: Context, resId: Int): String {
  return try {
    context.resources.getResourceEntryName(resId)
  } catch (e: Exception) {
    "diajak_activity_coffee_1783253596378"
  }
}

private fun getResourceId(context: Context, resName: String): Int {
  return try {
    val id = context.resources.getIdentifier(resName, "drawable", context.packageName)
    if (id != 0) id else R.drawable.diajak_activity_coffee_1783253596378
  } catch (e: Exception) {
    R.drawable.diajak_activity_coffee_1783253596378
  }
}

@Entity(tableName = "activities")
data class ActivityEntity(
  @PrimaryKey val id: String,
  val title: String,
  val category: String,
  val locationName: String,
  val address: String,
  val schedule: String,
  val rating: Double,
  val reviewsCount: Int,
  val priceFormatted: String,
  val priceValue: Int,
  val imageResName: String,
  val overview: String,
  val detailsListStr: String,
  val galleryImagesStr: String,
  val kreatorName: String,
  val kreatorVerified: Boolean,
  val maxPeserta: Int,
  val currentPeserta: Int,
  val mapX: Float,
  val mapY: Float,
  val isOngoing: Boolean = false,
  val isRecurring: Boolean = false,
  val recurringDays: String = "",
  val kreatorLastActiveDaysAgo: Int = 0
) {
  fun toModel(context: Context): ActivityModel {
    val details = if (detailsListStr.isBlank()) emptyList() else detailsListStr.split("|||")
    val gallery = if (galleryImagesStr.isBlank()) {
      emptyList()
    } else {
      galleryImagesStr.split(",").map { getResourceId(context, it.trim()) }
    }
    return ActivityModel(
      id = id,
      title = title,
      category = category,
      locationName = locationName,
      address = address,
      schedule = schedule,
      rating = rating,
      reviewsCount = reviewsCount,
      priceFormatted = priceFormatted,
      priceValue = priceValue,
      imageResId = getResourceId(context, imageResName),
      overview = overview,
      detailsList = details,
      galleryImages = gallery,
      kreatorName = kreatorName,
      kreatorVerified = kreatorVerified,
      maxPeserta = maxPeserta,
      currentPeserta = currentPeserta,
      mapX = mapX,
      mapY = mapY,
      isOngoing = isOngoing,
      isRecurring = isRecurring,
      recurringDays = recurringDays,
      kreatorLastActiveDaysAgo = kreatorLastActiveDaysAgo
    )
  }

  companion object {
    fun fromModel(model: ActivityModel, context: Context): ActivityEntity {
      val galleryStr = model.galleryImages.map { getResourceName(context, it) }.joinToString(",")
      return ActivityEntity(
        id = model.id,
        title = model.title,
        category = model.category,
        locationName = model.locationName,
        address = model.address,
        schedule = model.schedule,
        rating = model.rating,
        reviewsCount = model.reviewsCount,
        priceFormatted = model.priceFormatted,
        priceValue = model.priceValue,
        imageResName = getResourceName(context, model.imageResId),
        overview = model.overview,
        detailsListStr = model.detailsList.joinToString("|||"),
        galleryImagesStr = galleryStr,
        kreatorName = model.kreatorName,
        kreatorVerified = model.kreatorVerified,
        maxPeserta = model.maxPeserta,
        currentPeserta = model.currentPeserta,
        mapX = model.mapX,
        mapY = model.mapY,
        isOngoing = model.isOngoing,
        isRecurring = model.isRecurring,
        recurringDays = model.recurringDays,
        kreatorLastActiveDaysAgo = model.kreatorLastActiveDaysAgo
      )
    }
  }
}

typealias AcaraEntity = ActivityEntity

@Entity(tableName = "bookings")
data class BookingEntity(
  @PrimaryKey val id: String,
  val activityId: String,
  val activityTitle: String,
  val locationName: String,
  val schedule: String,
  val undanganCount: Int,
  val totalPriceFormatted: String,
  val bookingTimestamp: String,
  val imageResName: String,
  val status: String,
  val userName: String = "",
  val userEmail: String = "",
  val userPhone: String = "",
  val selectedDate: String = ""
) {
  val acaraId: String get() = activityId
  val acaraTitle: String get() = activityTitle

  fun toModel(context: Context): BookingModel {
    return BookingModel(
      id = id,
      activityId = activityId,
      activityTitle = activityTitle,
      locationName = locationName,
      schedule = schedule,
      undanganCount = undanganCount,
      totalPriceFormatted = totalPriceFormatted,
      bookingTimestamp = bookingTimestamp,
      imageResId = getResourceId(context, imageResName),
      status = status,
      userName = userName,
      userEmail = userEmail,
      userPhone = userPhone,
      selectedDate = selectedDate
    )
  }

  companion object {
    fun fromModel(model: BookingModel, context: Context): BookingEntity {
      return BookingEntity(
        id = model.id,
        activityId = model.activityId,
        activityTitle = model.activityTitle,
        locationName = model.locationName,
        schedule = model.schedule,
        undanganCount = model.undanganCount,
        totalPriceFormatted = model.totalPriceFormatted,
        bookingTimestamp = model.bookingTimestamp,
        imageResName = getResourceName(context, model.imageResId),
        status = model.status,
        userName = model.userName,
        userEmail = model.userEmail,
        userPhone = model.userPhone,
        selectedDate = model.selectedDate
      )
    }
  }
}

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
  @PrimaryKey val email: String,
  val name: String,
  val username: String,
  val bio: String,
  val gender: String,
  val birthDate: String,
  val phone: String,
  val imageUri: String?,
  val imageRes: Int
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
  @PrimaryKey val id: String, // Format: email_activityId
  val email: String,
  val activityId: String
) {
  val acaraId: String get() = activityId
}

