with open("app/src/main/java/com/example/ui/screens/BookingsScreen.kt", "r") as f:
    text = f.read()

count = 0
result = []
for i, char in enumerate(text):
    if char == '{':
        count += 1
    elif char == '}':
        count -= 1
    result.append(char)
    if count == 0 and "fun BookingsScreen" in "".join(result) and "fun UndanganDetailDialog" in "".join(result):
        # wait, if count is 0 and both functions have been seen, we reached the end of the last top-level function!
        pass

# Actually, counting isn't foolproof if there are braces in strings, but we don't have strings with unbalanced braces.
