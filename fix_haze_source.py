import re

# Fix PrivacyPolicyScreen: Move hazeSource down into the scrolling Column!
with open("app/src/main/java/com/example/ui/screens/PrivacyPolicyScreen.kt", "r") as f:
    text = f.read()

text = text.replace(
"""    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5E7EB)) // Standard Diajak background
            .hazeSource(state = hazeState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),""",
"""    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5E7EB)) // Standard Diajak background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState)
                .verticalScroll(scrollState),"""
)
with open("app/src/main/java/com/example/ui/screens/PrivacyPolicyScreen.kt", "w") as f:
    f.write(text)

# Fix HomeScreen: Move hazeSource down into the scrolling LazyColumn!
with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    text = f.read()

text = text.replace(
"""  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
      .pointerInput(Unit) {
        detectTapGestures(onTap = {
          focusManager.clearFocus()
        })
      }
      .hazeSource(state = hazeState)
  ) {
    LazyColumn(
      state = lazyListState,
      modifier = Modifier
        .fillMaxSize(),""",
"""  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
      .pointerInput(Unit) {
        detectTapGestures(onTap = {
          focusManager.clearFocus()
        })
      }
  ) {
    LazyColumn(
      state = lazyListState,
      modifier = Modifier
        .fillMaxSize()
        .hazeSource(state = hazeState),"""
)
with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(text)
