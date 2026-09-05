import re

with open("app/src/main/java/com/example/ui/screens/MessagesScreen.kt", "r") as f:
    text = f.read()

text = text.replace("// Clean helper to decide border outlines", "}\n\n// Clean helper to decide border outlines")

with open("app/src/main/java/com/example/ui/screens/MessagesScreen.kt", "w") as f:
    f.write(text)
