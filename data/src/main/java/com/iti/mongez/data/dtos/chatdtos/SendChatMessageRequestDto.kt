package com.iti.mongez.data.dtos.chatdtos

import com.google.gson.annotations.SerializedName

data class SendChatMessageRequestDto(
    @SerializedName("message") val message: String
)
