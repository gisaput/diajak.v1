import re

with open('app/src/main/java/com/example/ui/screens/BookingsScreen.kt', 'r') as f:
    content = f.read()

# Fix padding(horizontal = 20.dp, bottom = 100.dp) -> padding(start = 20.dp, top = 0.dp, end = 20.dp, bottom = 100.dp)
content = content.replace('.padding(horizontal = 20.dp, bottom = 100.dp)', '.padding(start = 20.dp, top = 0.dp, end = 20.dp, bottom = 100.dp)')

# Add import androidx.compose.ui.draw.drawWithContent if missing
if 'import androidx.compose.ui.draw.drawWithContent' not in content:
    content = content.replace('import androidx.compose.ui.Modifier', 'import androidx.compose.ui.Modifier\nimport androidx.compose.ui.draw.drawWithContent')

# Fix unresolved reference size.height inside drawWithContent inside dialog.txt
# wait, in the dialog:
#             .drawWithContent {
#               drawContent()
#               val notchY = size.height * 0.65f
# ... this size is inside graphicsLayer? No, drawWithContent receives DrawScope, which has size.
# Wait, why did it complain?
# "function invocation 'size(...)' expected."
# Because we imported androidx.compose.ui.geometry.Size, which is a class, and in the scope of Modifier, it might be confused.
# Wait! In my file I imported `androidx.compose.ui.geometry.Size`.
# But `DrawScope.size` is a property `androidx.compose.ui.geometry.Size`. So it shouldn't say "function invocation 'size(...)' expected".
# It says that when `drawWithContent` is NOT resolved!
# Yes, because `drawWithContent` was not imported, so the lambda was just a normal lambda, so `size` resolved to the `Size` class!
# Once we import `drawWithContent`, the lambda becomes `DrawScope.() -> Unit`, so `size` resolves to the property.

# Fix extra brackets at the end.
# We had 4 brackets at the end. We only need 1 for the end of UndanganDetailDialog function.
content = re.sub(r'\}\s*\}\s*\}\s*\}\s*$', '}\n', content)

with open('app/src/main/java/com/example/ui/screens/BookingsScreen.kt', 'w') as f:
    f.write(content)
