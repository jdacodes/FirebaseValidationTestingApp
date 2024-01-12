package com.jdacodes.firebaseapp.feature_chat.domain.use_case.userlist_screen

import com.jdacodes.firebaseapp.feature_chat.domain.repository.UserListScreenRepository

class CheckFriendListRegisterIsExistedFromFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke(
        acceptorEmail: String,
        acceptorUUID: String
    ) =
        userListScreenRepository.checkFriendListRegisterIsExistedFromFirebase(
            acceptorEmail,
            acceptorUUID
        )
}