package com.jdacodes.firebaseapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.storage
import com.jdacodes.firebaseapp.feature_chat.data.repository.ChatScreenRepositoryImpl
import com.jdacodes.firebaseapp.feature_chat.data.repository.UserListScreenRepositoryImpl
import com.jdacodes.firebaseapp.feature_chat.domain.repository.ChatScreenRepository
import com.jdacodes.firebaseapp.feature_chat.domain.repository.UserListScreenRepository
import com.jdacodes.firebaseapp.feature_chat.domain.use_case.chat_screen.BlockFriendToFirebase
import com.jdacodes.firebaseapp.feature_chat.domain.use_case.chat_screen.ChatScreenUseCases
import com.jdacodes.firebaseapp.feature_chat.domain.use_case.chat_screen.InsertMessageToFirebase
import com.jdacodes.firebaseapp.feature_chat.domain.use_case.chat_screen.LoadMessageFromFirebase
import com.jdacodes.firebaseapp.feature_chat.domain.use_case.chat_screen.LoadOpponentProfileFromFirebase
import com.jdacodes.firebaseapp.feature_chat.domain.use_case.userlist_screen.AcceptPendingFriendRequestToFirebase
import com.jdacodes.firebaseapp.feature_chat.domain.use_case.userlist_screen.CheckChatRoomExistedFromFirebase
import com.jdacodes.firebaseapp.feature_chat.domain.use_case.userlist_screen.CheckFriendListRegisterIsExistedFromFirebase
import com.jdacodes.firebaseapp.feature_chat.domain.use_case.userlist_screen.CreateChatRoomToFirebase
import com.jdacodes.firebaseapp.feature_chat.domain.use_case.userlist_screen.CreateFriendListRegisterToFirebase
import com.jdacodes.firebaseapp.feature_chat.domain.use_case.userlist_screen.LoadAcceptedFriendRequestListFromFirebase
import com.jdacodes.firebaseapp.feature_chat.domain.use_case.userlist_screen.LoadPendingFriendRequestListFromFirebase
import com.jdacodes.firebaseapp.feature_chat.domain.use_case.userlist_screen.OpenBlockedFriendToFirebase
import com.jdacodes.firebaseapp.feature_chat.domain.use_case.userlist_screen.RejectPendingFriendRequestToFirebase
import com.jdacodes.firebaseapp.feature_chat.domain.use_case.userlist_screen.SearchUserFromFirebase
import com.jdacodes.firebaseapp.feature_chat.domain.use_case.userlist_screen.UserListScreenUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {

    @Provides
//    fun provideFirebaseStorageInstance() = FirebaseStorage.getInstance()
    fun provideFirebaseStorageInstance() = Firebase.storage

    @Provides
//    fun provideFirebaseDatabaseInstance() = FirebaseDatabase.getInstance()
    fun provideFirebaseDatabaseInstance() = Firebase.database

    @Provides
    fun provideFirebaseAuthInstance() = Firebase.auth
    @Provides
    fun provideChatScreenRepository(
        auth: FirebaseAuth,
        database: FirebaseDatabase
    ): ChatScreenRepository = ChatScreenRepositoryImpl(auth, database)

    @Provides
    fun provideUserListScreenRepository(
        auth: FirebaseAuth,
        database: FirebaseDatabase
    ): UserListScreenRepository = UserListScreenRepositoryImpl(auth, database)

    @Provides
    fun provideChatScreenUseCase(chatScreenRepository: ChatScreenRepository) = ChatScreenUseCases(
        blockFriendToFirebase = BlockFriendToFirebase(chatScreenRepository),
        insertMessageToFirebase = InsertMessageToFirebase(chatScreenRepository),
        loadMessageFromFirebase = LoadMessageFromFirebase(chatScreenRepository),
        opponentProfileFromFirebase = LoadOpponentProfileFromFirebase(chatScreenRepository)
    )

    @Provides
    fun provideUserListScreenUseCase(userListScreenRepository: UserListScreenRepository) =
        UserListScreenUseCases(
            acceptPendingFriendRequestToFirebase = AcceptPendingFriendRequestToFirebase(
                userListScreenRepository
            ),
            checkChatRoomExistedFromFirebase = CheckChatRoomExistedFromFirebase(
                userListScreenRepository
            ),
            checkFriendListRegisterIsExistedFromFirebase = CheckFriendListRegisterIsExistedFromFirebase(
                userListScreenRepository
            ),
            createChatRoomToFirebase = CreateChatRoomToFirebase(userListScreenRepository),
            createFriendListRegisterToFirebase = CreateFriendListRegisterToFirebase(
                userListScreenRepository
            ),
            loadAcceptedFriendRequestListFromFirebase = LoadAcceptedFriendRequestListFromFirebase(
                userListScreenRepository
            ),
            loadPendingFriendRequestListFromFirebase = LoadPendingFriendRequestListFromFirebase(
                userListScreenRepository
            ),
            openBlockedFriendToFirebase = OpenBlockedFriendToFirebase(userListScreenRepository),
            rejectPendingFriendRequestToFirebase = RejectPendingFriendRequestToFirebase(
                userListScreenRepository
            ),
            searchUserFromFirebase = SearchUserFromFirebase(userListScreenRepository),
        )
}