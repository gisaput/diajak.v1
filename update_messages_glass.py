import re

with open("app/src/main/java/com/example/ui/screens/MessagesScreen.kt", "r") as f:
    text = f.read()

# Make sure dev.chrisbanes.haze is imported
if "import dev.chrisbanes.haze.HazeState" not in text:
    text = text.replace("import androidx.compose.ui.text.TextStyle", "import androidx.compose.ui.text.TextStyle\nimport dev.chrisbanes.haze.HazeState\nimport dev.chrisbanes.haze.hazeSource\n")

# Replace root Column with Box + Haze setup
root_column_target = """  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
  ) {"""
root_box_replacement = """  val hazeState = remember { HazeState() }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState)
    ) {
      Spacer(modifier = Modifier.statusBarsPadding().height(130.dp))"""

text = text.replace(root_column_target, root_box_replacement)

# Move the Header Section inside DiajakGlassHeader at the end of the Box
# We need to extract the Header Section and append it at the end of the Box.
# The Header Section starts right after the root column start (which is now root_box_replacement).
header_start_index = text.find("    // Header Section")
header_end_index = text.find("    if (filteredNotifications.isEmpty() && filteredThreads.isEmpty())")

header_code = text[header_start_index:header_end_index]
# Remove header_code from its original place
text = text[:header_start_index] + text[header_end_index:]

# The Box ends at the very end of MessagesScreen, right before `var selectedThread` or other Composable starts.
# Actually, `MessagesScreen` ends around line 500-600.
# Let's find the closing brace of MessagesScreen.
# In `MessagesScreen`, we have an inner `if (selectedThread != null)`... wait, we need to be careful not to break other states like `ChatDetailScreen`.

pass
