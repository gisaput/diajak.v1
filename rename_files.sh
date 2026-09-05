#!/bin/bash

# Rename files
mv app/src/main/java/com/example/model/AcaraModels.kt app/src/main/java/com/example/model/EventModels.kt
mv app/src/main/java/com/example/ui/screens/CreateAcaraDialog.kt app/src/main/java/com/example/ui/screens/CreateEventDialog.kt
mv app/src/main/java/com/example/ui/screens/KreatorDashboardScreen.kt app/src/main/java/com/example/ui/screens/CreatorDashboardScreen.kt
mv app/src/main/java/com/example/ui/screens/KreatorRegistrationDialog.kt app/src/main/java/com/example/ui/screens/CreatorRegistrationDialog.kt
mv app/src/main/java/com/example/ui/screens/UndanganDetailOverlay.kt app/src/main/java/com/example/ui/screens/InvitationDetailOverlay.kt

# Replace class/file references inside the codebase
# (AcaraModels has no class AcaraModels inside, but just in case, wait, it has data classes like AcaraModel, but let's just replace the specific file/class names)

find app/src/main/java -name "*.kt" -type f -exec sed -i 's/CreateAcaraDialog/CreateEventDialog/g' {} +
find app/src/main/java -name "*.kt" -type f -exec sed -i 's/KreatorDashboardScreen/CreatorDashboardScreen/g' {} +
find app/src/main/java -name "*.kt" -type f -exec sed -i 's/KreatorRegistrationDialog/CreatorRegistrationDialog/g' {} +
find app/src/main/java -name "*.kt" -type f -exec sed -i 's/UndanganDetailOverlay/InvitationDetailOverlay/g' {} +

# Notice that AcaraModels.kt mostly contains data classes, not a class named AcaraModels. We won't rename the data class inside it right now as user specifically asked to rename AcaraModels.kt -> EventModels.kt and just renaming file.

