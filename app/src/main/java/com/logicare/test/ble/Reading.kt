package com.logicare.test.ble

import android.os.Parcel
import android.os.Parcelable
import com.logicare.test.enum.TagReadingStatus

/**
 * @author Sanjay Sah
 */
data class Reading(val battery: Int,
                   val requiredPressure: Int,
                   val currentPressure: Int,
                   val currentTemperature: Int,
                   var tyreTemperDetectionCount: Int? = null,
                   var tagTemperDetectionCount: Int? = null,
                   var tagMalfunctionCount: Int? = null,
                   var tagImproperFitmentCount: Int? = null,
                   var impactDetectionCount: Int? = null,
                   var tyreID: String? = null,
                   var tyreStatus: TagReadingStatus = TagReadingStatus.CONFIGURED_AVAILABLE) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? String,
            TagReadingStatus.values()[parcel.readInt()])

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(battery)
        parcel.writeInt(requiredPressure)
        parcel.writeInt(currentPressure)
        parcel.writeInt(currentTemperature)
        parcel.writeValue(tyreTemperDetectionCount)
        parcel.writeValue(tagTemperDetectionCount)
        parcel.writeValue(tagMalfunctionCount)
        parcel.writeValue(tagImproperFitmentCount)
        parcel.writeValue(impactDetectionCount)
        parcel.writeValue(tyreID)
        parcel.writeInt(tyreStatus.ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reading> {
        override fun createFromParcel(parcel: Parcel): Reading {
            return Reading(parcel)
        }

        override fun newArray(size: Int): Array<Reading?> {
            return arrayOfNulls(size)
        }
    }
}