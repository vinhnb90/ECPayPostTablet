package views.ecpay.com.postabletecpay.view.Printer;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bbpos.simplyprint.SimplyPrintController;
import com.sewoo.jpos.command.ESCPOSConst;
import com.sewoo.port.android.BluetoothPort;
import com.sewoo.request.android.RequestHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;

import static android.content.ContentValues.TAG;

/**
 * Created by My_PC on 7/20/2017.
 */

public class Printer {
    public static final int THONG_BAO = 1;
    public static final int BIEN_NHAN = 2;
    public static final int BAO_CAO = 3;
    private FragmentActivity context;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothPort bluetoothPort;
    ArrayAdapter<String> adapter;
    private Vector<BluetoothDevice> remoteDevices;
    private static final int REQUEST_ENABLE_BT = 2;
    private ListView listBluetooth;
    private Thread hThread;
    private BroadcastReceiver discoveryResult;
    private BroadcastReceiver searchFinish;
    private static ProgressDialog pDialog;
    private int isThongbao;
    private boolean isSimply;
    protected static final String[] DEVICE_NAMES = new String[] { "BTPTR", "SIMPLY" };
    private SimplyPrintController controller;
    protected static ArrayAdapter<String> arrayAdapter;
    protected List<BluetoothDevice> foundDevice;
    private List<byte[]> receipts = null;
    private Account account;
    private int hdGiao;
    private long tienGiao, tienThu,tienVangLai,tienTraKHt;
    private int hdThu,hdVangLai, hdTraKH;
    private PayAdapter.BillEntityAdapter billEntityAdapter;
    private ArrayList<PayAdapter.BillEntityAdapter> dataAdapter;

    public Printer(FragmentActivity activity,int isThongbao, PayAdapter.BillEntityAdapter billEntityAdapter){
        this.context = activity;
        this.isThongbao = isThongbao;
        this.billEntityAdapter = billEntityAdapter;
    }

    public Printer(FragmentActivity activity,int isThongbao, ArrayList<PayAdapter.BillEntityAdapter> dataAdapter){
        this.context = activity;
        this.isThongbao = isThongbao;
        this.dataAdapter = dataAdapter;
    }
    public Printer(FragmentActivity activity, int isThongbao, Account account, int hdGiao, long tienGiao, int hdThu, long tienThu, int hdVangLai, long tienVangLai, int hdTraKH, long tienTraKHt){
        this.context = activity;
        this.account = account;
        this.isThongbao = isThongbao;
        this.hdGiao = hdGiao;
        this.tienGiao = tienGiao;
        this.tienThu = tienThu;
        this.tienVangLai = tienVangLai;
        this.tienTraKHt = tienTraKHt;
        this.hdThu = hdThu;
        this.hdVangLai = hdVangLai;
        this.hdTraKH = hdTraKH;

    }
    public void Action(){
        if (!Common.isBluetoothConnected) {
            dialogChonMayIn();
        }else
        if (!isSimply) {
            printer(billEntityAdapter);
        }else {
            receipts = new ArrayList<byte[]>();
            for (int i = 0, n = dataAdapter.size(); i < n; i ++) {
                receipts.add(ReceiptUtility.genReceiptTest(context,dataAdapter.get(i)));
            }

            controller.startPrinting(receipts.size(), 120, 120);
        }
    }

    //region Chon May In
    public void dialogChonMayIn() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_listbluetooth);

        final String[] connections = new String[2];
        connections[0] = "Simply";
        connections[1] = "Sewoo";

        ListView listView = (ListView)dialog.findViewById(R.id.dl_list_bluetooth);
        listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, connections));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    isSimply = true;
                    controller = new SimplyPrintController(context, new SimplyConnection());
                    Object[] pairedObjects = BluetoothAdapter.getDefaultAdapter().getBondedDevices().toArray();
                    final BluetoothDevice[] pairedDevices = new BluetoothDevice[pairedObjects.length];
                    for(int i = 0; i < pairedObjects.length; ++i) {
                        pairedDevices[i] = (BluetoothDevice)pairedObjects[i];
                    }
                    controller.scanBTv2(DEVICE_NAMES, 120);
                    final ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
                    for (int i = 0; i < pairedDevices.length; ++i) {
                        mArrayAdapter.add(pairedDevices[i].getName());
                    }
                    dialog.dismiss();
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_listbluetooth);

                    ListView listView = (ListView)dialog.findViewById(R.id.dl_list_bluetooth);
                    listView.setAdapter(mArrayAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            controller.startBTv2(pairedDevices[position]);
                            dialog.dismiss();
                        }

                    });
                    dialog.show();
                } else if(position == 1) {
                    bluetoothSetup();
                    isSimply = false;
                    dialog.dismiss();
                    if (!bluetoothPort.isConnected()) {
                        if (!mBluetoothAdapter.isDiscovering()) {
                            clearBtDevData();
                            adapter.clear();
                            mBluetoothAdapter.startDiscovery();
                            pDialog = new ProgressDialog(context);
                            pDialog.show();
                            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            pDialog.setContentView(R.layout.progress_dialog);
                        } else {
                            mBluetoothAdapter.cancelDiscovery();
                        }
                    }else {
                            printer(billEntityAdapter);
                    }
                }
            }

        });
        dialog.show();
    }

    //endregion

    //region Bluetooth
    public void bluetoothSetup() {
        // Initialize
        bluetoothPort = BluetoothPort.getInstance();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            openBluetooth();
        }else {
            openBluetooth();
        }
    }

    private static final String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "//temp";
    private static final String fileName = dir + "//BTPrinter";
    private String lastConnAddr;

    // region Setup Bluetooth
    private void openBluetooth(){
        loadSettingFile();
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
        addPairedDevices();
        discoveryResult = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String key;
                BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(remoteDevice != null)
                {
                    if(remoteDevice.getBondState() != BluetoothDevice.BOND_BONDED)
                    {
                        key = remoteDevice.getName() +"\n["+remoteDevice.getAddress()+"]";
                    }
                    else
                    {
                        key = remoteDevice.getName() +"\n["+remoteDevice.getAddress()+"] [Paired]";
                    }
                    if(bluetoothPort.isValidAddress(remoteDevice.getAddress()))
                    {
                        remoteDevices.add(remoteDevice);
                        adapter.add(key);
                    }
                }
            }
        };
        context.registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        searchFinish = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                showDialogListBlueTooth();
                pDialog.dismiss();
            }
        };
        context.registerReceiver(searchFinish, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));

    }

    private void loadSettingFile() {
        int rin = 0;
        char[] buf = new char[128];
        try {
            FileReader fReader = new FileReader(fileName);
            rin = fReader.read(buf);
            if (rin > 0) {
                lastConnAddr = new String(buf, 0, rin);
            }
            fReader.close();
        } catch (FileNotFoundException e) {
            Log.i(TAG, "Connection history not exists.");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void saveSettingFile() {
        try {
            File tempDir = new File(dir);
            if (!tempDir.exists()) {
                tempDir.mkdir();
            }
            FileWriter fWriter = new FileWriter(fileName);
            if (lastConnAddr != null)
                fWriter.write(lastConnAddr);
            fWriter.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    // clear device data used list.
    private void clearBtDevData() {
        remoteDevices = new Vector<BluetoothDevice>();
    }

    // add paired device to list
    private void addPairedDevices() {
        BluetoothDevice pairedDevice;
        Iterator<BluetoothDevice> iter = (mBluetoothAdapter.getBondedDevices()).iterator();
        while (iter.hasNext()) {
            pairedDevice = iter.next();
            if (bluetoothPort.isValidAddress(pairedDevice.getAddress())) {
                remoteDevices.add(pairedDevice);
                adapter.add(pairedDevice.getName() + "\n[" + pairedDevice.getAddress() + "] [Paired]");
            }
        }
    }

    private void showDialogListBlueTooth() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_listbluetooth);
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        listBluetooth = (ListView) dialog.findViewById(R.id.dl_list_bluetooth);
        listBluetooth.setAdapter(adapter);
        // Connect - click the List item.
        listBluetooth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                BluetoothDevice btDev = remoteDevices.elementAt(arg2);
                try {
                    if (mBluetoothAdapter.isDiscovering()) {
                        mBluetoothAdapter.cancelDiscovery();
                    }
                    btConn(btDev);
                    dialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        dialog.show();
    }

    // Bluetooth Connection method.
    private void btConn(final BluetoothDevice btDev) throws IOException {
        new connTask().execute(btDev);
    }

    // Bluetooth Disconnection method.
    private void btDisconn() {
        try {
            bluetoothPort.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        if ((hThread != null) && (hThread.isAlive()))
            hThread.interrupt();
        // UI
        listBluetooth.setEnabled(true);
    }

    // Bluetooth Connection Task.
    private class connTask extends AsyncTask<BluetoothDevice, Void, Integer> {
        private final ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(BluetoothDevice... params) {
            Integer retVal = null;
            try {
                bluetoothPort.connect(params[0]);
                lastConnAddr = params[0].getAddress();
                retVal = new Integer(0);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                retVal = new Integer(-1);
            }
            return retVal;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result.intValue() == 0)	// Connection success.
            {
                RequestHandler rh = new RequestHandler();
                hThread = new Thread(rh);
                hThread.start();
                // UI
                listBluetooth.setEnabled(false);
                if(dialog.isShowing())
                    dialog.dismiss();
                Toast toast = Toast.makeText(context, "Đã kết nối bluetooth", Toast.LENGTH_SHORT);
                toast.show();
                Common.isBluetoothConnected =true;
                printer(billEntityAdapter);
            }
            else	// Connection failed.
            {
                if(dialog.isShowing())
                    dialog.dismiss();
            }
            super.onPostExecute(result);
        }
    }

    private void printer(PayAdapter.BillEntityAdapter bill){
        int results = 0;
        ESCPOSSample sample = new ESCPOSSample();
        try {
            if (isThongbao == BIEN_NHAN) {
                results = sample.checkDienLuc(context,bill);
            }
            else if (isThongbao ==THONG_BAO ) {
                for (int i = 0, n = dataAdapter.size(); i < n; i ++) {
                        results = sample.Thongbao(context, dataAdapter.get(i));
                }
            }else {
                results = sample.baoCao(context,account,hdGiao,tienGiao,hdThu,tienThu,hdVangLai,tienVangLai,hdTraKH,tienTraKHt);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (results != 0) {
            switch (results) {
                case ESCPOSConst.STS_PAPEREMPTY:
                    Log.e(getClass().getName(),"Paper empty");
                    break;
                case ESCPOSConst.STS_COVEROPEN:
                    Log.e(getClass().getName(),"cover open");
                    break;
                case ESCPOSConst.STS_PAPERNEAREMPTY:
                    Log.e(getClass().getName(),"page near empty");
                    break;
            }
        }
    }

    public void stopConnection() {
        SimplyPrintController.ConnectionMode connectionMode = controller.getConnectionMode();
        if(connectionMode == SimplyPrintController.ConnectionMode.BLUETOOTH_2) {
            controller.stopBTv2();
        } else if(connectionMode == SimplyPrintController.ConnectionMode.BLUETOOTH_4) {
            controller.disconnectBTv4();
        }
    }

    private class SimplyConnection implements SimplyPrintController.SimplyPrintControllerListener{
        @Override
        public void onBTv2Detected() {
        }

        @Override
        public void onBTv2DeviceListRefresh(List<BluetoothDevice> foundDevices) {
            foundDevice = foundDevices;
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
            Common.isBluetoothConnected = true;
            receipts = new ArrayList<byte[]>();
//                receipts.add(ReceiptUtility.genReceipt(MainActivity.this));
            for (int i = 0, n = dataAdapter.size(); i < n; i ++) {
                receipts.add(ReceiptUtility.genReceiptTest(context,dataAdapter.get(i)));
            }
            controller.startPrinting(receipts.size(), 120, 120);
        }

        @Override
        public void onBTv2Disconnected() {
            Log.e(getClass().getName(),"Disconnected");
        }

        @Override
        public void onBTv2ScanStopped() {
            Toast.makeText(context,"error",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onBTv2ScanTimeout() {
            Log.e(getClass().getName(),"Scan Timeout");
        }

        @Override
        public void onBTv4DeviceListRefresh(List<BluetoothDevice> foundDevices) {
            foundDevice = foundDevices;
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
}
