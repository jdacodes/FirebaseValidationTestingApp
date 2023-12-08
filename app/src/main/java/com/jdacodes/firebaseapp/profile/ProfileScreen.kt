package com.jdacodes.firebaseapp.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.text.googlefonts.isAvailableOnDevice
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.jdacodes.firebaseapp.core.Constants
import com.jdacodes.firebaseapp.core.Constants.PROFILE_SCREEN
import com.jdacodes.firebaseapp.core.Constants.SIGN_OUT_MESSAGE
import com.jdacodes.firebaseapp.core.components.TopBar
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_in.UserData
import com.jdacodes.firebaseapp.navigation.NavigationItem
import com.jdacodes.firebaseapp.navigation.Screen
import com.jdacodes.firebaseapp.profile.component.ProfileScreenContent
import com.jdacodes.firebaseapp.profile.component.RevokeAccess
import com.jdacodes.firebaseapp.ui.theme.provider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userData: UserData?,
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateProfileScreen: (String) -> Unit
) {
    val TAG = "ProfileScreen"
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val items = listOf(
        NavigationItem(
            title = "Profile",
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Outlined.AccountCircle,
            route = Screen.ProfileScreen.route
        ),
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    val handler = CoroutineExceptionHandler { _, throwable ->
        // process the Throwable
        Log.e(TAG, Constants.FONT_FAILURE_MESSAGE, throwable)
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = {
                            Text(text = item.title)
                        },
                        selected = index == selectedItemIndex,
                        onClick = {
//                    navController.navigate(item.route)
                            navigateProfileScreen(item.route)
                            selectedItemIndex = index
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        badge = {
                            item.badgeCount?.let {
                                Text(text = item.badgeCount.toString())
                            }
                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        drawerState = drawerState
    )
    {
        CompositionLocalProvider(
            LocalFontFamilyResolver provides createFontFamilyResolver(LocalContext.current, handler)
        ) {

            LaunchedEffect(Unit) {
                if (provider.isAvailableOnDevice(context)) {
                    Log.d(TAG, Constants.FONT_SUCCESSFUL_MESSAGE)
                }
            }
            Scaffold(
                topBar = {
                    TopBar(
                        title = PROFILE_SCREEN,
                        signOut = {
                            viewModel.signOut()
                            Toast.makeText(
                                context,
                                SIGN_OUT_MESSAGE,
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        revokeAccess = {
                            viewModel.revokeAccess()
                        },
                        drawerStateOpen = {
                            scope.launch {
                                drawerState.open()
                            }

                        }
                    )
                },
                snackbarHost = { SnackbarHost(snackbarHostState) },
                content = { padding ->
                    ProfileScreenContent(
                        userData = userData,
                        padding = padding
                    )
                }
            )
        }
        RevokeAccess(
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            signOut = {
                viewModel.signOut()
            }
        )
    }
}