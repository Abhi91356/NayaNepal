package com.example.nayanepal.core.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ColorItem(name: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color)
        )
        Text(
            text = name,
            modifier = Modifier.padding(start = 16.dp),
            style = NayaNepalTypography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NayaNepalDesignSystemPreview() {
    NayaNepalTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Colors", style = NayaNepalTypography.h1)
            ColorItem("Primary", NayaNepalColors.primary)
            ColorItem("Secondary", NayaNepalColors.secondary)
            ColorItem("Accent", NayaNepalColors.accent)
            ColorItem("Background", NayaNepalColors.background)
            ColorItem("Success", NayaNepalColors.success)
            ColorItem("Warning", NayaNepalColors.warning)

            Text(
                text = "Typography",
                style = NayaNepalTypography.h1,
                modifier = Modifier.padding(top = 24.dp)
            )
            Text(text = "Heading 1", style = NayaNepalTypography.h1)
            Text(text = "Heading 2", style = NayaNepalTypography.h2)
            Text(text = "Heading 3", style = NayaNepalTypography.h3)
            Text(text = "Body Large", style = NayaNepalTypography.bodyLarge)
            Text(text = "Body Medium", style = NayaNepalTypography.bodyMedium)
            Text(text = "Body Small", style = NayaNepalTypography.bodySmall)
        }
    }
}
