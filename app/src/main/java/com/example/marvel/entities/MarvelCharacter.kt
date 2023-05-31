package com.example.marvel.entities

import android.os.Parcel
import android.os.Parcelable


data class MarvelCharacter(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Thumbnail
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(Thumbnail::class.java.classLoader) ?: Thumbnail("", "")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeParcelable(thumbnail, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MarvelCharacter> {
        override fun createFromParcel(parcel: Parcel): MarvelCharacter {
            return MarvelCharacter(parcel)
        }

        override fun newArray(size: Int): Array<MarvelCharacter?> {
            return arrayOfNulls(size)
        }
    }

    data class Thumbnail(
        val path: String,
        val extension: String
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: ""
        )

        fun getFullPath(): String {
            return "$path.$extension"
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(path)
            parcel.writeString(extension)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Thumbnail> {
            override fun createFromParcel(parcel: Parcel): Thumbnail {
                return Thumbnail(parcel)
            }

            override fun newArray(size: Int): Array<Thumbnail?> {
                return arrayOfNulls(size)
            }
        }
    }
}
