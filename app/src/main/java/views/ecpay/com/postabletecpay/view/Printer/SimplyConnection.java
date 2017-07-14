package views.ecpay.com.postabletecpay.view.Printer;

import android.bluetooth.BluetoothDevice;

import com.bbpos.simplyprint.SimplyPrintController;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by VuongPhuong on 7/14/2017.
 */

public class SimplyConnection implements SimplyPrintController.SimplyPrintControllerListener{

        @Override
        public void onBTv2Detected() {
        }

        @Override
        public void onBTv2DeviceListRefresh(List<BluetoothDevice> foundDevices) {
            MainActivity.foundDevices = foundDevices;
            if(arrayAdapter != null) {
                arrayAdapter.clear();
                for(int i = 0; i < foundDevices.size(); ++i) {
                    arrayAdapter.add(foundDevices.get(i).getName());
                }
                arrayAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onBTv2Connected(BluetoothDevice bluetoothDevice) {
        }

        @Override
        public void onBTv2Disconnected() {
        }

        @Override
        public void onBTv2ScanStopped() {
        }

        @Override
        public void onBTv2ScanTimeout() {
        }

        @Override
        public void onBTv4DeviceListRefresh(List<BluetoothDevice> foundDevices) {
            MainActivity.foundDevices = foundDevices;
            if(arrayAdapter != null) {
                arrayAdapter.clear();
                for(int i = 0; i < foundDevices.size(); ++i) {
                    arrayAdapter.add(foundDevices.get(i).getName());
                }
                arrayAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onBTv4Connected() {
        }

        @Override
        public void onBTv4Disconnected() {
        }

        @Override
        public void onBTv4ScanStopped() {
        }

        @Override
        public void onBTv4ScanTimeout() {
        }

        @Override
        public void onReturnDeviceInfo(Hashtable<String, String> deviceInfoTable) {
            String productId = deviceInfoTable.get("productId");
            String firmwareVersion = deviceInfoTable.get("firmwareVersion");
            String bootloaderVersion = deviceInfoTable.get("bootloaderVersion");
            String hardwareVersion = deviceInfoTable.get("hardwareVersion");
            String isUsbConnected = deviceInfoTable.get("isUsbConnected");
            String isCharging = deviceInfoTable.get("isCharging");
            String batteryLevel = deviceInfoTable.get("batteryLevel");

        }

        @Override
        public void onReturnPrinterResult(SimplyPrintController.PrinterResult printerResult) {
            if(printerResult == SimplyPrintController.PrinterResult.SUCCESS) {
            } else if(printerResult == SimplyPrintController.PrinterResult.NO_PAPER) {
            } else if(printerResult == SimplyPrintController.PrinterResult.WRONG_CMD) {
            } else if(printerResult == SimplyPrintController.PrinterResult.OVERHEAT) {

            }
        }

        @Override
        public void onReturnGetDarknessResult(int value) {
        }

        @Override
        public void onReturnSetDarknessResult(boolean isSuccess) {
            if(isSuccess) {
            } else {
            }
        }

        @Override
        public void onRequestPrinterData(int index, boolean isReprint) {
            controller.sendPrinterData(receipts.get(index));
            if(isReprint) {

            } else {
            }
        }

        @Override
        public void onPrinterOperationEnd() {

        }

        @Override
        public void onBatteryLow(SimplyPrintController.BatteryStatus batteryStatus) {
            if(batteryStatus == SimplyPrintController.BatteryStatus.LOW) {
            } else if(batteryStatus == SimplyPrintController.BatteryStatus.CRITICALLY_LOW) {
            }
        }

        @Override
        public void onBTv2DeviceNotFound() {
        }

        @Override
        public void onError(SimplyPrintController.Error errorState) {
            if(errorState == SimplyPrintController.Error.UNKNOWN) {
            } else if(errorState == SimplyPrintController.Error.CMD_NOT_AVAILABLE) {
            } else if(errorState == SimplyPrintController.Error.TIMEOUT) {
            } else if(errorState == SimplyPrintController.Error.DEVICE_BUSY) {
            } else if(errorState == SimplyPrintController.Error.INPUT_OUT_OF_RANGE) {
            } else if(errorState == SimplyPrintController.Error.INPUT_INVALID) {
            } else if(errorState == SimplyPrintController.Error.CRC_ERROR) {
            } else if(errorState == SimplyPrintController.Error.FAIL_TO_START_BTV2) {
            } else if(errorState == SimplyPrintController.Error.COMM_LINK_UNINITIALIZED) {
            } else if(errorState == SimplyPrintController.Error.BTV2_ALREADY_STARTED) {
            }
        }
}
