package com.iti.mongez.data.dtos.chatdtos

import com.google.gson.annotations.SerializedName

data class SendChatMessageResponseDto(
    @SerializedName("messages") val messages: List<ChatMessageDto>?
)
