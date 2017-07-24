package views.ecpay.com.postabletecpay.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;

import views.ecpay.com.postabletecpay.model.PayModel;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogout.LogoutResponse;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.Logout.ILogoutView;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.TrangChu.IMainPageView;

import static views.ecpay.com.postabletecpay.util.commons.Common.TAG;
import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_OUT_CONNECT;

/**
 * Created by VinhNB_PC on 6/19/2017.
 */

public class LogoutPresenter implements ILogoutPresenter {

    protected Context context;
    protected ILogoutView iLogoutView;

    private SoapAPI.AsyncSoapLogout soapLogout;
    private Handler handlerDelay = new Handler();

    public LogoutPresenter(ILogoutView iLogoutView) {
        this.iLogoutView = iLogoutView;
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void callLogout(String mEdong) {
        context = iLogoutView.getContextView();
        String textMessage = "";
        Boolean isErr = false;

        if (mEdong == null || mEdong.isEmpty() || mEdong.trim().equals("") && !isErr) {
            return;
        }

        iLogoutView.showStatusProgressLogout(Common.STATUS_PROGRESS.BEGIN);

        //check wifi and network
//        if (!Common.isConnectingWifi(context) && !isErr) {
//            textMessage = Common.MESSAGE_NOTIFY.ERR_WIFI.toString();
//            isErr = true;
//        }
        if (!Common.isNetworkConnected(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_NETWORK.toString();
            isErr = true;
        }
        if (isErr) {
            iLogoutView.showStatusProgressLogout(Common.STATUS_PROGRESS.ERROR);
            iLogoutView.showMessageLogout(textMessage);
            return;
        }

        //setup info login
        ConfigInfo configInfo;
        String versionApp = Common.getVersionApp(context);

        try {
            configInfo = Common.setupInfoRequest(context, mEdong, Common.COMMAND_ID.LOGOUT.toString(), versionApp);
        } catch (Exception e) {
            iLogoutView.showStatusProgressLogout(Common.STATUS_PROGRESS.ERROR);
            iLogoutView.showMessageLogout(textMessage);
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
            final String maKH = "";
            final String soTien = "";
            final String kyPhatSinh = "";
            Common.writeLogUser(MainActivity.mEdong, maKH, soTien, kyPhatSinh, "", "", Common.COMMAND_ID.LOGOUT, true);
            SoapAPI.AsyncSoapLogout.AsyncSoapLogoutCallBack asyncSoapLogoutCallBack = new SoapAPI.AsyncSoapLogout.AsyncSoapLogoutCallBack() {
                private String edong;

                @Override
                public void onPre(final SoapAPI.AsyncSoapLogout soapLogout) {
                    edong = soapLogout.getEdong();

                    iLogoutView.showStatusProgressLogout(Common.STATUS_PROGRESS.BEGIN);

                    //check wifi
                    boolean isHasNetwork = Common.isNetworkConnected(iLogoutView.getContextView());

                    if (!isHasNetwork) {
                        iLogoutView.showStatusProgressLogout(Common.STATUS_PROGRESS.ERROR);
                        iLogoutView.showMessageLogout(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString());

                        soapLogout.setEndCallSoap(true);
                        soapLogout.cancel(true);
                    }
                }

                @Override
                public void onUpdate(final String message) {
                    if (message == null || message.isEmpty() || message.trim().equals(""))
                        return;

                    iLogoutView.showStatusProgressLogout(Common.STATUS_PROGRESS.ERROR);
                    iLogoutView.showMessageLogout(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());

                }

                @Override
                public void onPost(LogoutResponse response) {
                    if (response == null) {
                        try {
                            Common.writeLogUser(MainActivity.mEdong, maKH, soTien, kyPhatSinh, "", "", Common.COMMAND_ID.LOGOUT, false);
                        } catch (Exception e) {
                            Log.e(ContentValues.TAG, "doInBackground: Lỗi khi không tạo được file log");
                        }
                        return;
                    }

                    String maLoi = "";
                    String moTaLoi = "";
                    if (response.getFooter() != null) {
                        maLoi = response.getFooter().getResponseCode();
                        moTaLoi = response.getFooter().getDescription();
                    }

                    try {
                        Common.writeLogUser(MainActivity.mEdong, maKH, soTien, kyPhatSinh, maLoi, moTaLoi, Common.COMMAND_ID.LOGOUT, false);
                    } catch (Exception e) {
                        Log.e(ContentValues.TAG, "doInBackground: Lỗi khi không tạo được file log");
                    }

                    iLogoutView.showRespone(response.getFooter().getResponseCode(), response.getFooter().getDescription());
                    Common.CODE_REPONSE_LOGOUT codeResponse = Common.CODE_REPONSE_LOGOUT.findCodeMessage(response.getFooter().getResponseCode());
                    if (codeResponse != Common.CODE_REPONSE_LOGOUT.e000) {
                        iLogoutView.showStatusProgressLogout(Common.STATUS_PROGRESS.ERROR);
                        iLogoutView.showMessageLogout(codeResponse.getMessage());
                        return;
                    }

                    iLogoutView.showStatusProgressLogout(Common.STATUS_PROGRESS.SUCCESS);
                    iLogoutView.showMessageLogout(Common.CODE_REPONSE_LOGOUT.e000.getMessage());
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Common.deleteAllFileFolderDownload();
                                Common.loadFolder((MainActivity) iLogoutView.getContextView());
                                iLogoutView.showLoginForm();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, Common.MORE_LONG_TIME_DELAY_ANIM);
                }

                @Override
                public void onTimeOut(final SoapAPI.AsyncSoapLogout asyncSoapLogoutCallBack) {
                    asyncSoapLogoutCallBack.cancel(true);

                    //thread call asyntask is running. must call in other thread to update UI
                    ((MainActivity) iLogoutView.getContextView()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!asyncSoapLogoutCallBack.isEndCallSoap()) {
                                iLogoutView.showMessageLogout(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());

                            }
                        }
                    });
                }
            };


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
            iLogoutView.showStatusProgressLogout(Common.STATUS_PROGRESS.ERROR);
            iLogoutView.showMessageLogout(Common.CODE_REPONSE_LOGOUT.e9999.getMessage());
            Log.e(TAG, "callLogout: " + Common.CODE_REPONSE_LOGOUT.e10000.getMessage());
            return;
        }
    }

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
}
