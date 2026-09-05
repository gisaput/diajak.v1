#!/bin/bash
for file in app/src/main/java/com/example/ui/screens/*.kt; do
    # Remove all lines that exactly match `import androidx.compose.material3.MaterialTheme`
    sed -i '/import androidx.compose.material3.MaterialTheme/d' "$file"
    # Now insert it exactly once, after `import com.example.ui.theme.spacing`
    sed -i '/import com.example.ui.theme.spacing/a \
import androidx.compose.material3.MaterialTheme' "$file"
done
