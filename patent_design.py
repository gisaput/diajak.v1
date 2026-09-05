with open('AGENTS.md', 'r') as f:
    content = f.read()

insertion = """- **Blur Intensity (Header)**: `blurRadius = 24.dp`
- **Progressive Fade Gradient (CRITICAL PATTERN)**: The glass effect MUST use `graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }` and `drawWithContent` with a `Brush.verticalGradient` using `BlendMode.DstIn` to create a smooth transition to transparent. The exact alpha stops (0.0f, 0.3f, 0.5f, 0.7f, 0.85f, 1.0f) MUST be strictly preserved to avoid horizontal banding.

### Core Component Implementation (`DiajakGlassHeader`):
```kotlin
@Composable
fun DiajakGlassHeader(
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        // Layer 1: Haze Glass Effect with Extra Smooth Progressive Fade
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            0.0f to Color.Black,
                            0.3f to Color.Black.copy(alpha = 0.9f),
                            0.5f to Color.Black.copy(alpha = 0.7f),
                            0.7f to Color.Black.copy(alpha = 0.4f),
                            0.85f to Color.Black.copy(alpha = 0.15f),
                            1.0f to Color.Transparent
                        ),
                        blendMode = BlendMode.DstIn
                    )
                }
                .hazeEffect(
                    state = hazeState,
                    style = HazeStyle(
                        backgroundColor = Color.Transparent,
                        tint = HazeTint(DiajakGlassConfig.BaseColor.copy(alpha = 0.65f)),
                        blurRadius = 24.dp,
                        noiseFactor = 0f
                    )
                )
        )
        // Layer 2: Content Layer
        content()
    }
}
```

### Standard Header Implementation (Template):"""

content = content.replace(
    "- **Blur Intensity (Header)**: `blurRadius = 24.dp`\n\n### Standard Header Implementation (Template):",
    insertion
)

with open('AGENTS.md', 'w') as f:
    f.write(content)
