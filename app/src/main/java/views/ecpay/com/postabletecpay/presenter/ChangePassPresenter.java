package views.ecpay.com.postabletecpay.presenter;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.ecpay.client.test.SecurityUtils;

import views.ecpay.com.postabletecpay.model.ChangePassModel;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.response.EntityChangePass.ChangePassResponse;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.DangNhap.LoginActivity;
import views.ecpay.com.postabletecpay.view.DoiMatKhau.IChangePassView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by VinhNB on 5/20/2017.
 */

public class ChangePassPresenter implements IChangePassPresenter {
    public ChangePassPresenter(IChangePassView mIChangePassView) {
        this.mIChangePassView = mIChangePassView;
        mChangePassModel = new ChangePassModel(mIChangePassView.getContextView());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void validateInputChangePass(String passOld, String passNew, String passRetype) {
        if (passOld == null || passOld.isEmpty() || passOld.trim().equals("")) {
            mIChangePassView.showText(Common.MESSAGE_NOTIFY.CHANGE_PASS_ERR_PASS_OLD.toString());
            return;
        }
        if (passNew == null || passNew.isEmpty() || passNew.trim().equals("")) {
            mIChangePassView.showText(Common.MESSAGE_NOTIFY.CHANGE_PASS_ERR_PASS_NEW.toString());
            return;
        }
        if (passNew.equals(passOld)) {
            mIChangePassView.showText(Common.MESSAGE_NOTIFY.CHANGE_PASS_ERR_PASS_NEW_NOT_EQUAL_PASS_OLD.toString());
            return;
        }
        if (!passRetype.equals(passNew)) {
            mIChangePassView.showText(Common.MESSAGE_NOTIFY.CHANGE_PASS_ERR_PASS_RETYPE_NOT_EQUAL_PASS_RETYPE.toString());
            return;
        }

        String userName = mChangePassModel.getManagerSharedPref()
                .getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE)
                .getString(Common.SHARE_REF_FILE_LOGIN_USER_NAME, "");

        String pass = mChangePassModel.getManagerSharedPref()
                .getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE)
                .getString(Common.SHARE_REF_FILE_LOGIN_PASS, "");

        ConfigInfo configInfo = null;
        String versionApp = "";
        try {
            versionApp = mIChangePassView.getContextView().getPackageManager()
                    .getPackageInfo(mIChangePassView.getContextView().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(mIChangePassView.getContextView(), userName, pass, Common.COMMAND_ID.CHANGE_PIN.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //encrypt pinLogin by Triple DES CBC
        String pinLoginEncrypted;
        try {
            pinLoginEncrypted = SecurityUtils.tripleDesc(mIChangePassView.getContextView(), pass.trim(), configInfo.getPRIVATE_KEY().trim(), configInfo.getPUBLIC_KEY().trim());
        } catch (Exception e) {
            mIChangePassView.showText(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
            return;
        }

        //create request to server
        String session = "";
        String jsonRequestChangePass = SoapAPI.getJsonRequestChangePass(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                pinLoginEncrypted,

                session,
                passNew.trim(),
                passRetype.trim(),

                configInfo.getAccountId());

        if (jsonRequestChangePass == null)
            return;

        try {
            final SoapAPI.AsyncSoapChangePass soapChangePass = new SoapAPI.AsyncSoapChangePass(mSoapChangePassCallBack);

            if (soapChangePass.getStatus() != AsyncTask.Status.RUNNING) {
                soapChangePass.execute(jsonRequestChangePass);

                //thread time out
                final Thread soapChangePassThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ChangePassResponse changePassResponse = null;

                        //call time out
                        try {
                            Thread.sleep(Common.TIME_OUT_CONNECT);
                        } catch (InterruptedException e) {
                            mIChangePassView.showText(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                        } finally {
                            if (changePassResponse == null) {
                                soapChangePass.callCountdown(soapChangePass);
                            }
                        }
                    }
                });

                soapChangePassThread.start();
            }
        } catch (Exception e) {
            mIChangePassView.showText(e.getMessage());
            return;
        }
    }

    private IChangePassView mIChangePassView;
    private ChangePassModel mChangePassModel;
    private SoapAPI.AsyncSoapChangePass.AsyncSoapChangePassCallBack mSoapChangePassCallBack = new SoapAPI.AsyncSoapChangePass.AsyncSoapChangePassCallBack() {
        @Override
        public void onPre(final SoapAPI.AsyncSoapChangePass soapChangePass) {
            if (soapChangePass == null)
                return;

            //check wifi
            boolean isHasWifi = Common.isConnectingWifi(mIChangePassView.getContextView());
            boolean isHasNetwork = Common.isNetworkConnected(mIChangePassView.getContextView());

            if (!isHasWifi) {
                mIChangePassView.hidePbar();
                mIChangePassView.showText(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());

                soapChangePass.setEndCallSoap(true);
                soapChangePass.cancel(true);
            }

            if (!isHasNetwork) {
                mIChangePassView.hidePbar();
                mIChangePassView.showText(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString());

                soapChangePass.setEndCallSoap(true);
                soapChangePass.cancel(true);
            }
        }

        @Override
        public void onUpdate(String message) {
            mIChangePassView.hidePbar();
            if (message == null || message.isEmpty() || message.trim().equals(""))
                return;

            mIChangePassView.showText(message);
        }

        @Override
        public void onPost(ChangePassResponse response) {
            mIChangePassView.hidePbar();

            if (response == null) {
                mIChangePassView.showText(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return;
            }

            Common.CODE_REPONSE_CHANGE_PASS codeResponse = Common.CODE_REPONSE_CHANGE_PASS.findCodeMessage(response.getFooterChangePassResponse().getResponseCode());
            if (codeResponse != Common.CODE_REPONSE_CHANGE_PASS.e000) {
                mIChangePassView.showText(codeResponse.getMessage());
                return;
            }

            mIChangePassView.showText(Common.MESSAGE.CHANGE_PASS_SUCSSES.getMessage());
        }

        @Override
        public void onTimeOut(final SoapAPI.AsyncSoapChangePass soapChangePass) {
            soapChangePass.cancel(true);

            //thread call asyntask is running. must call in other thread to update UI
            ((LoginActivity) mIChangePassView).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIChangePassView.hidePbar();
                    if (!soapChangePass.isEndCallSoap()) {
                        mIChangePassView.showText(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                    }
                }
            });
        }
    };
}