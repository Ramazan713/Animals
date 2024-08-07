package com.masterplus.animals.features.settings.presentation.link_accounts.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.animals.R
import com.masterplus.animals.core.shared_features.auth.domain.enums.AuthProviderType
import com.masterplus.animals.features.settings.presentation.link_accounts.models.LinkAccountModel

@Composable
fun LinkAccountItemRow(
    linkAccountModel: LinkAccountModel,
    isButtonEnabled: Boolean = true,
    onLinkClick: (() -> Unit)? = null,
    onUnLinkClick: (() -> Unit)? = null,
){
    LinkAccountItemRow(
        providerType = linkAccountModel.providerType,
        subContent = linkAccountModel.info?.asString(),
        isConnected = linkAccountModel.isConnected,
        isButtonEnabled = isButtonEnabled,
        onLinkClick = onLinkClick,
        onUnLinkClick = onUnLinkClick
    )
}



@Composable
fun LinkAccountItemRow(
    providerType: AuthProviderType,
    isConnected: Boolean,
    subContent: String? = null,
    isButtonEnabled: Boolean = true,
    onLinkClick: (() -> Unit)? = null,
    onUnLinkClick: (() -> Unit)? = null,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .heightIn(min = 60.dp)
                .padding(horizontal = 12.dp)

        ) {
            GetIcon(
                providerType = providerType
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text =  providerType.title.asString(),
                    style = MaterialTheme.typography.titleSmall
                )
                if(!subContent.isNullOrBlank()){
                    Text(
                        text = subContent,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if(isButtonEnabled){
                GetButtons(
                    isConnected = isConnected,
                    onLinkClick = onLinkClick,
                    onUnLinkClick = onUnLinkClick
                )
            }

        }
    }
}

@Composable
private fun GetIcon(
    providerType: AuthProviderType,
    modifier: Modifier = Modifier
) {
    when(providerType){
        AuthProviderType.Email -> {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                modifier = modifier.width(24.dp)
            )
        }
        AuthProviderType.Google -> {
            Image(
                painter = painterResource(id = R.drawable.google_icon),
                contentDescription = null,
                modifier = modifier.width(24.dp)
            )
        }
        AuthProviderType.X -> {
            Image(
                painter = painterResource(id = R.drawable.x_icon),
                contentDescription = null,
                modifier = modifier.width(24.dp)
            )
        }
    }
}


@Composable
private fun GetButtons(
    isConnected: Boolean,
    onLinkClick: (() -> Unit)?,
    onUnLinkClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    if(isConnected){
        OutlinedButton(
            modifier = modifier,
            onClick = { onUnLinkClick?.invoke() }
        ) {
            Text(text = "Bağlantıyı Kaldır")
        }
    }else{
        OutlinedButton(
            modifier = modifier,
            onClick = { onLinkClick?.invoke() }
        ) {
            Text(text = "Hesabı Bağla")
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun LinkAccountItemRowPreview() {
    LinkAccountItemRow(
        providerType = AuthProviderType.Google,
        subContent = "Sub Content",
        isConnected = true,
//        isButtonEnabled = false
    )
}