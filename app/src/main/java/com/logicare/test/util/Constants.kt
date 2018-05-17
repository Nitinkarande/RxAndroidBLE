package com.polidea.rxandroidble2.sample.util

import android.os.Environment

/**
 * @author Sanjay Sah
 */
object Constants {
    const val CAMERA = 0
    const val GALLERY = 1

    const val APP_NAME = "Treel"
    val ROOT_FOLDER = Environment.getExternalStorageDirectory().toString() + "/" + APP_NAME
    val IMAGE_DIR = ROOT_FOLDER + "/images/"
    val CRASH_DIR = ROOT_FOLDER + "/crash_logs/"
    val LOG_WRITER_DIR = ROOT_FOLDER + "/Logs/"


    const val PSI = "PSI"
    const val DEGREE_CENTIGRADE = "\u00b0" + "C"

    val OVR_RANGE = 65356

    /**
     * Parameters Ranges
     */
    val TEMPERATURE_MIN = -40
    val TEMPERATURE_MAX = 125

    val PRESSURE_MIN = 0
    val PRESSURE_MAX = 217

    const val COMMA_SEPARATOR = ','
    const val CSV = ".csv"
    const val CSV_HEADER_TREEL_REPORT = "Treel Report : "
    const val CSV_HEADER_TIRE_POSITION = "Tire Position"
    const val CSV_HEADER_TIMESTAMP = "Timestamp"
    const val CSV_HEADER_PRESSURE = "P"
    const val CSV_HEADER_TEMPERATURE = "T"
    const val CSV_HEADER_BLANK = " "

    val TREAD_DEPTH_REPORT_HEADERS = arrayOf("Tire Position", "Tire ID", "Pressure (PSI)", "Temperature", "Tamper Count (TC)", "Tread depth details (mm or %)")

    const val ODOMETER_UPDATE_REMINDER_DAYS = 30

    val OTP_NUMBER = "LEMBED"

    val ALERT_HIGH_PRESSURE = 1
    val ALERT_LOW_PRESSURE = 2
    val ALERT_HIGH_TEMPERATURE = 3
    val ALERT_TIRE_ROTATION = 4
    val ALERT_ = 4





}