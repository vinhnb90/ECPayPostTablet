package views.ecpay.com.postabletecpay.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;

import org.ecpay.client.test.SecurityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import views.ecpay.com.postabletecpay.model.MainPageModel;
import views.ecpay.com.postabletecpay.model.sharedPreference.SharePrefManager;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.response.EntityData.ListDataResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityDataZip.ListDataZipResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListEVNReponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.ListEvnPCLoginResponse;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.TrangChu.IMainPageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by VinhNB on 5/23/2017.
 */

public class MainPagePresenter implements IMainPagePresenter{
    private IMainPageView iMainPageView;
    private MainPageModel mainPageModel;
    private SharePrefManager mSharedPrefLogin;

    public MainPagePresenter(IMainPageView iMainPageView) {
        this.iMainPageView = iMainPageView;
        mainPageModel = new MainPageModel(iMainPageView.getContextView());
        mSharedPrefLogin = mainPageModel.getManagerSharedPref();
    }

    //region IMainPagePresenter
    @Override
    public void getInfoMain(String edong) {
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

        }

        @Override
        public void onTimeOut(SoapAPI.AsyncSoapSynchronizePC soapSynchronizeInvoices) {

        }
    };
    //endregion

    //region đồng bộ hoá đơn
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
            configInfo = Common.setupInfoRequest(context, userName, Common.COMMAND_ID.GET_FILE_GEN.toString(), versionApp, "PD16");
        } catch (Exception e) {
            iMainPageView.showTextMessage(e.getMessage());
            return;
        }

//        String dataSign = Common.getDataSignRSA(agent, commandId, auditNumber, macAdressHexValue, diskDriver, pcCode, accountId, privateKeyRSA);

        String jsonRequestZipData = SoapAPI.getJsonRequestSynDataZip(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
//                "DbwnSmHdcXKYCZ6yARbXW+KwGGLXwPbYuAuhvq66Sdsp3NXx88QLXWkSr7vqVV+zviRcF86YCMno\\n/Ycr+dGOLNfXpuMULu28bdc0JSxbQdV9TfI1fdFHPyGqhF8+x+Pe10Y5enaR4R8VaEnEVC1ortws\\nCk+oVBjXb+GyYG9fK/4=\\n",//configInfo.getSignatureEncrypted(),
//                configInfo.getPC_CODE(),
                "PD16",
                "",//"D1680225",
                configInfo.getAccountId());

        if (jsonRequestZipData != null) {
            try {
                final SoapAPI.AsyncSoapSynchronizeDataZip soapSynchronizeDataZip;

                soapSynchronizeDataZip = new SoapAPI.AsyncSoapSynchronizeDataZip(callBackData);

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
    }

    private SoapAPI.AsyncSoapSynchronizeDataZip.AsyncSoapSynchronizeDataZipCallBack callBackData = new SoapAPI.AsyncSoapSynchronizeDataZip.AsyncSoapSynchronizeDataZipCallBack() {
        @Override
        public void onPre(SoapAPI.AsyncSoapSynchronizeDataZip soapSynchronizeInvoices) {

        }

        @Override
        public void onUpdate(String message) {

        }

        @Override
        public void onPost(ListDataZipResponse response) {
            String sData = response.getBodyListDataResponse().getFile_data();
            byte[] decodedContent = org.apache.commons.codec.binary.Base64.decodeBase64(sData .getBytes());



//            ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(decodedContent));
//
//            try{
//                ZipEntry entry = null;
//
//                while ((entry = zipStream.getNextEntry()) != null) {
//
//                    String fileName = entry.getName();
//
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//                    byte[] buffer = new byte[1024];
//                    int count;
//
//                    while ((count = zipStream.read(buffer)) != -1) {
//                        baos.write(buffer, 0, count);
//                    }
//
//                    baos.close();
//                    zipStream.closeEntry();
//
//                    byte[] bytes = baos.toByteArray();
//
//                    writeBytesToFile(new File(fileName), bytes);
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally{
//                try {
//                    zipStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }



//            FileOutputStream fos = null;
//            try {
//                if (decoded != null) {
//                    fos = iMainPageView.getContextView().openFileOutput(Environment.getExternalStorageDirectory() + "/data.zip", Context.MODE_PRIVATE);
//                    byte[] decodedString = android.util.Base64.decode(decoded, android.util.Base64.DEFAULT);
//                    fos.write(decodedString);
//                    fos.flush();
//                    fos.close();
//                }
//
//            } catch (Exception e) {
//
//            } finally {
//                if (fos != null) {
//                    fos = null;
//                }
//            }
        }

        @Override
        public void onTimeOut(SoapAPI.AsyncSoapSynchronizeDataZip soapSynchronizeInvoices) {

        }
    };

    public static void writeBytesToFile(File theFile, byte[] bytes) throws IOException {
        BufferedOutputStream bos = null;

        try {
            FileOutputStream fos = new FileOutputStream(theFile);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        }finally {
            if(bos != null) {
                try  {
                    //flush and close the BufferedOutputStream
                    bos.flush();
                    bos.close();
                } catch(Exception e){}
            }
        }
    }
    //endregion
}
