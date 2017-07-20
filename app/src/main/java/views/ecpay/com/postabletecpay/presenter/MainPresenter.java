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
import java.util.Date;
import java.util.List;

import views.ecpay.com.postabletecpay.model.MainModel;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.EntityHoaDonThu;
import views.ecpay.com.postabletecpay.util.entities.EntityLichSuThanhToan;
import views.ecpay.com.postabletecpay.util.entities.request.EntityPostBill.TransactionOffItem;
import views.ecpay.com.postabletecpay.util.entities.response.Base.Respone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBill.BillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBillOnline.BillingOnlineRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBillOnline.BodyBillingOnlineRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer.CustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityData.ListDataResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityDataZip.ListDataZipResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListBookCmisResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListEVNReponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListEvnPCResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.FileGenResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListBillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListCustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityPostBill.BodyPostBillReponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityPostBill.PostBillResponse;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.Main.IMainView;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

import static android.content.Context.MODE_PRIVATE;
import static views.ecpay.com.postabletecpay.util.commons.Common.TAG;
import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_OUT_CONNECT;
import static views.ecpay.com.postabletecpay.util.commons.Common.ZERO;

/**
 * Created by VinhNB_PC on 6/12/2017.
 */

public class MainPresenter implements IMainPresenter {
    private IMainView mIMainView;
    private MainModel mainModel;

    private Handler mHandler = new Handler();

    private String edong;
    //    private String bookCmis;
    private static List<PayAdapter.DataAdapter> mDataPayAdapter = new ArrayList<>();

    private int allProcessDownload;
    private List<SoapAPI.AsyncSoapIncludeTimout<BillingOnlineRespone>> CurrentPostBillAsync = new ArrayList<>();

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
                    if (mainModel.checkEvnPCExist(listEvnPCResponse.getPcId(), edong) == 0) {
                        mainModel.insertEvnPC(listEvnPCResponse, edong);
                    }
                }
                //Insert BOOK CMIS
                List<ListBookCmisResponse> listBookCmisExist = new ArrayList<>();
                List<ListBookCmisResponse> listBookCmisNeedDownload = new ArrayList<>();

                String dataBookCmis = response.getBodyListEVNResponse().getListBookCmissResponse();
                JSONArray jaBookCmis = new JSONArray(dataBookCmis);
                for (int i = 0; i < jaBookCmis.length(); i++) {
                    ListBookCmisResponse listBookCmisResponse = gson.fromJson(jaBookCmis.getString(i), ListBookCmisResponse.class);

                    if (mainModel.checkBookCmisExist(listBookCmisResponse.getBookCmis()) == 0) {
                        mainModel.insertBookCmis(listBookCmisResponse);
                    }

                    File fileDownload = new File(Common.PATH_FOLDER_DOWNLOAD + listBookCmisResponse.getBookCmis() + "_" + edong + ".zip");

                    if (!fileDownload.exists()) {
                        listBookCmisNeedDownload.add(listBookCmisResponse);
                    } else {
                        listBookCmisExist.add(listBookCmisResponse);
                    }
                }

                //start UI prgressbar
                mIMainView.startShowPbarDownload();
                allProcessDownload = listBookCmisNeedDownload.size() + listBookCmisExist.size();
                synchronizeFileGen(listBookCmisNeedDownload);
                synchronizeData(listBookCmisExist);
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
    public void synchronizeFileGen(List<ListBookCmisResponse> listBookCmisNeedDownload) {
        String textMessage = "";
        Context context = mIMainView.getContextView();
        Boolean isErr = false;

        if (!Common.isNetworkConnected(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_NETWORK.toString();
            isErr = true;
        }
        if (isErr) {
            mIMainView.showTextMessage(textMessage);
            return;
        }

        for (int i = 0; i < listBookCmisNeedDownload.size(); i++) {
            final String bookCmis = listBookCmisNeedDownload.get(i).getBookCmis();
            String pcCodeExt = listBookCmisNeedDownload.get(i).getPcCodeExt();

            ConfigInfo configInfo;
            String versionApp = "";
            try {
                versionApp = mIMainView.getContextView().getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            try {
                configInfo = Common.setupInfoRequest(context, edong, Common.COMMAND_ID.GET_FILE_GEN.toString(), versionApp, pcCodeExt);
            } catch (Exception e) {
                mIMainView.showTextMessage(e.getMessage());
                return;
            }


            String signatureEncrypted = "";
            try {
                String dataSign = Common.getDataSignRSA(
                        configInfo.getAGENT(), configInfo.getCommandId(), configInfo.getAuditNumber(), configInfo.getMacAdressHexValue(),
                        configInfo.getDiskDriver(), pcCodeExt, configInfo.getAccountId(), configInfo.getPRIVATE_KEY().trim());
                Log.d(TAG, "setupInfoRequest: " + dataSign);
                signatureEncrypted = SecurityUtils.sign(dataSign, configInfo.getPRIVATE_KEY().trim());
                Log.d(TAG, "setupInfoRequest: " + signatureEncrypted);
            } catch (Exception e) {
            }
            configInfo.setSignatureEncrypted(signatureEncrypted);

            String jsonRequestZipData = SoapAPI.getJsonRequestSynDataZip(
                    configInfo.getAGENT(),
                    configInfo.getAgentEncypted(),
                    configInfo.getCommandId(),
                    configInfo.getAuditNumber(),
                    configInfo.getMacAdressHexValue(),
                    configInfo.getDiskDriver(),
                    configInfo.getSignatureEncrypted(),
                    pcCodeExt,
                    bookCmis,
                    configInfo.getAccountId());

            if (jsonRequestZipData != null) {
                try {
                    SoapAPI.AsyncSoapSynchronizeDataZip.AsyncSoapSynchronizeDataZipCallBack callBackFileGen = new SoapAPI.AsyncSoapSynchronizeDataZip.AsyncSoapSynchronizeDataZipCallBack() {
                        @Override
                        public void onPre(SoapAPI.AsyncSoapSynchronizeDataZip soapSynchronizeInvoices) {

                        }

                        @Override
                        public void onUpdate(String message) {

                        }

                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onPost(ListDataZipResponse response) {
                            //nếu kết thúc thì giảm đi 1 thread và kiểm tra nếu là thread cuối thì tắt progress dialog
                            allProcessDownload--;

                            if (response == null) {
                                if (allProcessDownload == ZERO)
                                    mIMainView.finishHidePbarDownload();
                                return;
                            }

                            String sData = response.getBodyListDataResponse().getFile_data();
                            if (sData != null && !sData.isEmpty()) {
                                byte[] zipByte = org.apache.commons.codec.binary.Base64.decodeBase64(sData.getBytes());

                                try {
                                    File file = new File(Common.PATH_FOLDER_DOWNLOAD + bookCmis + "_" + edong + ".zip");
                                    if (!file.exists()) {
                                        Common.writeBytesToFile(file, zipByte);
                                        if (file.exists()) {
                                            File fileText = new File(Common.PATH_FOLDER_DOWNLOAD, "full.txt");
                                            if (fileText.exists())
                                                fileText.delete();
                                            if (Common.unpackZip(Common.PATH_FOLDER_DOWNLOAD, bookCmis + "_" + edong + ".zip")) {
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

                                                    mainModel.setChangedGenFile(edong, bookCmis, fileGenResponse.getId_changed(), fileGenResponse.getDate_changed());

                                                    for (ListCustomerResponse listCustomerResponse : fileGenResponse.getCustomerResponse()) {
                                                        if(listCustomerResponse.getBodyCustomerResponse().getCustomerCode().equals("PH10000012075")) {
                                                            Log.e(TAG, "synchronizeFileGen Customer " + listCustomerResponse.getBodyCustomerResponse().getName());
                                                        }
                                                        if (mainModel.checkCustomerExist(listCustomerResponse.getBodyCustomerResponse().getCustomerCode()) == 0) {
                                                            if (mainModel.insertCustomer(listCustomerResponse) != -1) {
                                                                Log.i("INFO", "TEST");
                                                            }
                                                        }
                                                    }

                                                    Log.e(TAG, "synchronizeFileGen list bill " + fileGenResponse.getBillResponse().size());
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
                                                    throw e;
                                                } catch (JSONException e) {
                                                    throw e;
                                                }
                                            }
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
                                    if (allProcessDownload == ZERO)
                                        mIMainView.finishHidePbarDownload();
                                }

                                ((MainActivity) mIMainView.getContextView()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mIMainView.refreshInfoMain();
                                    }
                                });
                                if (allProcessDownload == ZERO)
                                    mIMainView.finishHidePbarDownload();
                            }
                        }

                        @Override
                        public void onTimeOut(final SoapAPI.AsyncSoapSynchronizeDataZip soapSynchronizeInvoices) {
                            // time out thì tắt tiến trình
                            soapSynchronizeInvoices.setEndCallSoap(true);
                            soapSynchronizeInvoices.cancel(true);
                        }
                    };

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
                                    Log.e(TAG, "run: " + e.getMessage());
                                } finally {
                                    // kết thúc time timeout mà vẫn không có dữ liệu trả về thì gọi callCountdown
                                    if (listDataZipResponse == null) {
                                        soapSynchronizeDataZip.callCountdown(soapSynchronizeDataZip);
                                    }
                                }
                            }
                        });

                        soapDataThread.start();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "synchronizeFileGen: " + e.getMessage());
                    return;
                }

            } else {
                //nếu lỗi thì giảm allProcessDownload xuống 1;
                allProcessDownload--;
            }
//                }
        }
    }

    public void synchronizeData(List<ListBookCmisResponse> listBookCmisExist) {
        String textMessage = "";
        Context context = mIMainView.getContextView();
        Boolean isErr = false;

        if (!Common.isNetworkConnected(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_NETWORK.toString();
            isErr = true;
        }
        if (isErr) {
            mIMainView.showTextMessage(textMessage);
            return;
        }


        for (int i = 0; i < listBookCmisExist.size(); i++) {
            String bookCmis = listBookCmisExist.get(i).getBookCmis();
            String pcCodeExt = listBookCmisExist.get(i).getPcCodeExt();

            ConfigInfo configInfo;
            String versionApp = "";
            try {
                versionApp = mIMainView.getContextView().getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            try {
                configInfo = Common.setupInfoRequest(context, edong, Common.COMMAND_ID.SYNC_DATA.toString(), versionApp, pcCodeExt);
            } catch (Exception e) {
                mIMainView.showTextMessage(e.getMessage());
                return;
            }

            String signatureEncrypted = "";
            try {
                String dataSign = Common.getDataSignRSA(
                        configInfo.getAGENT(), configInfo.getCommandId(), configInfo.getAuditNumber(), configInfo.getMacAdressHexValue(),
                        configInfo.getDiskDriver(), pcCodeExt, configInfo.getAccountId(), configInfo.getPRIVATE_KEY().trim());
                Log.d(TAG, "setupInfoRequest: " + dataSign);
                signatureEncrypted = SecurityUtils.sign(dataSign, configInfo.getPRIVATE_KEY().trim());
                Log.d(TAG, "setupInfoRequest: " + signatureEncrypted);
            } catch (Exception e) {
            }
            configInfo.setSignatureEncrypted(signatureEncrypted);


            File fileBookCmis = new File(Common.PATH_FOLDER_DOWNLOAD + bookCmis + "_" + edong + ".zip");
            if (fileBookCmis.exists()) {
                long maxIdChanged = mainModel.getMaxIdChanged(edong, bookCmis);
                String maxDateChanged = mainModel.getMaxDateChanged(edong, bookCmis);
                String jsonRequestData = SoapAPI.getJsonRequestSynData(
                        configInfo.getAGENT(),
                        configInfo.getAgentEncypted(),
                        configInfo.getCommandId(),
                        configInfo.getAuditNumber(),
                        configInfo.getMacAdressHexValue(),
                        configInfo.getDiskDriver(),
                        configInfo.getSignatureEncrypted(),
                        pcCodeExt,
                        bookCmis,
                        maxIdChanged,
                        maxDateChanged,
                        configInfo.getAccountId());


                if (jsonRequestData != null) {
                    try {
                        SoapAPI.AsyncSoapSynchronizeData.AsyncSoapSynchronizeDataCallBack callBackData = new SoapAPI.AsyncSoapSynchronizeData.AsyncSoapSynchronizeDataCallBack() {
                            @Override
                            public void onPre(SoapAPI.AsyncSoapSynchronizeData soapSynchronizeInvoices) {

                            }

                            @Override
                            public void onUpdate(String message) {

                            }

                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onPost(ListDataResponse response) {
                                //nếu kết thúc thì giảm đi 1 thread và kiểm tra nếu là thread cuối thì tắt progress dialog
                                allProcessDownload--;

                                if (response == null) {
                                    if (allProcessDownload == ZERO)
                                        mIMainView.finishHidePbarDownload();
                                    return;
                                }

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
                                                if(customerResponse.getBodyCustomerResponse().getCustomerCode().equals("PH10000012075")) {
                                                    Log.e(TAG, "synchronizeData Customer " + customerResponse.getBodyCustomerResponse().getName());
                                                }
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
                                            Log.e(TAG, "synchronizeData list bill " + jsonArray.length());
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
                                            throw e;
                                        } catch (JSONException e) {
                                            throw e;
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
                                    if (allProcessDownload == ZERO)
                                        mIMainView.finishHidePbarDownload();
                                }

                                ((MainActivity) mIMainView.getContextView()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mIMainView.refreshInfoMain();
                                    }
                                });

                                if (allProcessDownload == ZERO)
                                    mIMainView.finishHidePbarDownload();
                            }

                            @Override
                            public void onTimeOut(final SoapAPI.AsyncSoapSynchronizeData soapSynchronizeInvoices) {
                                // time out thì tắt tiến trình
                                soapSynchronizeInvoices.setEndCallSoap(true);
                                soapSynchronizeInvoices.cancel(true);
                            }
                        };

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
                        Log.e(TAG, "synchronizeData: " + e.getMessage());
                        return;
                    }
                } else {
                    // nếu lỗi tạo thread thì giảm
                    allProcessDownload--;
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean checkAndPostBill() {

        if (CurrentPostBillAsync != null && !CurrentPostBillAsync.isEmpty()) {
            return false;
        }

        List<PayAdapter.BillEntityAdapter> lstTransactionOff = mainModel.selectOfflineBill();
        if (lstTransactionOff.size() == 0)
            return true;


        for (int i = 0, n = lstTransactionOff.size(); i < n; i++) {
            try {
                payOnline(lstTransactionOff.get(i));
            } catch (Exception e) {
            }
        }

        return false;
    }



    @Override
    public void refreshDataPayAdapter() {
    }

    @Override
    public List<PayAdapter.DataAdapter> getDataPayAdapter() {
        return mDataPayAdapter;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void payOnline(final PayAdapter.BillEntityAdapter bill) throws Exception {

        ConfigInfo configInfo = null;
        try {
            configInfo = Common.setupInfoRequest(mIMainView.getContextView(), MainActivity.mEdong, Common.COMMAND_ID.BILLING.toString(), Common.getVersionApp(mIMainView.getContextView()));
        } catch (Exception e) {
            bill.setMessageError(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());

            return;
        }


        //Số ví edong thực hiện thanh toán
        String phone = MainActivity.mEdong;
        Long amount = bill.getTIEN_THANH_TOAN();

        //Phiên làm việc của TNV đang thực hiện
        String session = mainModel.getSession(MainActivity.mEdong);

        /**
         * Luồng quầy thu đang năng : DT0813
         * Luồng tài khoản tiền điện : DT0807
         * Còn lại sử dụng DT0605
         *
         * ECPAY mặc định sử dụng DT0605
         */

        String partnerCode = Common.PARTNER_CODE_DEFAULT;

        String providerCode = Common.PROVIDER_DEFAULT;

        //create request to server
        String jsonRequestBillOnline = SoapAPI.getJsonRequestBillOnline(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                bill.getMA_KHACH_HANG(),
                session,
                bill.getBillId(),
                amount,
                phone,
                providerCode,
                partnerCode,
                configInfo.getAccountId());

        if (jsonRequestBillOnline == null) {
            bill.setMessageError(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_REQUEST.toString());

            return;
        }


        final SoapAPI.AsyncSoapIncludeTimout<BillingOnlineRespone> billingOnlineResponeAsyncSoap = new SoapAPI.AsyncSoapIncludeTimout<BillingOnlineRespone>(mHandler, BillingOnlineRespone.class,
                new SoapAPI.AsyncSoapIncludeTimout.AsyncSoapCallBack() {
                    @Override
                    public void onPre(SoapAPI.AsyncSoapIncludeTimout soap) {

                    }

                    @Override
                    public void onUpdate(final String message) {
                        CurrentPostBillAsync.remove(this);
                    }

                    @Override
                    public void onPost(SoapAPI.AsyncSoapIncludeTimout soap, Respone response) {

                        if (response == null) {
                            CurrentPostBillAsync.remove(this);
                            return;
                        }

                        //TODO Xử lý nhận kết quả thanh toán các hóa đơn từ server ----- Nếu không thành công
                        final Common.CODE_REPONSE_BILL_ONLINE codeResponse = Common.CODE_REPONSE_BILL_ONLINE.findCodeMessage(response.getFooter().getResponseCode());
                        BodyBillingOnlineRespone body = response.getBodyByType();

                        if (codeResponse.getCode().equalsIgnoreCase(Common.CODE_REPONSE_BILL_ONLINE.e825.getCode())) {
                            /**
                             * Trường hợp hóa đơn đã được thanh toán bởi nguồn khác: Không thực hiện thanh toán hóa đơn
                             */
                            hoaDonDaThanhToanBoiNguonKhac(body);
                            bill.setTRANG_THAI_TT(Common.TRANG_THAI_TTOAN.TTOAN_BOI_NGUON_KHAC.getCode());
                            bill.setVI_TTOAN("");
                            bill.setMessageError(Common.CODE_REPONSE_BILL_ONLINE.ex10006.getMessage());

                        }else if (codeResponse.getCode().equalsIgnoreCase(Common.CODE_REPONSE_BILL_ONLINE.e814.getCode())) {
                            /**
                             * Trường hợp hóa đơn đã được thanh toán bởi ví khác: Không thực hiện thanh toán hóa đơn
                             */
                            hoaDonDaThanhToanBoiViKhac(body);
                            bill.setTRANG_THAI_TT(Common.TRANG_THAI_TTOAN.TTOAN_BOI_VI_KHAC.getCode());
                            bill.setVI_TTOAN(body.getPaymentEdong());
                            bill.setMessageError(Common.CODE_REPONSE_BILL_ONLINE.ex10007.getMessage());
                        }else if (!codeResponse.getCode().equalsIgnoreCase(Common.CODE_REPONSE_BILL_ONLINE.e000.getCode())
                                &&
                                !codeResponse.getCode().equalsIgnoreCase(Common.CODE_REPONSE_BILL_ONLINE.e095.getCode())) {
                            /**
                             * Hóa đơn chấm nợ lỗi: Không thực hiện thanh toán hóa đơn
                             */
                            hoaDonChamNoLoi(body);
                            bill.setMessageError(Common.CODE_REPONSE_BILL_ONLINE.ex10009.getMessage());
                        }else
                        {
                            String gateEVN = body.getBillingType();
                            if(codeResponse.getCode().equalsIgnoreCase(Common.CODE_REPONSE_BILL_ONLINE.e000.getCode()) && gateEVN.equalsIgnoreCase("ON"))
                            {//Thanh Toan THanh Cong Trong Gio Mo Ket Noi ECPay -> EVN
                                hoaDonThanhCongTrongGioMoKetNoi(body);
                            }else
                            {//Thanh Toan Thanh Cong Trong Gio Dong Ket Noi ECPay -> EVN
                                hoaDonThanhCongTrongGioDongKetNoi(body);
                            }

                            bill.setTRANG_THAI_TT(Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());
                            bill.setVI_TTOAN(MainActivity.mEdong);
                            bill.setMessageError(Common.CODE_REPONSE_BILL_ONLINE.ex10008.getMessage());
                        }



                        CurrentPostBillAsync.remove(soap);
                    }

                    @Override
                    public void onTimeOut(SoapAPI.AsyncSoapIncludeTimout soap) {
                        CurrentPostBillAsync.remove(soap);
                    }
                });

        CurrentPostBillAsync.add(billingOnlineResponeAsyncSoap);
        billingOnlineResponeAsyncSoap.execute(jsonRequestBillOnline);
    }


    private void hoaDonThanhCongTrongGioDongKetNoi(BodyBillingOnlineRespone respone) {
        //TODO mark
        /**
         * Trong giờ mở cổng kết nối thanh toán từ ECPAY sang EVN
         * (Trường hợp thanh toán thành công mã e000 && Trạng thái thanh toán ON/OFF billingType API trả về = ON)
         * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
         * Ví thanh toán edong = Ví đăng nhập
         * Trạng thái thanh toán status = 02_Đã thanh toán
         */

        mainModel.updateHoaDonNo(respone.getBillId(), Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode(), MainActivity.mEdong);

        //Lưu vào danh sách THU
        EntityHoaDonThu entityHoaDonThu = EntityHoaDonThu.copy(mainModel.getHoaDonNo(respone.getBillId()));

        //Update Hoa Don Thu
        mainModel.updateHoaDonThu(String.valueOf(respone.getBillId()), MainActivity.mEdong, Common.TRANG_THAI_TTOAN.DA_TTOAN,
                Common.TRANG_THAI_CHAM_NO.DANG_CHO_XU_LY.getCode(), Common.TRANG_THAI_DAY_CHAM_NO.DA_DAY.getCode(), Common.parse(new Date(), Common.DATE_TIME_TYPE.FULL.toString()), "");



        //Lưu lại lịch sử thanh toán
        EntityLichSuThanhToan entityLichSuThanhToan = EntityLichSuThanhToan.copy(entityHoaDonThu);
        entityLichSuThanhToan.setNGAY_PHAT_SINH(new Date());
        entityLichSuThanhToan.setMA_GIAO_DICH(Common.MA_GIAO_DICH.DAY_CHAM_NO.getCode());
        mainModel.insertLichSuThanhToan(entityLichSuThanhToan);
    }



    private void hoaDonThanhCongTrongGioMoKetNoi(BodyBillingOnlineRespone respone) {
        //TODO mark
        /**
         * Trong giờ mở cổng kết nối thanh toán từ ECPAY sang EVN
         * (Trường hợp thanh toán thành công mã e000 && Trạng thái thanh toán ON/OFF billingType API trả về = ON)
         * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
         * Ví thanh toán edong = Ví đăng nhập
         * Trạng thái thanh toán status = 02_Đã thanh toán
         */

        mainModel.updateHoaDonNo(respone.getBillId(), Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode(), MainActivity.mEdong);

        //Lưu vào danh sách THU
        EntityHoaDonThu entityHoaDonThu = mainModel.getHoaDonThu(respone.getBillId());


        //Update Hoa Don Thu
        mainModel.updateHoaDonThu(String.valueOf(respone.getBillId()), MainActivity.mEdong, Common.TRANG_THAI_TTOAN.DA_TTOAN,
                Common.TRANG_THAI_CHAM_NO.DA_CHAM.getCode(), Common.TRANG_THAI_DAY_CHAM_NO.DA_DAY.getCode(), Common.parse(new Date(), Common.DATE_TIME_TYPE.FULL.toString()), "");




        //Lưu lại lịch sử thanh toán
        EntityLichSuThanhToan entityLichSuThanhToan = EntityLichSuThanhToan.copy(entityHoaDonThu);
        entityLichSuThanhToan.setNGAY_PHAT_SINH(new Date());
        entityLichSuThanhToan.setMA_GIAO_DICH(Common.MA_GIAO_DICH.DAY_CHAM_NO.getCode());
        mainModel.insertLichSuThanhToan(entityLichSuThanhToan);
    }

    private void hoaDonChamNoLoi(BodyBillingOnlineRespone respone) {
        /**
         * Hóa đơn chấm nợ lỗi: Không thực hiện thanh toán hóa đơn
         * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
         * Ví thanh toán edong = Ví đăng nhập
         * Trạng thái thanh toán status = (SRS không yêu cầu cập nhật trường này)
         */

        mainModel.updateHoaDonNo(respone.getBillId(), Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode(), "");

        //Lưu vào danh sách THU
        EntityHoaDonThu entityHoaDonThu = mainModel.getHoaDonThu(respone.getBillId());

        //Update Hoa Don Thu
        mainModel.updateHoaDonThu(String.valueOf(respone.getBillId()), "", Common.TRANG_THAI_TTOAN.DA_TTOAN,
                Common.TRANG_THAI_CHAM_NO.CHAM_LOI.getCode(), Common.TRANG_THAI_DAY_CHAM_NO.DA_DAY.getCode(), Common.parse(new Date(), Common.DATE_TIME_TYPE.FULL.toString()), Common.TRANG_THAI_HOAN_TRA.CHUA_TRA.getCode());


        //Lưu lại lịch sử thanh toán
        EntityLichSuThanhToan entityLichSuThanhToan = EntityLichSuThanhToan.copy(entityHoaDonThu);
        entityLichSuThanhToan.setNGAY_PHAT_SINH(new Date());
        entityLichSuThanhToan.setMA_GIAO_DICH(Common.MA_GIAO_DICH.DAY_CHAM_NO.getCode());
        mainModel.insertLichSuThanhToan(entityLichSuThanhToan);

    }

    private void hoaDonDaThanhToanBoiViKhac(BodyBillingOnlineRespone response) {
        /**
         * Trường hợp hóa đơn đã được thanh toán bởi ví khác: Không thực hiện thanh toán hóa đơn
         * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
         * Ví thanh toán edong = Ví thanh toán do server trả về
         * Trạng thái thanh toán status = 04_Đã thanh toán bởi ví khác
         */

        mainModel.updateHoaDonNo(response.getBillId(), Common.TRANG_THAI_TTOAN.TTOAN_BOI_VI_KHAC.getCode(), response.getPaymentEdong());


        //Update Hoa Don Thu
        mainModel.updateHoaDonThu(String.valueOf(response.getBillId()), response.getPaymentEdong(), Common.TRANG_THAI_TTOAN.TTOAN_BOI_VI_KHAC,
                "", Common.TRANG_THAI_DAY_CHAM_NO.KHONG_THANH_CONG.getCode(), Common.parse(new Date(), Common.DATE_TIME_TYPE.FULL.toString()),
                Common.TRANG_THAI_HOAN_TRA.CHUA_TRA.getCode());




        //Lưu lại lịch sử thanh toán
        EntityLichSuThanhToan entityLichSuThanhToan = EntityLichSuThanhToan.copy(mainModel.getHoaDonNo(response.getBillId()));

        entityLichSuThanhToan.setVI_TTOAN(response.getPaymentEdong());
        entityLichSuThanhToan.setHINH_THUC_TT(Common.HINH_THUC_TTOAN.OFFLINE.getCode());
        entityLichSuThanhToan.setTRANG_THAI_TTOAN(Common.TRANG_THAI_TTOAN.TTOAN_BOI_VI_KHAC.getCode());
        entityLichSuThanhToan.setTRANG_THAI_CHAM_NO("");
        entityLichSuThanhToan.setTRANG_THAI_HUY("");
        entityLichSuThanhToan.setIN_THONG_BAO_DIEN("");
        entityLichSuThanhToan.setSO_IN_BIEN_NHAN(0);

        entityLichSuThanhToan.setNGAY_PHAT_SINH(new Date());
        entityLichSuThanhToan.setMA_GIAO_DICH(Common.MA_GIAO_DICH.DAY_CHAM_NO.getCode());
        mainModel.insertLichSuThanhToan(entityLichSuThanhToan);


    }

    private void hoaDonDaThanhToanBoiNguonKhac(BodyBillingOnlineRespone respone) {
        /** Trường hợp hóa đơn đã được thanh toán bởi nguồn khác: Không thực hiện thanh toán hóa đơn
         * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
         * Ví thanh toán edong = null
         * Trạng thái thanh toán status = 03_Đã thanh toán bởi nguồn khác (Trong database = 2)
         */

        mainModel.updateHoaDonNo(respone.getBillId(), Common.TRANG_THAI_TTOAN.TTOAN_BOI_NGUON_KHAC.getCode(), "");

        //Update Hoa Don Thu
        mainModel.updateHoaDonThu(String.valueOf(respone.getBillId()), "", Common.TRANG_THAI_TTOAN.TTOAN_BOI_NGUON_KHAC,
                "", Common.TRANG_THAI_DAY_CHAM_NO.KHONG_THANH_CONG.getCode(), Common.parse(new Date(), Common.DATE_TIME_TYPE.FULL.toString()), Common.TRANG_THAI_HOAN_TRA.CHUA_TRA.getCode());


        //Lưu lại lịch sử thanh toán
        EntityLichSuThanhToan entityLichSuThanhToan = EntityLichSuThanhToan.copy(mainModel.getHoaDonNo(respone.getBillId()));

        entityLichSuThanhToan.setVI_TTOAN("");
        entityLichSuThanhToan.setHINH_THUC_TT(Common.HINH_THUC_TTOAN.OFFLINE.getCode());
        entityLichSuThanhToan.setTRANG_THAI_TTOAN(Common.TRANG_THAI_TTOAN.TTOAN_BOI_NGUON_KHAC.getCode());
        entityLichSuThanhToan.setTRANG_THAI_CHAM_NO("");
        entityLichSuThanhToan.setTRANG_THAI_HUY("");
        entityLichSuThanhToan.setIN_THONG_BAO_DIEN("");
        entityLichSuThanhToan.setSO_IN_BIEN_NHAN(0);

        entityLichSuThanhToan.setNGAY_PHAT_SINH(new Date());
        entityLichSuThanhToan.setMA_GIAO_DICH(Common.MA_GIAO_DICH.DAY_CHAM_NO.getCode());
        mainModel.insertLichSuThanhToan(entityLichSuThanhToan);


    }


    /* private SoapAPI.AsyncSoapSynchronizeDataZip.AsyncSoapSynchronizeDataZipCallBack callBackFileGen = new SoapAPI.AsyncSoapSynchronizeDataZip.AsyncSoapSynchronizeDataZipCallBack() {
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
                     File file = new File(Common.PATH_FOLDER_DOWNLOAD + bookCmis + "_" + edong + ".zip");
                     if (!file.exists()) {
                         Common.writeBytesToFile(file, zipByte);
                         if (file.exists()) {
                             File fileText = new File(Common.PATH_FOLDER_DOWNLOAD, "full.txt");
                             if (fileText.exists())
                                 fileText.delete();
                             if (Common.unpackZip(Common.PATH_FOLDER_DOWNLOAD, bookCmis + "_" + edong + ".zip")) {
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

                                     mainModel.setChangedGenFile(edong, bookCmis, fileGenResponse.getId_changed(), fileGenResponse.getDate_changed());

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
                 } finally {
                     ((MainActivity) mIMainView.getContextView()).runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             mIMainView.refreshInfoMain();
                         }
                     });
                 }

                 ((MainActivity) mIMainView.getContextView()).runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         mIMainView.refreshInfoMain();
                     }
                 });
             }
         }

         @Override
         public void onTimeOut(SoapAPI.AsyncSoapSynchronizeDataZip soapSynchronizeInvoices) {

         }
     };
 */
    /*private SoapAPI.AsyncSoapSynchronizeData.AsyncSoapSynchronizeDataCallBack callBackData = new SoapAPI.AsyncSoapSynchronizeData.AsyncSoapSynchronizeDataCallBack() {
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

            ((MainActivity) mIMainView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIMainView.refreshInfoMain();
                }
            });
        }

        @Override
        public void onTimeOut(SoapAPI.AsyncSoapSynchronizeData soapSynchronizeInvoices) {

        }
    };
*/
//endregion


    public interface InteractorMainPresenter {
        List<PayAdapter.DataAdapter> getData();

        void refreshData();
    }
}
