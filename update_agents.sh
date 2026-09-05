#!/bin/bash
awk '
BEGIN { skip=0 }
/^## Top Bar \/ Header Smooth Gradient Fade Rule/ { skip=1 }
{ if (!skip) print $0 }
' AGENTS.md > AGENTS_new.md

cat << 'EOF2' >> AGENTS_new.md
## iOS-Style White Progressive Glass Header (3-Layer Glass)
To maintain visual consistency and a premium feel, **ALL Top App Bars overlaying scrollable content MUST use the 3-Layer iOS Progressive Glass Header implementation**, exactly as configured in `PrivacyPolicyScreen` and `DetailScreen`.

**Crucial Constraints:**
- The background of the screen itself remains Gray (`Color(0xFFE5E7EB)`), but the Header Glass and Solid Overlay MUST be pure White (`Color.White`), regardless of the screen background.
- The overall height of the content layer MUST be exactly `56.dp` with horizontal padding of `16.dp` (Instagram style).
- The `Spacer` pushing the scrollable content below the header must be exactly `56.dp`.
- DO NOT add extra vertical padding to the content layer.

### Standard 3-Layer Header Implementation:
```kotlin
val scrollState = rememberScrollState()
val hazeState = remember { HazeState() }

Box(
    modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFE5E7EB)) // Standard Diajak background
        .haze(state = hazeState)
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
    ) {
        Spacer(modifier = Modifier.statusBarsPadding().height(56.dp)) // MUST be 56.dp
        // Content goes here...
    }

    // Floating Top Bar (Progressive Glass)
    Box(
        modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth()
    ) {
        // Layer 1: Haze Glass Effect with DstIn Mask (Fading bottom)
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Black, Color.Black.copy(alpha = 0.8f), Color.Transparent),
                            startY = size.height * 0.4f,
                            endY = size.height
                        ),
                        blendMode = BlendMode.DstIn
                    )
                }
                .hazeChild(
                    state = hazeState,
                    style = HazeStyle(backgroundColor = Color.White, tint = HazeTint(Color.White.copy(alpha = 0.7f)), blurRadius = 24.dp)
                )
        )

        // Layer 2: Solid Tint Overlay (Makes the top very solid to hide text in emulator)
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White, Color.White.copy(alpha = 0.95f), Color.Transparent)
                    )
                )
        )

        // Layer 3: Content Layer (Buttons and Title)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
                .height(56.dp)
        ) {
            // See Frosted Glass Circular Buttons below for IconButton
            // Title goes here...
        }
    }
}
```

## Frosted Glass Circular Buttons
Any circular icon button overlaid on content (like the Back or Share button in the header) MUST use the following precise glass styling:
- **Shape**: `CircleShape`
- **Background**: `Color.Black.copy(alpha = 0.03f)`
- **Border**: `0.5.dp, Color.Black.copy(alpha = 0.08f)`
- **HazeChild**: Uses a standalone haze effect.

### Standard Glass Button Implementation:
```kotlin
IconButton(
    onClick = { /* action */ },
    modifier = Modifier
        .size(40.dp)
        .clip(CircleShape)
        .background(Color.Black.copy(alpha = 0.03f))
        .hazeChild(
            state = hazeState,
            style = HazeStyle(
                backgroundColor = Color(0xFFF5F5F5),
                tint = HazeTint(Color.White.copy(alpha = 0.6f)),
                blurRadius = 12.dp
            )
        )
        .border(0.5.dp, Color.Black.copy(alpha = 0.08f), CircleShape)
) {
    Icon(
        imageVector = Icons.AutoMirrored.Outlined.ArrowBack, // Or other icon
        contentDescription = "Icon",
        tint = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.size(24.dp)
    )
}
```
EOF2

mv AGENTS_new.md AGENTS.md
rm update_agents.sh