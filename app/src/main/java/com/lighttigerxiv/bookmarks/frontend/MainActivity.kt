package com.lighttigerxiv.bookmarks.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.lighttigerxiv.bookmarks.R
import com.lighttigerxiv.bookmarks.frontend.screens.root.RootScreen
import com.lighttigerxiv.bookmarks.frontend.system_bars.ChangeNavigationBarsColor
import com.lighttigerxiv.bookmarks.frontend.system_bars.ChangeStatusBarColor
import com.lighttigerxiv.bookmarks.frontend.ui.theme.BookmarksTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm = ViewModelProvider(this)[AppVM::class.java]

        setContent {
            BookmarksTheme {

                val initialized = vm.initialized.collectAsState().value

                ChangeStatusBarColor(MaterialTheme.colorScheme.surface)
                ChangeNavigationBarsColor(MaterialTheme.colorScheme.surface)

                if(!initialized){
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){

                        Icon(
                            modifier = Modifier
                                .size(150.dp),
                            painter = painterResource(id = R.drawable.icon_bookmark),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }else{
                    RootScreen(appVM = vm)
                }
            }
        }
    }
}