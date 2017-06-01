package views.ecpay.com.postabletecpay.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.ecpay.client.test.SecurityUtils;

import views.ecpay.com.postabletecpay.model.MainPageModel;
import views.ecpay.com.postabletecpay.model.sharedPreference.SharePrefManager;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
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

        //Vinh NB : anh viết các tham số vào theo mô phỏng đầu ra json của GET_BOOK_CMIS_BY_CASHIER
        String jsonRequestSynchronizePC = SoapAPI.getJsonRequestSynchronizePC();

        if (jsonRequestSynchronizePC != null) {
            try {
                final SoapAPI.AsyncSoapSynchronizePC soapSynchronizePC;

                soapSynchronizePC = new SoapAPI.AsyncSoapSynchronizePC(callBack);

                if (soapSynchronizePC.getStatus() != AsyncTask.Status.RUNNING) {
                    soapSynchronizePC.execute(jsonRequestSynchronizePC);

                    //thread time out
                    Thread soapLoginThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ListEvnPCLoginResponse listEvnPCLoginResponse = null;

                            //call time out
                            try {
                                Thread.sleep(Common.TIME_OUT_CONNECT);
                            } catch (InterruptedException e) {
                                iMainPageView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                            } finally {
                                if (listEvnPCLoginResponse == null) {
                                    soapSynchronizePC.callCountdown(soapSynchronizePC);
                                }
                            }
                        }
                    });

                    soapLoginThread.start();
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
}
