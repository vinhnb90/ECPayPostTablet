package views.ecpay.com.postabletecpay.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ecpay.client.test.SecurityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import views.ecpay.com.postabletecpay.model.MainModel;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.request.EntityPostBill.TransactionOffItem;
import views.ecpay.com.postabletecpay.util.entities.response.Base.Respone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBill.BillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer.CustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityData.ListDataResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityDataZip.ListDataZipResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListBookCmisResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListEVNReponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListEvnPCResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.FileGenResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListBillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListCustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityPostBill.PostBillResponse;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.Main.IMainView;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

import static android.content.Context.MODE_PRIVATE;
import static views.ecpay.com.postabletecpay.util.commons.Common.TAG;
import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_OUT_CONNECT;

/**
 * Created by VinhNB_PC on 6/12/2017.
 */

public class MainPresenter implements IMainPresenter {
    private IMainView mIMainView;
    private MainModel mainModel;

    private Handler mHandler = new Handler();

    private String edong;
    private String bookCmis;
    private static List<PayAdapter.DataAdapter> mDataPayAdapter = new ArrayList<>();


    private SoapAPI.AsyncSoapIncludeTimout<PostBillResponse> CurrentPostBillAsync = null;

    public MainPresenter(IMainView mIMainView, String edong) {
        this.mIMainView = mIMainView;
        this.mainModel = new MainModel(mIMainView.getContextView());
        this.edong = edong;

        mainModel.getManagerSharedPref().addSharePref(Common.SHARE_REF_CHANGED_GEN_FILE, MODE_PRIVATE);

    }


    @Override
    public void callPutTransactionOffBill(String edong) {
        boolean fail = TextUtils.isEmpty(edong);
        if (fail)
            return;

        //check network
        Context context = mIMainView.getContextView();
        boolean isHasNetwork = Common.isNetworkConnected(mIMainView.getContextView());

//        if (!isHasNetwork) {
//            mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString());
//            return;
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void synchronizePC() {
        String textMessage = "";
        Context context = mIMainView.getContextView();
        Boolean isErr = false;

//        if (!Common.isConnectingWifi(context) && !isErr) {
//            textMessage = Common.MESSAGE_NOTIFY.ERR_WIFI.toString();
//            isErr = true;
//        }
        if (!Common.isNetworkConnected(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_NETWORK.toString();
            isErr = true;
        }
        if (isErr) {
            mIMainView.showTextMessage(textMessage);
            return;
        }

        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = mIMainView.getContextView().getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, edong, Common.COMMAND_ID.GET_BOOK_CMIS_BY_CASHIER.toString(), versionApp);
        } catch (Exception e) {
            mIMainView.showTextMessage(e.getMessage());
            return;
        }

        String jsonRequestEVN = SoapAPI.getJsonSyncPC(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                configInfo.getAccountId(),
                configInfo.getAccountId());

        if (jsonRequestEVN != null) {
            try {
                final SoapAPI.AsyncSoapSynchronizePC soapSynchronizePC;

                soapSynchronizePC = new SoapAPI.AsyncSoapSynchronizePC(callBack);

                if (soapSynchronizePC.getStatus() != AsyncTask.Status.RUNNING) {
                    soapSynchronizePC.execute(jsonRequestEVN);

                    //thread time out
                    Thread soapevnThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ListEVNReponse listEVNReponse = null;

                            //call time out
                            try {
                                Thread.sleep(TIME_OUT_CONNECT);
                            } catch (InterruptedException e) {
                                mIMainView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                            } finally {
                                if (listEVNReponse == null) {
                                    soapSynchronizePC.callCountdown(soapSynchronizePC);
                                }
                            }
                        }
                    });

                    soapevnThread.start();
                }
            } catch (Exception e) {
                mIMainView.showTextMessage(e.getMessage());
                return;
            }

        }
    }

    private SoapAPI.AsyncSoapSynchronizePC.AsyncSoapSynchronizePCCallBack callBack = new SoapAPI.AsyncSoapSynchronizePC.AsyncSoapSynchronizePCCallBack() {

        @Override
        public void onPre(SoapAPI.AsyncSoapSynchronizePC soapSynchronizeInvoices) {

        }

        @Override
        public void onUpdate(String message) {

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPost(ListEVNReponse response) {
            if (response == null)
                return;

            try {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                //Insert EVN PC
                ListEvnPCResponse listEvnPCResponse;
                String dataEvnPC = response.getBodyListEVNResponse().getListEvnPCLoginResponse();
                JSONArray jaEvnPC = new JSONArray(dataEvnPC);
                for (int i = 0; i < jaEvnPC.length(); i++) {
                    listEvnPCResponse = gson.fromJson(jaEvnPC.getString(i), ListEvnPCResponse.class);
                    if (mainModel.checkEvnPCExist(listEvnPCResponse.getPcId()) == 0) {
                        mainModel.insertEvnPC(listEvnPCResponse);
                    }
                }
                //Insert BOOK CMIS
                ListBookCmisResponse listBookCmisResponse;
                String dataBookCmis = response.getBodyListEVNResponse().getListBookCmissResponse();
                JSONArray jaBookCmis = new JSONArray(dataBookCmis);
                for (int i = 0; i < jaBookCmis.length(); i++) {
                    listBookCmisResponse = gson.fromJson(jaBookCmis.getString(i), ListBookCmisResponse.class);
                    if (mainModel.checkBookCmisExist(listBookCmisResponse.getBookCmis()) == 0) {
                        mainModel.insertBookCmis(listBookCmisResponse);
                    }
                }

                String path = Common.PATH_FOLDER_DOWNLOAD;
                File directory = new File(path);
                File[] allFiles = directory.listFiles();
                if (allFiles.length < 2) {
                    synchronizeFileGen();
                } else {
                    synchronizeData();
                }
            } catch (Exception ex) {
                mIMainView.showTextMessage(ex.getMessage());
            }
        }

        @Override
        public void onTimeOut(SoapAPI.AsyncSoapSynchronizePC soapSynchronizeInvoices) {

        }
    };

    //region đồng bộ hoá đơn
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void synchronizeFileGen() {
        String textMessage = "";
        Context context = mIMainView.getContextView();
        Boolean isErr = false;

//        if (!Common.isConnectingWifi(context) && !isErr) {
//            textMessage = Common.MESSAGE_NOTIFY.ERR_WIFI.toString();
//            isErr = true;
//        }
        if (!Common.isNetworkConnected(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_NETWORK.toString();
            isErr = true;
        }
        if (isErr) {
            mIMainView.showTextMessage(textMessage);
            return;
        }


        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = mIMainView.getContextView().getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, edong, Common.COMMAND_ID.GET_FILE_GEN.toString(), versionApp, mainModel.getPcCode());
        } catch (Exception e) {
            mIMainView.showTextMessage(e.getMessage());
            return;
        }


        String signatureEncrypted = "";
        try {
            String dataSign = Common.getDataSignRSA(
                    configInfo.getAGENT(), configInfo.getCommandId(), configInfo.getAuditNumber(), configInfo.getMacAdressHexValue(),
                    configInfo.getDiskDriver(), mainModel.getPcCode(), configInfo.getAccountId(), configInfo.getPRIVATE_KEY().trim());
            Log.d(TAG, "setupInfoRequest: " + dataSign);
            signatureEncrypted = SecurityUtils.sign(dataSign, configInfo.getPRIVATE_KEY().trim());
            Log.d(TAG, "setupInfoRequest: " + signatureEncrypted);
        } catch (Exception e) {
        }
        configInfo.setSignatureEncrypted(signatureEncrypted);

        Cursor c = mainModel.getAllBookCmis();
        if (c.moveToFirst()) {
            do {
                bookCmis = c.getString(0);

                String jsonRequestZipData = SoapAPI.getJsonRequestSynDataZip(
                        configInfo.getAGENT(),
                        configInfo.getAgentEncypted(),
                        configInfo.getCommandId(),
                        configInfo.getAuditNumber(),
                        configInfo.getMacAdressHexValue(),
                        configInfo.getDiskDriver(),
                        configInfo.getSignatureEncrypted(),
                        c.getString(1),
                        c.getString(0),
                        configInfo.getAccountId());

                if (jsonRequestZipData != null) {
                    try {
                        final SoapAPI.AsyncSoapSynchronizeDataZip soapSynchronizeDataZip;

                        soapSynchronizeDataZip = new SoapAPI.AsyncSoapSynchronizeDataZip(callBackFileGen, mIMainView.getContextView());

                        if (soapSynchronizeDataZip.getStatus() != AsyncTask.Status.RUNNING) {
                            soapSynchronizeDataZip.execute(jsonRequestZipData);

                            //thread time out
                            Thread soapDataThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    ListDataZipResponse listDataZipResponse = null;

                                    //call time out
                                    try {
                                        Thread.sleep(TIME_OUT_CONNECT);
                                    } catch (InterruptedException e) {
                                        mIMainView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                                    } finally {
                                        if (listDataZipResponse == null) {
                                            soapSynchronizeDataZip.callCountdown(soapSynchronizeDataZip);
                                        }
                                    }
                                }
                            });

                            soapDataThread.start();
                        }
                    } catch (Exception e) {
                        mIMainView.showTextMessage(e.getMessage());
                        return;
                    }

                }
            } while (c.moveToNext());
        }
    }

    public void synchronizeData() {
        String textMessage = "";
        Context context = mIMainView.getContextView();
        Boolean isErr = false;

//        if (!Common.isConnectingWifi(context) && !isErr) {
//            textMessage = Common.MESSAGE_NOTIFY.ERR_WIFI.toString();
//            isErr = true;
//        }
        if (!Common.isNetworkConnected(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_NETWORK.toString();
            isErr = true;
        }
        if (isErr) {
            mIMainView.showTextMessage(textMessage);
            return;
        }

        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = mIMainView.getContextView().getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, edong, Common.COMMAND_ID.SYNC_DATA.toString(), versionApp, mainModel.getPcCode());
        } catch (Exception e) {
            mIMainView.showTextMessage(e.getMessage());
            return;
        }


        String signatureEncrypted = "";
        try {
            String dataSign = Common.getDataSignRSA(
                    configInfo.getAGENT(), configInfo.getCommandId(), configInfo.getAuditNumber(), configInfo.getMacAdressHexValue(),
                    configInfo.getDiskDriver(), mainModel.getPcCode(), configInfo.getAccountId(), configInfo.getPRIVATE_KEY().trim());
            Log.d(TAG, "setupInfoRequest: " + dataSign);
            signatureEncrypted = SecurityUtils.sign(dataSign, configInfo.getPRIVATE_KEY().trim());
            Log.d(TAG, "setupInfoRequest: " + signatureEncrypted);
        } catch (Exception e) {
        }
        configInfo.setSignatureEncrypted(signatureEncrypted);

        Cursor c = mainModel.getAllBookCmis();
        if (c.moveToFirst()) {
            do {

                bookCmis = c.getString(0);
                File fileBookCmis = new File(Common.PATH_FOLDER_DOWNLOAD + bookCmis + ".zip");
                if (fileBookCmis.exists()) {
                    long maxIdChanged = mainModel.getMaxIdChanged(bookCmis);
                    String maxDateChanged = mainModel.getMaxDateChanged(bookCmis);
                    String jsonRequestData = SoapAPI.getJsonRequestSynData(
                            configInfo.getAGENT(),
                            configInfo.getAgentEncypted(),
                            configInfo.getCommandId(),
                            configInfo.getAuditNumber(),
                            configInfo.getMacAdressHexValue(),
                            configInfo.getDiskDriver(),
                            configInfo.getSignatureEncrypted(),
                            mainModel.getPcCode(),
                            c.getString(0),
                            maxIdChanged,
                            maxDateChanged,
                            configInfo.getAccountId());


                    if (jsonRequestData != null) {
                        try {
                            final SoapAPI.AsyncSoapSynchronizeData soapSynchronizeData;

                            soapSynchronizeData = new SoapAPI.AsyncSoapSynchronizeData(callBackData, mIMainView.getContextView());

                            if (soapSynchronizeData.getStatus() != AsyncTask.Status.RUNNING) {
                                soapSynchronizeData.execute(jsonRequestData);

                                //thread time out
                                Thread soapDataThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ListDataResponse listDataResponse = null;

                                        //call time out
                                        try {
                                            Thread.sleep(TIME_OUT_CONNECT);
                                        } catch (InterruptedException e) {
                                            mIMainView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                                        } finally {
                                            if (listDataResponse == null) {
                                                soapSynchronizeData.callCountdown(soapSynchronizeData);
                                            }
                                        }
                                    }
                                });

                                soapDataThread.start();
                            }
                        } catch (Exception e) {
                            mIMainView.showTextMessage(e.getMessage());
                            return;
                        }

                    }
                }
            } while (c.moveToNext());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean checkAndPostBill() {

        if (CurrentPostBillAsync != null && !CurrentPostBillAsync.isEndCallSoap()) {
            return false;
        }

        ArrayList<TransactionOffItem> lstTransactionOff = (ArrayList<TransactionOffItem>) mainModel.selectOfflineBill();
        if (lstTransactionOff.size() == 0)
            return true;


        Context context = mIMainView.getContextView();


        ConfigInfo configInfo;

        try {
            configInfo = Common.setupInfoRequest(context, edong, Common.COMMAND_ID.PUT_TRANSACTION_OFF.toString(), Common.getVersionApp(mIMainView.getContextView()));////TO-DO:NEED CHECK HERE AGAIN, mainModel.getPcCode());
        } catch (Exception e) {
            mIMainView.showTextMessage(e.getMessage());
            return false;
        }


//        String signatureEncrypted = "";
//        try {
//            String dataSign = Common.getDataSignRSA(
//                    configInfo.getAGENT(), configInfo.getCommandId(), configInfo.getAuditNumber(), configInfo.getMacAdressHexValue(),
//                    configInfo.getDiskDriver(), mainModel.getPcCode(), configInfo.getAccountId(), configInfo.getPRIVATE_KEY().trim());
//            Log.d(TAG, "setupInfoRequest: " + dataSign);
//            signatureEncrypted = SecurityUtils.sign(dataSign, configInfo.getPRIVATE_KEY().trim());
//            Log.d(TAG, "setupInfoRequest: " + signatureEncrypted);
//        } catch (Exception e) {
//        }
//        configInfo.setSignatureEncrypted(signatureEncrypted);

        for (int i = 0, n = lstTransactionOff.size(); i < n; i++) {
            lstTransactionOff.get(i).setProvide_code(Common.PROVIDER_DEFAULT);
            lstTransactionOff.get(i).setAudit_number(String.valueOf(configInfo.getAuditNumber()));
        }

        String jsonRequestPushBill = SoapAPI.getJsonRequestPostBill(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                lstTransactionOff,
                configInfo.getAccountId());


        if (jsonRequestPushBill != null) {
            try {
                CurrentPostBillAsync = new SoapAPI.AsyncSoapIncludeTimout<PostBillResponse>(mHandler, PostBillResponse.class, new SoapAPI.AsyncSoapIncludeTimout.AsyncSoapCallBack() {
                    @Override
                    public void onPre(SoapAPI.AsyncSoapIncludeTimout soap) {

                    }

                    @Override
                    public void onUpdate(String message) {

                    }

                    @Override
                    public void onPost(SoapAPI.AsyncSoapIncludeTimout soap, Respone response) {
                        if (response != null) {

                        }
                    }

                    @Override
                    public void onTimeOut(SoapAPI.AsyncSoapIncludeTimout soap) {

                    }
                });
                CurrentPostBillAsync.execute(jsonRequestPushBill);
                return true;
            } catch (Exception e) {
                CurrentPostBillAsync = null;
                mIMainView.showTextMessage(e.getMessage());
                return false;
            }

        }
        return false;
    }

    @Override
    public void refreshDataPayAdapter() {
//        mDataPayAdapter.clear();
//        List<PayAdapter.PayEntityAdapter> listKH = new ArrayList<>();
//        //get List Customer
//        List<EntityKhachHang> listCustomer = mainModel.selectAllCustomer(edong);
//
//        //with every one
//        int index = 0;
//        int maxIndex = listCustomer.size();
//        for (; index < maxIndex; index++) {
//            EntityKhachHang customer = listCustomer.get(index);
//
//            PayAdapter.PayEntityAdapter pay = new PayAdapter.PayEntityAdapter();
//            pay.setEdong(edong);
//            pay.setTenKH(customer.getTEN_KHANG());
//            pay.setDiaChi(customer.getDIA_CHI());
//            //get loTrinh
//            pay.setLoTrinh(customer.getLO_TRINH());
//            pay.setMaKH(customer.getMA_KHANG());
//            //get totalMoney
//            long totalMoney = mainModel.countMoneyAllBillOfCustomer(edong, customer.getMA_KHANG());
//            pay.setTongTien(totalMoney);
//            //check status pay
////            boolean isPayed = mainModel.checkStatusPayedOfCustormer(edong, customer.getCode());
////            pay.setPayed(isPayed);
////            pay.setShowBill(customer.isShowBill());
//
//            listKH.add(pay);
//        }
//
//        for(index = 0; index<listKH.size();index++)
//        {
//            PayAdapter.PayEntityAdapter customer = listKH.get(index);
//            String code = customer.getMaKH();
//            List<PayAdapter.BillEntityAdapter> listBill = mainModel.selectInfoBillOfCustomerToRecycler(edong, code);
//            Collections.sort(listBill, PayAdapter.BillEntityAdapter.TermComparatorBillEntityAdapter);
//            PayAdapter.DataAdapter dataAdapter = new PayAdapter.DataAdapter(customer, listBill);
//            mDataPayAdapter.add(dataAdapter);
//        }
    }

    @Override
    public List<PayAdapter.DataAdapter> getDataPayAdapter() {
        return mDataPayAdapter;
    }

    private SoapAPI.AsyncSoapSynchronizeDataZip.AsyncSoapSynchronizeDataZipCallBack callBackFileGen = new SoapAPI.AsyncSoapSynchronizeDataZip.AsyncSoapSynchronizeDataZipCallBack() {
        @Override
        public void onPre(SoapAPI.AsyncSoapSynchronizeDataZip soapSynchronizeInvoices) {

        }

        @Override
        public void onUpdate(String message) {

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPost(ListDataZipResponse response) {
            if (response == null)
                return;


            String sData = response.getBodyListDataResponse().getFile_data();
            if (sData != null && !sData.isEmpty()) {
                byte[] zipByte = org.apache.commons.codec.binary.Base64.decodeBase64(sData.getBytes());

                try {
                    File file = new File(Common.PATH_FOLDER_DOWNLOAD + bookCmis + ".zip");
                    if (!file.exists()) {
                        Common.writeBytesToFile(file, zipByte);
                        if (file.exists()) {
                            File fileText = new File(Common.PATH_FOLDER_DOWNLOAD, "full.txt");
                            if (fileText.exists())
                                fileText.delete();
                            if (Common.unpackZip(Common.PATH_FOLDER_DOWNLOAD, bookCmis + ".zip")) {
                                StringBuilder text = new StringBuilder();
                                try {
                                    BufferedReader br = new BufferedReader(new FileReader(fileText));
                                    String line;

                                    while ((line = br.readLine()) != null) {
                                        text.append(line);//chỗ tạo file đây
                                        text.append('\n');
                                    }
                                    br.close();

                                    JSONObject ja = new JSONObject(text.toString());
                                    GsonBuilder gsonBuilder = new GsonBuilder();
                                    Gson gson = gsonBuilder.create();
                                    FileGenResponse fileGenResponse = gson.fromJson(ja.toString(), FileGenResponse.class);

                                    mainModel.setChangedGenFile(bookCmis, fileGenResponse.getId_changed(), fileGenResponse.getDate_changed());

                                    for (ListCustomerResponse listCustomerResponse : fileGenResponse.getCustomerResponse()) {
                                        if (mainModel.checkCustomerExist(listCustomerResponse.getBodyCustomerResponse().getCustomerCode()) == 0) {
                                            if (mainModel.insertCustomer(listCustomerResponse) != -1) {
                                                Log.i("INFO", "TEST");
                                            }
                                        }
                                    }

                                    for (ListBillResponse listBillResponse : fileGenResponse.getBillResponse()) {
                                        listBillResponse.getBodyBillResponse().setEdong(MainActivity.mEdong);
                                        if (mainModel.checkBillExist(listBillResponse.getBodyBillResponse().getBillId()) == 0) {
                                            if (mainModel.insertBill(listBillResponse) != -1) {
                                                Log.i("INFO", "TEST");
                                            }
                                        }
                                    }

                                    //update mDataPayAdapter
                                    MainPresenter.this.refreshDataPayAdapter();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    ((MainActivity) mIMainView.getContextView()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mIMainView.refreshInfoMain();
                        }
                    });
                }
            }
        }

        @Override
        public void onTimeOut(SoapAPI.AsyncSoapSynchronizeDataZip soapSynchronizeInvoices) {

        }
    };

    private SoapAPI.AsyncSoapSynchronizeData.AsyncSoapSynchronizeDataCallBack callBackData = new SoapAPI.AsyncSoapSynchronizeData.AsyncSoapSynchronizeDataCallBack() {
        @Override
        public void onPre(SoapAPI.AsyncSoapSynchronizeData soapSynchronizeInvoices) {

        }

        @Override
        public void onUpdate(String message) {

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPost(ListDataResponse response) {
            if (response == null)
                return;

            try {
                String sDataCustomer = response.getBodyListDataResponse().getCustomer();
                if (sDataCustomer != null && !sDataCustomer.isEmpty()) {
                    byte[] zipByteCustomer = org.apache.commons.codec.binary.Base64.decodeBase64(sDataCustomer.getBytes());

                    try {
                        String sCustomer = Common.decompress(zipByteCustomer);

                        JSONArray jsonArray = new JSONArray(sCustomer);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject ja = jsonArray.getJSONObject(i);
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            CustomerResponse customerResponse = gson.fromJson(ja.toString(), CustomerResponse.class);
                            if (mainModel.checkCustomerExist(customerResponse.getBodyCustomerResponse().getCustomerCode()) == 0) {
                                if (mainModel.insertCustomer(customerResponse) != -1) {
                                    Log.i("INFO", "SUCCESS");
                                }
                            } else {
                                if (mainModel.updateCustomer(customerResponse) != -1) {
                                    Log.i("INFO", "SUCCESS");
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                String sDataBill = response.getBodyListDataResponse().getBill();
                if (sDataBill != null && !sDataBill.isEmpty()) {
                    byte[] zipByteBill = org.apache.commons.codec.binary.Base64.decodeBase64(sDataBill.getBytes());

                    try {
                        String sCustomer = Common.decompress(zipByteBill);

                        JSONArray jsonArray = new JSONArray(sCustomer);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject ja = jsonArray.getJSONObject(i);
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            BillResponse billResponse = gson.fromJson(ja.toString(), BillResponse.class);
                            billResponse.getBodyBillResponse().setEdong(MainActivity.mEdong);
                            if (mainModel.checkBillExist(billResponse.getBodyBillResponse().getBillId()) == 0) {
                                if (mainModel.insertBill(billResponse) != -1) {
                                    Log.i("INFO", "SUCCESS");
                                }
                            } else {
                                if (mainModel.updateBill(billResponse) != -1) {
                                    Log.i("INFO", "SUCCESS");
                                }
                            }
                        }

                        //update data
                        MainPresenter.this.refreshDataPayAdapter();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ((MainActivity) mIMainView.getContextView()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIMainView.refreshInfoMain();
                    }
                });
            }
        }

        @Override
        public void onTimeOut(SoapAPI.AsyncSoapSynchronizeData soapSynchronizeInvoices) {

        }
    };

    //endregion


    public interface InteractorMainPresenter {
        List<PayAdapter.DataAdapter> getData();

        void refreshData();
    }
}
