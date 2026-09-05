import re

with open("app/src/main/java/com/example/ui/screens/MessagesScreen.kt", "r") as f:
    text = f.read()

# Replace LazyColumn content padding back to normal (remove the top padding)
lazy_column_target = """      LazyColumn(
        contentPadding = PaddingValues(top = 165.dp, bottom = if (isKreatorMode) 20.dp else 100.dp),"""
lazy_column_replacement = """      LazyColumn(
        contentPadding = PaddingValues(bottom = if (isKreatorMode) 20.dp else 100.dp),"""
text = text.replace(lazy_column_target, lazy_column_replacement)

# Add the item Spacer
item_spacer = """      LazyColumn(
        contentPadding = PaddingValues(bottom = if (isKreatorMode) 20.dp else 100.dp),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        modifier = Modifier
          .weight(1f)
          .background(Color.Transparent)
      ) {
        item { Spacer(modifier = Modifier.statusBarsPadding().height(145.dp)) }
"""
lazy_column_full_target = """      LazyColumn(
        contentPadding = PaddingValues(bottom = if (isKreatorMode) 20.dp else 100.dp),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        modifier = Modifier
          .weight(1f)
          .background(Color.Transparent)
      ) {"""
text = text.replace(lazy_column_full_target, item_spacer)

# Empty state padding is fine, it just uses `statusBarsPadding()` and `padding(top = 145.dp)`.
with open("app/src/main/java/com/example/ui/screens/MessagesScreen.kt", "w") as f:
    f.write(text)
