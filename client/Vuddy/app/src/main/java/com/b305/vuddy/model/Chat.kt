package com.b305.vuddy.model

import android.os.Parcel
import android.os.Parcelable

data class ChatList(
    var chatId: Int?,
    var chatList: ArrayList<Chat>,
)

data class Chat(
    var profileImage:String? = null,
    var chatId: Int? = null,
    var nickname: String? = null,
    var message:String? = null,
    var time:String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(profileImage)
        parcel.writeInt(chatId!!)
        parcel.writeString(nickname)
        parcel.writeString(message)
        parcel.writeString(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Chat> {
        override fun createFromParcel(parcel: Parcel): Chat {
            return Chat(parcel)
        }

        override fun newArray(size: Int): Array<Chat?> {
            return arrayOfNulls(size)
        }
    }
}

