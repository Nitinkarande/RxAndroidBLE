package com.logicare.test.ble;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

/**
 * Created by sanjay.sah on 3/26/2017.
 */

public class ConversionUtils {
    public static final String UNIT_TEMPERATURE_CENTIGRADE = "\u00b0"+"C";
    public static final String UNIT_PERCENT = "%";
    public static final String UNIT_PASCAL = "mBar";
    public static final String UNIT_VIBRATION = "g";
    public static final String UNIT_VIBRATION_MS = "m/s\u00B2";
    public static final String UNIT_VIBRATION_MMS = "m/s\u00B2";


    public static final String UNIT_FT = "ft";

    public static final String NA = "N/A";
    /*public static final String TEMPERATURE = "Temperature";
    public static final String HUMIDITY = "Humidity";
    public static final String PRESSURE = "Pressure";
    public static final String VIBRATION = "Vibration";*/


    private static final long INVALID_3BYTES_MAX = 16777215;
    private static final long INVALID_2BYTES_MAX = 65535;
    private static final long INVALID_BYTES_MIN = 0;


    /**
     * Calculate actual temperature from raw temperature data
     * @param tempBytes raw temperature data
     * @return actual temperature
     */
    public static float calculateTemperature(byte[] tempBytes) {
        int rawTemperature = byteArrayToInt(tempBytes);
        float temperature = (float) ((((float) rawTemperature / 65536) * 175.72) - 46.85);
        DecimalFormat formatter = new DecimalFormat("#.##");
        return Float.parseFloat(formatter.format(temperature));
    }

    /**
     * Calculate actual humidity from raw humidity data
     * @param humidityBytes raw humidity data
     * @return actual humidity in percentage
     */
    public static float calculateHumidity(byte[] humidityBytes) {
        int rawHumidity = byteArrayToInt(humidityBytes);
        float humidity =  (((float) rawHumidity / 65536) * 125) - 6;
        DecimalFormat formatter = new DecimalFormat("#.#");
        return Float.parseFloat(formatter.format(humidity));
    }

    /**
     * Calculate actual Pressure from raw pressure data
     * @param pressureBytes raw pressure data
     * @return actual Pressure
     */
    public static int calculatePressure(byte[] pressureBytes) {
        int rawPressure = byteArrayToInt(pressureBytes);
        float pressure = rawPressure / 100;
        //DecimalFormat formatter = new DecimalFormat("#.##");
        return Math.round(pressure);
    }

    public static int calculateSurfaceTemperature(byte[] surfaceTemperatureBytes) {
        int rawSurfaceTemperature = byteArrayToInt(surfaceTemperatureBytes);
        float surfaceTemperature;
        if(rawSurfaceTemperature <= 32768) {
            surfaceTemperature = rawSurfaceTemperature / 100;
        } else {
            surfaceTemperature = -((rawSurfaceTemperature - 32768) / 100);
        }
        //DecimalFormat formatter = new DecimalFormat("#.##");
        return /*Float.parseFloat(formatter.format(surfaceTemperature))*/ Math.round(surfaceTemperature);
    }

    public static float calculateVibrationG(byte[] vibrationBytes) {
        int rawVibration = byteArrayToInt(vibrationBytes);
        float vibration = rawVibration / 1000f;
        DecimalFormat formatter = new DecimalFormat("#.###");
        return Float.parseFloat(formatter.format(vibration));
    }

    public static float calculateVibrationMs2(byte[] vibrationBytes) {
        int rawVibration = byteArrayToInt(vibrationBytes);
        float vibration = rawVibration / 1000f;
        DecimalFormat formatter = new DecimalFormat("#.###");
        return Float.parseFloat(formatter.format(vibration));
    }

    public static float calculateVibrationVelocity(byte[] vibrationBytes) {
        int rawVibration = byteArrayToInt(vibrationBytes);
        float vibration = rawVibration / 1000f;
        DecimalFormat formatter = new DecimalFormat("#.###");
        return Float.parseFloat(formatter.format(vibration));
    }

    /**
     * Helper Methods
     */
    /**
     * Convert Byte to integer
     *
     * @param bytes
     *            byte array
     * @return converted integer
     */
    public static int byteArrayToInt(byte[] bytes) {
        /*int MASK = 0xFF;
        int result = 0;
        result = bytes[0] & MASK;
        result = result + ((bytes[1] & MASK) << 8);*/

        int value = 0;
        for (int i = 0; i < bytes.length; i++) {
            value += (bytes[i] & 0xffL) << (8 * i);
        }
        return value;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    public static byte[] decimalToByteArray(int value) {
        String hexString =  Integer.toHexString(value);
        if(hexString.length() % 2 != 0) {
            hexString = "0"+hexString;
        }
        return hexStringToByteArray(hexString);
    }
    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        if(bytes == null) return "null";
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }

        return new String(hexChars);
    }


    public static boolean isInvalidData(byte[] bytes) {
        int intValue = byteArrayToInt(bytes);
        switch (bytes.length) {
            case 2:
                if(intValue == INVALID_2BYTES_MAX || intValue == INVALID_BYTES_MIN) {
                    return true;
                }
            case 3:
                if(intValue == INVALID_3BYTES_MAX || intValue == INVALID_BYTES_MIN) {
                    return true;
                }
                break;
        }
        return false;
    }

    //Vibration value can be 0
    public static boolean isInvalidVibration(byte[] bytes) {
        int intValue = byteArrayToInt(bytes);
        return intValue == INVALID_2BYTES_MAX;

    }

    public static boolean isInvalidPressure(byte[] bytes) {
        int intValue = byteArrayToInt(bytes);
        return intValue == INVALID_2BYTES_MAX;

    }

    public static boolean isInvalidTemperature(byte[] bytes) {
        int intValue = byteArrayToInt(bytes);
        return intValue == INVALID_2BYTES_MAX;

    }

    /**
     * Calculate Device distance from the Rssi value
     * @param rssi
     * @return Distance in ft
     */
    public static String getDistanceInFtFromRssi(float rssi) {
        int distance = 0;
        float absRssi = Math.abs(rssi);
        if (absRssi >= 35 && absRssi <= 45) {
            distance = (int) ((absRssi - 35) / 2);
        } else if (absRssi >= 46 && absRssi <= 55) {
            distance = (int) (((absRssi - 45) / 2) + 5);
        } else if (absRssi >= 56 && absRssi <= 65) {
            distance = (int) (((absRssi - 55) * 2) + 10);
        } else if (absRssi >= 66 && absRssi <= 75) {
            distance = (int) (((absRssi - 65)) + 30);
        } else if (absRssi >= 76 && absRssi <= 85) {
            distance = (int) (((absRssi - 75) * 2) + 40);
        } else if (absRssi >= 86 && absRssi <= 95) {
            distance = (int) (((absRssi - 85) * 3) + 60);
        } else if (absRssi >= 96 /*&& rssi <= -100*/) {
            distance = (int) (((absRssi - 95) * 2) + 90);
        }
        return String.format("%s %s", distance,UNIT_FT);
    }

    /**
     * Return last 3 digit from Mac id
     * @param macId
     * @return
     */
    public static String getLast3DigitFromMacId(String macId) {
        String[] array = macId.split(":");
        StringBuilder hexString = new StringBuilder();
        for (int i = array.length - 3, j = 0; i < array.length; i++, j++ ) {
            hexString.append(array[i]);
        }
        int decimalValue = Integer.parseInt(hexString.toString(), 16);
        int remainder = decimalValue % 1000;
        if(remainder < 10) {
            return String.format("00%d",remainder);
        } else if(remainder < 100) {
            return String.format("0%d",remainder);
        } else {
            return String.valueOf(remainder);
        }
    }

    public static int getDecimal(byte value) {
        return value & 0xFF;
    }

    @NotNull
    public static String getDateAndTime(@NotNull byte[] dateAndTimeBytes) {

        return String.valueOf(dateAndTimeBytes[0] & 0xFF) +"-"+//Day
                (dateAndTimeBytes[1] & 0xFF) +"-"+//Month
                (dateAndTimeBytes[2] & 0xFF) +" "+//Year (Last 2 digit ex. 17 of 2017)
                (dateAndTimeBytes[3] & 0xFF) +":"+//Hours
                (dateAndTimeBytes[4] & 0xFF) +":"+//Minutes
                (dateAndTimeBytes[5] & 0xFF)//Seconds
                ;
    }

    @NotNull
    public static int getBatteryHealth(byte raw) {
        return raw & 0xFF;
    }

    public static int getTyreTemperCount(byte rawTemperCount) {
        return rawTemperCount & 0xFF;
    }
}

