package com.example.model

import androidx.compose.ui.graphics.Color

data class ActivityModel(
  val id: String,
  val title: String,
  val category: String,
  val locationName: String,
  val address: String,
  val schedule: String,
  val rating: Double,
  val reviewsCount: Int,
  val priceFormatted: String,
  val priceValue: Int,
  val imageResId: Int,
  val overview: String,
  val detailsList: List<String>,
  val galleryImages: List<Int>,
  val kreatorName: String,
  val kreatorVerified: Boolean = true,
  val maxPeserta: Int = 15,
  val currentPeserta: Int = 8,
  val mapX: Float = 0.5f, // Normalized 0.0 to 1.0 on map canvas
  val mapY: Float = 0.5f,
  val isOngoing: Boolean = false,
  val isRecurring: Boolean = false,
  val recurringDays: String = "",
  val kreatorLastActiveDaysAgo: Int = 0,
  val isPublished: Boolean = true
)

typealias AcaraModel = ActivityModel

data class PromoModel(
  val id: String,
  val title: String,
  val subtitle: String,
  val buttonText: String,
  val imageResId: Int,
  val badge: String
)

data class BookingModel(
  val id: String,
  val activityId: String,
  val activityTitle: String,
  val locationName: String,
  val schedule: String,
  val undanganCount: Int,
  val totalPriceFormatted: String,
  val bookingTimestamp: String,
  val imageResId: Int,
  val status: String = "Terkonfirmasi",
  val userName: String = "",
  val userEmail: String = "",
  val userPhone: String = "",
  val selectedDate: String = "",
  val acaraId: String = activityId,
  val acaraTitle: String = activityTitle
)

data class CategoryItem(
  val id: String,
  val name: String,
  val icon: String // Emoji representation
)

data class ReviewModel(
  val id: String,
  val userName: String,
  val rating: Int,
  val comment: String,
  val date: String
)

data class DiajakNotification(
  val id: String,
  val title: String,
  val body: String,
  val type: String, // "activity" | "booking" | "message" | "promo" | "order"
  val timestamp: String,
  val isRead: Boolean = false,
  val relatedId: String? = null,
  val role: String = "peserta", // "peserta" or "kreator"
  val isArchived: Boolean = false
)

data class MessageThread(
  val id: String,
  val senderName: String,
  val senderRole: String,
  val lastMessage: String,
  val time: String,
  val unreadCount: Int,
  val avatarColor: Color,
  val activityTitle: String,
  val role: String = "peserta", // "peserta" or "kreator"
  val isArchived: Boolean = false,
  val acaraTitle: String = activityTitle
)


