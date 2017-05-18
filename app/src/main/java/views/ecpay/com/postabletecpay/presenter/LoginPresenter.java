package views.ecpay.com.postabletecpay.presenter;

import android.content.Context;
import android.os.AsyncTask;

import views.ecpay.com.postabletecpay.model.LoginModel;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.LoginResponse;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI.AsyncSoapLogin.AsyncSoapLoginCallBack;
import views.ecpay.com.postabletecpay.view.DangNhap.ILoginView;
import views.ecpay.com.postabletecpay.view.DangNhap.LoginActivity;

/**
 * Created by VinhNB on 5/11/2017.
 */

public class LoginPresenter implements ILoginPresenter {
    private LoginModel mLoginModel;

    public LoginPresenter(ILoginView mILoginView) {
        if (mILoginView == null)
            return;
        this.mILoginView = mILoginView;
        mLoginModel = new LoginModel();
    }

    @Override
    public void validateInput(String userName, String pass) {
        String textMessage = "";
        Context context = mILoginView.getContextView();
        Boolean isErr = false;

        mILoginView.hidePbarLogin();
        if ((userName == null || userName.isEmpty() || userName.trim().equals("")) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.LOGIN_ERR_USER.toString();
            isErr = true;
        }
        if ((pass == null || pass.isEmpty() || pass.trim().equals("")) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.LOGIN_ERR_PASS.toString();
            isErr = true;
        }
/*
        //check wifi and network
        if (!Common.isConnectingWifi(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_WIFI.toString();
            isErr = true;
        }
        if (!Common.isNetworkConnected(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_NETWORK.toString();
            isErr = true;
        }*/

        if (isErr) {
            mILoginView.showTextMessage(textMessage);
            mILoginView.hidePbarLogin();
            return;
        }

        //setup info login
        long auditNumber;

        String agent, commandId, macAdressHexValue, diskDriver, pcCode, accountId, privateKeyRSA = "";
        ConfigInfo configInfo;

        configInfo = Common.getDataFileConfig();

        String timeNow = Common.getDateTimeNow6Digit();
        auditNumber = Common.createAuditNumber(timeNow);
        commandId = Common.COMMAND_ID.LOGIN.name();

        try {
            //get and convert mac adress to hex
            macAdressHexValue = Common.getMacAddress(context);
        } catch (Exception e) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString();
            mILoginView.showTextMessage(textMessage);
            return;
        }

        diskDriver = Common.getIMEIAddress(context);
        pcCode = configInfo.getPC_CODE();
        accountId = userName.toString().trim();
        agent = configInfo.getAGENT();
        privateKeyRSA = configInfo.getPRIVATE_KEY();

        if (privateKeyRSA == null || privateKeyRSA.isEmpty() || privateKeyRSA.trim().isEmpty()) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString();
            mILoginView.showTextMessage(textMessage);
            return;
        }

        //encrypt password of agent by RSA
        String passwordAgent, passwordAgentEcrypted;

        passwordAgent = configInfo.getPASS_WORD();
        if (passwordAgent == null || passwordAgent.isEmpty() || passwordAgent.trim().isEmpty()) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString();
            mILoginView.showTextMessage(textMessage);
            return;
        }

        try {
            passwordAgentEcrypted = Common.encryptPasswordAgentByRSA(passwordAgent.trim(), privateKeyRSA);
        } catch (Exception e) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString();
            mILoginView.showTextMessage(textMessage);
            return;
        }

       /* //test
        String passDecypted = "";
        try {
            AsymmetricCryptography ac = new AsymmetricCryptography();
            PublicKey publicKey = ac.getPublic(publicKeyRSA);
            passDecypted = ac.decryptText(passEncrypted, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //end test*/

        //encrypt signature by RSA
        String signatureEncrypted;

        try {
            signatureEncrypted = Common.encryptSignatureByRSA(agent, commandId, auditNumber, macAdressHexValue, diskDriver, pcCode, accountId, privateKeyRSA);
        } catch (Exception e) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString();
            mILoginView.showTextMessage(textMessage);
            return;
        }

        //encrypt pinLogin by Triple DES CBC
        String publicKeyRSA, pinLoginEncrypted;

        publicKeyRSA = configInfo.getPUBLIC_KEY();

        if (privateKeyRSA == null || privateKeyRSA.isEmpty() || privateKeyRSA.trim().isEmpty()) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString();
            mILoginView.showTextMessage(textMessage);
            return;
        }

//        pinLogin = Common.encryptPassByTripleDsCbc(pass.trim(), keyTripleDsCbc, initializationVector, padding);
        try {
            pinLoginEncrypted = Common.encryptPassByTripleDsCbc(pass.trim(), publicKeyRSA, privateKeyRSA);
        } catch (Exception e) {
            mILoginView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_PASS.toString());
            return;
        }


        //create request to server
        String jsonRequestLogin = SoapAPI.getJsonRequestLogin(agent, passwordAgentEcrypted, commandId, auditNumber, macAdressHexValue, diskDriver, signatureEncrypted, pinLoginEncrypted, accountId);

        if (jsonRequestLogin != null) {
            try {
                final SoapAPI.AsyncSoapLogin soapLogin;

                soapLogin = new SoapAPI.AsyncSoapLogin(soapLoginCallBack);

                if (soapLogin.getStatus() != AsyncTask.Status.RUNNING) {
                    soapLogin.execute();

                    //thread time out
                    Thread soapLoginThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LoginResponse loginResponse = null;

                            //call time out
                            try {
                                Thread.sleep(Common.TIME_OUT_CONNECT);
                            } catch (InterruptedException e) {
                                mILoginView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                            } finally {
                                if (loginResponse == null) {
                                    soapLogin.callCountdown(soapLogin);
                                }
                            }
                        }
                    });

                    soapLoginThread.start();
                }

            } catch (Exception e) {
                mILoginView.showTextMessage(e.getMessage());
                return;
            }

        }
    }

    private ILoginView mILoginView;

    private AsyncSoapLoginCallBack soapLoginCallBack = new AsyncSoapLoginCallBack() {
        @Override
        public void onPre(final SoapAPI.AsyncSoapLogin soapLogin) {
            mILoginView.hideTextMessage();
            mILoginView.showPbarLogin();

            //check wifi
            boolean isHasWifi = Common.isConnectingWifi(mILoginView.getContextView());
            boolean isHasNetwork = Common.isNetworkConnected(mILoginView.getContextView());

            if (!isHasWifi) {
                mILoginView.hidePbarLogin();
                mILoginView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());

                soapLogin.setEndCallSoap(true);
                soapLogin.cancel(true);
            }

            if (!isHasNetwork) {
                mILoginView.hidePbarLogin();
                mILoginView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString());

                soapLogin.setEndCallSoap(true);
                soapLogin.cancel(true);
            }

        }

        @Override
        public void onUpdate(String message) {
            mILoginView.hidePbarLogin();
            if (message == null || message.isEmpty() || message.trim().equals(""))
                return;

            mILoginView.showTextMessage(message);
        }

        @Override
        public void onPost(LoginResponse response) {
            mILoginView.hidePbarLogin();

            if (response == null) {
                mILoginView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return;
            }

            mILoginView.showMainScreen();
        }

        @Override
        public void onTimeOut(final SoapAPI.AsyncSoapLogin soapLogin) {
            soapLogin.cancel(true);

            //thread call asyntask is running. must call in other thread to update UI
            ((LoginActivity) mILoginView).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mILoginView.hidePbarLogin();
                    if (!soapLogin.isEndCallSoap()) {
                        mILoginView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                    }
                }
            });
        }
    };
}
