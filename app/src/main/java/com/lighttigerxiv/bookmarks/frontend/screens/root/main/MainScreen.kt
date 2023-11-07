package com.lighttigerxiv.bookmarks.frontend.screens.root.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lighttigerxiv.bookmarks.R
import com.lighttigerxiv.bookmarks.backend.utils.onLandscape
import com.lighttigerxiv.bookmarks.frontend.AppVM
import com.lighttigerxiv.bookmarks.frontend.composables.HorizontalSpacer
import com.lighttigerxiv.bookmarks.frontend.composables.NormalText
import com.lighttigerxiv.bookmarks.frontend.composables.SpacerSize
import com.lighttigerxiv.bookmarks.frontend.composables.VerticalSpacer
import com.lighttigerxiv.bookmarks.frontend.navigation.Routes
import com.lighttigerxiv.bookmarks.frontend.navigation.openAddBookmark
import com.lighttigerxiv.bookmarks.frontend.navigation.openAddGroup
import com.lighttigerxiv.bookmarks.frontend.navigation.openBookmarks
import com.lighttigerxiv.bookmarks.frontend.navigation.openGroups
import com.lighttigerxiv.bookmarks.frontend.screens.root.main.bookmarks.BookmarksScreen
import com.lighttigerxiv.bookmarks.frontend.screens.root.main.groups.GroupsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    rootController: NavHostController,
    appVM: AppVM
) {

    val mainController = rememberNavController()
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)



    Column {

        BottomSheetScaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState,
            backgroundColor = MaterialTheme.colorScheme.background,
            sheetContent = {
                AddSheet(sheetState, rootController)
            },
            sheetPeekHeight = 0.dp,
            sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        ) { scaffoldPadding ->

            if(onLandscape()){
                Row {
                    NavigationBar(mainController, sheetState)

                    MainHost(
                        modifier = Modifier.weight(1f, fill = true),
                        scaffoldPadding = scaffoldPadding,
                        mainController = mainController,
                        rootController = rootController,
                        appVM = appVM
                    )
                }
            }
            Column {

                MainHost(
                    modifier = Modifier.weight(1f, fill = true),
                    scaffoldPadding = scaffoldPadding,
                    mainController = mainController,
                    rootController = rootController,
                    appVM = appVM
                )

                NavigationBar(mainController, sheetState)
            }
        }
    }
}

@Composable
fun MainHost(
    modifier: Modifier,
    scaffoldPadding: PaddingValues,
    mainController: NavHostController,
    rootController: NavHostController,
    appVM: AppVM
) {

    val settings = appVM.settings.collectAsState().value

    NavHost(
        modifier = modifier
            .padding(scaffoldPadding)
            .fillMaxHeight()
            .padding(16.dp),
        navController = mainController,
        startDestination = if(settings!!.openGroupsByDefault) Routes.Groups else Routes.Bookmarks
    ) {

        composable(Routes.Bookmarks) {

            BookmarksScreen(rootController, appVM)
        }

        composable(Routes.Groups) {

            GroupsScreen(rootController, appVM)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddSheet(
    sheetState: BottomSheetState,
    rootController: NavHostController
) {

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
    ) {

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Box(
                Modifier
                    .width(30.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }

        VerticalSpacer(SpacerSize.Large)

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clip(CircleShape)
                .clickable {
                    rootController.openAddBookmark()
                    scope.launch { sheetState.collapse() }
                }
                .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bookmark),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                HorizontalSpacer(SpacerSize.Small)

                NormalText(
                    text = "Bookmark",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            VerticalSpacer(size = SpacerSize.Small)

            Row(modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clip(CircleShape)
                .clickable {
                    rootController.openAddGroup()
                    scope.launch { sheetState.collapse() }
                }
                .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.folder),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                HorizontalSpacer(SpacerSize.Small)

                NormalText(
                    text = "Group",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavigationBar(controller: NavHostController, sheetState: BottomSheetState) {

    val scope = rememberCoroutineScope()

    val route = controller.currentBackStackEntryAsState().value?.destination?.route ?: ""
    val onBookmarksRoute = route == Routes.Bookmarks
    val onGroupsRoute = route == Routes.Groups

    val bookmarksColor = if (onBookmarksRoute) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val groupsColor = if (onGroupsRoute) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    if (onLandscape()) {

        Column(
            modifier = Modifier
                .width(120.dp)
                .fillMaxHeight()
                .padding(16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1f, fill = true)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { controller.openBookmarks() },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.bookmark),
                    contentDescription = "Bookmarks Icon",
                    tint = bookmarksColor
                )

                VerticalSpacer(size = SpacerSize.Small)

                NormalText(text = "Bookmarks", color = bookmarksColor)
            }

            Box(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .clickable { scope.launch { sheetState.expand() } }
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = "Add Button",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1f, fill = true)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { controller.openGroups() },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.folder),
                    contentDescription = "Groups Icon",
                    tint = groupsColor
                )

                VerticalSpacer(size = SpacerSize.Small)

                NormalText(text = "Groups", color = groupsColor)
            }
        }
    } else {
        Row(
            modifier = Modifier
                .height(70.dp)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .weight(1f, fill = true)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { controller.openBookmarks() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.bookmark),
                    contentDescription = "Bookmarks Icon",
                    tint = bookmarksColor
                )

                HorizontalSpacer(size = SpacerSize.Small)

                NormalText(text = "Bookmarks", color = bookmarksColor)
            }

            Box(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .clickable { scope.launch { sheetState.expand() } }
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = "Add Button",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .weight(1f, fill = true)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { controller.openGroups() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.folder),
                    contentDescription = "Library Icon",
                    tint = groupsColor
                )

                HorizontalSpacer(size = SpacerSize.Small)

                NormalText(text = "Groups", color = groupsColor)
            }
        }
    }
}