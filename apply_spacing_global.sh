#!/bin/bash
for file in app/src/main/java/com/example/ui/screens/*.kt; do
    # Ensure import is present if we use MaterialTheme.spacing
    if ! grep -q "import com.example.ui.theme.spacing" "$file"; then
        sed -i '/package com.example.ui.screens/a \
import com.example.ui.theme.spacing\
import androidx.compose.material3.MaterialTheme' "$file"
    fi

    # Spacers
    sed -i 's/Spacer(modifier = Modifier.height(32.dp))/Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))/g' "$file"
    sed -i 's/Spacer(modifier = Modifier.height(24.dp))/Spacer(modifier = Modifier.height(MaterialTheme.spacing.section))/g' "$file"
    sed -i 's/Spacer(modifier = Modifier.height(16.dp))/Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))/g' "$file"
    sed -i 's/Spacer(modifier = Modifier.height(12.dp))/Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))/g' "$file"
    sed -i 's/Spacer(modifier = Modifier.height(8.dp))/Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))/g' "$file"
    sed -i 's/Spacer(modifier = Modifier.height(4.dp))/Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))/g' "$file"

    sed -i 's/Spacer(modifier = Modifier.width(24.dp))/Spacer(modifier = Modifier.width(MaterialTheme.spacing.large))/g' "$file"
    sed -i 's/Spacer(modifier = Modifier.width(16.dp))/Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))/g' "$file"
    sed -i 's/Spacer(modifier = Modifier.width(12.dp))/Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))/g' "$file"
    sed -i 's/Spacer(modifier = Modifier.width(8.dp))/Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))/g' "$file"
    sed -i 's/Spacer(modifier = Modifier.width(4.dp))/Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))/g' "$file"

    # Exact padding matches
    sed -i 's/padding(32.dp)/padding(MaterialTheme.spacing.extraLarge)/g' "$file"
    sed -i 's/padding(24.dp)/padding(MaterialTheme.spacing.large)/g' "$file"
    sed -i 's/padding(16.dp)/padding(MaterialTheme.spacing.medium)/g' "$file"
    sed -i 's/padding(12.dp)/padding(MaterialTheme.spacing.medium)/g' "$file"
    sed -i 's/padding(8.dp)/padding(MaterialTheme.spacing.small)/g' "$file"
    sed -i 's/padding(4.dp)/padding(MaterialTheme.spacing.extraSmall)/g' "$file"
    
    # Horizontal padding
    sed -i 's/horizontal = 32.dp/horizontal = MaterialTheme.spacing.extraLarge/g' "$file"
    sed -i 's/horizontal = 24.dp/horizontal = MaterialTheme.spacing.screenMargin/g' "$file"
    sed -i 's/horizontal = 16.dp/horizontal = MaterialTheme.spacing.screenMargin/g' "$file"
    sed -i 's/horizontal = 12.dp/horizontal = MaterialTheme.spacing.screenMargin/g' "$file"
    sed -i 's/horizontal = 8.dp/horizontal = MaterialTheme.spacing.small/g' "$file"
    
    # Vertical padding
    sed -i 's/vertical = 32.dp/vertical = MaterialTheme.spacing.extraLarge/g' "$file"
    sed -i 's/vertical = 24.dp/vertical = MaterialTheme.spacing.section/g' "$file"
    sed -i 's/vertical = 16.dp/vertical = MaterialTheme.spacing.medium/g' "$file"
    sed -i 's/vertical = 12.dp/vertical = MaterialTheme.spacing.medium/g' "$file"
    sed -i 's/vertical = 8.dp/vertical = MaterialTheme.spacing.small/g' "$file"
    sed -i 's/vertical = 4.dp/vertical = MaterialTheme.spacing.extraSmall/g' "$file"
    
    # Top padding
    sed -i 's/top = 32.dp/top = MaterialTheme.spacing.extraLarge/g' "$file"
    sed -i 's/top = 24.dp/top = MaterialTheme.spacing.section/g' "$file"
    sed -i 's/top = 16.dp/top = MaterialTheme.spacing.medium/g' "$file"
    sed -i 's/top = 12.dp/top = MaterialTheme.spacing.medium/g' "$file"
    sed -i 's/top = 8.dp/top = MaterialTheme.spacing.small/g' "$file"
    sed -i 's/top = 4.dp/top = MaterialTheme.spacing.extraSmall/g' "$file"
    
    # Bottom padding
    sed -i 's/bottom = 32.dp/bottom = MaterialTheme.spacing.extraLarge/g' "$file"
    sed -i 's/bottom = 24.dp/bottom = MaterialTheme.spacing.section/g' "$file"
    sed -i 's/bottom = 16.dp/bottom = MaterialTheme.spacing.medium/g' "$file"
    sed -i 's/bottom = 12.dp/bottom = MaterialTheme.spacing.medium/g' "$file"
    sed -i 's/bottom = 8.dp/bottom = MaterialTheme.spacing.small/g' "$file"
    sed -i 's/bottom = 4.dp/bottom = MaterialTheme.spacing.extraSmall/g' "$file"

    # Start and End padding
    sed -i 's/start = 24.dp/start = MaterialTheme.spacing.screenMargin/g' "$file"
    sed -i 's/start = 16.dp/start = MaterialTheme.spacing.screenMargin/g' "$file"
    sed -i 's/start = 12.dp/start = MaterialTheme.spacing.screenMargin/g' "$file"
    sed -i 's/start = 8.dp/start = MaterialTheme.spacing.small/g' "$file"
    
    sed -i 's/end = 24.dp/end = MaterialTheme.spacing.screenMargin/g' "$file"
    sed -i 's/end = 16.dp/end = MaterialTheme.spacing.screenMargin/g' "$file"
    sed -i 's/end = 12.dp/end = MaterialTheme.spacing.screenMargin/g' "$file"
    sed -i 's/end = 8.dp/end = MaterialTheme.spacing.small/g' "$file"
    
    # Arrangement spacedBy
    sed -i 's/spacedBy(32.dp)/spacedBy(MaterialTheme.spacing.extraLarge)/g' "$file"
    sed -i 's/spacedBy(24.dp)/spacedBy(MaterialTheme.spacing.large)/g' "$file"
    sed -i 's/spacedBy(16.dp)/spacedBy(MaterialTheme.spacing.medium)/g' "$file"
    sed -i 's/spacedBy(12.dp)/spacedBy(MaterialTheme.spacing.medium)/g' "$file"
    sed -i 's/spacedBy(8.dp)/spacedBy(MaterialTheme.spacing.small)/g' "$file"
    sed -i 's/spacedBy(4.dp)/spacedBy(MaterialTheme.spacing.extraSmall)/g' "$file"

done
