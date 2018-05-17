package com.logicare.test.scan

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import butterknife.ButterKnife
import com.logicare.test.BleApplication
import com.logicare.test.R
import com.logicare.test.ble.BluetoothUUID
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.exceptions.BleScanException
import com.polidea.rxandroidble2.scan.ScanFilter
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*

import java.util.*
import java.util.concurrent.TimeUnit

class ScanActivity : AppCompatActivity(), View.OnClickListener {


    private var rxBleClient: RxBleClient? = null
    private var scanDisposable: Disposable? = null
    private var resultsAdapter: ScanResultsAdapter? = null

    private val isScanning: Boolean
        get() = scanDisposable != null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        rxBleClient = BleApplication.getRxBleClient(this)
        configureResultList()
        scan_toggle_btn.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v) {
            scan_toggle_btn -> {
                onScanToggleClick()
            }
        }
    }

    private fun onScanToggleClick() {

        if (isScanning) {
            scanDisposable!!.dispose()
        } else {
            val filter = ScanFilter.Builder()
                    .setServiceUuid(BluetoothUUID.TREEL_TAG_SERVICE_UUID)
                    .build()

            scanDisposable = rxBleClient!!.scanBleDevices(
                    ScanSettings.Builder()
                            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                            .build(),
                    ScanFilter.Builder()
                            //                            .setDeviceAddress("B4:99:4C:34:DC:8B")
                            // add custom filters if needed
                            .build(),filter
            )

                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(Action { this.dispose() })
                    .subscribe(Consumer<ScanResult> { resultsAdapter!!.addScanResult(it) }, Consumer<Throwable> { this.onScanFailure(it) })
        }

        updateButtonUIState()
    }

    private fun handleBleScanException(bleScanException: BleScanException) {
        val text: String

        when (bleScanException.reason) {
            BleScanException.BLUETOOTH_NOT_AVAILABLE -> text = "Bluetooth is not available"
            BleScanException.BLUETOOTH_DISABLED -> text = "Enable bluetooth and try again"
            BleScanException.LOCATION_PERMISSION_MISSING -> text = "On Android 6.0 location permission is required. Implement Runtime Permissions"
            BleScanException.LOCATION_SERVICES_DISABLED -> text = "Location services needs to be enabled on Android 6.0"
            BleScanException.SCAN_FAILED_ALREADY_STARTED -> text = "Scan with the same filters is already started"
            BleScanException.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> text = "Failed to register application for bluetooth scan"
            BleScanException.SCAN_FAILED_FEATURE_UNSUPPORTED -> text = "Scan with specified parameters is not supported"
            BleScanException.SCAN_FAILED_INTERNAL_ERROR -> text = "Scan failed due to internal error"
            BleScanException.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES -> text = "Scan cannot start due to limited hardware resources"
            BleScanException.UNDOCUMENTED_SCAN_THROTTLE -> text = String.format(
                    Locale.getDefault(),
                    "Android 7+ does not allow more scans. Try in %d seconds",
                    secondsTill(bleScanException.retryDateSuggestion!!)
            )
            BleScanException.UNKNOWN_ERROR_CODE, BleScanException.BLUETOOTH_CANNOT_START -> text = "Unable to start scanning"
            else -> text = "Unable to start scanning"
        }
        Log.w("EXCEPTION", text, bleScanException)
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun secondsTill(retryDateSuggestion: Date): Long {
        return TimeUnit.MILLISECONDS.toSeconds(retryDateSuggestion.time - System.currentTimeMillis())
    }

    public override fun onPause() {
        super.onPause()

        if (isScanning) {
            /*
             * Stop scanning in onPause callback. You can use rxlifecycle for convenience. Examples are provided later.
             */
            scanDisposable!!.dispose()
        }
    }

    private fun configureResultList() {

        scan_results.setHasFixedSize(true)
        scan_results.itemAnimator = null
        val recyclerLayoutManager = LinearLayoutManager(this)
        scan_results.layoutManager = recyclerLayoutManager
        resultsAdapter = ScanResultsAdapter()
        scan_results.adapter = resultsAdapter
        resultsAdapter!!.setOnAdapterItemClickListener { view ->
            val childAdapterPosition = scan_results.getChildAdapterPosition(view)
            val itemAtPosition = resultsAdapter!!.getItemAtPosition(childAdapterPosition)
            onAdapterItemClick(itemAtPosition)
        }
    }

    private fun onAdapterItemClick(scanResults: ScanResult) {
        val v = scanResults.scanRecord.manufacturerSpecificData

        Log.d("BLE_Nitin", "==$v")
    //    val reading = parseResponse(scanResults);
       // Log.d("BLE_Nitin", "reading==$reading")
        val macAddress = scanResults.bleDevice.macAddress
      /*  val intent = Intent(this, DeviceActivity::class.java)
        intent.putExtra(DeviceActivity.EXTRA_MAC_ADDRESS, macAddress)
        startActivity(intent)*/
    }

   /* private fun parseResponse(scanResult: ScanResult): Reading {
        return DeviceReadingParser.parse(scanResult.scanRecord.bytes)
    }*/

    private fun onScanFailure(throwable: Throwable) {

        if (throwable is BleScanException) {
            handleBleScanException(throwable)
        }
    }

    private fun dispose() {
        scanDisposable = null
        resultsAdapter!!.clearScanResults()
        updateButtonUIState()
    }

    private fun updateButtonUIState() {
        scan_toggle_btn.setText(if (isScanning) R.string.stop_scan else R.string.start_scan)
    }
}
