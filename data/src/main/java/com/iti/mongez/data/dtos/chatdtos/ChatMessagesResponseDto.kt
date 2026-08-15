package com.iti.mongez.data.dtos.chatdtos

import com.google.gson.annotations.SerializedName

data class ChatMessagesResponseDto(
    @SerializedName("pagination") val pagination: ChatPaginationDto?,
    @SerializedName("messages") val messages: List<ChatMessageDto>?
)

data class ChatPaginationDto(
    @SerializedName("page") val page: Int?,
    @SerializedName("size") val size: Int?,
    @SerializedName("has_next") val hasNext: Boolean?
)
