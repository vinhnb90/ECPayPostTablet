package views.ecpay.com.postabletecpay.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.apache.log4j.chainsaw.Main;
import org.ecpay.client.test.SecurityUtils;

import java.lang.reflect.Type;
import java.util.List;

import views.ecpay.com.postabletecpay.model.LoginModel;
import views.ecpay.com.postabletecpay.model.sharedPreference.SharePrefManager;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.commons.Common.CODE_REPONSE_LOGIN;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.AccountLoginResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.ListEvnPCLoginResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.LoginResponseReponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.ResponseLoginResponse;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.util.entities.sqlite.EvnPC;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI.AsyncSoapLogin.AsyncSoapLoginCallBack;
import views.ecpay.com.postabletecpay.view.DangNhap.ILoginView;
import views.ecpay.com.postabletecpay.view.DangNhap.LoginActivity;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_OUT_CONNECT;

/**
 * Created by VinhNB on 5/11/2017.
 */

public class LoginPresenter implements ILoginPresenter {
    private LoginModel mLoginModel;
    private SoapAPI.AsyncSoapLogin soapLogin;
    private Handler handlerDelay = new Handler();
    private Runnable runnableCountTimeLogin = new Runnable() {
        @Override
        public void run() {
            if (soapLogin != null && soapLogin.isEndCallSoap())
                return;
            //Do something after 100ms
            LoginResponseReponse loginResponseReponse = soapLogin.getLoginResponseReponse();

            if (loginResponseReponse == null && !soapLogin.isEndCallSoap()) {
                //call time out
                soapLogin.callCountdown(soapLogin);
            }
        }
    };


    public LoginPresenter(ILoginView mILoginView) {
        if (mILoginView == null)
            return;
        this.mILoginView = mILoginView;
        mLoginModel = new LoginModel(mILoginView.getContextView());

        mSharedPrefLogin = mLoginModel.getManagerSharedPref();
        mSharedPrefLogin.addSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void validateInput(String userName, String pass) {
//        mILoginView.showMainScreen("01214500702");
//        if(true)
//            return;

        String textMessage = "";
        Context context = mILoginView.getContextView();
        Boolean isErr = false;

        if ((userName == null || userName.isEmpty() || userName.trim().equals("")) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.LOGIN_ERR_USER.toString();
            isErr = true;
        }
        if ((pass == null || pass.isEmpty() || pass.trim().equals("")) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.LOGIN_ERR_PASS.toString();
            isErr = true;
        }

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
            mILoginView.showTextMessage(textMessage);
            mILoginView.hidePbarLogin();
            return;
        }

        //setup info login
        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = mILoginView.getContextView().getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, userName, Common.COMMAND_ID.LOGIN.toString(), versionApp);
//            configInfo = Common.setupInfoRequest(context, userName, Common.COMMAND_ID.GET_BOOK_CMIS_BY_CASHIER.toString(), versionApp);
        } catch (Exception e) {
            mILoginView.showTextMessage(e.getMessage());
            return;
        }

        //encrypt pinLogin by Triple DES CBC
        String pinLoginEncrypted;
        try {
            pinLoginEncrypted = SecurityUtils.tripleDesc(context, pass.trim(), configInfo.getPRIVATE_KEY().trim(), configInfo.getPUBLIC_KEY().trim());
        } catch (Exception e) {
            mILoginView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
            return;
        }

        //create request to server
        String jsonRequestLogin = SoapAPI.getJsonRequestLogin(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                pinLoginEncrypted,
                configInfo.getAccountId(),
                configInfo.getVersionApp());
        final String soViQuay = configInfo.getAccountId();
        try {
            Common.writeLogUser(soViQuay, "", "", "", "", "", Common.COMMAND_ID.LOGIN, true);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Common.loadFolder((LoginActivity)mILoginView.getContextView());
        }

        if (jsonRequestLogin != null) {
            try {
                AsyncSoapLoginCallBack soapLoginCallBack = new AsyncSoapLoginCallBack() {
                    @Override
                    public void onPre(final SoapAPI.AsyncSoapLogin soapLogin) {
                        mILoginView.hideTextMessage();
                        mILoginView.showPbarLogin();

                        boolean isHasNetwork = Common.isNetworkConnected(mILoginView.getContextView());

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
                    public void onPost(LoginResponseReponse response) {
                        if (response == null) {
                            try {
                                Common.writeLogUser(soViQuay, "", "", "", "", "", Common.COMMAND_ID.LOGIN, false);
                            } catch (Exception e) {
                                Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
                            }
                            finally {
                                Common.loadFolder((LoginActivity)mILoginView.getContextView());
                            }
                            return;
                        }

                        String maLoi = "";
                        String moTaLoi = "";
                        if (response.getFooterLoginResponse() != null) {
                            maLoi = response.getFooterLoginResponse().getResponseCode();
                            moTaLoi = response.getFooterLoginResponse().getDescription();
                        }

                        try {
                            Common.writeLogUser(soViQuay, "", "", "", maLoi, moTaLoi, Common.COMMAND_ID.LOGIN, false);
                        } catch (Exception e) {
                            Log.e(TAG, "doInBackground: Lỗi khi không tạo được file log");
                        }
                        finally {
                            Common.loadFolder((LoginActivity)mILoginView.getContextView());
                        }

                        mILoginView.showRespone(response.getFooterLoginResponse().getResponseCode(), response.getFooterLoginResponse().getDescription());
                        mILoginView.hidePbarLogin();

                        if (response == null) {
                            mILoginView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                            return;
                        }

                        CODE_REPONSE_LOGIN codeResponse = CODE_REPONSE_LOGIN.findCodeMessage(response.getFooterLoginResponse().getResponseCode());
                        if (codeResponse != CODE_REPONSE_LOGIN.e000) {
                            mILoginView.showTextMessage(codeResponse.getMessage());
                            return;
                        }

                        //get responseLoginResponse from body response
                        //because server return string not object
                        String responseLoginResponseData = response.getBodyLoginResponse().getResponseLoginResponse();
                        // định dạng kiểu Object JSON
                        Type type = new TypeToken<ResponseLoginResponse>() {
                        }.getType();
                        ResponseLoginResponse responseLoginResponse = null;
                        try {
                            responseLoginResponse = new Gson().fromJson(responseLoginResponseData, type);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }

                        if (responseLoginResponse == null)
                            return;

                        //get AccountLoginResponse from body response
                        AccountLoginResponse accountLoginResponse = responseLoginResponse.getAccountLoginResponse();
//                    response.getBodyLoginResponse().getResponseLoginResponse().getAccountLoginResponse();

                        //get account
                        Account account = null;
                        if (accountLoginResponse != null) {
                            account = new Account(
                                    accountLoginResponse.getEdong(),
                                    accountLoginResponse.getName(),
                                    accountLoginResponse.getAddress(),
                                    accountLoginResponse.getPhone(),
                                    accountLoginResponse.getEmail(),
                                    accountLoginResponse.getBirthday(),
                                    accountLoginResponse.getSession(),
                                    accountLoginResponse.getBalance(),
                                    accountLoginResponse.getLockMoney(),
                                    accountLoginResponse.getChangedPIN(),
                                    accountLoginResponse.getVerified(),
                                    accountLoginResponse.getMac(),
                                    accountLoginResponse.getIp(),
                                    (String) accountLoginResponse.getStrLoginTime(),
                                    (String) accountLoginResponse.getStrLogoutTime(),
                                    accountLoginResponse.getType(),
                                    accountLoginResponse.getStatus(),
                                    accountLoginResponse.getIdNumber(),
                                    accountLoginResponse.getIdNumberDate(),
                                    accountLoginResponse.getIdNumberPlace(),
                                    accountLoginResponse.getParentEdong()
                            );

                            //write database
                            mLoginModel.writeSqliteAccountTable(account);
                        }

            /*//get List<ListEvnPCLoginResponse> from body response
            List<ListEvnPCLoginResponse> evnPCList = responseLoginResponse.getListEvnPCLoginResponse();
//                    response.getBodyLoginResponse().getResponseLoginResponse().getListEvnPCLoginResponse();

            if (evnPCList != null) {
                for (ListEvnPCLoginResponse pc :
                        evnPCList) {
                    EvnPC evnPC = new EvnPC();

                    evnPC.setParentId(pc.getParentId());
                    evnPC.setPcId(pc.getPcId());
                    evnPC.setCode(pc.getCode());
                    evnPC.setExt(pc.getExt());
                    evnPC.setFullName(pc.getFullName());
                    evnPC.setShortName(pc.getShortName());
                    evnPC.setAddress((String) pc.getAddress());
                    evnPC.setTaxCode((String) pc.getTaxCode());
                    evnPC.setPhone1((String) pc.getPhone1());
                    evnPC.setPhone2((String) pc.getPhone2());
                    evnPC.setFax((String) pc.getFax());
                    evnPC.setLevel(pc.getLevel());

                    //write database
                    mLoginModel.writeSqliteEvnPcTable(evnPC, account.getEdong());
                }
            }*/
                        //show main
                        mILoginView.showMainScreen(account.getEdong());
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

                if (soapLogin == null) {
                    //if null then create new
                    soapLogin = new SoapAPI.AsyncSoapLogin(soapLoginCallBack);
                } else if (soapLogin.getStatus() == AsyncTask.Status.PENDING) {
                    //if running not yet then run

                } else if (soapLogin.getStatus() == AsyncTask.Status.RUNNING) {
                    //if is running
                    soapLogin.setEndCallSoap(true);
                    soapLogin.cancel(true);

                    handlerDelay.removeCallbacks(runnableCountTimeLogin);
                    soapLogin = new SoapAPI.AsyncSoapLogin(soapLoginCallBack);
                } else {
                    //if running or finish
                    handlerDelay.removeCallbacks(runnableCountTimeLogin);

                    soapLogin = new SoapAPI.AsyncSoapLogin(soapLoginCallBack);
                }

                soapLogin.execute(jsonRequestLogin);

                //thread time out
                //sleep
                handlerDelay.postDelayed(runnableCountTimeLogin, TIME_OUT_CONNECT);


                /*soapLogin = new SoapAPI.AsyncSoapLogin(soapLoginCallBack);

                if (soapLogin.getTRANG_THAI_TT() != AsyncTask.Status.RUNNING) {
                    soapLogin.execute(jsonRequestLogin);

                    //thread time out
                    Thread soapLoginThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LoginResponseReponse loginResponseReponse = null;

                            //call time out
                            try {
                                Thread.sleep(Common.TIME_OUT_CONNECT);
                            } catch (InterruptedException e) {
                                mILoginView.showTextMessage(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                            } finally {
                                if (loginResponseReponse == null) {
                                    soapLogin.callCountdown(soapLogin);
                                }
                            }
                        }
                    });

                    soapLoginThread.start();
                }*/
            } catch (Exception e) {
                mILoginView.showTextMessage(e.getMessage());
                return;
            }
        }
    }

    @Override
    public void writeSharedPrefLogin(String userName, String pass) {
        if (userName == null || userName.isEmpty()) {
            return;
        }
        if (pass == null || pass.isEmpty()) {
            return;
        }

        mSharedPrefLogin.getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).
                edit().
                putString(Common.SHARE_REF_FILE_LOGIN_USER_NAME, userName).
                putString(Common.SHARE_REF_FILE_LOGIN_PASS, pass).
                putBoolean(Common.SHARE_REF_FILE_LOGIN_IS_SAVE, true).
                commit();
    }

    @Override
    public void clearSharedPrefLogin() {
        if (mSharedPrefLogin != null) {
            mSharedPrefLogin.getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).
                    edit().
                    clear().
                    commit();
        }
    }

    @Override
    public void showInfoSharePrefLogin() {
        if (mSharedPrefLogin != null) {
            String userName = mSharedPrefLogin.getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).
                    getString(Common.SHARE_REF_FILE_LOGIN_USER_NAME, "");
            String pass = mSharedPrefLogin.getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).
                    getString(Common.SHARE_REF_FILE_LOGIN_PASS, "");
            boolean isSaveLogin = mSharedPrefLogin.getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE).
                    getBoolean(Common.SHARE_REF_FILE_LOGIN_IS_SAVE, false);

            mILoginView.showTextUserPass(userName, pass);
            mILoginView.showTickCheckbox(isSaveLogin);
        }
    }

    private ILoginView mILoginView;

    private SharePrefManager mSharedPrefLogin;

}