package com.iti.mongez.presentation.studyroom.uiState

import com.iti.mongez.presentation.studyroom.view.ChatMessage

data class StudyRoomState(
    val title: String = "",
    val taskId: String = "",
    val timeRemaining: Int = 25 * 60,
    val isPaused: Boolean = false,
    val showEndSessionDialog: Boolean = false,
    val inputText: String = "",
    val messages: List<ChatMessage> = listOf(
        ChatMessage("• A binary tree is a tree data structure where each node has at most two children.\n\n• The two children are referred to as the left child and the right child.\n\n• Common traversals: Inorder, Preorder, Postorder, Level Order.", false),
        ChatMessage("Can you explain the difference between DFS and BFS ?", true),
        ChatMessage("Sure! DFS goes as deep as possible along each branch before backtracking. BFS explores all neighbors at the present depth prior to moving on to the nodes at the next depth level.", false)
    )
)
