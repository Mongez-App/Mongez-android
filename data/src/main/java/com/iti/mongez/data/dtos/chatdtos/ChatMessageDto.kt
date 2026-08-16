package com.iti.mongez.data.dtos.chatdtos

import com.google.gson.annotations.SerializedName

data class ChatMessageDto(
    @SerializedName("message_id") val messageId: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("created_at") val createdAt: String?
)
