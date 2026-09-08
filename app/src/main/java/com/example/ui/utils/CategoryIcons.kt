package com.example.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsBike
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

fun getCategoryIconVector(categoryNameOrId: String): ImageVector {
  val clean = categoryNameOrId.trim().lowercase()
  return when {
    clean in listOf("religi", "agama") || clean.contains("agama") || clean.contains("religi") -> Icons.Outlined.AccountBalance
    clean == "alam" || clean.contains("alam") || clean.contains("outdoor") -> Icons.Outlined.Park
    clean == "amal" || clean.contains("amal") -> Icons.Outlined.FavoriteBorder
    clean == "bahasa" || clean.contains("bahasa") -> Icons.Outlined.Translate
    clean == "bazar" || clean.contains("bazar") -> Icons.Outlined.Storefront
    clean == "belanja" || clean.contains("belanja") -> Icons.Outlined.ShoppingBag
    clean == "bimbingan" || clean.contains("bimbingan") -> Icons.Outlined.Explore
    clean == "bisnis" || clean.contains("bisnis") -> Icons.Outlined.BusinessCenter
    clean == "budaya" || clean.contains("budaya") -> Icons.Outlined.Museum
    clean == "busana" || clean.contains("busana") -> Icons.Outlined.Checkroom
    clean == "diskusi" || clean.contains("diskusi") -> Icons.Outlined.Forum
    clean in listOf("esport", "e-sport", "e-sports") || clean.contains("esport") || clean.contains("gaming") -> Icons.Outlined.SportsEsports
    clean == "festival" || clean.contains("festival") -> Icons.Outlined.Celebration
    clean == "film" || clean.contains("film") || clean.contains("movie") -> Icons.Outlined.Theaters
    clean == "fotografi" || clean.contains("foto") || clean.contains("photography") -> Icons.Outlined.PhotoCamera
    clean == "gelarwicara" || clean.contains("wicara") || clean.contains("talkshow") -> Icons.Outlined.Mic
    clean == "hewan" || clean.contains("hewan") || clean.contains("pet") -> Icons.Outlined.Pets
    clean == "hiburan" || clean.contains("hiburan") -> Icons.Outlined.Attractions
    clean == "hobi" || clean.contains("hobi") -> Icons.Outlined.Extension
    clean == "identitas" || clean.contains("identitas") -> Icons.Outlined.Badge
    clean == "investasi" || clean.contains("investasi") -> Icons.AutoMirrored.Outlined.TrendingUp
    clean == "jejaring" || clean.contains("jejaring") || clean.contains("networking") -> Icons.Outlined.Hub
    clean == "karier" || clean.contains("karier") || clean.contains("karir") -> Icons.Outlined.WorkOutline
    clean == "kebugaran" || clean.contains("kebugaran") || clean.contains("fitness") || clean.contains("gym") -> Icons.Outlined.FitnessCenter
    clean == "kecantikan" || clean.contains("kecantikan") || clean.contains("beauty") -> Icons.Outlined.Face
    clean == "keluarga" || clean.contains("keluarga") || clean.contains("family") -> Icons.Outlined.FamilyRestroom
    clean == "kemah" || clean.contains("kemah") || clean.contains("camping") -> Icons.Outlined.Cabin
    clean == "kesehatan" || clean.contains("kesehatan") || clean.contains("health") -> Icons.Outlined.LocalHospital
    clean in listOf("kesejahteraan", "wellness") || clean.contains("kesejahteraan") || clean.contains("wellness") -> Icons.Outlined.Spa
    clean == "keuangan" || clean.contains("keuangan") || clean.contains("finance") -> Icons.Outlined.Payments
    clean == "komedi" || clean.contains("komedi") || clean.contains("comedy") -> Icons.Outlined.Mood
    clean == "komunitas" || clean.contains("komunitas") || clean.contains("community") -> Icons.Outlined.Groups
    clean == "konser" || clean.contains("konser") -> Icons.Outlined.MusicNote
    clean == "kopi" || clean.contains("kopi") || clean.contains("coffee") || clean.contains("cafe") -> Icons.Outlined.LocalCafe
    clean == "kriya" || clean.contains("kriya") || clean.contains("craft") -> Icons.Outlined.ContentCut
    clean == "kuliner" || clean.contains("kuliner") || clean.contains("food") || clean.contains("makan") -> Icons.Outlined.Restaurant
    clean == "lari" || clean.contains("lari") || clean.contains("run") -> Icons.AutoMirrored.Outlined.DirectionsRun
    clean == "lingkungan" || clean.contains("lingkungan") -> Icons.Outlined.Eco
    clean == "lokakarya" || clean.contains("lokakarya") || clean.contains("workshop") -> Icons.Outlined.Build
    clean == "media" || clean.contains("media") -> Icons.Outlined.Newspaper
    clean == "meditasi" || clean.contains("meditasi") -> Icons.Outlined.SelfImprovement
    clean == "menari" || clean.contains("menari") || clean.contains("dance") -> Icons.Outlined.Nightlife
    clean == "menulis" || clean.contains("menulis") || clean.contains("write") -> Icons.Outlined.Edit
    clean == "musik" || clean.contains("musik") || clean.contains("music") || clean.contains("karaoke") -> Icons.Outlined.MusicNote
    clean == "nongkrong" || clean.contains("nongkrong") -> Icons.Outlined.Weekend
    clean == "olahraga" || clean.contains("olahraga") || clean.contains("sport") -> Icons.Outlined.SportsSoccer
    clean == "otomotif" || clean.contains("otomotif") || clean.contains("auto") -> Icons.Outlined.DirectionsCar
    clean == "pameran" || clean.contains("pameran") || clean.contains("exhibition") -> Icons.Outlined.Collections
    clean in listOf("pelatihan", "pendidikan") || clean.contains("pelatihan") || clean.contains("pendidikan") || clean.contains("edukasi") -> Icons.Outlined.School
    clean == "pendakian" || clean.contains("pendakian") || clean.contains("hiking") -> Icons.Outlined.Terrain
    clean == "pengembangan" || clean.contains("pengembangan") -> Icons.Outlined.RocketLaunch
    clean == "perjalanan" || clean.contains("perjalanan") || clean.contains("trip") || clean.contains("travel") -> Icons.Outlined.Flight
    clean == "permainan" || clean.contains("permainan") || clean.contains("game") -> Icons.Outlined.Casino
    clean == "pesta" || clean.contains("pesta") || clean.contains("party") -> Icons.Outlined.Celebration
    clean == "petualangan" || clean.contains("petualangan") || clean.contains("adventure") -> Icons.Outlined.Explore
    clean == "piknik" || clean.contains("piknik") || clean.contains("picnic") -> Icons.Outlined.Deck
    clean in listOf("relaksasi", "spa") || clean.contains("relaksasi") || clean.contains("spa") -> Icons.Outlined.Spa
    clean == "relawan" || clean.contains("relawan") || clean.contains("volunteer") -> Icons.Outlined.VolunteerActivism
    clean == "sains" || clean.contains("sains") || clean.contains("science") -> Icons.Outlined.Science
    clean == "seminar" || clean.contains("seminar") -> Icons.Outlined.CoPresent
    clean == "seni" || clean.contains("seni") || clean.contains("art") -> Icons.Outlined.Palette
    clean == "sepeda" || clean.contains("sepeda") || clean.contains("bike") || clean.contains("cycling") -> Icons.AutoMirrored.Outlined.DirectionsBike
    clean == "sosial" || clean.contains("sosial") || clean.contains("social") -> Icons.Outlined.People
    clean == "spiritualitas" || clean.contains("spiritual") -> Icons.Outlined.AutoAwesome
    clean == "teater" || clean.contains("teater") || clean.contains("theatre") -> Icons.Outlined.TheaterComedy
    clean == "teknologi" || clean.contains("teknologi") || clean.contains("tech") -> Icons.Outlined.Computer
    clean == "wisata" || clean.contains("wisata") || clean.contains("tour") -> Icons.Outlined.BeachAccess
    clean == "yoga" || clean.contains("yoga") -> Icons.Outlined.SelfImprovement
    else -> Icons.Outlined.Explore
  }
}
