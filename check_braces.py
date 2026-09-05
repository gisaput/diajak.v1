def check():
    with open("app/src/main/java/com/example/ui/screens/MessagesScreen.kt", "r") as f:
        text = f.read()
    
    depth = 0
    lines = text.split('\n')
    for i, line in enumerate(lines):
        for char in line:
            if char == '{':
                depth += 1
            elif char == '}':
                depth -= 1
        
        if depth == 0 and "fun MessagesScreen" not in line and i > 60:
            print(f"Reached depth 0 at line {i+1}: {line}")

check()
