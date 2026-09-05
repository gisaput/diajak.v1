import re

with open("app/src/main/java/com/example/ui/screens/MessagesScreen.kt", "r") as f:
    content = f.read()

if "import dev.chrisbanes.haze.HazeState" not in content:
    content = content.replace("import androidx.compose.ui.text.TextStyle", "import androidx.compose.ui.text.TextStyle\nimport dev.chrisbanes.haze.HazeState\nimport dev.chrisbanes.haze.hazeSource\nimport com.example.ui.components.DiajakGlassHeader")

start_str = """  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
  ) {"""

end_str = """  var chatMessages by remember { mutableStateOf(listOf<String>()) }"""

start_index = content.find(start_str)
end_index = content.find(end_str)

if start_index != -1 and end_index != -1:
    block = content[start_index + len(start_str):end_index]
    
    header_start = block.find("    // Header Section")
    header_end = block.find("    if (filteredNotifications.isEmpty() && filteredThreads.isEmpty()) {")
    
    header_code = block[header_start:header_end]
    rest_code = block[header_end:]
    
    new_root = """  val hazeState = remember { HazeState() }

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
      // Push content down to avoid overlapping the glass header
      Spacer(modifier = Modifier.statusBarsPadding().height(145.dp))
""" + rest_code + """    }

    DiajakGlassHeader(
      hazeState = hazeState,
      modifier = Modifier.align(Alignment.TopCenter)
    ) {
""" + header_code + """
    }
  }

"""
    content = content[:start_index] + new_root + content[end_index:]
    
    with open("app/src/main/java/com/example/ui/screens/MessagesScreen.kt", "w") as f:
        f.write(content)
    print("Successfully replaced content.")
else:
    print("Could not find blocks.")

