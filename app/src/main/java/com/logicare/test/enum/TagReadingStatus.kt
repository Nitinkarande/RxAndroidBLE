package com.logicare.test.enum

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Sanjay Sah on 17/11/2017.
 */
enum class TagReadingStatus : Parcelable {
    CONFIGURED, CONFIGURED_AVAILABLE, CONFIGURED_NOT_AVAILABLE, NOT_CONFIGURED;

    override fun writeToParcel(parcel: Parcel, flags: Int) {
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TagReadingStatus> {
        override fun createFromParcel(parcel: Parcel): TagReadingStatus {
            return TagReadingStatus.values()[parcel.readInt()]
        }

        override fun newArray(size: Int): Array<TagReadingStatus?> {
            return arrayOfNulls(size)
        }
    }
}