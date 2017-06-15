package views.ecpay.com.postabletecpay.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
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

import views.ecpay.com.postabletecpay.model.MainPageModel;
import views.ecpay.com.postabletecpay.model.sharedPreference.SharePrefManager;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.response.EntityData.ListDataResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityDataZip.ListDataZipResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListBookCmisResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListEVNReponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListEvnPCResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.FileGenResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListBillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListCustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.TrangChu.IMainPageView;

import static android.content.Context.MODE_PRIVATE;
import static views.ecpay.com.postabletecpay.util.commons.Common.TAG;

/**
 * Created by VinhNB on 5/23/2017.
 */

public class MainPagePresenter implements IMainPagePresenter{
    private IMainPageView iMainPageView;
    private MainPageModel mainPageModel;
    private SharePrefManager mSharedPrefLogin;

    private String bookCmis;

    public MainPagePresenter(IMainPageView iMainPageView) {
        this.iMainPageView = iMainPageView;
        mainPageModel = new MainPageModel(iMainPageView.getContextView());
        mSharedPrefLogin = mainPageModel.getManagerSharedPref();
    }

    //region IMainPagePresenter
    @Override
    public void callInfoMain(String edong) {
        Account account = mainPageModel.getAccountInfo(edong);
        int countTotalBill = mainPageModel.getTotalBill(edong);
        int countTotalMoney = mainPageModel.getTotalMoney(edong);

        iMainPageView.showMainPageInfo(account.getName(), account.getBalance(), countTotalBill, countTotalMoney);
    }
    //endregion

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void synchronizePC() {
        String userName = mSharedPrefLogin.getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE)
                .getString(Common.SHARE_REF_FILE_LOGIN_USER_NAME, "");
        String pass = mSharedPrefLogin.getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE)
                .getString(Common.SHARE_REF_FILE_LOGIN_PASS, "");

        String textMessage = "";
        Context context = iMainPageView.getContextView();
        Boolean isErr = false;

        if ((userName == null || userName.isEmpty() || userName.trim().equals("") || userName.length() > Common.LENGTH_USER_NAME) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.LOGIN_ERR_USER.toString();
            isErr = true;
        }
        if ((pass == null || pass.isEmpty() || pass.trim().equals("") || pass.length() > Common.LENGTH_PASS) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.LOGIN_ERR_PASS.toString();
            isErr = true;
        }

        if (!Common.isConnectingWifi(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_WIFI.toString();
            isErr = true;
        }
        if (!Common.isNetworkConnected(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_NETWORK.toString();
            isErr = true;
        }
        if (isErr) {
            iMainPageView.showTextMessage(textMessage);
            return;
        }

        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = iMainPageView.getContextView().getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, userName, Common.COMMAND_ID.GET_BOOK_CMIS_BY_CASHIER.toString(), versionApp);
        } catch (Exception e) {
            iMainPageView.showTextMessage(e.getMessage());
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
                                Thread.sleep(Common.TIME_OUT_CONNECT);
                            } catch (InterruptedException e) {
                                iMainPageView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
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
                iMainPageView.showTextMessage(e.getMessage());
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

        @Override
        public void onPost(ListEVNReponse response) {
            try {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                //Insert EVN PC
                ListEvnPCResponse listEvnPCResponse;
                String dataEvnPC = response.getBodyListEVNResponse().getListEvnPCLoginResponse();
                JSONArray jaEvnPC = new JSONArray(dataEvnPC);
                for (int i = 0; i < jaEvnPC.length(); i++){
                    listEvnPCResponse = gson.fromJson(jaEvnPC.getString(i), ListEvnPCResponse.class);
                    if(mainPageModel.deleteAllPC() != -1) {
                        mainPageModel.insertEvnPC(listEvnPCResponse);
                    }
                }
                //Insert BOOK CMIS
                ListBookCmisResponse listBookCmisResponse;
                String dataBookCmis = response.getBodyListEVNResponse().getListBookCmissResponse();
                JSONArray jaBookCmis = new JSONArray(dataBookCmis);
//                if(mainPageModel.deleteAllBookCmis() != -1) {
                    for (int i = 0; i < jaBookCmis.length(); i++){
                        listBookCmisResponse = gson.fromJson(jaBookCmis.getString(i), ListBookCmisResponse.class);
                        if(mainPageModel.checkBookCmisExist(listBookCmisResponse.getBookCmis()) == 0) {
                            mainPageModel.insertBookCmis(listBookCmisResponse);
                        }
                    }
//                }
            } catch(Exception ex) {
                iMainPageView.showTextMessage(ex.getMessage());
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
        String userName = mSharedPrefLogin.getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE)
                .getString(Common.SHARE_REF_FILE_LOGIN_USER_NAME, "");
        String pass = mSharedPrefLogin.getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE)
                .getString(Common.SHARE_REF_FILE_LOGIN_PASS, "");

        String textMessage = "";
        Context context = iMainPageView.getContextView();
        Boolean isErr = false;

        if ((userName == null || userName.isEmpty() || userName.trim().equals("") || userName.length() > Common.LENGTH_USER_NAME) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.LOGIN_ERR_USER.toString();
            isErr = true;
        }
        if ((pass == null || pass.isEmpty() || pass.trim().equals("") || pass.length() > Common.LENGTH_PASS) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.LOGIN_ERR_PASS.toString();
            isErr = true;
        }

        if (!Common.isConnectingWifi(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_WIFI.toString();
            isErr = true;
        }
        if (!Common.isNetworkConnected(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_NETWORK.toString();
            isErr = true;
        }
        if (isErr) {
            iMainPageView.showTextMessage(textMessage);
            return;
        }

        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = iMainPageView.getContextView().getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, userName, Common.COMMAND_ID.GET_FILE_GEN.toString(), versionApp, "PD16");
        } catch (Exception e) {
            iMainPageView.showTextMessage(e.getMessage());
            return;
        }


        String signatureEncrypted = "";
        try {
            String dataSign = Common.getDataSignRSA(
                    configInfo.getAGENT(), configInfo.getCommandId(), configInfo.getAuditNumber(), configInfo.getMacAdressHexValue(),
                    configInfo.getDiskDriver(), "PD16", configInfo.getAccountId(), configInfo.getPRIVATE_KEY().trim());
            Log.d(TAG, "setupInfoRequest: " + dataSign);
            signatureEncrypted = SecurityUtils.sign(dataSign, configInfo.getPRIVATE_KEY().trim());
            Log.d(TAG, "setupInfoRequest: " + signatureEncrypted);
        } catch (Exception e) {
        }
        configInfo.setSignatureEncrypted(signatureEncrypted);

        Cursor c = mainPageModel.getAllBookCmis();
        if(c.moveToFirst()) {
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
                        mainPageModel.getPcCode(),
                        c.getString(0),
                        configInfo.getAccountId());

                if (jsonRequestZipData != null) {
                    try {
                        final SoapAPI.AsyncSoapSynchronizeDataZip soapSynchronizeDataZip;

                        soapSynchronizeDataZip = new SoapAPI.AsyncSoapSynchronizeDataZip(callBackFileGen, iMainPageView.getContextView());

                        if (soapSynchronizeDataZip.getStatus() != AsyncTask.Status.RUNNING) {
                            soapSynchronizeDataZip.execute(jsonRequestZipData);

                            //thread time out
                            Thread soapDataThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    ListDataZipResponse listDataZipResponse = null;

                                    //call time out
                                    try {
                                        Thread.sleep(Common.TIME_OUT_CONNECT);
                                    } catch (InterruptedException e) {
                                        iMainPageView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
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
                        iMainPageView.showTextMessage(e.getMessage());
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
        Context context = iMainPageView.getContextView();
        Boolean isErr = false;

        if ((userName == null || userName.isEmpty() || userName.trim().equals("") || userName.length() > Common.LENGTH_USER_NAME) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.LOGIN_ERR_USER.toString();
            isErr = true;
        }
        if ((pass == null || pass.isEmpty() || pass.trim().equals("") || pass.length() > Common.LENGTH_PASS) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.LOGIN_ERR_PASS.toString();
            isErr = true;
        }

        if (!Common.isConnectingWifi(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_WIFI.toString();
            isErr = true;
        }
        if (!Common.isNetworkConnected(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_NETWORK.toString();
            isErr = true;
        }
        if (isErr) {
            iMainPageView.showTextMessage(textMessage);
            return;
        }

        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = iMainPageView.getContextView().getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, userName, Common.COMMAND_ID.SYNC_DATA.toString(), versionApp, "PD16");
        } catch (Exception e) {
            iMainPageView.showTextMessage(e.getMessage());
            return;
        }


        String signatureEncrypted = "";
        try {
            String dataSign = Common.getDataSignRSA(
                    configInfo.getAGENT(), configInfo.getCommandId(), configInfo.getAuditNumber(), configInfo.getMacAdressHexValue(),
                    configInfo.getDiskDriver(), "PD16", configInfo.getAccountId(), configInfo.getPRIVATE_KEY().trim());
            Log.d(TAG, "setupInfoRequest: " + dataSign);
            signatureEncrypted = SecurityUtils.sign(dataSign, configInfo.getPRIVATE_KEY().trim());
            Log.d(TAG, "setupInfoRequest: " + signatureEncrypted);
        } catch (Exception e) {
        }
        configInfo.setSignatureEncrypted(signatureEncrypted);

        Cursor c = mainPageModel.getAllBookCmis();
        if(c.moveToFirst()) {
            do {
                bookCmis = c.getString(0);
                File fileBookCmis = new File(Common.PATH_FOLDER_DOWNLOAD + bookCmis + ".zip");
                if(fileBookCmis.exists()) {
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

                            soapSynchronizeData = new SoapAPI.AsyncSoapSynchronizeData(callBackData, iMainPageView.getContextView());

                            if (soapSynchronizeData.getStatus() != AsyncTask.Status.RUNNING) {
                                soapSynchronizeData.execute(jsonRequestData);

                                //thread time out
                                Thread soapDataThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ListDataResponse listDataResponse = null;

                                        //call time out
                                        try {
                                            Thread.sleep(Common.TIME_OUT_CONNECT);
                                        } catch (InterruptedException e) {
                                            iMainPageView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
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
                            iMainPageView.showTextMessage(e.getMessage());
                            return;
                        }

                    }
                }
            } while (c.moveToNext());
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
            String sData = response.getBodyListDataResponse().getFile_data();
            if(sData != null && !sData.isEmpty()) {
                byte[] zipByte = org.apache.commons.codec.binary.Base64.decodeBase64(sData.getBytes());

                try {
                    File file = new File(Common.PATH_FOLDER_DOWNLOAD + bookCmis + ".zip");
                    Common.writeBytesToFile(file, zipByte);
                    if(file.exists()) {
                        File fileText = new File(Common.PATH_FOLDER_DOWNLOAD,"full.txt");
                        if(fileText.exists())
                            fileText.delete();
                        if(Common.unpackZip(Common.PATH_FOLDER_DOWNLOAD, bookCmis + ".zip")) {
                            StringBuilder text = new StringBuilder();
                            try {
                                BufferedReader br = new BufferedReader(new FileReader(fileText));
                                String line;

                                while ((line = br.readLine()) != null) {
                                    text.append(line);
                                    text.append('\n');
                                }
                                br.close();

                                JSONObject ja = new JSONObject(text.toString());
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                FileGenResponse fileGenResponse = gson.fromJson(ja.toString(), FileGenResponse.class);
                                for(ListCustomerResponse listCustomerResponse : fileGenResponse.getCustomerResponse()) {
                                    if(mainPageModel.checkCustomerExist(listCustomerResponse.getBodyCustomerResponse().getCustomerCode()) == 0){
                                        if(mainPageModel.insertCustomer(listCustomerResponse) != -1) {
                                            Log.i("INFO", "TEST");
                                        }
                                    }
                                }
                                for(ListBillResponse listBillResponse : fileGenResponse.getBillResponse()) {
                                    listBillResponse.getBodyBillResponse().setEdong(MainActivity.mEdong);
                                    if(mainPageModel.checkBillExist(Integer.parseInt(listBillResponse.getBodyBillResponse().getBillId())) == 0) {
                                        if(mainPageModel.insertBill(listBillResponse) != -1) {
                                            Log.i("INFO", "TEST");
                                        }
                                    }
                                }
                            }
                            catch (IOException e) {
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
            if(sDataCustomer != null && !sDataCustomer.isEmpty()) {
                byte[] zipByteCustomer = org.apache.commons.codec.binary.Base64.decodeBase64(sDataCustomer.getBytes());

                try {
//                    String sCustomer = android.util.Base64.encodeToString(zipByteCustomer, android.util.Base64.DEFAULT);
//                    Log.i("sCustomer", sCustomer);
                    File file = new File(Common.PATH_FOLDER_DATA + bookCmis + "_CUSTOMER.zip");
                    Common.writeBytesToFile(file, zipByteCustomer);
                    if(file.exists()) {
                        File fileText = new File(Common.PATH_FOLDER_DATA,bookCmis + "_CUSTOMER");
                        if(fileText.exists())
                            fileText.delete();
                        if(Common.unpackZip(Common.PATH_FOLDER_DATA, bookCmis + "_CUSTOMER.zip")) {
                            StringBuilder text = new StringBuilder();
                            try {
                                BufferedReader br = new BufferedReader(new FileReader(fileText));
                                String line;

                                while ((line = br.readLine()) != null) {
                                    text.append(line);
                                    text.append('\n');
                                }
                                br.close();

//                                JSONObject ja = new JSONObject(text.toString());
//                                GsonBuilder gsonBuilder = new GsonBuilder();
//                                Gson gson = gsonBuilder.create();
//                                FileGenResponse fileGenResponse = gson.fromJson(ja.toString(), FileGenResponse.class);
//                                for(ListCustomerResponse listCustomerResponse : fileGenResponse.getCustomerResponse()) {
//                                    if(mainPageModel.checkCustomerExist(listCustomerResponse.getBodyCustomerResponse().getCustomerCode()) == 0){
//                                        if(mainPageModel.insertCustomer(listCustomerResponse) != -1) {
//                                            Log.i("INFO", "TEST");
//                                        }
//                                    }
//                                }
//                                for(ListBillResponse listBillResponse : fileGenResponse.getBillResponse()) {
//                                    if(mainPageModel.checkBillExist(Integer.parseInt(listBillResponse.getBodyBillResponse().getBillId())) == 0) {
//                                        if(mainPageModel.insertBill(listBillResponse) != -1) {
//                                            Log.i("INFO", "TEST");
//                                        }
//                                    }
//                                }
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
//                            catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String sDataBill = response.getBodyListDataResponse().getBill();
            if(sDataBill  != null && !sDataBill .isEmpty()) {
                byte[] zipByteBill = org.apache.commons.codec.binary.Base64.decodeBase64(sDataBill.getBytes());

                try {
                    File file = new File(Common.PATH_FOLDER_DATA + bookCmis + "_BILL.zip");
                    Common.writeBytesToFile(file, zipByteBill);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onTimeOut(SoapAPI.AsyncSoapSynchronizeData soapSynchronizeInvoices) {

        }
    };
    //endregion
}
