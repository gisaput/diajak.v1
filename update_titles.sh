#!/bin/bash
# Profile and Info Screens
sed -i 's/text = "Ubah Profil",\n          fontSize = 14.sp,/text = "Ubah Profil",\n          fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/EditProfileScreen.kt
sed -i 's/text = "Tentang Aplikasi",\n          fontSize = 14.sp,/text = "Tentang Aplikasi",\n          fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/AboutDiajakScreen.kt
sed -i 's/text = "Pusat Bantuan \/ FAQ",\n          fontSize = 14.sp,/text = "Pusat Bantuan \/ FAQ",\n          fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/HelpCenterScreen.kt
sed -i 's/text = "Kebijakan Privasi",\n          fontSize = 14.sp,/text = "Kebijakan Privasi",\n          fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/PrivacyPolicyScreen.kt
sed -i 's/text = "Syarat & Ketentuan",\n          fontSize = 14.sp,/text = "Syarat & Ketentuan",\n          fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/TermsAndConditionsScreen.kt

# Bookings & Favorites
sed -i 's/text = "Undangan Acara",\n        fontSize = 14.sp,/text = "Undangan Acara",\n        fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/BookingsScreen.kt
sed -i 's/text = "Acara Favorit",\n        fontSize = 14.sp,/text = "Acara Favorit",\n        fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/FavoritesScreen.kt

# Messages
sed -i 's/text = if (isKreatorMode) "Kelola Pesan" else "Pesan Masuk",\n            fontSize = 14.sp,/text = if (isKreatorMode) "Kelola Pesan" else "Pesan Masuk",\n            fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/MessagesScreen.kt

# Creator Dashboard Screens
sed -i 's/text = "Halaman Kreator",\n              fontSize = 14.sp,/text = "Halaman Kreator",\n              fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/CreatorDashboardScreen.kt
sed -i 's/text = "Tambah Acara",\n              fontWeight = FontWeight.Medium,\n              fontSize = 14.sp,/text = "Tambah Acara",\n              fontWeight = FontWeight.Medium,\n              fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/CreatorDashboardScreen.kt
sed -i 's/text = "Tambah Acara",\n          fontSize = 14.sp,/text = "Tambah Acara",\n          fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/CreatorDashboardScreen.kt
sed -i 's/text = "Tentukan Benefit",\n          fontSize = 14.sp,/text = "Tentukan Benefit",\n          fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/CreatorDashboardScreen.kt
sed -i 's/text = "Tentukan Jadwal",\n        fontSize = 14.sp,/text = "Tentukan Jadwal",\n        fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/CreatorDashboardScreen.kt
sed -i 's/text = "Tentukan Kategori",\n        fontSize = 14.sp,/text = "Tentukan Kategori",\n        fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/CreatorDashboardScreen.kt
sed -i 's/text = "Kelola Pendaftaran",\n        fontSize = 14.sp,/text = "Kelola Pendaftaran",\n        fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/CreatorDashboardScreen.kt
sed -i 's/text = "Kelola Saldo",\n        fontSize = 14.sp,/text = "Kelola Saldo",\n        fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/CreatorDashboardScreen.kt
sed -i 's/text = "Tarik Saldo",\n        fontSize = 14.sp,/text = "Tarik Saldo",\n        fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/CreatorDashboardScreen.kt
sed -i 's/text = "Kelola Ulasan",\n        fontSize = 14.sp,/text = "Kelola Ulasan",\n        fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/CreatorDashboardScreen.kt
sed -i 's/text = "Voucher Diskon",\n        fontSize = 14.sp,/text = "Voucher Diskon",\n        fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/CreatorDashboardScreen.kt
sed -i 's/text = "Buat Voucher",\n        fontSize = 14.sp,/text = "Buat Voucher",\n        fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/CreatorDashboardScreen.kt

# Dialogs
sed -i 's/Text("Buat Acara Baru", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)/Text("Buat Acara Baru", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)/g' app/src/main/java/com/example/ui/screens/CreateEventDialog.kt
sed -i 's/text = "Gabung Jadi Kreator",\n          fontSize = 14.sp,/text = "Gabung Jadi Kreator",\n          fontSize = 16.sp,/g' app/src/main/java/com/example/ui/screens/CreatorRegistrationDialog.kt
