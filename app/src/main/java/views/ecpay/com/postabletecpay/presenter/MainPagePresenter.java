package views.ecpay.com.postabletecpay.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.ecpay.client.test.SecurityUtils;

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
import static views.ecpay.com.postabletecpay.util.commons.Common.TAG;

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
            configInfo = Common.setupInfoRequest(context, userName, Common.COMMAND_ID.GET_FILE_GEN.toString(), versionApp);
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
//        String dataSign = Common.getDataSignRSA(agent, commandId, auditNumber, macAdressHexValue, diskDriver, pcCode, accountId, privateKeyRSA);

        String jsonRequestZipData = SoapAPI.getJsonRequestSynDataZip(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                "PD16",
                "D1680225",
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

        }

        @Override
        public void onTimeOut(SoapAPI.AsyncSoapSynchronizeDataZip soapSynchronizeInvoices) {

        }
    };
    //endregion
}
