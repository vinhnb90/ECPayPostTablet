package views.ecpay.com.postabletecpay.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
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

import views.ecpay.com.postabletecpay.model.MainPageModel;
import views.ecpay.com.postabletecpay.model.PayModel;
import views.ecpay.com.postabletecpay.model.sharedPreference.SharePrefManager;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.request.EntityPostBill.ListTransactionOff;
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
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogout.LogoutResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityPostBill.PostBillResponse;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.TrangChu.IMainPageView;

import static android.content.Context.MODE_PRIVATE;
import static views.ecpay.com.postabletecpay.util.commons.Common.TAG;
import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_OUT_CONNECT;

/**
 * Created by VinhNB on 5/23/2017.
 */

public class MainPagePresenter implements IMainPagePresenter {
    private IMainPageView mIMainPageView;
    private MainPageModel mainPageModel;
    private SharePrefManager mSharedPrefLogin;

    private String bookCmis;
    private SoapAPI.AsyncSoapLogout soapLogout;
    private Handler handlerDelay = new Handler();

    public MainPagePresenter(IMainPageView mIMainPageView) {
        this.mIMainPageView = mIMainPageView;
        mainPageModel = new MainPageModel(mIMainPageView.getContextView());
        mSharedPrefLogin = mainPageModel.getManagerSharedPref();
    }

    //region IMainPagePresenter
    @Override
    public void callInfoMain(String edong) {
        Account account = mainPageModel.getAccountInfo(edong);
        int countTotalBill = mainPageModel.getTotalBill(edong);
        int countTotalMoney = mainPageModel.getTotalMoney(edong);

        mIMainPageView.showMainPageInfo(account.getName(), account.getBalance(), countTotalBill, countTotalMoney);
    }
    //endregion

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void synchronizePC() {
        String userName =
                mSharedPrefLogin.getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE)
                        .getString(Common.SHARE_REF_FILE_LOGIN_USER_NAME, "");
        String pass =
                mSharedPrefLogin.getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE)
                        .getString(Common.SHARE_REF_FILE_LOGIN_PASS, "");

        String textMessage = "";
        Context context = mIMainPageView.getContextView();
        Boolean isErr = false;

        if ((userName == null || userName.isEmpty() || userName.trim().equals("") || userName.length() > Common.MAX_LENGTH) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.LOGIN_ERR_USER.toString();
            isErr = true;
        }
        if ((pass == null || pass.isEmpty() || pass.trim().equals("") || pass.length() > Common.LENGTH_PASS) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.LOGIN_ERR_PASS.toString();
            isErr = true;
        }

//        if (!Common.isConnectingWifi(context) && !isErr) {
//            textMessage = Common.MESSAGE_NOTIFY.ERR_WIFI.toString();
//            isErr = true;
//        }
        if (!Common.isNetworkConnected(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_NETWORK.toString();
            isErr = true;
        }
        if (isErr) {
            mIMainPageView.showTextMessage(textMessage);
            return;
        }

        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = mIMainPageView.getContextView().getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, userName, Common.COMMAND_ID.GET_BOOK_CMIS_BY_CASHIER.toString(), versionApp);
        } catch (Exception e) {
            mIMainPageView.showTextMessage(e.getMessage());
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
                                mIMainPageView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
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
                mIMainPageView.showTextMessage(e.getMessage());
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
                    if (mainPageModel.deleteAllPC() != -1) {
                        mainPageModel.insertEvnPC(listEvnPCResponse);
                    }
                }
                //Insert BOOK CMIS
                ListBookCmisResponse listBookCmisResponse;
                String dataBookCmis = response.getBodyListEVNResponse().getListBookCmissResponse();
                JSONArray jaBookCmis = new JSONArray(dataBookCmis);
                for (int i = 0; i < jaBookCmis.length(); i++) {
                    listBookCmisResponse = gson.fromJson(jaBookCmis.getString(i), ListBookCmisResponse.class);
                    if (mainPageModel.checkBookCmisExist(listBookCmisResponse.getBookCmis()) == 0) {
                        mainPageModel.insertBookCmis(listBookCmisResponse);
                    }
                }

                String path = Common.PATH_FOLDER_DOWNLOAD;
                File directory = new File(path);
                File[] allFiles = directory.listFiles();
                if (allFiles.length == 0) {
                    synchronizeFileGen();
                } else {
                    synchronizeData();
                }
            } catch (Exception ex) {
                mIMainPageView.showTextMessage(ex.getMessage());
            }
        }

        @Override
        public void onTimeOut(SoapAPI.AsyncSoapSynchronizePC soapSynchronizeInvoices) {

        }
    };
    //endregion

    //region đồng bộ hoá đơn
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void synchronizeFileGen() {
        String userName =
                mSharedPrefLogin.getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE)
                        .getString(Common.SHARE_REF_FILE_LOGIN_USER_NAME, "");
        String pass =
                mSharedPrefLogin.getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE)
                        .getString(Common.SHARE_REF_FILE_LOGIN_PASS, "");
        String textMessage = "";
        Context context = mIMainPageView.getContextView();
        Boolean isErr = false;

        if ((userName == null || userName.isEmpty() || userName.trim().equals("") || userName.length() > Common.MAX_LENGTH) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.LOGIN_ERR_USER.toString();
            isErr = true;
        }
        if ((pass == null || pass.isEmpty() || pass.trim().equals("") || pass.length() > Common.LENGTH_PASS) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.LOGIN_ERR_PASS.toString();
            isErr = true;
        }

//        if (!Common.isConnectingWifi(context) && !isErr) {
//            textMessage = Common.MESSAGE_NOTIFY.ERR_WIFI.toString();
//            isErr = true;
//        }
        if (!Common.isNetworkConnected(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_NETWORK.toString();
            isErr = true;
        }
        if (isErr) {
            mIMainPageView.showTextMessage(textMessage);
            return;
        }


        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = mIMainPageView.getContextView().getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, userName, Common.COMMAND_ID.GET_FILE_GEN.toString(), versionApp, mainPageModel.getPcCode());
        } catch (Exception e) {
            mIMainPageView.showTextMessage(e.getMessage());
            return;
        }


        String signatureEncrypted = "";
        try {
            String dataSign = Common.getDataSignRSA(
                    configInfo.getAGENT(), configInfo.getCommandId(), configInfo.getAuditNumber(), configInfo.getMacAdressHexValue(),
                    configInfo.getDiskDriver(), mainPageModel.getPcCode(), configInfo.getAccountId(), configInfo.getPRIVATE_KEY().trim());
            Log.d(TAG, "setupInfoRequest: " + dataSign);
            signatureEncrypted = SecurityUtils.sign(dataSign, configInfo.getPRIVATE_KEY().trim());
            Log.d(TAG, "setupInfoRequest: " + signatureEncrypted);
        } catch (Exception e) {
        }
        configInfo.setSignatureEncrypted(signatureEncrypted);

        Cursor c = mainPageModel.getAllBookCmis();
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

                        soapSynchronizeDataZip = new SoapAPI.AsyncSoapSynchronizeDataZip(callBackFileGen, mIMainPageView.getContextView());

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
                                        mIMainPageView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
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
                        mIMainPageView.showTextMessage(e.getMessage());
                        return;
                    }

                }
            } while (c.moveToNext());
        }
    }

    @Override
    public void synchronizeData() {
        String userName = mSharedPrefLogin.getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE)
                .getString(Common.SHARE_REF_FILE_LOGIN_USER_NAME, "");
        String pass = mSharedPrefLogin.getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE)
                .getString(Common.SHARE_REF_FILE_LOGIN_PASS, "");

        String textMessage = "";
        Context context = mIMainPageView.getContextView();
        Boolean isErr = false;

        if ((userName == null || userName.isEmpty() || userName.trim().equals("") || userName.length() > Common.MAX_LENGTH) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.LOGIN_ERR_USER.toString();
            isErr = true;
        }
        if ((pass == null || pass.isEmpty() || pass.trim().equals("") || pass.length() > Common.LENGTH_PASS) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.LOGIN_ERR_PASS.toString();
            isErr = true;
        }

//        if (!Common.isConnectingWifi(context) && !isErr) {
//            textMessage = Common.MESSAGE_NOTIFY.ERR_WIFI.toString();
//            isErr = true;
//        }
        if (!Common.isNetworkConnected(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_NETWORK.toString();
            isErr = true;
        }
        if (isErr) {
            mIMainPageView.showTextMessage(textMessage);
            return;
        }

        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = mIMainPageView.getContextView().getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, userName, Common.COMMAND_ID.SYNC_DATA.toString(), versionApp, mainPageModel.getPcCode());
        } catch (Exception e) {
            mIMainPageView.showTextMessage(e.getMessage());
            return;
        }


        String signatureEncrypted = "";
        try {
            String dataSign = Common.getDataSignRSA(
                    configInfo.getAGENT(), configInfo.getCommandId(), configInfo.getAuditNumber(), configInfo.getMacAdressHexValue(),
                    configInfo.getDiskDriver(), mainPageModel.getPcCode(), configInfo.getAccountId(), configInfo.getPRIVATE_KEY().trim());
            Log.d(TAG, "setupInfoRequest: " + dataSign);
            signatureEncrypted = SecurityUtils.sign(dataSign, configInfo.getPRIVATE_KEY().trim());
            Log.d(TAG, "setupInfoRequest: " + signatureEncrypted);
        } catch (Exception e) {
        }
        configInfo.setSignatureEncrypted(signatureEncrypted);

        Cursor c = mainPageModel.getAllBookCmis();
        if (c.moveToFirst()) {
            do {

                bookCmis = c.getString(0);
                File fileBookCmis = new File(Common.PATH_FOLDER_DOWNLOAD + bookCmis + ".zip");
                if (fileBookCmis.exists()) {

                    String jsonRequestData = SoapAPI.getJsonRequestSynData(
                            configInfo.getAGENT(),
                            configInfo.getAgentEncypted(),
                            configInfo.getCommandId(),
                            configInfo.getAuditNumber(),
                            configInfo.getMacAdressHexValue(),
                            configInfo.getDiskDriver(),
                            configInfo.getSignatureEncrypted(),
                            mainPageModel.getPcCode(),
                            c.getString(0),
                            mainPageModel.getMaxIdChanged(),
                            mainPageModel.getMaxDateChanged(),
                            configInfo.getAccountId());


                    if (jsonRequestData != null) {
                        try {
                            final SoapAPI.AsyncSoapSynchronizeData soapSynchronizeData;

                            soapSynchronizeData = new SoapAPI.AsyncSoapSynchronizeData(callBackData, mIMainPageView.getContextView());

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
                                            mIMainPageView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
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
                            mIMainPageView.showTextMessage(e.getMessage());
                            return;
                        }

                    }
                }
            } while (c.moveToNext());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void callLogout(String mEdong) {
        String textMessage = "";
        Context context = mIMainPageView.getContextView();
        Boolean isErr = false;

        if (mEdong == null || mEdong.isEmpty() || mEdong.trim().equals("") && !isErr) {
            return;
        }

        mIMainPageView.showStatusProgressLogout(Common.STATUS_PROGRESS.BEGIN);

        //check wifi and network
//        if (!Common.isConnectingWifi(context) && !isErr) {
//            textMessage = Common.MESSAGE_NOTIFY.ERR_WIFI.toString();
//            isErr = true;
//        }

/*   mIMainPageView.showStatusProgressLogout(Common.STATUS_PROGRESS.ERROR);
        mIMainPageView.showMessageLogout(textMessage);
        return;
    }*/
        //setup info login
        ConfigInfo configInfo;
        String versionApp = Common.getVersionApp(context);

        try {
            configInfo = Common.setupInfoRequest(context, mEdong, Common.COMMAND_ID.LOGOUT.toString(), versionApp);
        } catch (
                Exception e)

        {
            mIMainPageView.showStatusProgressLogout(Common.STATUS_PROGRESS.ERROR);
            mIMainPageView.showMessageLogout(textMessage);
            return;
        }

        String session = new PayModel(context).getSession(mEdong);
        //create request to server
        String jsonRequestLogout = SoapAPI.getJsonRequestLogout(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),

                session,

                configInfo.getAccountId());

        if (jsonRequestLogout == null)
            return;
        try {
            if (soapLogout == null) {
                //if null then create new
                soapLogout = new SoapAPI.AsyncSoapLogout(mEdong, asyncSoapLogoutCallBack);
            } else if (soapLogout.getStatus() == AsyncTask.Status.PENDING) {
                //if running not yet then run

            } else if (soapLogout.getStatus() == AsyncTask.Status.RUNNING) {
                //if is running
                soapLogout.setEndCallSoap(true);
                soapLogout.cancel(true);

                handlerDelay.removeCallbacks(runnableCountTimeLogout);
                soapLogout = new SoapAPI.AsyncSoapLogout(mEdong, asyncSoapLogoutCallBack);
            } else {
                //if running or finish
                handlerDelay.removeCallbacks(runnableCountTimeLogout);

                soapLogout = new SoapAPI.AsyncSoapLogout(mEdong, asyncSoapLogoutCallBack);
            }

            soapLogout.execute(jsonRequestLogout);

            //thread time out
            //sleep
            handlerDelay.postDelayed(runnableCountTimeLogout, TIME_OUT_CONNECT);

        } catch (Exception e) {
            mIMainPageView.showStatusProgressLogout(Common.STATUS_PROGRESS.ERROR);
            mIMainPageView.showMessageLogout(Common.CODE_REPONSE_LOGOUT.e9999.getMessage());
            Log.e(TAG, "callLogout: " + Common.CODE_REPONSE_LOGOUT.e10000.getMessage());
            return;
        }
    }


    @Override
    public void postBill() {
        String userName = mSharedPrefLogin.getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE)
                .getString(Common.SHARE_REF_FILE_LOGIN_USER_NAME, "");
        String pass = mSharedPrefLogin.getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE)
                .getString(Common.SHARE_REF_FILE_LOGIN_PASS, "");

        String textMessage = "";
        Context context = mIMainPageView.getContextView();
        Boolean isErr = false;

        if ((userName == null || userName.isEmpty() || userName.trim().equals("") || userName.length() > Common.MAX_LENGTH || userName.length() < Common.MIN_LENGTH) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.LOGIN_ERR_USER.toString();
            isErr = true;
        }
        if ((pass == null || pass.isEmpty() || pass.trim().equals("") || pass.length() > Common.LENGTH_PASS) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.LOGIN_ERR_PASS.toString();
            isErr = true;
        }
        if (!Common.isNetworkConnected(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_NETWORK.toString();
            isErr = true;
        }
        if (isErr) {
            mIMainPageView.showTextMessage(textMessage);
            return;
        }

        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = mIMainPageView.getContextView().getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, userName, Common.COMMAND_ID.PUT_TRANSACTION_OFF.toString(), versionApp, mainPageModel.getPcCode());
        } catch (Exception e) {
            mIMainPageView.showTextMessage(e.getMessage());
            return;
        }


        String signatureEncrypted = "";
        try {
            String dataSign = Common.getDataSignRSA(
                    configInfo.getAGENT(), configInfo.getCommandId(), configInfo.getAuditNumber(), configInfo.getMacAdressHexValue(),
                    configInfo.getDiskDriver(), mainPageModel.getPcCode(), configInfo.getAccountId(), configInfo.getPRIVATE_KEY().trim());
            Log.d(TAG, "setupInfoRequest: " + dataSign);
            signatureEncrypted = SecurityUtils.sign(dataSign, configInfo.getPRIVATE_KEY().trim());
            Log.d(TAG, "setupInfoRequest: " + signatureEncrypted);
        } catch (Exception e) {
        }
        configInfo.setSignatureEncrypted(signatureEncrypted);

        ArrayList<ListTransactionOff> lstTransactionOff = new ArrayList<>();
        Cursor c = mainPageModel.selectOfflineBill();
        if (c.moveToFirst()) {
            do {
                ListTransactionOff listTransactionOff = new ListTransactionOff();
                listTransactionOff.setCustomer_code(c.getString(c.getColumnIndex("customerCode")));
                listTransactionOff.setProvide_code(c.getString(c.getColumnIndex("")));
                listTransactionOff.setAmount(c.getString(c.getColumnIndex("amount")));
                listTransactionOff.setBill_id(c.getString(c.getColumnIndex("billId")));
                listTransactionOff.setEdong(c.getString(c.getColumnIndex("edong")));
                listTransactionOff.setAudit_number(String.valueOf(configInfo.getAuditNumber()));
                lstTransactionOff.add(listTransactionOff);
            } while (c.moveToNext());
        }
        String jsonRequestData = SoapAPI.getJsonRequestPostBill(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                lstTransactionOff,
                configInfo.getAccountId());


        if (jsonRequestData != null) {
            try {
                final SoapAPI.AsyncSoapPostBill soapPostBill;

                soapPostBill = new SoapAPI.AsyncSoapPostBill(callBackPostBill, mIMainPageView.getContextView());

                if (soapPostBill.getStatus() != AsyncTask.Status.RUNNING) {
                    soapPostBill.execute(jsonRequestData);

                    //thread time out
                    Thread soapDataThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            PostBillResponse postBillResponse = null;

                            //call time out
                            try {
                                Thread.sleep(Common.TIME_OUT_CONNECT);
                            } catch (InterruptedException e) {
                                mIMainPageView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                            } finally {
                                if (postBillResponse == null) {
                                    soapPostBill.callCountdown(soapPostBill);
                                }
                            }
                        }
                    });

                    soapDataThread.start();
                }
            } catch (Exception e) {
                mIMainPageView.showTextMessage(e.getMessage());
                return;
            }

        }
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
                                for (ListCustomerResponse listCustomerResponse : fileGenResponse.getCustomerResponse()) {
                                    if (mainPageModel.checkCustomerExist(listCustomerResponse.getBodyCustomerResponse().getCustomerCode()) == 0) {
                                        if (mainPageModel.insertCustomer(listCustomerResponse) != -1) {
                                            Log.i("INFO", "TEST");
                                        }
                                    }
                                }

                                for (ListBillResponse listBillResponse : fileGenResponse.getBillResponse()) {
                                    listBillResponse.getBodyBillResponse().setEdong(MainActivity.mEdong);
                                    if (mainPageModel.checkBillExist(listBillResponse.getBodyBillResponse().getBillId()) == 0) {
                                        if (mainPageModel.insertBill(listBillResponse) != -1) {
                                            Log.i("INFO", "TEST");
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
                        if (mainPageModel.checkCustomerExist(customerResponse.getBodyCustomerResponse().getCustomerCode()) == 0) {
                            if (mainPageModel.insertCustomer(customerResponse) != -1) {
                                Log.i("INFO", "SUCCESS");
                            }
                        } else {
                            if (mainPageModel.updateCustomer(customerResponse) != -1) {
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
                        if (mainPageModel.checkBillExist(billResponse.getBodyBillResponse().getId()) == 0) {
                            if (mainPageModel.insertBill(billResponse) != -1) {
                                Log.i("INFO", "SUCCESS");
                            }
                        } else {
                            if (mainPageModel.updateBill(billResponse) != -1) {
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

        }

        @Override
        public void onTimeOut(SoapAPI.AsyncSoapSynchronizeData soapSynchronizeInvoices) {

        }
    };

    private SoapAPI.AsyncSoapLogout.AsyncSoapLogoutCallBack asyncSoapLogoutCallBack = new SoapAPI.AsyncSoapLogout.AsyncSoapLogoutCallBack() {
        private String edong;

        @Override
        public void onPre(final SoapAPI.AsyncSoapLogout soapLogout) {
            edong = soapLogout.getEdong();

            mIMainPageView.showStatusProgressLogout(Common.STATUS_PROGRESS.BEGIN);

            //check wifi
            boolean isHasWifi = Common.isConnectingWifi(mIMainPageView.getContextView());
            boolean isHasNetwork = Common.isNetworkConnected(mIMainPageView.getContextView());

//            if (!isHasWifi) {
//                mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());
//
//                soapSearchOnline.setEndCallSoap(true);
//                soapSearchOnline.cancel(true);
//            }
            if (!isHasNetwork) {
                mIMainPageView.showStatusProgressLogout(Common.STATUS_PROGRESS.ERROR);
                mIMainPageView.showMessageLogout(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString());

                soapLogout.setEndCallSoap(true);
                soapLogout.cancel(true);
            }
        }

        @Override
        public void onUpdate(final String message) {
            if (message == null || message.isEmpty() || message.trim().equals(""))
                return;

            mIMainPageView.showStatusProgressLogout(Common.STATUS_PROGRESS.ERROR);
            mIMainPageView.showMessageLogout(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString());

        }

        @Override
        public void onPost(LogoutResponse response) {
            if (response == null) {
                return;
            }

            Common.CODE_REPONSE_LOGOUT codeResponse = Common.CODE_REPONSE_LOGOUT.findCodeMessage(response.getFooter().getResponseCode());
            if (codeResponse != Common.CODE_REPONSE_LOGOUT.e000) {
                mIMainPageView.showStatusProgressLogout(Common.STATUS_PROGRESS.ERROR);
                mIMainPageView.showMessageLogout(codeResponse.getMessage());
                return;
            }

            mIMainPageView.showStatusProgressLogout(Common.STATUS_PROGRESS.SUCCESS);
            mIMainPageView.showMessageLogout(Common.CODE_REPONSE_LOGOUT.e000.getMessage());
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIMainPageView.showLoginForm();
                }
            }, Common.MORE_LONG_TIME_DELAY_ANIM);
        }

        @Override
        public void onTimeOut(final SoapAPI.AsyncSoapLogout asyncSoapLogoutCallBack) {
            asyncSoapLogoutCallBack.cancel(true);

            //thread call asyntask is running. must call in other thread to update UI
            ((MainActivity) mIMainPageView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!asyncSoapLogoutCallBack.isEndCallSoap()) {
                        mIMainPageView.showMessageLogout(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                    }
                }
            });
        }
    };

    private Runnable runnableCountTimeLogout = new Runnable() {
        @Override
        public void run() {
            if (soapLogout != null && soapLogout.isEndCallSoap())
                return;
            //Do something after 100ms
            LogoutResponse searchOnlineResponse = soapLogout.getLogoutResponse();

            if (searchOnlineResponse == null && !soapLogout.isEndCallSoap()) {
                //call time out
                soapLogout.callCountdown(soapLogout);
            }
        }
    };
    //endregion


    private SoapAPI.AsyncSoapPostBill.AsyncSoapPostBillCallBack callBackPostBill = new SoapAPI.AsyncSoapPostBill.AsyncSoapPostBillCallBack() {
        @Override
        public void onPre(SoapAPI.AsyncSoapPostBill soapPostBill) {

        }

        @Override
        public void onUpdate(String message) {

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPost(PostBillResponse response) {

        }

        @Override
        public void onTimeOut(SoapAPI.AsyncSoapPostBill soapPostBill) {
        }
    };
}

