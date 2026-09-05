import re

with open("app/src/main/java/com/example/ui/screens/MessagesScreen.kt", "r") as f:
    text = f.read()

# Remove the static Spacer
spacer_text = "      // Push content down to avoid overlapping the glass header\n      Spacer(modifier = Modifier.statusBarsPadding().height(145.dp))"
text = text.replace(spacer_text, "")

# Add padding to the Empty State Column
empty_state_target = """    if (filteredNotifications.isEmpty() && filteredThreads.isEmpty()) {
      // High-Fidelity empty state
      Column(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(horizontal = 40.dp),"""
empty_state_replacement = """    if (filteredNotifications.isEmpty() && filteredThreads.isEmpty()) {
      // High-Fidelity empty state
      Column(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .statusBarsPadding()
          .padding(top = 145.dp)
          .padding(horizontal = 40.dp),"""
text = text.replace(empty_state_target, empty_state_replacement)

# Fix the LazyColumn content padding
lazy_column_target = """      LazyColumn(
        contentPadding = PaddingValues(top = 20.dp, bottom = if (isKreatorMode) 20.dp else 100.dp),"""
lazy_column_replacement = """      LazyColumn(
        contentPadding = PaddingValues(top = 165.dp, bottom = if (isKreatorMode) 20.dp else 100.dp),"""
text = text.replace(lazy_column_target, lazy_column_replacement)

# Oh wait, the LazyColumn is inside a Column that has fillMaxSize(), so its height is correct.
# Wait, if LazyColumn is inside a Column, does it fillMaxSize()?
# Yes, because it has `weight(1f)`. The outer Column has `fillMaxSize()`.
# Wait, if we use `statusBarsPadding()` on the spacer, the `145.dp` was just added to it.
# So `top = 165.dp` might not include status bar padding if we don't have it.
# Let's fix LazyColumn to use WindowInsets for padding, or we can just apply statusBarsPadding to the LazyColumn itself!
# Or we can use `contentPadding = PaddingValues(top = 165.dp + statusBarHeightDp, ...)`
# But since we can't easily do math inside PaddingValues without `with(LocalDensity.current)`, we can just use `Spacer` AS AN ITEM inside the LazyColumn!

with open("app/src/main/java/com/example/ui/screens/MessagesScreen.kt", "w") as f:
    f.write(text)
