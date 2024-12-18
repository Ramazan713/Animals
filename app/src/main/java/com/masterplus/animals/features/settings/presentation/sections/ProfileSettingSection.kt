package com.masterplus.animals.features.settings.presentation.sections

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.transformations
import coil3.transform.CircleCropTransformation
import com.masterplus.animals.core.shared_features.auth.domain.models.User
import com.masterplus.animals.features.settings.presentation.SettingsAction
import com.masterplus.animals.features.settings.presentation.SettingsDialogEvent
import com.masterplus.animals.R
import com.masterplus.animals.features.settings.presentation.components.UnRegisteredProfileIcon

@Composable
fun ProfileSettingSection(
    user: User?,
    onAction: (SettingsAction) -> Unit,
){
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 13.dp)
    ) {

        if(user != null){
            val photoUri = user.photoUri

            if(photoUri != null){
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(photoUri.toString())
                        .diskCacheKey(user.uid)
                        .transformations(
                            CircleCropTransformation()
                        )
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.size(156.dp),
                )
            }else{
                UnRegisteredProfileIcon()
            }

            user.name?.let { name->
                if(name.isNotBlank()){
                    Text(
                        name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }
            }
            user.email?.let { email->
                if(email.isNotBlank()){
                    Text(
                        email,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }
            }
        }else{
            UnRegisteredProfileIcon()

            Button(onClick = { onAction(SettingsAction.ShowDialog(SettingsDialogEvent.ShowAuthDia)) }) {
                Text(text = stringResource(R.string.sign_in))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileSettingSectionPreview() {
    ProfileSettingSection(
        user = null,
        onAction = {}
    )
}