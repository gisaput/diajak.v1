#!/bin/bash
for file in app/src/main/java/com/example/ui/screens/*.kt; do
    if grep -A 3 "modifier = Modifier.statusBarsPadding()" "$file" | grep -q "Text"; then
        echo -n "$file: "
        grep -A 5 "modifier = Modifier.statusBarsPadding()" "$file" | grep "text =" | sed 's/^[ \t]*//'
    fi
    if grep -A 3 "// Header" "$file" | grep -q "Text"; then
        echo -n "$file: "
        grep -A 8 "// Header" "$file" | grep "text =" | sed 's/^[ \t]*//'
    fi
done
