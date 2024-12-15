package com.masterplus.animals.core.presentation.dialogs

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.models.ImageMetadata
import com.masterplus.animals.core.presentation.components.SharedHeader
import com.masterplus.animals.core.presentation.components.icon.IconButtonForImage
import com.masterplus.animals.core.presentation.components.image.DefaultImage
import com.masterplus.animals.core.presentation.utils.SampleDatas

@Composable
fun ShowImageMetadataDia(
    imageData: Any,
    metadata: ImageMetadata,
    onDismiss: () -> Unit
) {
    BaseDialog(
        onClosed = onDismiss
    ) {
        Box {
            IconButtonForImage(
                imageVector = Icons.Default.Close,
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .zIndex(2f)
                    .padding(8.dp),
            )

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DefaultImage(
                    imageData = imageData,
                    modifier = Modifier.fillMaxWidth()
                )
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    metadata.artistName?.let { artistName ->
                        InfoItem(
                            title = "Artist",
                            content = artistName
                        )
                    }

                    metadata.imageDescription?.let { description ->
                        InfoItem(
                            title = "Description",
                            content = description
                        )
                    }

                    InfoItem(
                        title = "Usage Terms",
                        content = "${metadata.usageTerms} (${metadata.licenseShortName})"
                    )

                    metadata.dateTimeOriginal?.let { originalDate ->
                        InfoItem(
                            title = "Date Created",
                            content = originalDate
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ClickableText(
                            text = "License Details",
                            link = metadata.licenseUrl
                        )
                        Spacer(Modifier.width(4.dp))
                        ClickableText(
                            text = "More Information",
                            link = metadata.descriptionUrl
                        )
                    }
                }
            }
        }

    }
}


private fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

@Composable
private fun ClickableText(
    text: String,
    link: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
        modifier = modifier.clickable {
            openUrl(context, link)
        }
    )
}

@Composable
private fun InfoItem(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Text(
            "$title:",
            style = MaterialTheme.typography.titleSmall
        )
        Text(content)
    }
}



@Preview(showBackground = true)
@Composable
private fun ShowImageMetadataDiaPreview() {
    ShowImageMetadataDia(
        metadata = SampleDatas.imageWithMetadata.metadata!!,
        imageData = R.drawable.animals_plants,
        onDismiss = {}
    )
}