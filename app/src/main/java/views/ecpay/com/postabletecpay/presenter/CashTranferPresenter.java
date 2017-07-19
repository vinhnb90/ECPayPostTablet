package views.ecpay.com.postabletecpay.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;

import views.ecpay.com.postabletecpay.model.CashTranferModel;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.response.EntityCashTranfer.CashTranferRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityChangePass.ChangePassResponse;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.DoiMatKhau.ChangePassActivity;
import views.ecpay.com.postabletecpay.view.TrangChu.CashTranferFragment;
import views.ecpay.com.postabletecpay.view.TrangChu.ICashTranferView;

import static views.ecpay.com.postabletecpay.util.commons.Common.LONG_TIME_DELAY_ANIM;

/**
 * Created by MyPC on 20/06/2017.
 */

public class CashTranferPresenter implements ICashTranferPresenter{

    private  ICashTranferView iCashTranferView;


    private CashTranferModel cashTranferModel;
    private String mEDong;

    private SoapAPI.AsyncSoapCashTranfer.AsyncSoapCashTranferCallBack callBack = new SoapAPI.AsyncSoapCashTranfer.AsyncSoapCashTranferCallBack() {
        @Override
        public void onPre(final SoapAPI.AsyncSoapCashTranfer soapChangePass) {
            if (soapChangePass == null)
                return;

            //check wifi
            boolean isHasWifi = Common.isConnectingWifi(iCashTranferView.getContextView());
            boolean isHasNetwork = Common.isNetworkConnected(iCashTranferView.getContextView());

            if (!isHasWifi) {
                iCashTranferView.setVisibleBar(false);
                iCashTranferView.showText(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());

                soapChangePass.setEndCallSoap(true);
                soapChangePass.cancel(true);
            }

            if (!isHasNetwork) {
                iCashTranferView.setVisibleBar(false);
                iCashTranferView.showText(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString());

                soapChangePass.setEndCallSoap(true);
                soapChangePass.cancel(true);
            }

            iCashTranferView.setVisibleBar(true);
        }

        @Override
        public void onUpdate(String message) {
            iCashTranferView.setVisibleBar(false);
            if (message == null || message.isEmpty() || message.trim().equals(""))
                return;

            iCashTranferView.showText(message);
        }

        @Override
        public void onPost(CashTranferRespone response) {
            iCashTranferView.setVisibleBar(false);

            if (response == null) {
                iCashTranferView.showText(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                return;
            }


            if(response.getFooter().getResponseCode().equals("000")) //Success
            {

                Account account = cashTranferModel.getAccountInfo(mEDong);
                account.setBalance(account.getBalance() - response.getBody().getAmount());
                cashTranferModel.writeSqliteAccountTable(account);

                iCashTranferView.showText(response.getFooter().getDescription());

                iCashTranferView.onBack();
            }else
            {
                iCashTranferView.showText(response.getFooter().getDescription());
            }

        }

        @Override
        public void onTimeOut(final SoapAPI.AsyncSoapCashTranfer soapChangePass) {
            //thread call asyntask is running. must call in other thread to update UI
            iCashTranferView.setVisibleBar(false);
            if (!soapChangePass.isEndCallSoap()) {
                soapChangePass.cancel(true);
                iCashTranferView.showText(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
            }
        }
    };

    public CashTranferPresenter(ICashTranferView view, String eDong)
    {
        iCashTranferView = view;
        cashTranferModel = new CashTranferModel(view.getContextView());
        mEDong = eDong;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void send(Long amount, String sendPhone, String receivedPhone, String description) {

        Context context = iCashTranferView.getContextView();
        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, mEDong, Common.COMMAND_ID.CASH_TRANSFER.toString(), versionApp);
        } catch (Exception e) {
            iCashTranferView.showText(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString());
            return;
        }


        String json = SoapAPI.getJsonCashTranfer(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                cashTranferModel.getSessionLogin(mEDong),
                sendPhone,
                receivedPhone,
                amount,
                description,
                configInfo.getAccountId()
        );


        if (json == null)
            return;


        try {
            final SoapAPI.AsyncSoapCashTranfer soapChangePass = new SoapAPI.AsyncSoapCashTranfer(callBack);

            if (soapChangePass.getStatus() != AsyncTask.Status.RUNNING) {
                soapChangePass.execute(json);

                //thread time out
                final Thread soapChangePassThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ChangePassResponse changePassResponse = null;

                        //call time out
                        try {
                            Thread.sleep(Common.TIME_OUT_CONNECT);
                        } catch (InterruptedException e) {
                            iCashTranferView.showText(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
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
            iCashTranferView.showText(e.getMessage());
            return;
        }
    }

    @Override
    public Long getCurrentBalance() {
        Account account = cashTranferModel.getAccountInfo(mEDong);
        return account.getBalance();
    }
}
