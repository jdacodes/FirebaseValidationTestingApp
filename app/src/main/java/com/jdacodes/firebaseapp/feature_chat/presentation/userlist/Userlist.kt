package com.jdacodes.firebaseapp.feature_chat.presentation.userlist

import android.annotation.SuppressLint
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.jdacodes.firebaseapp.core.SnackbarController
import com.jdacodes.firebaseapp.feature_chat.presentation.bottom_navigation.BottomNavigation
import com.jdacodes.firebaseapp.feature_chat.presentation.common_component.ChatSnackBar
import com.jdacodes.firebaseapp.feature_chat.presentation.userlist.component.AcceptPendingRequestList
import com.jdacodes.firebaseapp.feature_chat.presentation.userlist.component.PendingFriendRequestList
import com.jdacodes.firebaseapp.navigation.Screen
import com.jdacodes.firebaseapp.profile.component.ProfileAppBar
import com.jdacodes.firebaseapp.ui.theme.spacing

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Userlist(
    userListViewModel: UserListViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    keyboardController: SoftwareKeyboardController
) {
//    val keyboardController = LocalSoftwareKeyboardController.current
//    val snackbarHostState = remember { SnackbarHostState() }
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val toastMessage = userListViewModel.toastMessage.value
    LaunchedEffect(key1 = toastMessage) {
        if (toastMessage != "") {
            SnackbarController(this).showSnackbar(snackbarHostState, toastMessage, "Close")
        }
    }
    var chatRoomUUID: String? by remember { mutableStateOf(null) }
    var opponentUUID: String? by remember { mutableStateOf(null) }
    var oneSignalUserId: String? by remember { mutableStateOf(null) }
    var registerUUID: String? by remember { mutableStateOf(null) }
//    if (chatRoomUUID != null) {
//        LaunchedEffect(key1 = Unit) {
//            navController.popBackStack()
//            navController.navigate(BottomNavItem.Chat.screen_route)
//        }
//    }
    if (chatRoomUUID != null && opponentUUID != null && registerUUID != null && oneSignalUserId != null) {
        LaunchedEffect(key1 = Unit) {
            navController.popBackStack()
            navController.navigate(Screen.ChatScreen.route + "/${chatRoomUUID}" + "/${opponentUUID}" + "/${registerUUID}" + "/${oneSignalUserId}")
        }
    }

    LaunchedEffect(key1 = Unit) {
        userListViewModel.refreshingFriendList()
    }
    val acceptedFriendRequestList = userListViewModel.acceptedFriendRequestList
    val pendingFriendRequestList = userListViewModel.pendingFriendRequestList

//    var showAlertDialog by remember {
//        mutableStateOf(false)
//    }
//    if (showAlertDialog) {
//        AlertDialogChat(
//            onDismiss = { showAlertDialog = !showAlertDialog },
//            onConfirm = {
//                showAlertDialog = !showAlertDialog
//                userListViewModel.createFriendshipRegisterToFirebase(it)
//            })
//    }

    val scrollState = rememberLazyListState()
    var isRefreshing by remember { userListViewModel.isRefreshing }

    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                ChatSnackBar(
                    snackbarData = data
                )
            }
        },
        bottomBar = {
            bottomBarState.value =
                currentRoute != Screen.SignInScreen.route &&
                        currentRoute != Screen.SignUpScreen.route &&
                        currentRoute != Screen.ChatScreen.fullRoute &&
                        currentRoute != Screen.VerifyEmailScreen.route &&
                        currentRoute != Screen.ForgotPasswordScreen.route

            BottomNavigation(
                navController = navController,
                bottomBarState = bottomBarState.value,
                snackbarHostState
            )
        },
    ) {
        SwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                isRefreshing = true
                userListViewModel.refreshingFriendList()
            },
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(top = MaterialTheme.spacing.large),
                    state = state,
                    refreshTriggerDistance = trigger,
                    fade = true,
                    scale = true,
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .focusable()
                    .pointerInput(Unit) {
                        detectTapGestures { keyboardController?.hide() }
                    }
            ) {
                ProfileAppBar()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
//                .weight(1f),
                    state = scrollState,
                ) {
                    items(acceptedFriendRequestList.value) { item ->
                        AcceptPendingRequestList(item) {
                            chatRoomUUID = item.chatRoomUUID
                            registerUUID = item.registerUUID
                            opponentUUID = item.userUUID
                            oneSignalUserId = item.oneSignalUserId
                        }
                    }
                    items(pendingFriendRequestList.value) { item ->
                        PendingFriendRequestList(item, {
                            userListViewModel.acceptPendingFriendRequestToFirebase(item.registerUUID)
                            userListViewModel.refreshingFriendList()
                        }, {
                            userListViewModel.cancelPendingFriendRequestToFirebase(item.registerUUID)
                            userListViewModel.refreshingFriendList()
                        })
                    }
                }
            }
        }
    }


}