package com.example.pizzeria.models

import android.os.Parcel
import android.os.Parcelable

data class Pizza(
    val id: Long = 0,
    val name: String,
    val description: String,
    val imageUri: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(imageUri)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Pizza> {
        override fun createFromParcel(parcel: Parcel): Pizza = Pizza(parcel)
        override fun newArray(size: Int): Array<Pizza?> = arrayOfNulls(size)
    }
}