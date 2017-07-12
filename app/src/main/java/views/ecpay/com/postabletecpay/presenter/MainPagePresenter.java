package views.ecpay.com.postabletecpay.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;

import views.ecpay.com.postabletecpay.model.MainPageModel;
import views.ecpay.com.postabletecpay.model.PayModel;
import views.ecpay.com.postabletecpay.model.sharedPreference.SharePrefManager;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogout.LogoutResponse;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.TrangChu.IMainPageView;

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
}

