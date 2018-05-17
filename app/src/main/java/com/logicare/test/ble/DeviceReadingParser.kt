package com.logicare.test.ble

import com.logicare.test.util.Constants


/**
 * @author Sanjay Sah
 */
object DeviceReadingParser {

    fun parse(scanResult: ByteArray): Reading {

        /**
         * Calculating Temperature from scan record
         */
        /**
         * Extracting 2 bytes start from 6th position from the notification data
         * 1st byte is Raw temperature value LSB
         * 2nd byte is Raw temperature value MSB
         */
        val tempBytes = extractBytes(scanResult, 16, 2)
        var temperature: Int
        if (ConversionUtils.isInvalidTemperature(tempBytes)) {
            temperature = Constants.OVR_RANGE
        } else {
            temperature = ConversionUtils.calculateSurfaceTemperature(tempBytes)
            if (temperature < Constants.TEMPERATURE_MIN || temperature > Constants.TEMPERATURE_MAX) {
                temperature = Constants.OVR_RANGE
            }
        }
        /*if(temperature != Constants.OVR_RANGE) {
            temperature = Constants.TEXT_OVR
        } else {
            parsedData[2] = String.format("%s%s", temperature.toString() , ConversionUtils.UNIT_TEMPERATURE_CENTIGRADE)

        }*/

        /**
         * Calculating Pressure from scan record
         */
        /**
         * Extracting 3 bytes start from 10th position from the notification data
         * 1st byte is Raw Pressure value LSB
         * 2nd byte is Raw Pressure value next byte
         * 3rd byte is Raw Pressure value MSB
         */
        val pressureBytes = extractBytes(scanResult, 18, 2)
        var pressure: Int
        if (ConversionUtils.isInvalidPressure(pressureBytes)) {
            pressure = Constants.OVR_RANGE
        } else {
            pressure = ConversionUtils.calculatePressure(pressureBytes)
            if (pressure < Constants.PRESSURE_MIN || pressure > Constants.PRESSURE_MAX) {
                pressure = Constants.OVR_RANGE
            }
        }
        /**
         * Getting Battery health in percentage from scan record
         */
        /**
         * Extracting 1 bytes start from 19th position from the scan record
         * 1st byte is Battery health status in percent
         */
        var batteryHealth = ConversionUtils.getBatteryHealth(scanResult[20])
        batteryHealth = if (batteryHealth > 100) 100 else batteryHealth


        val tyreTemperCount = ConversionUtils.getTyreTemperCount(scanResult[20])

        return Reading(batteryHealth, pressure, pressure, temperature, tyreTemperDetectionCount = tyreTemperCount)
    }

    // Helper method to extract bytes from byte array.
    private fun extractBytes(scanRecord: ByteArray, start: Int, length: Int): ByteArray {
        val bytes = ByteArray(length)
        System.arraycopy(scanRecord, start, bytes, 0, length)
        return bytes
    }
}