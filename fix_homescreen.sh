sed -i 's/\.shadow(elevation = 4\.dp, shape = RoundedCornerShape(24\.dp))/\.shadow(elevation = 6\.dp, shape = RoundedCornerShape(50))/g' app/src/main/java/com/example/ui/screens/HomeScreen.kt
sed -i 's/\.border(/ \/\/\.border(/g' app/src/main/java/com/example/ui/screens/HomeScreen.kt
sed -i 's/shape = RoundedCornerShape(24\.dp)/shape = RoundedCornerShape(50)/g' app/src/main/java/com/example/ui/screens/HomeScreen.kt
