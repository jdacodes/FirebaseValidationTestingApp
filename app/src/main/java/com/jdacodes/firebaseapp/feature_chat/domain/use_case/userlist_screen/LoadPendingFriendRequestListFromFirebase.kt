package com.jdacodes.firebaseapp.feature_chat.domain.use_case.userlist_screen

import com.jdacodes.firebaseapp.feature_chat.domain.repository.UserListScreenRepository

class LoadPendingFriendRequestListFromFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke() =
        userListScreenRepository.loadPendingFriendRequestListFromFirebase()
}