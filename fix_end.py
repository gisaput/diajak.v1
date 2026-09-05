import re
with open("app/src/main/java/com/example/ui/screens/BookingsScreen.kt", "r") as f:
    content = f.read()

# find the last Spacer
match = re.search(r'Spacer\(modifier = Modifier\.height\(20\.dp\)\)[\s\}]+$', content)
if match:
    content = content[:match.start()] + "Spacer(modifier = Modifier.height(20.dp))\n      }\n    }\n  }\n}\n"
    with open("app/src/main/java/com/example/ui/screens/BookingsScreen.kt", "w") as f:
        f.write(content)
