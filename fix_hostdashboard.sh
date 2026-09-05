sed -i 's/icon: String/icon: androidx.compose.ui.graphics.vector.ImageVector/g' app/src/main/java/com/example/ui/screens/HostDashboardScreen.kt
sed -i 's/Text(icon, fontSize = 24.sp)/Icon(imageVector = icon, contentDescription = label, modifier = Modifier.size(24.dp), tint = Color(0xFF2D3748))/g' app/src/main/java/com/example/ui/screens/HostDashboardScreen.kt

sed -i 's/ServiceGridItem(icon = "🎪"/ServiceGridItem(icon = Icons.Outlined.Festival/g' app/src/main/java/com/example/ui/screens/HostDashboardScreen.kt
sed -i 's/ServiceGridItem(icon = "📅"/ServiceGridItem(icon = Icons.Outlined.EventNote/g' app/src/main/java/com/example/ui/screens/HostDashboardScreen.kt
sed -i 's/ServiceGridItem(icon = "💰"/ServiceGridItem(icon = Icons.Outlined.AccountBalanceWallet/g' app/src/main/java/com/example/ui/screens/HostDashboardScreen.kt
sed -i 's/ServiceGridItem(icon = "📈"/ServiceGridItem(icon = Icons.Outlined.Insights/g' app/src/main/java/com/example/ui/screens/HostDashboardScreen.kt
sed -i 's/ServiceGridItem(icon = "🎫"/ServiceGridItem(icon = Icons.Outlined.ConfirmationNumber/g' app/src/main/java/com/example/ui/screens/HostDashboardScreen.kt
sed -i 's/ServiceGridItem(icon = "📞"/ServiceGridItem(icon = Icons.Outlined.SupportAgent/g' app/src/main/java/com/example/ui/screens/HostDashboardScreen.kt

# Replace other emojis with icons
sed -i 's/Text("⚠️", fontSize = 18.sp)/Icon(Icons.Outlined.WarningAmber, contentDescription = null, tint = Color(0xFF9B2C2C))/g' app/src/main/java/com/example/ui/screens/HostDashboardScreen.kt
sed -i 's/Text("🎁", fontSize = 18.sp)/Icon(Icons.Outlined.CardGiftcard, contentDescription = null, tint = Color(0xFF805E2A))/g' app/src/main/java/com/example/ui/screens/HostDashboardScreen.kt
sed -i 's/Text("👤", fontSize = 18.sp)/Icon(Icons.Outlined.PersonOutline, contentDescription = null, tint = Color(0xFF1E232A))/g' app/src/main/java/com/example/ui/screens/HostDashboardScreen.kt
sed -i 's/Text("🎪", fontSize = 64.sp)/Icon(Icons.Outlined.Festival, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color(0xFFA0AEC0))/g' app/src/main/java/com/example/ui/screens/HostDashboardScreen.kt
sed -i 's/Text("📅", fontSize = 64.sp)/Icon(Icons.Outlined.EventNote, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color(0xFFA0AEC0))/g' app/src/main/java/com/example/ui/screens/HostDashboardScreen.kt
sed -i 's/Text("💰", fontSize = 64.sp)/Icon(Icons.Outlined.AccountBalanceWallet, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color(0xFFA0AEC0))/g' app/src/main/java/com/example/ui/screens/HostDashboardScreen.kt
sed -i 's/Text("💳", fontSize = 64.sp)/Icon(Icons.Outlined.CreditCard, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color(0xFFA0AEC0))/g' app/src/main/java/com/example/ui/screens/HostDashboardScreen.kt
sed -i 's/Text("📈", fontSize = 64.sp)/Icon(Icons.Outlined.Insights, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color(0xFFA0AEC0))/g' app/src/main/java/com/example/ui/screens/HostDashboardScreen.kt
sed -i 's/Text("🎫", fontSize = 64.sp)/Icon(Icons.Outlined.ConfirmationNumber, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color(0xFFA0AEC0))/g' app/src/main/java/com/example/ui/screens/HostDashboardScreen.kt
