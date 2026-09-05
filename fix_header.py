import re

with open("app/src/main/java/com/example/ui/components/DiajakGlassHeader.kt", "r") as f:
    text = f.read()

# Fix BaseColor alpha in Layer 2 (make sure it's not 100% opaque at the top)
text = text.replace(
"""                        0.0f to DiajakGlassConfig.BaseColor,
                        0.4f to DiajakGlassConfig.BaseColor.copy(alpha = DiajakGlassConfig.SolidTintOverlayAlpha),
                        0.8f to Color.Transparent""",
"""                        0.0f to DiajakGlassConfig.BaseColor.copy(alpha = DiajakGlassConfig.SolidTintOverlayAlpha),
                        0.6f to DiajakGlassConfig.BaseColor.copy(alpha = DiajakGlassConfig.SolidTintOverlayAlpha),
                        0.95f to Color.Transparent"""
)

# Fix Layer 1 gradient to start fade much later and end later
text = text.replace(
"""                            colors = listOf(Color.Black, Color.Black, Color.Transparent),
                            startY = size.height * 0.85f,
                            endY = size.height""",
"""                            colors = listOf(Color.Black, Color.Black, Color.Transparent),
                            startY = size.height * 0.6f,
                            endY = size.height * 0.95f"""
)

# Fix SolidTintOverlayAlpha to be semi-transparent (e.g. 0.85) instead of 0.95 which might be too solid
text = text.replace("val SolidTintOverlayAlpha = 0.95f", "val SolidTintOverlayAlpha = 0.85f")
text = text.replace("val TintAlpha = 0.85f", "val TintAlpha = 0.75f")

with open("app/src/main/java/com/example/ui/components/DiajakGlassHeader.kt", "w") as f:
    f.write(text)
