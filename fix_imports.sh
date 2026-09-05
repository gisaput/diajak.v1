#!/bin/bash
for file in app/src/main/java/com/example/ui/screens/*.kt; do
    # Remove duplicate MaterialTheme imports
    # Since they might be exactly the same line, let's just sort and uniq the imports block, or simply delete the second occurrence?
    # Better: just delete all instances of `import androidx.compose.material3.MaterialTheme` and then add exactly one at the top of the imports block.
    
    # Or just use an awk script to remove duplicates.
    awk '!seen[$0] || !/^import /' "$file" > "$file.tmp" && mv "$file.tmp" "$file"
done
