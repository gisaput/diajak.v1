sed -i 's/  Card(/  Column(/g' app/src/main/java/com/example/ui/screens/HomeScreen.kt
sed -i 's/    shape = RoundedCornerShape(16.dp),/    //shape = RoundedCornerShape(16.dp),/g' app/src/main/java/com/example/ui/screens/HomeScreen.kt
sed -i 's/    colors = CardDefaults.cardColors(containerColor = Color.White),/    //colors/g' app/src/main/java/com/example/ui/screens/HomeScreen.kt
sed -i 's/    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)/    //elevation/g' app/src/main/java/com/example/ui/screens/HomeScreen.kt
sed -i 's/  ) {/  ) {/g' app/src/main/java/com/example/ui/screens/HomeScreen.kt
sed -i 's/    Column(modifier = Modifier.fillMaxWidth()) {//g' app/src/main/java/com/example/ui/screens/HomeScreen.kt
