import re

with open('app/src/main/java/com/example/ui/components/DiajakGlassHeader.kt', 'r') as f:
    content = f.read()

# Replace the single-layer haze with the progressive haze
new_haze = """        // Layer 1: Haze Glass Effect with Progressive Fade
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            0.0f to Color.Black,
                            0.6f to Color.Black,
                            0.8f to Color.Black.copy(alpha = 0.6f),
                            0.95f to Color.Black.copy(alpha = 0.2f),
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
        )"""

content = re.sub(r'        // Layer 1: Haze Glass Effect \(Uniform blur and tint, no gradient fade\).*?\.hazeEffect\([^)]+\)\n\s*\)', new_haze, content, flags=re.DOTALL)

with open('app/src/main/java/com/example/ui/components/DiajakGlassHeader.kt', 'w') as f:
    f.write(content)
