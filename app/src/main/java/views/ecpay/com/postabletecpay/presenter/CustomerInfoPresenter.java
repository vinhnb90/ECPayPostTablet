package views.ecpay.com.postabletecpay.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;

import views.ecpay.com.postabletecpay.model.CustomerInfoModel;
import views.ecpay.com.postabletecpay.model.CustomerSearchModel;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.EntityKhachHang;
import views.ecpay.com.postabletecpay.util.entities.response.Base.Respone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityMapCustomerCard.MapCustomerCardRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchCustomerBill.SearchCustomerBillRespone;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.TrangChu.ICustomerInfoView;

/**
 * Created by MyPC on 23/06/2017.
 */

public class CustomerInfoPresenter implements ICustomerInfoPresenter {

    ICustomerInfoView customerInfoView;
    CustomerInfoModel customerInfoModel;

    Handler mHander = new Handler();

    public CustomerInfoPresenter(ICustomerInfoView view)
    {
        customerInfoView = view;
        customerInfoModel = new CustomerInfoModel(view.getContextView());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void register(final EntityKhachHang customer, final String mEDong, final String eCard, final String phoneEcpay, final String bankAcc,  final String bankName) {
        Context context = customerInfoView.getContextView();
        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, mEDong, Common.COMMAND_ID.MAP_CUSTOMER_CARD.toString(), versionApp);
        } catch (Exception e) {

            return;
        }


        String json = SoapAPI.getJsonMapCustomerCard(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                eCard,
                customer.getMA_KHANG(),
                1L, //Update
                phoneEcpay,
                bankAcc,
//                customer.getIdNumber(),
                "",
                bankName,
                configInfo.getAccountId()
        );



        if (json == null)
        {
            try
            {
                customerInfoView.showMessageText(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_PASS.toString());
            }catch (Exception e)
            {

            }
            return;
        }




        try {
            final SoapAPI.AsyncSoapIncludeTimout<MapCustomerCardRespone> soapChangePass = new SoapAPI.AsyncSoapIncludeTimout<MapCustomerCardRespone>(mHander, MapCustomerCardRespone.class, new SoapAPI.AsyncSoapIncludeTimout.AsyncSoapCallBack() {
                @Override
                public void onPre(SoapAPI.AsyncSoapIncludeTimout soap) {

                }

                @Override
                public void onUpdate(String message) {
                    if (message == null || message.isEmpty() || message.trim().equals(""))
                        return;
                    try
                    {
                        customerInfoView.showMessageText(message);
                    }catch (Exception e)
                    {

                    }
                }

                @Override
                public void onPost(SoapAPI.AsyncSoapIncludeTimout soap, Respone response) {
                    customerInfoView.setLoading(false);
                    if(response == null)
                    {
                        try
                        {
                            customerInfoView.showMessageText(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                        }catch (Exception e)
                        {

                        }
                        return;
                    }

                    customerInfoView.showMessageText(response.getFooter().getDescription());
                    if(response.getFooter().getResponseCode().equals("000"))
                    {
                        customer.setMA_THE(eCard);
                        customer.setSDT_ECPAY(phoneEcpay);
//                        customer.setBankName(bankName);
//                        customer.setBankAccount(bankAcc);
                        UpdateDataBase(customer);
                    }
                }

                @Override
                public void onTimeOut(SoapAPI.AsyncSoapIncludeTimout soap) {
                    try
                    {
                        customerInfoView.showMessageText(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                    }catch (Exception e)
                    {

                    }
                }
            });
            soapChangePass.execute(json);
        } catch (Exception e) {
            //iCashTranferView.showText(e.getMessage());
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void update(final EntityKhachHang customer, final String mEDong, final String eCard, final String phoneEcpay, final String bankAcc, final String bankName) {
        Context context = customerInfoView.getContextView();
        ConfigInfo configInfo;
        String versionApp = "";
        try {
            versionApp = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configInfo = Common.setupInfoRequest(context, mEDong, Common.COMMAND_ID.MAP_CUSTOMER_CARD.toString(), versionApp);
        } catch (Exception e) {

            try
            {
                customerInfoView.showMessageText(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_PASS.toString());
            }catch (Exception e1)
            {

            }
            return;
        }


        String json = SoapAPI.getJsonMapCustomerCard(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                eCard,
                customer.getMA_KHANG(),
                2L, //Update
                phoneEcpay,
                bankAcc,
                "",
                bankName,
                configInfo.getAccountId()
        );



        if (json == null)
        {
            try
            {
                customerInfoView.showMessageText(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_PASS.toString());
            }catch (Exception e)
            {

            }
            return;
        }




        try {
            final SoapAPI.AsyncSoapIncludeTimout<MapCustomerCardRespone> soapChangePass = new SoapAPI.AsyncSoapIncludeTimout<MapCustomerCardRespone>(mHander, MapCustomerCardRespone.class, new SoapAPI.AsyncSoapIncludeTimout.AsyncSoapCallBack() {
                @Override
                public void onPre(SoapAPI.AsyncSoapIncludeTimout soap) {

                }

                @Override
                public void onUpdate(String message) {
                    if (message == null || message.isEmpty() || message.trim().equals(""))
                        return;
                    try
                    {
                        customerInfoView.showMessageText(message);
                    }catch (Exception e)
                    {

                    }
                }

                @Override
                public void onPost(SoapAPI.AsyncSoapIncludeTimout soap, Respone response) {
                    customerInfoView.setLoading(false);
                    if(response == null)
                    {
                        try
                        {
                            customerInfoView.showMessageText(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_EMPTY.toString());
                        }catch (Exception e)
                        {

                        }
                        return;
                    }

                    customerInfoView.showMessageText(response.getFooter().getDescription());
                    if(response.getFooter().getResponseCode().equals("000"))
                    {
                        customer.setMA_THE(eCard);
                        customer.setSDT_ECPAY(phoneEcpay);
//                        customer.setBankName(bankName);
//                        customer.setBankAccount(bankAcc);
                        UpdateDataBase(customer);
                    }else
                    {

                    }
                }

                @Override
                public void onTimeOut(SoapAPI.AsyncSoapIncludeTimout soap) {
                    try
                    {
                        customerInfoView.showMessageText(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString());
                    }catch (Exception e)
                    {

                    }
                }
            });
            soapChangePass.execute(json);
        } catch (Exception e) {
            //iCashTranferView.showText(e.getMessage());
            return;
        }
    }

    protected void UpdateDataBase(EntityKhachHang customer)
    {
        customerInfoModel.UpdateCustomer(customer);
        customerInfoView.refill(customer);
        customerInfoView.setLoading(false);
    }
}
