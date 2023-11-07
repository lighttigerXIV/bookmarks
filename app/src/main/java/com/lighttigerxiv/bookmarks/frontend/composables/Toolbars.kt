package com.lighttigerxiv.bookmarks.frontend.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.lighttigerxiv.bookmarks.R
import com.lighttigerxiv.bookmarks.frontend.modifyIf
import com.lighttigerxiv.bookmarks.frontend.navigation.goBack

@Composable
fun Toolbar(
    title: String = "",
    controller: NavHostController,
    content: (@Composable RowScope.() -> Unit)? = null
) {

    Row(verticalAlignment = Alignment.CenterVertically) {

        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { controller.goBack() }
                .padding(8.dp),
            painter = painterResource(id = R.drawable.chevron_left),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        HorizontalSpacer(SpacerSize.Medium)

        NormalText(text = title, fontSize = 18.sp)

        if (content != null) {
            content()
        }
    }
}


@Composable
fun EditToolbar(
    showContent: Boolean = true,
    controller: NavHostController,
    onDeleteClick: () -> Unit,
    onSaveClick: () -> Unit,
    disableSaveButton: Boolean = false
) {

    Toolbar(controller = controller) {

        if(showContent){

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {

                Box(
                    modifier = Modifier
                        .border(1.dp, MaterialTheme.colorScheme.error, CircleShape)
                        .clickable { onDeleteClick() }
                        .padding(8.dp)
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    NormalText(text = "Delete", color = MaterialTheme.colorScheme.error)
                }

                HorizontalSpacer(size = SpacerSize.Small)

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background( if(disableSaveButton) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary)
                        .modifyIf(!disableSaveButton){
                            clickable { onSaveClick() }
                        }
                        .padding(8.dp)
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    NormalText(text = "Save", color = if(disableSaveButton) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}