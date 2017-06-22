package views.ecpay.com.postabletecpay.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.ecpay.client.test.SecurityUtils;

import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.response.EntityChangePass.ChangePassResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchCustomer.SearchCustomerRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchCustomerBill.SearchCustomerBillRespone;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.TrangChu.ISearchCustomerView;

import static views.ecpay.com.postabletecpay.util.commons.Common.TAG;

/**
 * Created by MyPC on 22/06/2017.
 */

public class SearchCustomerPresenter implements ISearchCustomerPresenter {

    private ISearchCustomerView searchCustomerView;
    private String mEDong;

    private  SoapAPI.AsyncSoapSearchCustomerBill.AsyncSoapCallBack callBack = new SoapAPI.AsyncSoapSearchCustomerBill.AsyncSoapCallBack() {
        @Override
        public void onPre(SoapAPI.AsyncSoapSearchCustomerBill soap) {

        }

        @Override
        public void onUpdate(String message) {

        }

        @Override
        public void onPost(SearchCustomerBillRespone response) {
            Log.d("LOG", "RESOUN");
        }

        @Override
        public void onTimeOut(SoapAPI.AsyncSoapSearchCustomerBill soap) {

        }
    };

    public  SearchCustomerPresenter(ISearchCustomerView view, String eDong)
    {
        searchCustomerView = view;
        mEDong = eDong;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void search(String maKH, String tenKH, String dcKH, String phoneKH, String gtKH, String pcCode, int directEVN) {
        Context context = searchCustomerView.getContextView();
        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, mEDong, Common.COMMAND_ID.SEARCH_CUSTOMER_BILL.toString(), versionApp);
        } catch (Exception e) {

            return;
        }


//        String json = SoapAPI.getJsonSearchCustomer(
//                configInfo.getAGENT(),
//                configInfo.getAgentEncypted(),
//                configInfo.getCommandId(),
//                configInfo.getAuditNumber(),
//                configInfo.getMacAdressHexValue(),
//                configInfo.getDiskDriver(),
//                configInfo.getSignatureEncrypted(),
//                maKH,
//                tenKH,
//                phoneKH,
//                dcKH,
//                gtKH,
//                pcCode,
//                directEVN,
//                configInfo.getAccountId()
//        );

        String json = SoapAPI.getJsonSearchCustomerBill(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                maKH,
                configInfo.getAccountId()
        );



        if (json == null)
            return;


        try {
            final SoapAPI.AsyncSoapSearchCustomerBill soapChangePass = new SoapAPI.AsyncSoapSearchCustomerBill(callBack);

            if (soapChangePass.getStatus() != AsyncTask.Status.RUNNING) {
                soapChangePass.execute(json);

                //thread time out
                final Thread soapChangePassThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SearchCustomerBillRespone changePassResponse = null;

                        //call time out
                        try {
                            Thread.sleep(Common.TIME_OUT_CONNECT);
                        } catch (InterruptedException e) {
                            //iCashTranferView.showText(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
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
            //iCashTranferView.showText(e.getMessage());
            return;
        }

//        try {
//            final SoapAPI.AsyncSoapSearchCustomer soapChangePass = new SoapAPI.AsyncSoapSearchCustomer(callBack);
//
//            if (soapChangePass.getStatus() != AsyncTask.Status.RUNNING) {
//                soapChangePass.execute(json);
//
//                //thread time out
//                final Thread soapChangePassThread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ChangePassResponse changePassResponse = null;
//
//                        //call time out
//                        try {
//                            Thread.sleep(Common.TIME_OUT_CONNECT);
//                        } catch (InterruptedException e) {
//                            //iCashTranferView.showText(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
//                        } finally {
//                            if (changePassResponse == null) {
//                                soapChangePass.callCountdown(soapChangePass);
//                            }
//                        }
//                    }
//                });
//
//                soapChangePassThread.start();
//            }
//        } catch (Exception e) {
//            //iCashTranferView.showText(e.getMessage());
//            return;
//        }
    }
}
