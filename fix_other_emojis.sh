# Fix in BookingsScreen
sed -i 's/Text(/Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Rounded.LocationOn, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color(0xFF718096)); Spacer(modifier = Modifier.width(4.dp)); Text(/g' app/src/main/java/com/example/ui/screens/BookingsScreen.kt
sed -i 's/text = "📍 ${booking.locationName}"/text = "${booking.locationName}"/g' app/src/main/java/com/example/ui/screens/BookingsScreen.kt
sed -i 's/color = Color(0xFF718096)\n                  )/color = Color(0xFF718096)\n                  ) }/g' app/src/main/java/com/example/ui/screens/BookingsScreen.kt

sed -i 's/Text(/Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Rounded.Schedule, contentDescription = null, modifier = Modifier.size(11.dp), tint = Color(0xFF718096)); Spacer(modifier = Modifier.width(4.dp)); Text(/g' app/src/main/java/com/example/ui/screens/BookingsScreen.kt
sed -i 's/text = "🕒 ${booking.schedule}"/text = "${booking.schedule}"/g' app/src/main/java/com/example/ui/screens/BookingsScreen.kt
